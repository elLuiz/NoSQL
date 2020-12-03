package com.server;

import com.disk.DiskOperations;
import com.service.Subscriber;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPersistence implements Subscriber {
    private final static Logger LOGGER = Logger.getLogger(ServerPersistence.class.getName());
    private long writeTimeOut;
    private ConcurrentHashMap<BigInteger, ValueHandler> currentHashMap;
    private ScheduledExecutorService scheduledExecutorService;

    public ServerPersistence(){
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start(){
        final Runnable writeOnDisk = new Runnable() {
            @Override
            public void run() {
                writeOnDisk();
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(writeOnDisk, 0, writeTimeOut, TimeUnit.MILLISECONDS);
    }

    public synchronized void writeOnDisk(){
        if(currentHashMap != null){
            DiskOperations.write(currentHashMap);
            setCurrentHashMap(null);
            LOGGER.log(Level.INFO,"Saved");
        }
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public void setCurrentHashMap(ConcurrentHashMap<BigInteger, ValueHandler> currentHashMap) {
        this.currentHashMap = currentHashMap;
    }

    public void update(ConcurrentHashMap<BigInteger, ValueHandler> storage) {
        LOGGER.log(Level.INFO, "Version: " + storage.hashCode());
        setCurrentHashMap(storage);
    }
}
