package com.service;

import com.google.protobuf.ByteString;
import com.hashTable.KeyValue;
import com.hashTable.ResponseBuilder;
import com.hashTable.hashTableServiceGrpc;
import com.utils.BigIntegerHandler;
import com.utils.LongHandler;
import com.utils.ValueHandler;
import java.io.IOException;
import java.math.BigInteger;
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
        String message = "set:" + key.toString(Charset.defaultCharset()) + ":" + request.getTimestamp() + ":" + request.getData().toStringUtf8();
        String response =  sendTransactionalRequest(message);
        createResponse(responseObserver, response.split(":"));
    }

    @Override
    public synchronized void get(Get request, StreamObserver<Response> responseObserver){
        ByteString key = request.getKey();
        String message = "get:" + key.toString(Charset.defaultCharset());
        String response = sendQuery(message);
        createResponse(responseObserver, response.split(":"));
    }

    @Override
    public synchronized void del(Del request, StreamObserver<Response> responseStreamObserver){
        ByteString key = request.getKey();
        String message = "del:" + key.toString(Charset.defaultCharset());
        String response = sendTransactionalRequest(message);
        createResponse(responseStreamObserver, response.split(":"));
    }

    @Override
    public synchronized void delKV(KeyValue.DelKV request, StreamObserver<Response> responseStreamObserver){
        ByteString key = request.getKey();
        long version = request.getVersion();
        String message = "delKV:" + key.toString(Charset.defaultCharset()) + ":" + version;
        String response = sendTransactionalRequest(message);
        createResponse(responseStreamObserver, response.split(":"));
    }

    @Override
    public synchronized void testAndSet(TestAndSet request, StreamObserver<Response> responseObserver){
        ByteString key = request.getKey();
        String message = "testAndSet:" + key.toString(Charset.defaultCharset()) +
                ":" + request.getValue().getTimestamp() +
                ":" + request.getValue().getData().toStringUtf8() +
                ":" + request.getVersion();
        String response = sendTransactionalRequest(message);
        createResponse(responseObserver, response.split(":"));
    }

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

    private String sendTransactionalRequest(String request){
        String response =  "";
        try {
            LOGGER.log(Level.INFO, "Sending transaction:" + request);
            RaftClientReply reply;
            reply = raftClient.send(Message.valueOf(request));
            response = reply.getMessage().getContent().toString(Charset.defaultCharset());
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, "Error: " + ioException.getMessage());
            LOGGER.log(Level.WARNING, "Cause: " + ioException.getCause());
        }
        return response;
    }

    private String sendQuery(String query){
        String response =  "";
        try {
            LOGGER.log(Level.INFO, "Sending query:" + query);
            RaftClientReply reply;
            reply = raftClient.sendReadOnly(Message.valueOf(query));
            response = reply.getMessage().getContent().toString(Charset.defaultCharset());
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, "Error: " + ioException.getMessage());
            LOGGER.log(Level.WARNING, "Cause: " + ioException.getCause());
        }
        return response;
    }
}
