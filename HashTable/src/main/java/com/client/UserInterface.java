package com.client;

import java.util.Scanner;

public class UserInterface {
    public static void main(String []args){
        Scanner scanner = new Scanner(System.in);
        String chosenService = "";
        SimpleClient.connectToServer();
        while(chosenService.compareToIgnoreCase("6")!=0)
        {
            SimpleClient simpleClient = new SimpleClient();
            
            System.out.println("Escolha o numero que representa o servico que deseja utilizar:");
            System.out.println("1- Set Service:");
            System.out.println("2- Get Service:");
            System.out.println("3- Del Service:");
            System.out.println("4- DelKeyVersion Service:");
            System.out.println("5- TestAndSet Service:");
            System.out.println("6- End program:");

            chosenService = scanner.nextLine();
            switch (chosenService)
            {
                case "1":
                    simpleClient.set();
                    break;
                case "2":
                    simpleClient.get();
                    break;
                case "3":
                    simpleClient.del();
                    break;
                case "4":
                    simpleClient.delKeyVersion();
                    break;
                case "5":
                    simpleClient.testAndSet();
                    break;
                case "6":
                    break;
                default:
                    System.out.println("Insert a valid value.");
            }
        }

    }
}
