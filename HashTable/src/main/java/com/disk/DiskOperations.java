package com.disk;

import com.utils.BigIntegerHandler;
import com.utils.ByteStringHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
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

    //Index deve ser atomico, e ser incrementado a cada escrita
    @Override
    public synchronized boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap, int index){
        try{
            FileWriter fileWriter = new FileWriter(PATH_FILE);
            bufferedWriter = new BufferedWriter(fileWriter);
            BigInteger key = getNextItemKey(hashMap, index);
            ValueHandler valueHandler = hashMap.get(key);
            bufferedWriter.write(key.toString() + " : " + valueHandler.getVersion() + "  " + valueHandler.getTimestamp() + " " + valueHandler.getData()) ;
            bufferedWriter.newLine();
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
    public ValueHandler search(BigInteger key) {
        return null;
    }

    private BigInteger getNextItemKey(ConcurrentHashMap<BigInteger, ValueHandler>  concurrentHashMap,int index){
        ArrayList<BigInteger> arrayList = new ArrayList<>(concurrentHashMap.keySet());
        return arrayList.get(index);
    }
}
