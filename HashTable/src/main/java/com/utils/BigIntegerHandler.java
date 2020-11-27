package com.utils;

import com.google.protobuf.ByteString;
import com.sun.tools.sjavac.Log;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BigIntegerHandler {
    private static final Logger LOGGER = Logger.getLogger(BigIntegerHandler.class.getName());

    public static BigInteger fromStringToBigInteger(String string){
        BigInteger bigInteger = new BigInteger(string);
        return bigInteger;
    }

    public static BigInteger fromBytesStringToBigInteger(ByteString byteString){
        try{
            BigInteger bigInteger = new BigInteger(byteString.toString(Charset.defaultCharset()));
            return bigInteger;
        }catch(Exception exception){
            LOGGER.log(Level.SEVERE, exception.getMessage());
        }

        return null;
    }
}
