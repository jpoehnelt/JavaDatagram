/*
 * CS 460 Datagram Project
 * Authors: Justin Poehnelt, Matt Siewierski
 */
package datagram;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.*;
import java.net.*;


public class Server {

    /**
     * Server run loop.
     *
     * @param port Integer
     */
    public static void runServer(int port) {
        DatagramSocket socket;

        try {
            socket = new DatagramSocket(port);
        } catch (IOException e) {
            System.out.println("Failed to create socket");
            return;
        }

        byte[] buffer = new byte[512];

        // Create a packet to receive data into the buffer
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {

            System.out.println("");
            System.out.println("Waiting for request...");
            System.out.println("");

            try {
                socket.receive(packet);
                System.out.println(String.format("Received packet from: %s:%d", packet.getAddress(), packet.getPort()));
                System.out.println(String.format("Packet Type: %d", (int) buffer[0]));
                System.out.println(String.format("Request Data: %s", new String(buffer)));
            } catch (IOException e) {
                System.out.println("Failed to receive packet");
                continue;
            }

            switch ((int) buffer[0]) {
                case 1:
                    buffer[0] = (byte) 2;
                    break;
                case 3:
                    buffer[0] = (byte) 4;
                    Date date = new Date();
                    DateFormat format = new SimpleDateFormat("HH:mm");
                    String time = format.format(date);
                    System.arraycopy(time.getBytes(), 0, buffer, 1, time.getBytes().length);
                    break;
                case 5:
                    buffer[0] = (byte) 6;
                    try {
                        InetAddress inetAddress = InetAddress.getByName(new String(buffer).substring(1));
                        System.arraycopy(inetAddress.getHostAddress().getBytes(), 0, buffer, 1, inetAddress.getHostAddress().getBytes().length);
                    } catch (UnknownHostException e) {
                        continue;
                    }
                    break;
            }


            System.out.println("");

            packet.setData(buffer);

            try {
                socket.send(packet);
                System.out.println(String.format("Sent packet to: %s:%d", packet.getAddress(), packet.getPort()));
                System.out.println(String.format("Packet Type: %d", (int) buffer[0]));
                System.out.println(String.format("Data: %s", new String(buffer)));
            } catch (IOException e) {
                System.out.println("Could not send socket.");
            }
        }
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

        runServer(port);
    }
}
