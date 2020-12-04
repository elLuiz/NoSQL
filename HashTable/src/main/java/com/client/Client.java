package com.client;

import com.client.api.ClientAPI;

import java.util.Scanner;

public class Client {
    public static void main(String []args){
        ClientAPI.connectToServer();
        selectService();
    }

    private static void selectService(){
        Scanner scanner = new Scanner(System.in);
        int chosenService = 0;
        ClientAPI simpleClient = new ClientAPI();
        while(chosenService != 6)
        {
            displayMenu();
            chosenService = scanner.nextInt();
            switch (chosenService)
            {
                case 1:
                    simpleClient.set();
                    break;
                case 2:
                    simpleClient.get();
                    break;
                case 3:
                    simpleClient.del();
                    break;
                case 4:
                    simpleClient.delKeyVersion();
                    break;
                case 5:
                    simpleClient.testAndSet();
                    break;
                case 6:
                    System.exit(1);
                default:
                    System.out.println("Insert a valid value.");
            }
        }
    }

    private static void displayMenu(){
        System.out.println("Choose an option:");
        System.out.println("1- Set Service");
        System.out.println("2- Get Service");
        System.out.println("3- Del Service");
        System.out.println("4- DelKeyVersion Service");
        System.out.println("5- TestAndSet Service");
        System.out.println("6- End program");
    }


}
