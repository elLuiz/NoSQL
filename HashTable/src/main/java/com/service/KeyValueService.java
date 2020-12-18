package com.service;

import com.google.protobuf.ByteString;
import com.hashTable.ResponseBuilder;
import com.hashTable.hashTableServiceGrpc;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.Get;
import com.hashTable.KeyValue.Del;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Response;
import io.grpc.stub.StreamObserver;
import org.apache.ratis.client.RaftClient;
import com.client.ratis.RatisClient;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;

public class KeyValueService extends hashTableServiceGrpc.hashTableServiceImplBase {
    private final static Logger LOGGER = Logger.getLogger(KeyValueService.class.getName());
    private RaftClient raftClient;

    public KeyValueService(){
        RatisClient ratisConnection = new RatisClient();
        ratisConnection.connectToRatisServer();
        raftClient = ratisConnection.setClientProprieties();
    }

    @Override
    public synchronized void set(Set request, StreamObserver<Response> responseObserver){
        ByteString key = request.getKey();
        String message = "set:" + key.toString(Charset.defaultCharset()) + ":" + request.getTimestamp() + ":" + request.getData().toString(Charset.defaultCharset());
        String response =  "";

        try {
            LOGGER.log(Level.INFO, "Sending request." + message);
            RaftClientReply reply;
            reply = raftClient.send(Message.valueOf(message));
            response = reply.getMessage().getContent().toString(Charset.defaultCharset());
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, "Error: " + ioException.getMessage());
            LOGGER.log(Level.WARNING, "Cause: " + ioException.getCause());
        }
        createResponse(responseObserver, response.split(":"));
    }

//    @Override
//    public synchronized void get(Get request, StreamObserver<Response> responseObserver){
//        ByteString key = request.getKey();
//        String message = "get:" + key;
//        LOGGER.log(Level.INFO, "Key: " + key);
//
//        try {
//            LOGGER.log(Level.INFO, "Sending request.");
//            RaftClientReply reply;
//            reply = raftClient.sendReadOnly(Message.valueOf(message));
//            String response = reply.getMessage().getContent().toString(Charset.defaultCharset());
//            LOGGER.log(Level.INFO, "Sent");
//            System.out.println(response);
//        }catch (IOException ioException){
//            LOGGER.log(Level.WARNING, "Error: " + ioException.getMessage());
//            LOGGER.log(Level.WARNING, "Cause: " + ioException.getCause());
//        }
//
//        createResponse(responseObserver, null, "SUCESS");
//    }
//
//    @Override
//    public synchronized void del(Del request, StreamObserver<Response> responseStreamObserver){
//        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
//        ValueHandler valueHandler;
//        String messageStatus;
//        if((valueHandler = storage.remove(key)) == null){
//            messageStatus = "ERROR";
//        }
//        else{
//            messageStatus = "SUCCESS";
//            keyValueManager.notify(storage);
//        }
//        createResponse(responseStreamObserver, valueHandler, messageStatus);
//    }
//
//    @Override
//    public synchronized void delKV(KeyValue.DelKV request, StreamObserver<Response> responseStreamObserver){
//        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
//        long version = request.getVersion();
//        ValueHandler valueHandler;
//        String messageStatus;
//        if((valueHandler = storage.get(key)) == null){
//            messageStatus = "ERROR_NE";
//        }else{
//            if(version == valueHandler.getVersion()){
//                storage.remove(key);
//                keyValueManager.notify(storage);
//                messageStatus = "SUCCESS";
//            }else{
//                messageStatus = "ERROR_WV";
//            }
//        }
//        createResponse(responseStreamObserver, valueHandler, messageStatus);
//    }
//
//    @Override
//    public synchronized void testAndSet(TestAndSet request, StreamObserver<Response> responseObserver){
//        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
//        Long version = request.getVersion();
//        LOGGER.log(Level.INFO, "" + version);
//        ValueHandler valueHandlerGet;
//        String messageStatus;
//
//        if ((valueHandlerGet = storage.get(key)) == null){
//            messageStatus = "ERROR_NE";
//        } else {
//            if (valueHandlerGet.getVersion() == version) {
//                valueHandlerGet = ValueHandler.testAndSetValueHandler(request);
//                storage.put(key, valueHandlerGet);
//                messageStatus = "SUCCESS";
//                keyValueManager.notify(storage);
//            } else {
//                messageStatus = "ERROR_WV";
//            }
//        }
//        createResponse(responseObserver, valueHandlerGet, messageStatus);
//    }

    private void createResponse(StreamObserver<Response> responseStreamObserver, String []result){
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.setResponseMessage(result[0]);

        if(result.length == 2){
            responseStreamObserver.onNext(responseBuilder.buildResponse(null));
        }else{
            ValueHandler valueHandler = new ValueHandler();
            long version = LongHandler.convertFromStringToLong(result[1]);
            long timestamp = LongHandler.convertFromStringToLong(result[2]);
            byte[] data = result[3].getBytes(Charset.defaultCharset());

            valueHandler.setVersion(version);
            valueHandler.setTimestamp(timestamp);
            valueHandler.setData(data);
            responseStreamObserver.onNext(responseBuilder.buildResponse(valueHandler));
        }
        responseStreamObserver.onCompleted();
    }
}
