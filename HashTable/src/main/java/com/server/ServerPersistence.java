package com.server;

import com.service.KeyValueService;
import com.service.Subscriber;
import com.utils.ValueHandler;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPersistence extends Thread implements Subscriber {
    private final static Logger LOGGER = Logger.getLogger(ServerPersistence.class.getName());
    private long writeTimeOut = 2000;
    private KeyValueService keyValueService;
    public ServerPersistence(){
        keyValueService = new KeyValueService();
    }

    public void sayHello(){
        while (true){
            try{
                Thread.sleep(writeTimeOut);
//                LOGGER.log(Level.INFO, "Map version: " + keyValueService.getStorage().hashCode());
            }catch(InterruptedException interruptedException){
                LOGGER.log(Level.SEVERE, "Got interrupted: " + interruptedException.getMessage());
            }
        }
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "I'm" + Thread.currentThread().getName());
        sayHello();
    }

    public void update(ConcurrentHashMap<BigInteger, ValueHandler> storage) {
        LOGGER.log(Level.INFO, "A new mapped has been sent.");
        LOGGER.log(Level.INFO, "Version: " + storage.hashCode());
    }
}
