package com.disk;

import com.hashTable.KeyValue.Response;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public interface Disk {
    String PATH_FILE = "hashTable.dat";
    ValueHandler read(BigInteger key);
    boolean write(ConcurrentHashMap<BigInteger, ValueHandler> hashMap, int index);
    boolean delete(BigInteger key);
    boolean delete(BigInteger key, long version);
    boolean update(BigInteger key, ValueHandler valueHandler, long version);
}
