package com.hashTable;

import com.utils.ValueHandler;
import com.hashTable.KeyValue.Response;
import com.hashTable.KeyValue.Null;
import com.hashTable.KeyValue.Value;


public class ResponseBuilder {
    private String responseMessage;

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Response buildResponse(ValueHandler valueHandler){
        Response.Builder responseBuilder = Response.newBuilder();
        if(valueHandler == null){
            Null.Builder nullBuilder = Null.newBuilder();
            responseBuilder.setMessage(this.responseMessage);
            responseBuilder.setNull(nullBuilder.build());
        }else{
            Value.Builder valueBuilder = Value.newBuilder();
            valueBuilder.setData(valueHandler.getData());
            valueBuilder.setTimestamp(valueHandler.getTimestamp());
            valueBuilder.setVersion(valueHandler.getVersion());
            responseBuilder.setMessage(this.responseMessage);
            responseBuilder.setValue(valueBuilder.build());
        }

        return responseBuilder.build();
    }
}
