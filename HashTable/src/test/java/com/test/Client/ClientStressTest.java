package com.test.Client;

import com.client.ClientConnection;
import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.utils.LongHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientStressTest extends ClientConnection {
    private static final int TESTS = 1000;

    @BeforeEach
    void init(){
        connectToServer();
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("SET FUNCTION")
    @Override
    public void set() {
        ByteString key, data;
        key = data = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 1000L));
        long timestamp = getCurrentTimestamp();
        KeyValue.Set setRequest = createSetRequest(key, data, timestamp);
        KeyValue.Response response = clientKeyValueStub.set(setRequest);
        displayMessage(key, "Set", response.getMessage());

    }

    @Override
    public void get() {}

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("DEL FUNCTION")
    @Override
    public void del() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 1000L));
        KeyValue.Del delRequest = createDelRequest(key);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DEL", response.getMessage());
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("DELKEYVERSION TEST")
    @Override
    public void delKeyVersion() {
        ByteString key;
        key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 1000L));
        long version = generateRandomLong(1L, 10L);
        KeyValue.Del delRequest = createDelRequest(key, version);
        KeyValue.Response response = clientKeyValueStub.del(delRequest);
        displayMessage(key, "DELKV", response.getMessage());
    }

    @Override
    public void testAndSet() {

    }

    private long generateRandomLong(long leftLimit, long rightLimit){
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    private void displayMessage(ByteString key, String function, String message){
        System.out.println("Function: " + function);
        System.out.println("Key: " + key);
        System.out.println("Message: " + message);
    }
}
