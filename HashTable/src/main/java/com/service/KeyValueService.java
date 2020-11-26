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
    private static final long WRITE_DELAY = 50;
    private ConcurrentHashMap<BigInteger, ValueHandler> storage = new ConcurrentHashMap<>();
    private Logger logger = Logger.getLogger(KeyValueService.class.getName());

    @Override
    public synchronized void set(Set request, StreamObserver<Response> responseObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        ResponseBuilder responseBuilder = new ResponseBuilder();
        Disk diskOperation = new DiskOperations();
        ValueHandler valueHandler;

        if((valueHandler = storage.get(key)) == null){
            valueHandler = ValueHandler.setValueHandler(request);
            storage.put(key, valueHandler);
            delayWrite();
            boolean operationResult = diskOperation.write(storage);
            if(operationResult){
                responseBuilder.setResponseMessage("SUCCESS");
                responseObserver.onNext(responseBuilder.buildResponse(null));
                responseObserver.onCompleted();
            }
        }else{
            responseBuilder.setResponseMessage("ERROR");
            responseObserver.onNext(responseBuilder.buildResponse(valueHandler));
            responseObserver.onCompleted();
        }
    }

    @Override
    public synchronized void get(Get request, StreamObserver<Response> responseObserver){}

    @Override
    public synchronized void del(Del request, StreamObserver<Response> responseStreamObserver){}

    @Override
    public synchronized void testAndSet(TestAndSet request, StreamObserver<Response> responseObserver){
        // Função não finalizada - Entender toda a estrutura das condições
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        Long version = request.getVersion();
        ValueHandler valueHandlerGet;
        ResponseBuilder responseBuilder = new ResponseBuilder();
        Disk diskOperation = new DiskOperations();

        if ((valueHandlerGet = storage.get(key)) == null){
            responseBuilder.setResponseMessage("ERROR_NE");
            responseObserver.onNext(responseBuilder.buildResponse(null));
        } else {
            if (valueHandlerGet.getVersion() == version) {
                valueHandlerGet = ValueHandler.testAndSetValueHandler(request);
                storage.put(key, valueHandlerGet);
                delayWrite();
                boolean operationResult = diskOperation.write(storage);
                if (operationResult) {
                    responseBuilder.setResponseMessage("SUCCESS");
                    responseObserver.onNext(responseBuilder.buildResponse(valueHandlerGet));
                }
            } else {
//                responseBuilder.setResponseMessage("ERROR_WV");
            }
        }
    }

    private void delayWrite(){
        try{
            Thread.sleep(WRITE_DELAY);
        }catch(InterruptedException interruptedException){
            Thread.currentThread().interrupt();
            logger.log(Level.WARNING, interruptedException.getMessage());
        }
    }

}
