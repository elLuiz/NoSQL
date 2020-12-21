package com.connection;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConnectionProps {
    private static final Logger LOGGER = Logger.getLogger(ConnectionProps.class.getName());

    protected static Map<String, InetSocketAddress> readConfigFile(){
        File configurationFile = new File("configuration/ports.txt");
        Map<String, InetSocketAddress> configMap = new HashMap<>();

        try{
            Scanner fileData = new Scanner(configurationFile);
            while (fileData.hasNextLine()){
                String[] config = fileData.nextLine().split(" ");
                String serverID = config[0];
                int port = Integer.parseInt(config[1]);
                configMap.put(serverID, new InetSocketAddress("127.0.0.1", port));
            }
        }catch (IOException ioException){
            LOGGER.warning("Error: " + ioException.getMessage());
            LOGGER.warning("Cause: " + ioException.getCause());
        }

        return configMap;
    }
}
