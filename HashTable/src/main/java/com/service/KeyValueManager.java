package com.service;

import com.hashTable.KeyValue;
import com.utils.ValueHandler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyValueManager {
    private ArrayList<Subscriber> subscribers = new ArrayList<>();
    private Logger logger = Logger.getLogger(KeyValueManager.class.getName());
    private static volatile KeyValueManager keyValueManagerInstance;

    private KeyValueManager(){}

    public static KeyValueManager getInstance(){
        if(keyValueManagerInstance == null){
            synchronized (KeyValueManager.class){
                if(keyValueManagerInstance == null){
                    return keyValueManagerInstance = new KeyValueManager();
                }
            }
        }

        return keyValueManagerInstance;
    }

    public void subscribe(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void notify(ConcurrentHashMap<BigInteger, ValueHandler> storage){
        subscribers.forEach((subscriber -> subscriber.update(storage)));
    }

}
