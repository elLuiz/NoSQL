package com.utils;

import java.sql.Timestamp;

public class LongHandler {
    public static Long convertFromStringToLong(String longString){
        return Long.parseLong(longString);
    }
    
    public static long convertTimestampToLong(Timestamp longTimestamp) {
    	return longTimestamp.getTime();
    }
    
    public static Timestamp convertLongToTimestamp(long timestampLong) {    	
    	return new Timestamp(timestampLong);
    }
}
