package com.utils;

import com.google.protobuf.ByteString;

import java.nio.charset.Charset;

public class ByteStringHandler {
    public static ByteString convertFromStringToByteString(String string){
        return ByteString.copyFrom(string, Charset.defaultCharset());
    }
}


