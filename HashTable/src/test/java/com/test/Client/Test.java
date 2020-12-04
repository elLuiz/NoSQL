package com.test.Client;

import com.client.ClientConnection;
import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class Test extends ClientConnection {
    protected long tests;
    protected long left_limit;
    protected long right_limit;

    public Test(long tests, long left_limit, long right_limit){
        this.tests = tests;
        this.left_limit = left_limit;
        this.right_limit = right_limit;
    }

    @Override
    public void set() {
        ByteString key, data;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(left_limit, right_limit));
        data = ByteStringHandler.convertFromStringToByteString("data_v1");
        long timestamp = getCurrentTimestamp();
        KeyValue.Set setRequest = createSetRequest(key, data, timestamp);
        KeyValue.Response response = clientKeyValueStub.set(setRequest);
        displayMessage(key, "SET", response.getMessage());
    }
    @Override
    public void get() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(left_limit, right_limit));
        KeyValue.Get getRequest = createGetRequest(key);
        KeyValue.Response response = clientKeyValueStub.get(getRequest);
        displayMessage(key, "GET", response.getMessage());
    }

    @Override
    public void del() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(left_limit, right_limit));
        KeyValue.Del delRequest = createDelRequest(key);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DEL", response.getMessage());
    }

    @Override
    public void delKeyVersion() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(left_limit, right_limit));
        long version = generateRandomLong(left_limit, 10L);
        KeyValue.Del delRequest = createDelRequest(key, version);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DELKV", response.getMessage());
    }

    @Override
    public void testAndSet() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(left_limit, right_limit));
        long version = getVersion(key);
        KeyValue.Value value = createValue(version);
        KeyValue.TestAndSet testAndSetRequest = createTestAndSetRequest(key, value, version);
        KeyValue.Response response = clientKeyValueStub.testAndSet(testAndSetRequest);
        displayMessage(key, "TEST AND SET", response.getMessage());
    }

    protected void displayMessage(ByteString key, String function, String message){
        System.out.println("Function: " + function);
        System.out.println("Key: " + key.toStringUtf8());
        System.out.println("Status: " + message);
        System.out.println("Response received at: " + new Timestamp(System.currentTimeMillis()).toString());
    }

    protected KeyValue.Value createValue(long version){
        KeyValue.Value.Builder valueBuilder = KeyValue.Value.newBuilder();
        valueBuilder.setData(ByteString.copyFrom("data_v2".getBytes(StandardCharsets.UTF_8)));
        valueBuilder.setVersion(version);
        valueBuilder.setTimestamp(getCurrentTimestamp());

        return valueBuilder.build();
    }

    protected long generateRandomLong(long leftLimit, long rightLimit){
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
