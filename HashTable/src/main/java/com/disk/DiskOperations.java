package com.disk;

import com.utils.ValueHandler;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiskOperations implements Disk{
    private final static Logger LOGGER = Logger.getLogger(DiskOperations.class.getName());
    private static FileOutputStream fileOutputStream;
    private static ObjectOutputStream objectOutputStream;

    public synchronized ConcurrentHashMap<BigInteger, ValueHandler> retrieveRecords(){
        ConcurrentHashMap<BigInteger, ValueHandler> storage = new ConcurrentHashMap<>();
        if(checkFileCreation()){
            try{
                FileInputStream fileInputStream = new FileInputStream(PATH_FILE);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                storage = (ConcurrentHashMap) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            }catch (ClassNotFoundException exception){
                LOGGER.log(Level.SEVERE, exception.getMessage());
                LOGGER.log(Level.SEVERE, exception.getLocalizedMessage());
                LOGGER.log(Level.INFO, "" + exception.getCause());
            }catch (IOException ioException){
                if(ioException.getLocalizedMessage() == null){
                    LOGGER.log(Level.INFO, "No data available yet.");
                }else{
                    LOGGER.log(Level.SEVERE, ioException.getMessage());
                    LOGGER.log(Level.SEVERE, ioException.getLocalizedMessage());
                    LOGGER.log(Level.INFO, "" + ioException.getCause());
                }
            }
        }
        return storage;
    }

    public static boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        try{
            objectOutputStream.writeObject(hashMap);
            return true;
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, ioException.getMessage());
            LOGGER.log(Level.WARNING, ioException.getLocalizedMessage());
        }
        return false;
    }

    public static void openFile(){
        try{
            fileOutputStream = new FileOutputStream(PATH_FILE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, ioException.getMessage());
        }
    }

    private boolean checkFileCreation(){
        if(Files.exists(Paths.get(PATH_FILE)))
            return true;
        return false;
    }
}
