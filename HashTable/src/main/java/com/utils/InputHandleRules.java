package com.utils;

import com.client.exceptions.ClientInputException;
import com.google.protobuf.ByteString;

public class InputHandleRules {
	public static void checkNumericNotNull(ByteString valueByteString, String fieldName) throws ClientInputException {
		String value = null;
		
		if(valueByteString != null) {
			value = valueByteString.toStringUtf8();
		}
		
    	if(value==null || value.isEmpty() || value.length() == 0 || !isNumericInt(value)){
    		throw new ClientInputException(fieldName);
    	}
    }
	
	public static void checkNotNull(Object value, String fieldName) throws ClientInputException {
    	if(value==null){
    		throw new ClientInputException(fieldName);
    	}
    }

	public static boolean isNumericInt(String str) {
		boolean isNumeric = true;
		   try {
		     int i = Integer.parseInt(str);
		   } catch(NumberFormatException nfe) {
			   isNumeric = false;
		   }
		   return isNumeric;
	}
}
