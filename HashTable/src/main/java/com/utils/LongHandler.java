package com.utils;

import com.google.protobuf.ByteString;

public class LongHandler {
    public static Long convertFromStringToLong(String longString){
        return Long.parseLong(longString);
    }

    public static ByteString convertFromLongToByteString(long longValue){
        return ByteStringHandler.convertFromStringToByteString(String.valueOf(longValue));
    }

}
