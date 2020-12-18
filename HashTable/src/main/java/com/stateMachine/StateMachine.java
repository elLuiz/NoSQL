package com.stateMachine;

import com.service.KeyValueService;
import com.utils.ValueHandler;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachine extends BaseStateMachine {
    private final ConcurrentHashMap<BigInteger, ValueHandler> hashMap = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Message> query(Message request){
        final String[] operationKey = request.getContent().toString(Charset.defaultCharset()).split(":");
        String response = selectService(operationKey[0], operationKey);
        return CompletableFuture.completedFuture(Message.valueOf(response));
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext transactionContext){
        final RaftProtos.LogEntryProto entry = transactionContext.getLogEntry();
        final String[] operation = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");
        // operation[0] = operation(set, get, del, delKV, testAndSet)
        String response = selectService(operation[0], operation);
        LOG.info("Response ratis: " + response);
        LOG.debug("Map: " + hashMap);
        return CompletableFuture.completedFuture(Message.valueOf(response));
    }

    private String selectService(String api, String[] data){
        String response = "";
        switch (api){
            case "set":
                response = StateMachineAPI.set(data, hashMap);
                return response;
            case "get":
                response = StateMachineAPI.get(data, hashMap);
                return response;
            case "del":
                response = StateMachineAPI.del(data, hashMap);
                return response;
            case "delKV":
                response = StateMachineAPI.delKV(data, hashMap);
                return response;
            case "testAndSet":
                response = StateMachineAPI.testAndSet(data, hashMap);
                return response;
            default:
                LOG.info("Wrong credencials");
        }

        return "";
    }

}
