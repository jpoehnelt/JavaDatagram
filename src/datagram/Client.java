/*
 * CS 460 Datagram Project
 * Authors: Justin Poehnelt, Matt Siewierski
 */
package datagram;

import java.util.Arrays;
import java.net.*;
import java.io.*;

public class Client {


    public static void run(String host, int port, String service, String arg) {
        DatagramSocket socket;
        InetAddress address;
        byte buffer[] = new byte[512];

        try {
            socket = new DatagramSocket();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Get the address
        try {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            return;
        }

        // build buffer for packet

        if (service.equals("echo")) {
            buffer[0] = (byte) 1;
            System.arraycopy(arg.getBytes(), 0, buffer, 1, arg.length());
        } else if (service.equals("time")) { // get time
            buffer[0] = (byte) 3;
        } else if (service.equals("lookup")) { // dns lookup
            buffer[0] = (byte) 5;
            System.arraycopy(arg.getBytes(), 0, buffer, 1, arg.length());
        } else {
            return;
        }

        // Create a packet to receive data into the buffer
        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, address, port);

        System.out.println("");

        try {
            socket.send(packet);
            System.out.println(String.format("Sent packet to: %s:%d", packet.getAddress(), packet.getPort()));
            System.out.println(String.format("Packet Type: %d", (int) buffer[0]));
            System.out.println(String.format("Data: %s", new String(buffer)));
        } catch (IOException e) {
            System.out.println("Could not send socket.");
        }

        // Wait for response packet
        System.out.println("");
        System.out.println("Waiting for response...");
        System.out.println("");

        try {
            socket.receive(packet);
            System.out.println(String.format("Received packet from: %s:%d", packet.getAddress(), packet.getPort()));
            System.out.println(String.format("Packet Type: %d", (int) buffer[0]));
            System.out.println(String.format("Response: %s", new String(buffer)));

        } catch (IOException e) {
            System.out.println("Failed to receive packet");
        }
    }

    /**
     * Start a client with host, ip and thread count for options;
     *
     * @param args
     */
    public static void main(String[] args) {

        // run service
        System.out.println("--------------------");
        System.out.println("Testing Echo Service");
        run("localhost", 8989, "echo", "Hello World");

        System.out.println("--------------------");
        System.out.println("Testing Time Service");
        run("localhost", 8989, "time", "");

        System.out.println("--------------------");
        System.out.println("Testing Lookup Service");
        run("localhost", 8989, "lookup", "google.com");
    }
}
