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
    private final Map<BigInteger, ValueHandler> hashMap = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Message> query(Message request){
        final String[] operationKey = request.getContent().toString(Charset.defaultCharset()).split(":");
        LOG.debug(operationKey[0]);
        return CompletableFuture.completedFuture(Message.valueOf(operationKey[0]));
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext transactionContext){
        final RaftProtos.LogEntryProto entry = transactionContext.getLogEntry();
        final String[] operation = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");
        // operation[0] = operation(set, get, del, delKV, testAndSet)
        selectService(operation[0]);

        return null;
    }

    private void selectService(String api){
        switch (api){
            case "set":
                break;
            case "get":
                break;
            case "del":
                break;
            case "delKV":
                break;
            case "testAndSet":
                break;
            default:
                LOG.info("Wrong credencials");
        }
    }

}
