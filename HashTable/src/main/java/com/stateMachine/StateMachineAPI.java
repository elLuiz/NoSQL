package com.stateMachine;

import com.utils.BigIntegerHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineAPI {
    // Luiz
    // message:NULL
    // message:version:timestamp:data
    // [0] -> operation, [1] -> key, [2] -> timestamp, [3] -> data
    protected static String set(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        long timestamp = LongHandler.convertFromStringToLong(data[2]);
        byte[] dataBytes = data[3].getBytes(Charset.defaultCharset());
        ValueHandler valueHandler;
        String response;
        if((valueHandler = hashMap.get(key)) == null){
            valueHandler = new ValueHandler();
            valueHandler.setTimestamp(timestamp);
            valueHandler.setData(dataBytes);
            valueHandler.setVersion(1);
            hashMap.put(key, valueHandler);

            response = "SUCCESS:" + "NULL";
        }else{
            response = "ERROR:" + valueHandler.getVersion() + ":" + valueHandler.getTimestamp() + ":" + valueHandler.getData().toString();
        }

        return response;
    }

    //Guilherme
    protected static String get(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[1]);
        ValueHandler valueHandler;
        String response;

        if ((valueHandler = hashMap.get(key)) == null){
            response = "ERROR";
        } else {
            response = "SUCCESS" + valueHandler.getVersion() + ":" + valueHandler.getTimestamp() + ":" + valueHandler.getData();
        }
        return response;
    }

    //Luiz
    protected static String del(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        return "";
    }
    //Luiz
    protected static String delKV(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        return "";
    }
    // Guilherme
    protected static String testAndSet(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        return "";
    }
}
