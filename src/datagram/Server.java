/*
 * CS 460 Datagram Project
 * Authors: Justin Poehnelt, Matt Siewierski
 */
package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Server {

    /**
     * Server run loop.
     *
     * @param port Integer
     */
    public static void runServer(int port) {
        DatagramSocket serverSocket;
        String packetData;

        try {
            serverSocket = new DatagramSocket(port);
        } catch (IOException e) {
            System.out.println("Failed to create socket");
            return;
        }

        byte[] buffer = new byte[512];

        // Create a packet to receive data into the buffer
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            
            System.out.println("Waiting for datagram...");
            try {
                serverSocket.receive(packet);
                
                processPacket(packet.getData());
                System.out.println(packet.getAddress());
                System.out.println(packet.getPort());

                int packetType = (int) buffer[0];
                byte[] incData = new byte[512];
                System.arraycopy(buffer, 1, incData, 0, buffer.length-1);
                System.out.println(new String(incData));
                
                switch (packetType) {
                    case 1:
                        buffer[0] = (byte) 2;
                        break;
                    case 3:
                        buffer[0] = (byte) 4;
                        break;
                    case 5:
                        buffer[0] = (byte) 6;
                        InetAddress inetAddress =  InetAddress.getByName(new String(incData));
                        System.out.println(inetAddress.getHostAddress());
                        System.arraycopy(inetAddress.getHostAddress().getBytes(), 0, buffer, 1, inetAddress.getHostAddress().getBytes().length);
                        break;
                }
                
                packet.setData(buffer);
                
                try {
                    serverSocket.send(packet);
                } catch (IOException e) {
                    System.out.println("Could not send socket.");
                }
                
            } catch (IOException e) {
                System.out.println("Failed to receive packet");
            }
        }
    }

    private static void processPacket(byte [] packetBuffer) {
        int port, type; 
        InetAddress clientAddress;

        /*
        type = (int) packetBuffer[0] - 48;
        
        switch (type) {
            case 1:
                System.out.println(type);
                break;
            case 3:
                break;
            case 5:
                break;
        }
        */
        
        
    }

    /**
     * Starts the server with port as argument.
     *
     * @param args
     */
    public static void main(String[] args) {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = 8989;
        }

        System.out.println("Starting Java Server");

        runServer(port);
    }
}
