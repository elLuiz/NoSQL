package com.client.ratis;

import com.connection.RatisConnection;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class RatisClient extends RatisConnection {
    private List<RaftPeer> raftPeers;

    public void connectToRatisServer(){
        Map<String, InetSocketAddress> addressMap = createAddressesMap();
        raftPeers = generatePeersList(addressMap);
        setClientProprieties();
    }

    public RaftClient setClientProprieties(){
        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(GROUP_ID)), raftPeers);
        RaftProperties raftProperties = new RaftProperties();
        RaftClient raftClient = RaftClient.newBuilder()
                .setProperties(raftProperties)
                .setRaftGroup(raftGroup)
                .setClientRpc(new GrpcFactory(new Parameters())
                                .newRaftClientRpc(ClientId.randomId(), raftProperties))
                .build();

        return raftClient;
    }



}
