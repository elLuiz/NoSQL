package com.client;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.Get;
import com.hashTable.KeyValue.Del;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Response;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;
import io.grpc.StatusRuntimeException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;

public class SimpleClient extends ClientConnection{

    public static void main(String []args){
        connectToServer();
        SimpleClient simpleClient = new SimpleClient();
//        System.out.println("-----------SET-------------");
//        simpleClient.set();
//        System.out.println("-----------GET-------------");
//        simpleClient.get();
//        System.out.println("-----------DEL-------------");
//        simpleClient.del();
//        System.out.println("-----------DEL KEY VERSION-------------");
//        simpleClient.delKeyVersion();
        System.out.println("-----------TEST AND SET-------------");
        simpleClient.testAndSet();
    }

    @Override
    public void set() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your key: ");
        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
        System.out.println("Enter the data: ");
        ByteString data = ByteString.copyFrom(scanner.nextLine().getBytes(StandardCharsets.UTF_8));

        long timestamp = getCurrentTimestamp();
        Set setRequest = createSetRequest(key, data, timestamp);
        Response response = clientKeyValueStub.set(setRequest);
        displayResponse(response);
    }

    @Override
    public void get() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the key: ");

        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
        Get getRequest = createGetRequest(key);
        Response response = clientKeyValueStub.get(getRequest);
        displayResponse(response);
    }

    @Override
    public void del() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the key: ");

        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
        Del delRequest = createDelRequest(key);
        Response response = clientKeyValueStub.del(delRequest);
        displayResponse(response);
    }

    @Override
    public void delKeyVersion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the key: ");
        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
        System.out.println("Enter the version: ");

        Long version = LongHandler.convertFromStringToLong(scanner.nextLine());
        Del delRequest = createDelRequest(key, version);
        Response response = clientKeyValueStub.del(delRequest);
        displayResponse(response);
    }

    @Override
    public void testAndSet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the key: ");
        ByteString key = ByteStringHandler.convertFromStringToByteString(scanner.nextLine());
        System.out.println("Enter the version: ");
        Long version = LongHandler.convertFromStringToLong(scanner.nextLine());
        KeyValue.Value value = createValue(version, scanner);

        TestAndSet testAndSetRequest = createTestAndSetRequest(key, value, version);
        Response response = clientKeyValueStub.testAndSet(testAndSetRequest);
        displayResponse(response);
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

    @Override
    public void displayResponse(Response response) {
        try {
            String dataResponse = response.getValue().getData().toStringUtf8();
            long versionResponse = response.getValue().getVersion();
            long timestamp = response.getValue().getTimestamp();

            System.out.println(response.getMessage());
            System.out.println("Version: " + versionResponse);
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Data: " + dataResponse);
        }catch(StatusRuntimeException statusRuntimeException){
            statusRuntimeException.printStackTrace();
        }
    }
}
