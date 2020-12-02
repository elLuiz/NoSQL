package com.utils;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Value;

public class ValueHandler {
    private long version;
    private long timestamp;
    private byte[] data;
    private Value value;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static ValueHandler setValueHandler(Set request){
        ValueHandler valueHandler = new ValueHandler();
        valueHandler.setData(request.getData().toByteArray());
        valueHandler.setTimestamp(request.getTimestamp());
        valueHandler.setVersion(1);

        return valueHandler;
    }

    public static ValueHandler testAndSetValueHandler(TestAndSet request){
        ValueHandler valueHandler = new ValueHandler();
        valueHandler.setData(request.getValue().getData().toByteArray());
        valueHandler.setTimestamp(request.getValue().getTimestamp());
        valueHandler.setVersion(request.getVersion() + 1);

        return valueHandler;
    }

}
