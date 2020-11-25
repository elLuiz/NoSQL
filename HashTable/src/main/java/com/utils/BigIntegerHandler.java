package com.utils;

import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class BigIntegerHandler {
    public static BigInteger fromStringToBigInteger(String string){
        BigInteger bigInteger = new BigInteger(string);
        return bigInteger;
    }

    public static BigInteger fromBytesStringToBigInteger(ByteString byteString){
        BigInteger bigInteger = new BigInteger(byteString.toString());
        return bigInteger;
    }
}
