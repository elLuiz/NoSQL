package com.hashTable;

import com.disk.Disk;
import com.disk.DiskOperations;
import com.google.protobuf.ByteString;
import com.sun.tools.sjavac.Log;
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

public class KeyValueService extends hashTableServiceGrpc.hashTableServiceImplBase {
    private static final long WRITE_DELAY = 50;
    private ConcurrentHashMap<BigInteger, ValueHandler> storage = new ConcurrentHashMap<>();
    private Logger logger = Logger.getLogger(KeyValueService.class.getName());

    @Override
    public synchronized void set(Set request, io.grpc.stub.StreamObserver<com.hashTable.KeyValue.Response> responseObserver){
        BigInteger key = BigIntegerHandler.fromBytesStringToBigInteger(request.getKey());
        ResponseBuilder responseBuilder = new ResponseBuilder();
        Disk diskOperation = new DiskOperations();

        if(storage.get(key) == null){
            ValueHandler valueHandler = ValueHandler.setValueHandler(request);
            storage.put(key, valueHandler);
            delayWrite();
            //OBS: Esse valor será incrementado, depende da explicação do Lásaro para a escrita em disco
            boolean operationResult = diskOperation.write(storage, 0);
            if(operationResult){
                responseBuilder.setResponseMessage("SUCCESS");
                responseObserver.onNext(responseBuilder.buildResponse(valueHandler));
                responseObserver.onCompleted();
                return;
            }
        }else{
            responseBuilder.setResponseMessage("ERROR");
            responseObserver.onNext(responseBuilder.buildResponse(null));
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
