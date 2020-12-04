package com.disk;

import com.utils.BigIntegerHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiskOperations implements Disk{
    private final static Logger LOGGER = Logger.getLogger(DiskOperations.class.getName());

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
                LOGGER.log(Level.SEVERE, ioException.getMessage());
                LOGGER.log(Level.SEVERE, ioException.getLocalizedMessage());
                LOGGER.log(Level.INFO, "" + ioException.getCause());

            }
        }
        return storage;
    }

    public static boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(PATH_FILE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hashMap);
            fileOutputStream.close();
            objectOutputStream.close();
            return true;
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, ioException.getMessage());
            LOGGER.log(Level.WARNING, ioException.getLocalizedMessage());
        }
        return false;
    }

    private boolean checkFileCreation(){
        if(Files.exists(Paths.get(PATH_FILE)))
            return true;
        return false;
    }

//    private static void closeFile(){
//        try {
//            fileWriter.flush();
//            fileWriter.close();
//        }catch (IOException ioException){
//            LOGGER.log(Level.SEVERE, ioException.getMessage());
//            LOGGER.log(Level.INFO, ioException.getLocalizedMessage());
//        }
//    }
}

