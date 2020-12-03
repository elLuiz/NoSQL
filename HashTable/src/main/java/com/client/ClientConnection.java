package com.client;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.hashTable.hashTableServiceGrpc;
import com.hashTable.hashTableServiceGrpc.hashTableServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.hashTable.KeyValue.Response;
import io.grpc.StatusRuntimeException;
import java.sql.Timestamp;

public abstract class ClientConnection {
	protected static ManagedChannelBuilder channelBuilder;
    protected static ManagedChannel channel;
    protected static hashTableServiceBlockingStub clientKeyValueStub;

    public static void connectToServer(){
        try {
            channelBuilder = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext();
            channel = channelBuilder.build();
            clientKeyValueStub = hashTableServiceGrpc.newBlockingStub(channel);
        }catch (StatusRuntimeException statusRuntimeException){
            System.out.println(statusRuntimeException.getMessage());
        }
    }

    public static long getCurrentTimestamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    protected KeyValue.Set createSetRequest(ByteString key, ByteString data, long timestamp){
        KeyValue.Set.Builder request = KeyValue.Set.newBuilder();
        request.setKey(key);
        request.setData(data);
        request.setTimestamp(timestamp);
        return request.build();
    }

    protected KeyValue.Get createGetRequest(ByteString key){
        KeyValue.Get.Builder request = KeyValue.Get.newBuilder();
        request.setKey(key);
        return request.build();
    }

    protected KeyValue.Del createDelRequest(ByteString key){
        KeyValue.Del.Builder request = KeyValue.Del.newBuilder();
        request.setKey(key);
        return request.build();
    }

    protected KeyValue.Del createDelRequest(ByteString key, long version){
        KeyValue.Del.Builder request = KeyValue.Del.newBuilder();
        request.setKey(key);
        request.setVersion(version);
        return request.build();
    }

    protected KeyValue.TestAndSet createTestAndSetRequest(ByteString key, KeyValue.Value value, long version){
        KeyValue.TestAndSet.Builder request = KeyValue.TestAndSet.newBuilder();
        request.setKey(key);
        request.setValue(value);
        request.setVersion(version);
        return request.build();
    }

    public long getVersion(ByteString key){
        KeyValue.Get getRequest = createGetRequest(key);
        Response response = clientKeyValueStub.get(getRequest);
        return response.getValue().getVersion();
    }

    public void displayResponse(Response response){
        try {
            String dataResponse = response.getValue().getData().toStringUtf8();
            long versionResponse = response.getValue().getVersion();
            long timestamp = response.getValue().getTimestamp();
            System.out.println(response.getMessage());
            if(versionResponse != 0 && timestamp != 0 && dataResponse != ""){
                System.out.println("Version: " + versionResponse);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("Data: " + dataResponse);
            }
        }catch(StatusRuntimeException statusRuntimeException){
            statusRuntimeException.printStackTrace();
        }
    }

    public abstract void set();
    public abstract void get();
    public abstract void del();
    public abstract void delKeyVersion();
    public abstract void testAndSet();
}
