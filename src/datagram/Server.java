/*
 * CS 460 Number Guessing Project
 * Authors: Justin Poehnelt, Matt Siewierski
*/

package datagram;

import java.io.*;
import java.net.*;

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

            } catch (IOException e) {
                System.out.println("Failed to receive packet");
            }
        }
    }

    private static void processPacket(byte [] packetBuffer) {
        int port; InetAddress clientAddress;

        System.out.println(new String(packetBuffer));
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
