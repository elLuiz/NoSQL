package com.stateMachine;

import com.utils.BigIntegerHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineAPI {

    // [0] -> operation, [1] -> key, [2] -> timestamp, [3] -> data
    protected static String set(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        long timestamp = LongHandler.convertFromStringToLong(data[2]);
        byte[] dataBytes = data[3].getBytes(StandardCharsets.UTF_8);
        ValueHandler valueHandler;
        String response;
        if((valueHandler = hashMap.get(key)) == null){
            final ValueHandler value = new ValueHandler();
            value.setTimestamp(timestamp);
            value.setData(dataBytes);
            value.setVersion(1);
            hashMap.computeIfAbsent(key, v -> value);
            response = createResponse("SUCCESS", null);
        }else{
            response = createResponse("ERROR", valueHandler);
        }
        return response;
    }

    protected static String get(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        ValueHandler valueHandler;
        String response;

        if ((valueHandler = hashMap.get(key)) == null){
            response = createResponse("ERROR", null);
        } else {
            response = createResponse("SUCCESS", valueHandler);
        }
        return response;
    }

    // [0] -> del, [1] -> key
    protected static String del(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        String response;
        ValueHandler valueHandler;
        if((valueHandler = hashMap.remove(key)) == null){
            response = createResponse("ERROR", null);
        }else{
            response = createResponse("SUCCESS", valueHandler);
        }
        return response;
    }

    // [0]-> delKV, [1]->key, [2]->version
    protected static String delKV(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        long version = LongHandler.convertFromStringToLong(data[2]);
        ValueHandler valueHandler;
        String response;
        if((valueHandler = hashMap.get(key)) == null){
            response = createResponse("ERROR_NE", null);
        }else{
            if(valueHandler.getVersion() != version){
                response = createResponse("ERROR_WV", valueHandler);
            }else{
                hashMap.remove(key);
                response = createResponse("SUCCESS", valueHandler);
            }
        }
        return response;
    }

    protected static String testAndSet(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        long valueTimestamp = LongHandler.convertFromStringToLong(data[2]);
        byte[] valueDataBytes = data[3].getBytes(StandardCharsets.UTF_8);
        long version = LongHandler.convertFromStringToLong(data[4]);
        ValueHandler valueHandler;
        String response;

        if ((valueHandler = hashMap.get(key)) == null){
            response = createResponse("ERROR_NE", null);
        }else {
            if (valueHandler.getVersion() == version){
                final ValueHandler value = new ValueHandler();
                value.setData(valueDataBytes);
                value.setTimestamp(valueTimestamp);
                value.setVersion(version + 1);
                hashMap.computeIfPresent(key, (k, v) -> value);

                response = createResponse("SUCCESS", value);
            }else {
                response = createResponse("ERROR_WV", valueHandler);
            }
        }
        return response;
    }

    private static String createResponse(String status, ValueHandler valueHandler){
        if(valueHandler == null)
            return status + ":" + "NULL";
        else
            return status + ":" + valueHandler.getVersion() + ":" + valueHandler.getTimestamp()
                    + ":" + new String(valueHandler.getData(), StandardCharsets.UTF_8);
    }
}
