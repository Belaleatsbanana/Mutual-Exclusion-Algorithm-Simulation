package process;

import resource.Resource;
import java.io.*;
import java.net.Socket;

public class Process extends Thread {
    private final String processId;
    private final String coordinatorHost;
    private final int coordinatorPort;
    private final Resource resource;

    public Process(String processId, String coordinatorHost, int coordinatorPort, Resource resource) {
        this.processId = processId;
        this.coordinatorHost = coordinatorHost;
        this.coordinatorPort = coordinatorPort;
        this.resource = resource;
    }

    @Override
    public void run() {
        try (Socket coordinatorSocket = new Socket(coordinatorHost, coordinatorPort);
             DataOutputStream coordinatorOutput = new DataOutputStream(coordinatorSocket.getOutputStream());
             DataInputStream coordinatorInput = new DataInputStream(coordinatorSocket.getInputStream())) {

            // Send request to Coordinator
            String request = "REQUEST " + resource.getType() + " " + processId;
            coordinatorOutput.writeUTF(request);

            // Continuously listen for responses from the coordinator
            while (true) {
                String response = coordinatorInput.readUTF();
                System.out.println("Coordinator response: " + response);

                if (response.startsWith("REDIRECT")) {
                    connectToResourceServer(response);
                    break; // Exit after redirect
                } else if (response.equals("WAIT")) {
                    System.out.println("Process " + processId + " is waiting...");
                    // Continue looping to listen for the next message
                }
            }
        } catch (IOException ex) {
            System.out.println("Process exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void connectToResourceServer(String response) {
        // Parse the resource server address
        String[] parts = response.split(" ");
        String[] addressParts = parts[1].split(":");
        String host = addressParts[0];
        int port = Integer.parseInt(addressParts[1]);

        // Connect to the resource server
        try (Socket resourceSocket = new Socket(host, port);
             ObjectOutputStream resourceOutput = new ObjectOutputStream(resourceSocket.getOutputStream());
             DataInputStream resourceInput = new DataInputStream(resourceSocket.getInputStream())) {

            // Send the resource reservation request
            resourceOutput.writeObject(resource);
            resourceOutput.flush();

            // Get the response from the resource server
            String resourceResponse = resourceInput.readUTF();
            System.out.println("Resource Server response: " + resourceResponse);
        } catch (IOException e) {
            System.out.println("Failed to connect to resource server: " + e.getMessage());
        }
    }
}