package com.connection;

import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RatisConnection {
    public static final String GROUP_ID = "raft_group____id";

    public static Map<String, InetSocketAddress> createAddressesMap(){
        Map<String, InetSocketAddress> addressMap = new HashMap<>();
        addressMap.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        addressMap.put("p2", new InetSocketAddress("127.0.0.1", 4000));
        addressMap.put("p3", new InetSocketAddress("127.0.0.1", 4500));

        return addressMap;
    }

    public static List<RaftPeer> generatePeersList(Map<String, InetSocketAddress> peers){
        List<RaftPeer> raftPeerList = peers.entrySet()
                .stream()
                .map(peer -> new RaftPeer(RaftPeerId.valueOf(peer.getKey()), peer.getValue()))
                .collect(Collectors.toList());

        return raftPeerList;
    }
}