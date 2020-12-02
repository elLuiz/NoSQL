package com.client;

import java.sql.Timestamp;
import java.util.Scanner;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue.Del;
import com.hashTable.KeyValue.Get;
import com.hashTable.KeyValue.Response;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Value;
import com.hashTable.KeyValue.Value.Builder;
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
    	Scanner myObj = new Scanner(System.in);
        System.out.println("Entre com a key: ");
        String key = myObj.nextLine();
        
        System.out.println("Entre com os dados: ");
        String data = myObj.nextLine();
        
        System.out.println("Entre com a versão: ");
        String version = myObj.nextLine();
        
        connectToServer();
        testAndSet(key,data,version);
    }
    
    public static void connectToServer(){
        channelBuilder = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext();
        channel = channelBuilder.build();
        clientKeyValueStub = hashTableServiceGrpc.newBlockingStub(channel);
    }
	
	public static void set(String keyString, String dataString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(keyString);
		ByteString data = ByteStringHandler.convertFromStringToByteString(dataString);
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
	
	public static void get(String keyString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(keyString);
		Get request = Get.newBuilder().setKey(key).build();
		
		Response response;
		
		try {
			
			response = clientKeyValueStub.get(request);
			
			String dataResponse = response.getValue().getData().toStringUtf8();
			long versionResponse = response.getValue().getVersion();
			Timestamp timesmtampresposne = LongHandler.convertLongToTimestamp(response.getValue().getTimestamp());
			
			System.out.println("DADOS: "+ dataResponse +" \n"+"VERSAO: " + versionResponse + " \n" + "TIMESTAMP: "+ timesmtampresposne.toString());
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	public static void del(String keyString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(keyString);
		Del request = Del.newBuilder().setKey(key).build();
		
		Response response;
		
		try {
			
			response = clientKeyValueStub.del(request);
			
			String dataResponse = response.getValue().getData().toStringUtf8();
			long versionResponse = response.getValue().getVersion();
			Timestamp timesmtampresposne = LongHandler.convertLongToTimestamp(response.getValue().getTimestamp());
			
			System.out.println("DADOS: "+ dataResponse +" \n"+"VERSAO: " + versionResponse + " \n" + "TIMESTAMP: "+ timesmtampresposne.toString());
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	public static void del(String keyString, String versionString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(keyString);
		long version = LongHandler.convertFromStringToLong(versionString);
		Del request = Del.newBuilder().setKey(key).setVersion(version).build();
		
		Response response;
		
		try {
			
			response = clientKeyValueStub.del(request);
			
			String dataResponse = response.getValue().getData().toStringUtf8();
			long versionResponse = response.getValue().getVersion();
			Timestamp timesmtampresposne = LongHandler.convertLongToTimestamp(response.getValue().getTimestamp());
			
			System.out.println("DADOS: "+ dataResponse +" \n"+"VERSAO: " + versionResponse + " \n" + "TIMESTAMP: "+ timesmtampresposne.toString());
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	public static void testAndSet(String keyString, String dataString, String versionString) {
		//criando um request
		ByteString key = ByteStringHandler.convertFromStringToByteString(keyString);
		ByteString data = ByteStringHandler.convertFromStringToByteString(dataString);
		long version = LongHandler.convertFromStringToLong(versionString);
		long timesmtamp = LongHandler.convertTimestampToLong(new Timestamp(System.currentTimeMillis()));
		
		Builder valueBuilder = Value.newBuilder();
		
		valueBuilder.setTimestamp(timesmtamp).setData(data).setVersion(version);
		
		TestAndSet request = TestAndSet.newBuilder().setKey(key).setVersion(version).setValue(valueBuilder).build();
		
		Response response;
		
		try {
			
			response = clientKeyValueStub.testAndSet(request);
			
			String dataResponse = response.getValue().getData().toStringUtf8();
			long versionResponse = response.getValue().getVersion();
			Timestamp timesmtampresposne = LongHandler.convertLongToTimestamp(response.getValue().getTimestamp());
			
			System.out.println("DADOS: "+ dataResponse +" \n"+"VERSAO: " + versionResponse + " \n" + "TIMESTAMP: "+ timesmtampresposne.toString());
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
