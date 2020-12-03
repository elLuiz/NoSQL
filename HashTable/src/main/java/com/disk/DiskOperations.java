package com.disk;

import com.utils.BigIntegerHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;

import javax.swing.text.AbstractDocument;
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
    private static FileWriter fileWriter;
    private static BufferedWriter bufferedWriter;

    public ConcurrentHashMap<BigInteger, ValueHandler> retrieveRecords(){
        BufferedReader bufferedReader = null;
        ConcurrentHashMap<BigInteger, ValueHandler> storage = new ConcurrentHashMap<>();
        if(checkFileCreation()){
            try{
                FileReader fileReader = new FileReader(PATH_FILE);
                bufferedReader = new BufferedReader(fileReader);
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null){
                    String []data = currentLine.split("[\\s:]+", 4);
                    BigInteger key = BigIntegerHandler.fromStringToBigInteger(data[0]);
                    Long version = LongHandler.convertFromStringToLong(data[1]);
                    Long timestamp = LongHandler.convertFromStringToLong(data[2]);
                    byte[] dataBytes = data[3].getBytes(StandardCharsets.UTF_8);

                    ValueHandler valueHandler = new ValueHandler();
                    valueHandler.setVersion(version);
                    valueHandler.setTimestamp(timestamp);
                    valueHandler.setData(dataBytes);

                    storage.put(key, valueHandler);
                }
                fileReader.close();
            }catch (IOException ioException){
                LOGGER.log(Level.INFO, "" + ioException.getCause());
            }finally {
                try {
                    bufferedReader.close();
                }catch (IOException ioException){
                    LOGGER.log(Level.INFO, "" + ioException.getCause());
                }
            }
        }
        return storage;
    }

    public static synchronized boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        try{
            fileWriter = new FileWriter(PATH_FILE);
            bufferedWriter = new BufferedWriter(fileWriter);
            for(Map.Entry<BigInteger, ValueHandler> entry: hashMap.entrySet()){
                bufferedWriter.write(entry.getKey() + " : "
                        + entry.getValue().getVersion() + "  "
                        + entry.getValue().getTimestamp() + " "
                        + entry.getValue().getData());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, ioException.getMessage());
            LOGGER.log(Level.WARNING, ioException.getLocalizedMessage());
            return false;
        }finally {
            closeFile();
            LOGGER.log(Level.INFO, "Closed");
        }
        return true;
    }

    private boolean checkFileCreation(){
        if(Files.exists(Paths.get(PATH_FILE)))
            return true;
        return false;
    }

    private static void closeFile(){
        try {
            fileWriter.close();
        }catch (IOException ioException){
            LOGGER.log(Level.SEVERE, ioException.getMessage());
            LOGGER.log(Level.INFO, ioException.getLocalizedMessage());
        }
    }
}

