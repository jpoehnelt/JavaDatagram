/*
 * CS 460 Number Guessing Project
 * Authors: Justin Poehnelt, Matt Siewierski
 */
package datagram;


import java.util.Arrays;
import java.net.*;
import java.io.*;

public class Client implements Runnable {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * The run method for making multiple clients via threads.
     */
    @Override
    public void run() {
        DatagramSocket socket;
        InetAddress address;

        try {
            socket = new DatagramSocket();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        String test = "Hello world.";
        byte[] buffer = new byte[512];
        buffer = Arrays.copyOf(test.getBytes(), buffer.length);

        // Get the address
        try {
            address =  InetAddress.getByName(this.host);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            return;
        }

        // Create a packet to receive data into the buffer
        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, address, this.port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Could not send socket.");
        }

        // Wait for response packet
        System.out.println("Waiting for response.");
        try {
            socket.receive(packet);

            System.out.println(new String(packet.getData()));

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
        String host;
        int port, thread_count;

        // try to get the arguements
        try {
            host = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = 8989;
        }

        try {
            thread_count = Integer.parseInt(args[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            thread_count = 1;
        }

        // create threads to test server
        for (int i = 0; i < thread_count; i++) {
            new Thread(new Client(host, port)).start();
        }

    }

    /**
     * Talk to a number guessing server.
     *
     * @param socket
     */
    private void send(DatagramSocket socket) {


    }



}
