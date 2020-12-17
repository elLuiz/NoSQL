package com.stateMachine;

import com.service.KeyValueService;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachine extends BaseStateMachine {
    private final Map<String, String> hashMap = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Message> query(Message request){
        final String[] operationKey = request.getContent().toString(Charset.defaultCharset()).split(":");
        LOG.debug(operationKey[0]);
        return CompletableFuture.completedFuture(Message.valueOf(operationKey[0]));
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
