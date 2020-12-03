package com.client;

import com.client.exceptions.ClientInputException;
import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.Get;
import com.hashTable.KeyValue.Del;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Response;
import com.utils.ByteStringHandler;
import com.utils.InputHandleRules;
import com.utils.LongHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimpleClient extends ClientConnection{
	@Override
    public void set() {
    	try{
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter your key: ");
	        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
	        InputHandleRules.checkNumericNotNull(key,"key");
	        System.out.println("Enter the data: ");
	        ByteString data = ByteString.copyFrom(scanner.nextLine().getBytes(StandardCharsets.UTF_8));

	        long timestamp = getCurrentTimestamp();
	        Set setRequest = createSetRequest(key, data, timestamp);
	        Response response = clientKeyValueStub.set(setRequest);
	        displayResponse(response);
    	}catch (ClientInputException exception) {
			System.out.println(exception.getMessage());
		}
    }

    @Override
    public void get() {
    	try{
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter the key: ");
	
	        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
	        InputHandleRules.checkNumericNotNull(key,"key");
	        
	        Get getRequest = createGetRequest(key);
	        Response response = clientKeyValueStub.get(getRequest);
	        displayResponse(response);
    	}catch (ClientInputException exception) {
			System.out.println(exception.getMessage());
		}
    }

    public long getVersion(ByteString key){
		Get getRequest = createGetRequest(key);
		Response response = clientKeyValueStub.get(getRequest);
		return response.getValue().getVersion();
	}

    @Override
    public void del() {
    	try{
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter the key: ");
	
	        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
	        InputHandleRules.checkNumericNotNull(key,"key");
	        
	        Del delRequest = createDelRequest(key);
	        Response response = clientKeyValueStub.del(delRequest);
	        displayResponse(response);
    	}catch (ClientInputException exception) {
			System.out.println(exception.getMessage());
		}
    }

    @Override
    public void delKeyVersion() {
    	try{
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter the key: ");
	        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
	        InputHandleRules.checkNumericNotNull(key,"key");

	        System.out.println("Enter the version: ");
	        Long version = LongHandler.convertFromStringToLong(scanner.nextLine());
	        Del delRequest = createDelRequest(key, version);
	        Response response = clientKeyValueStub.del(delRequest);
	        
	        displayResponse(response);
    	}catch (ClientInputException exception) {
			System.out.println(exception.getMessage());
		}
    }

    @Override
    public void testAndSet() {
    	try{
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter the key: ");
	        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
	        InputHandleRules.checkNumericNotNull(key,"key");
	        long version = getVersion(key);
	        KeyValue.Value value = createValue(version, scanner);
	
	        TestAndSet testAndSetRequest = createTestAndSetRequest(key, value, version);
	        Response response = clientKeyValueStub.testAndSet(testAndSetRequest);
	        displayResponse(response);
    	}catch (ClientInputException exception) {
			System.out.println(exception.getMessage());
		}
    }

    private KeyValue.Value createValue(Long version, Scanner scanner){
        System.out.println("Enter the data: ");
        ByteString data = ByteString.copyFrom(scanner.nextLine().getBytes(StandardCharsets.UTF_8));
        long timestamp = getCurrentTimestamp();

        KeyValue.Value.Builder value = KeyValue.Value.newBuilder();
        value.setVersion(version);
        value.setTimestamp(timestamp);
        value.setData(data);
        return value.build();
    }
}
