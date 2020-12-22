package com.server;

import com.service.KeyValueService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyValueGRPCServer extends Thread{
    private static final Logger LOGGER = Logger.getLogger(KeyValueGRPCServer.class.getName());
    private int port;

    public KeyValueGRPCServer(int port){
        this.port = port;
    }

    @Override
    public void run(){
        try{
            startGRPCServer(port);
        }catch (IOException ioException){
            LOGGER.log(Level.WARNING, "Error: " + ioException.getMessage());
            LOGGER.log(Level.WARNING, "Cause: " + ioException.getCause());
        }
    }
    public void startGRPCServer(int port) throws IOException {
        KeyValueService service = new KeyValueService();
        ServerBuilder serverBuilder = ServerBuilder.forPort(port);
        serverBuilder.addService(service);
        Server server = serverBuilder.build();
        server.start();
        System.out.println("GRPC server started. 127.0.0.1:"  + server.getPort());
        try{
            server.awaitTermination();
        }catch (InterruptedException interruptedException){
            LOGGER.log(Level.WARNING, interruptedException.getMessage());
        }
    }

}
