package com.stateMachine;

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
        return "";
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
