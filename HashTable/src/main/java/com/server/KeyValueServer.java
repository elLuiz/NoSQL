package com.server;

import com.connection.RatisConnection;
import com.disk.DiskOperations;
import com.service.KeyValueManager;
import com.service.KeyValueService;
import com.stateMachine.StateMachine;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.LifeCycle;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyValueServer extends RatisConnection{
    private final static Logger LOGGER = Logger.getLogger(KeyValueServer.class.getName());
    private static RaftPeerId serverId;
    private static Map<String, InetSocketAddress> addressMap;
    private static RaftProperties raftProperties;

    public static void main(String []args) throws IOException, InterruptedException{
        String id = args[0];
        addressMap = createAddressesMap();
        verifyServerId(id);
        List<RaftPeer> peerList = generatePeersList(addressMap);
        serverId = RaftPeerId.valueOf(id);
        setupServer(id);
        joinGroupOfProcesses(peerList);
    }

    private static void verifyServerId(String serverId){
        if(!addressMap.containsKey(serverId)){
            LOGGER.log(Level.WARNING, "The key " + serverId + " is invalid");
            System.exit(1);
        }
    }

    private static void setupServer(String id){
        raftProperties = new RaftProperties();
        raftProperties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
        GrpcConfigKeys.Server.setPort(raftProperties, addressMap.get(id).getPort());
        RaftServerConfigKeys.setStorageDir(raftProperties, Collections.singletonList(new File("tmp/" + serverId)));
    }

    private static void joinGroupOfProcesses(List<RaftPeer> addresses) throws IOException, InterruptedException{
        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(GROUP_ID)), addresses);
        RaftServer raftServer = RaftServer.newBuilder()
                .setServerId(serverId)
                .setStateMachine(new StateMachine())
                .setProperties(raftProperties)
                .setGroup(raftGroup)
                .build();
        raftServer.start();

        while (raftServer.getLifeCycleState() != LifeCycle.State.CLOSED){
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
