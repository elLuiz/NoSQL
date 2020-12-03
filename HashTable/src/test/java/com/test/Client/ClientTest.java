package com.test.Client;

import com.client.ClientConnection;
import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.utils.LongHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ClientTest extends ClientConnection {
    private static final int TESTS = 1000;

    @BeforeEach
    void connect(){
        connectToServer();
    }

    @Override
    @Test
    @RepeatedTest(TESTS)
    public void set() {
        ByteString key, data;
        key = data = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 500L));
        long timestamp = getCurrentTimestamp();
        KeyValue.Set setRequest = createSetRequest(key, data, timestamp);
        KeyValue.Response response = clientKeyValueStub.set(setRequest);
        assertEquals("SUCCESS", response.getMessage());
    }

    @Override
    public void get() {

    }

    @Override
    public void del() {
        ByteString key;
        for (int i = 0; i < TESTS; i++){
            key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 500L));
            KeyValue.Del delRequest = createDelRequest(key);
            KeyValue.Response response = clientKeyValueStub.del(delRequest);
            displayResponse(response);

        }
    }

    @Override
    public void delKeyVersion() {
        ByteString key;
        for (int i = 0; i < TESTS; i++){
            key = LongHandler.convertFromLongToByteString(generateRandomLong(1L, 500L));
            long version = generateRandomLong(1L, 10L);
            KeyValue.Del delRequest = createDelRequest(key, version);
            KeyValue.Response response = clientKeyValueStub.del(delRequest);
            displayResponse(response);
        }
    }

    @Override
    public void testAndSet() {

    }

    private long generateRandomLong(long leftLimit, long rightLimit){
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

}
