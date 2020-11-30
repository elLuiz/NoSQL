package com.server;

import com.disk.Disk;
import com.disk.DiskOperations;
import com.service.Subscriber;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPersistence extends Thread implements Subscriber {
    private final static Logger LOGGER = Logger.getLogger(ServerPersistence.class.getName());
    private long writeTimeOut = 2000;
    private ConcurrentHashMap<BigInteger, ValueHandler> currentHashMap;

    public void writeOnDisk(){
        while (true){
            try{
                Thread.sleep(writeTimeOut);
                if(currentHashMap != null){
                    LOGGER.log(Level.INFO,"Writing");
                    Disk diskOperation = new DiskOperations();
                    diskOperation.write(currentHashMap);
                    synchronized (ServerPersistence.class){
                        setCurrentHashMap(null);
                    }
                    LOGGER.log(Level.INFO,"Saved");
                }
            }catch(InterruptedException interruptedException){
                LOGGER.log(Level.SEVERE, "Got interrupted: " + interruptedException.getMessage());
            }
        }
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public void setCurrentHashMap(ConcurrentHashMap<BigInteger, ValueHandler> currentHashMap) {
        this.currentHashMap = currentHashMap;
    }

    @Override
    public void run() {
        writeOnDisk();
    }

    public void update(ConcurrentHashMap<BigInteger, ValueHandler> storage) {
        LOGGER.log(Level.INFO, "Version: " + storage.hashCode());
        setCurrentHashMap(storage);
    }
}
