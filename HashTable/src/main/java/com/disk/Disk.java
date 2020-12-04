package com.disk;

import com.hashTable.KeyValue.Response;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public interface Disk {
    String PATH_FILE = "hashTable.ser";
    ConcurrentHashMap<BigInteger, ValueHandler> retrieveRecords();
}
