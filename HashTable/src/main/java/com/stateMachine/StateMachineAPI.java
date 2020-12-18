package com.stateMachine;

import com.utils.BigIntegerHandler;
import com.utils.ValueHandler;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineAPI {
    // Luiz
    // message:NULL
    // message:version:timestamp:data
    protected static String set(String []data, ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        return "";
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
