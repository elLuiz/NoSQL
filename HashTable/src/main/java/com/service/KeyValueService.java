package com.service;

import com.disk.Disk;
import com.disk.DiskOperations;
import com.hashTable.ResponseBuilder;
import com.hashTable.hashTableServiceGrpc;
import com.utils.BigIntegerHandler;
import com.utils.ValueHandler;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.hashTable.KeyValue.Set;
import com.hashTable.KeyValue.Get;
import com.hashTable.KeyValue.Del;
import com.hashTable.KeyValue.TestAndSet;
import com.hashTable.KeyValue.Response;
import io.grpc.stub.StreamObserver;

public class KeyValueService extends hashTableServiceGrpc.hashTableServiceImplBase {
    private KeyValueManager keyValueManager = KeyValueManager.getInstance();
    private ConcurrentHashMap<BigInteger, ValueHandler> storage;
    private final static Logger LOGGER = Logger.getLogger(KeyValueService.class.getName());

    public KeyValueService(){
        Disk diskOperation = new DiskOperations();
        storage = diskOperation.retrieveRecords();
        if(storage == null){
            LOGGER.log(Level.WARNING, "Could not retrieve data");
        }else
            LOGGER.log(Level.INFO, "Map loaded: " + storage.hashCode());
    }

    public synchronized void set(Set request, StreamObserver<Response> responseObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        String messageStatus = "";
        ValueHandler valueHandler;

        LOGGER.log(Level.INFO, "" +key);
        if((valueHandler = storage.get(key)) == null){
            valueHandler = ValueHandler.setValueHandler(request);
            messageStatus = "SUCCESS";
            storage.put(key, valueHandler);
            keyValueManager.notify(storage);
            // Pois v' = Null
            valueHandler = null;
        }else{
            messageStatus = "ERROR";
        }

        createResponse(responseObserver, valueHandler, messageStatus);
    }

    @Override
    public synchronized void get(Get request, StreamObserver<Response> responseObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        String messageStatus;
        ValueHandler valueHandler;

        if((valueHandler = storage.get(key)) == null){
            messageStatus = "ERROR";
        }else{
            messageStatus = "SUCCESS";
        }
        createResponse(responseObserver, valueHandler, messageStatus);
    }

    @Override
    public synchronized void del(Del request, StreamObserver<Response> responseStreamObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        long version = request.getVersion();
        if(version > 0)
            delKeyVersion(key, version, responseStreamObserver);
        else
            delKey(key, responseStreamObserver);
    }

    public synchronized void delKey(BigInteger key, StreamObserver<Response> responseStreamObserver){
        ValueHandler valueHandler;
        String messageStatus;
        if((valueHandler = storage.remove(key)) == null){
            messageStatus = "ERROR";
        }
        else{
            messageStatus = "SUCCESS";
            keyValueManager.notify(storage);
        }
        createResponse(responseStreamObserver, valueHandler, messageStatus);
    }

    public synchronized void delKeyVersion(BigInteger key, long version, StreamObserver<Response> responseStreamObserver){
        ValueHandler valueHandler;
        String messageStatus;
        if((valueHandler = storage.get(key)) == null){
            messageStatus = "ERROR_NE";
        }else{
            if(version == valueHandler.getVersion()){
                storage.remove(key);
                keyValueManager.notify(storage);
                messageStatus = "SUCCESS";
            }else{
                messageStatus = "ERROR_WV";
            }
        }
        createResponse(responseStreamObserver, valueHandler, messageStatus);
    }

    @Override
    public synchronized void testAndSet(TestAndSet request, StreamObserver<Response> responseObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        Long version = request.getVersion();
        ValueHandler valueHandlerGet;
        String messageStatus;

        if ((valueHandlerGet = storage.get(key)) == null){
            messageStatus = "ERROR_NE";
        } else {
            if (valueHandlerGet.getVersion() == version) {
                valueHandlerGet = ValueHandler.testAndSetValueHandler(request);
                storage.put(key, valueHandlerGet);
                messageStatus = "SUCCESS";
                keyValueManager.notify(storage);
            } else {
                messageStatus = "ERROR_WV";
            }
        }

        createResponse(responseObserver, valueHandlerGet, messageStatus);
    }

    private void createResponse(StreamObserver<Response> responseStreamObserver, ValueHandler valueHandler, String message){
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.setResponseMessage(message);
        responseStreamObserver.onNext(responseBuilder.buildResponse(valueHandler));
        responseStreamObserver.onCompleted();
    }
}
