package com.test.Client;

import com.client.ClientConnection;
import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest extends ClientConnection {

//    private String keyParam;
//    private long versionParam;
//    private String  dataParam;
//    private String resultParam;
//
//    public ClientTest(String key, long version, String data, String result){
//        super();
//        this.keyParam = key;
//        this.versionParam = version;
//        this.dataParam = data;
//        this.resultParam = result;
//    }
//
//    @Parameters
//    public static Collection inputs(){
//        return Arrays.asList(new Object[][]{
//                {"223223", 1,  "data", "SUCCESS"},
//                {"212122", 1,  "data 1", "SUCCESS"},
//                {"212122", 1,  "data 2", "ERROR"}
//        });
//    }

    @BeforeEach
    void init(){
        connectToServer();
    }

    @Test
    @RepeatedTest(10)
    @Override
    public void set() {
        ByteString key, data;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(0, 10L));
        data = ByteStringHandler.convertFromStringToByteString("data_v1");
        long timestamp = getCurrentTimestamp();
        KeyValue.Set setRequest = createSetRequest(key, data, timestamp);
        KeyValue.Response response = clientKeyValueStub.set(setRequest);
        displayMessage(key, "SET", response.getMessage());
    }

    @Test
    @RepeatedTest(10)
    @Override
    public void get() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(0L, 10L));
        KeyValue.Get getRequest = createGetRequest(key);
        KeyValue.Response response = clientKeyValueStub.get(getRequest);
        displayMessage(key, "GET", response.getMessage());
    }

    @Test
    @RepeatedTest(10)
    @Override
    public void del() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(0L, 10L));
        KeyValue.Del delRequest = createDelRequest(key);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DEL", response.getMessage());
    }

    @Test
    @RepeatedTest(10)
    @Override
    public void delKeyVersion() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 10L));
        long version = generateRandomLong(1L, 10L);
        KeyValue.Del delRequest = createDelRequest(key, version);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DELKV", response.getMessage());
    }

    @Test
    @RepeatedTest(10)
    @Override
    public void testAndSet() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 10L));
        long version = getVersion(key);
        KeyValue.Value value = createValue(version);
        KeyValue.TestAndSet testAndSetRequest = createTestAndSetRequest(key, value, version);
        KeyValue.Response response = clientKeyValueStub.testAndSet(testAndSetRequest);
        displayMessage(key, "TEST AND SET", response.getMessage());
    }

    private long generateRandomLong(long leftLimit, long rightLimit){
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    private void displayMessage(ByteString key, String function, String message){
        System.out.println("Function: " + function);
        System.out.println("Key: " + key);
        System.out.println("Message: " + message);
    }

    public KeyValue.Value createValue(long version){
        KeyValue.Value.Builder valueBuilder = KeyValue.Value.newBuilder();
        valueBuilder.setData(ByteStringHandler.convertFromStringToByteString("data_v2"));
        valueBuilder.setVersion(version);
        valueBuilder.setTimestamp(getCurrentTimestamp());

        return valueBuilder.build();
    }
}
