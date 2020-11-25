package com.hashTable;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class KeyValueServer {
    public static void main(String []args){
        try{
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
}
