package com.utils;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue.Set;

public class ValueHandler {
    private long version;
    private long timestamp;
    private ByteString data;

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

    public ByteString getData() {
        return data;
    }

    public void setData(ByteString data) {
        this.data = data;
    }

    public static ValueHandler setValueHandler(Set request){
        ValueHandler valueHandler = new ValueHandler();
        valueHandler.setData(request.getData());
        valueHandler.setTimestamp(request.getTimestamp());
        valueHandler.setVersion(1);

        return valueHandler;
    }
}
