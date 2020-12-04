package com.server;

import com.disk.DiskOperations;
import com.service.KeyValueManager;
import com.service.KeyValueService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.logging.Logger;

public class KeyValueServer {
    private static ServerPersistence serverPersistence;
    private final static Logger LOGGER = Logger.getLogger(KeyValueServer.class.getName());
    public static void main(String []args){
        try{
            serverPersistence = new ServerPersistence();
            subscribeToKeyValue();
            configServerPersistenceThread();
            configServerPersistenceThread();
            DiskOperations.openFile();
            startServer();
        }catch(Exception exception){
            System.out.println("Cause: " + exception.getCause());
            System.out.println("Message: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static void startServer() throws IOException, InterruptedException{
        KeyValueService service = new KeyValueService();
        ServerBuilder serverBuilder = ServerBuilder.forPort(9090);
        serverBuilder.addService(service);
        Server server = serverBuilder.build();
        server.start();
        System.out.println("Server started. *:" + server.getPort());
        server.awaitTermination();
    }

    public static void subscribeToKeyValue(){
        KeyValueManager keyValueManager = KeyValueManager.getInstance();
        keyValueManager.subscribe(serverPersistence);
    }

    public static void configServerPersistenceThread(){
        serverPersistence.setWriteTimeOut(10000);
        serverPersistence.start();
    }
}
