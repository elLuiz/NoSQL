package com.client;

import java.sql.Timestamp;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue.Response;
import com.hashTable.KeyValue.Set;
import com.hashTable.hashTableServiceGrpc;
import com.hashTable.hashTableServiceGrpc.hashTableServiceBlockingStub;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ConectClient {
	
	private static ManagedChannelBuilder channelBuilder;
    private static ManagedChannel channel;
    private static hashTableServiceBlockingStub clientKeyValueStub;
    
    public static void main(String []args){
        connectToServer();
        set(args[0],args[1]);
    }
    
    public static void connectToServer(){
        channelBuilder = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext();
        channel = channelBuilder.build();
        clientKeyValueStub = hashTableServiceGrpc.newBlockingStub(channel);
    }
	
	public static void set(String keyString, String dataString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(dataString);
		ByteString data = ByteStringHandler.convertFromStringToByteString(keyString);
		long timesmtamp = LongHandler.convertTimestampToLong(new Timestamp(System.currentTimeMillis()));
		
		Set request = Set.newBuilder().setKey(key).setData(data).setTimestamp(timesmtamp).build();
		
		Response response;
		
		try {
			
			response = clientKeyValueStub.set(request);
			
			String dataResponse = response.getValue().getData().toStringUtf8();
			long versionResponse = response.getValue().getVersion();
			Timestamp timesmtampresposne = LongHandler.convertLongToTimestamp(response.getValue().getTimestamp());
			
			System.out.println("DADOS: "+ dataResponse +" \n"+"VERSAO: " + versionResponse + " \n" + "TIMESTAMP: "+ timesmtampresposne.toString());
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
}
