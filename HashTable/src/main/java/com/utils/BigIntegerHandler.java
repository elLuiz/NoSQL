package com.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BigIntegerHandler {
    public static BigInteger fromBytesStringtoBigInteger(String string){
        BigInteger bigInteger = new BigInteger(string);
        return bigInteger;
    }
}
