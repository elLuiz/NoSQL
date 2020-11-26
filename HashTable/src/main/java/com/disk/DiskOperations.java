package com.disk;

import com.utils.BigIntegerHandler;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DiskOperations implements Disk{
    private BufferedWriter bufferedWriter = null;
    private final static Logger LOGGER = Logger.getLogger(DiskOperations.class.getName());

    @Override
    public ValueHandler read(BigInteger key) {
        BufferedReader bufferedReader = null;
        ValueHandler valueHandler = new ValueHandler();
        try{
            FileReader fileReader = new FileReader(PATH_FILE);
            bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            while((currentLine = bufferedReader.readLine()) != null){
                String [] data = currentLine.split("[\\s:]+", 4);
                BigInteger bigIntegerKey = BigIntegerHandler.fromStringToBigInteger(data[0]);
                if((bigIntegerKey.compareTo(key) == 0)){
                    String version = data[1];
                    String timestamp = data[2];
                    String dataBytes = data[3];

                    valueHandler.setVersion(LongHandler.convertFromStringToLong(version));
                    valueHandler.setTimestamp(LongHandler.convertFromStringToLong(timestamp));
                    valueHandler.setData(ByteStringHandler.convertFromStringToByteString(dataBytes));

                    return valueHandler;
                }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally {
            try{
                bufferedReader.close();
            }catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public synchronized boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap){
        try{
            FileWriter fileWriter = new FileWriter(PATH_FILE);
            bufferedWriter = new BufferedWriter(fileWriter);
            for(Map.Entry<BigInteger, ValueHandler> entry: hashMap.entrySet()){
                bufferedWriter.write(entry.getKey() + " : "
                        + entry.getValue().getVersion() + "  "
                        + entry.getValue().getTimestamp() + " "
                        + entry.getValue().getData());
                bufferedWriter.newLine();
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
            return false;
        }finally {
            try{
                bufferedWriter.close();
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public synchronized boolean delete(BigInteger key) {
        return true;
    }

    @Override
    public synchronized boolean delete(BigInteger key, long version) {
        return true;
    }

    @Override
    public boolean update(BigInteger key, ValueHandler valueHandler, long version) {
        return false;
    }
}
