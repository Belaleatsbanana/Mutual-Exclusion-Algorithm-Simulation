package process;

import resource.Resource;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Interface for handling responses from the resource server.
 */
interface ProcessResponseHandler {
    void handleResponse(String message);
}

/**
 * The Process class represents a client process that requests resources from the Coordinator
 * and interacts with the ResourceServer to reserve resources.
 */
public class Process extends Thread {

    // Unique identifier for this process
    private final String processId;

    // Coordinator server host address
    private final String coordinatorHost;

    // Coordinator server port
    private final int coordinatorPort;

    // Resource requested by this process
    private final Resource resource;

    // Handler for processing responses from the resource server
    private final ProcessResponseHandler responseHandler;

    /**
     * Constructor for Process.
     *
     * @param processId        The unique identifier for this process.
     * @param coordinatorHost  The host address of the Coordinator.
     * @param coordinatorPort  The port of the Coordinator.
     * @param resource         The resource requested by this process.
     * @param responseHandler  The handler for processing responses from the resource server.
     */
    public Process(String processId, String coordinatorHost, int coordinatorPort,
                   Resource resource, ProcessResponseHandler responseHandler) {
        this.processId = processId;
        this.coordinatorHost = coordinatorHost;
        this.coordinatorPort = coordinatorPort;
        this.resource = resource;
        this.responseHandler = responseHandler;
    }

    /**
     * Main run method for the Process thread.
     * Connects to the Coordinator, sends a resource request, and handles responses.
     */
    @Override
    public void run() {
        try (Socket coordinatorSocket = new Socket(coordinatorHost, coordinatorPort);
             DataOutputStream coordinatorOutput = new DataOutputStream(coordinatorSocket.getOutputStream());
             DataInputStream coordinatorInput = new DataInputStream(coordinatorSocket.getInputStream())) {

            // Send a resource request to the Coordinator
            String request = "REQUEST " + resource.getType() + " " + processId;
            coordinatorOutput.writeUTF(request);
            System.out.println("Sent request to Coordinator: " + request);

            // Continuously listen for responses from the Coordinator
            while (true) {
                String response = coordinatorInput.readUTF();
                System.out.println("Coordinator response: " + response);

                if (response.startsWith("REDIRECT")) {
                    // If redirected, connect to the specified ResourceServer
                    connectToResourceServer(response);
                    break; // Exit after handling the redirect
                } else if (response.equals("WAIT")) {
                    // If asked to wait, continue listening for updates
                    System.out.println("Process " + processId + " is waiting...");
                }
            }
        } catch (IOException ex) {
            System.out.println("Process exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Connects to the ResourceServer specified in the Coordinator's response.
     *
     * @param response The response from the Coordinator containing the ResourceServer address.
     */
    private void connectToResourceServer(String response) {
        // Parse the ResourceServer address from the response
        String[] parts = response.split(" ");
        String[] addressParts = parts[1].split(":");
        String host = addressParts[0];
        int port = Integer.parseInt(addressParts[1]);

        // Connect to the ResourceServer
        try (Socket resourceSocket = new Socket(host, port);
             ObjectOutputStream resourceOutput = new ObjectOutputStream(resourceSocket.getOutputStream());
             DataInputStream resourceInput = new DataInputStream(resourceSocket.getInputStream())) {

            // Send the resource reservation request to the ResourceServer
            resourceOutput.writeObject(resource);
            resourceOutput.flush();
            System.out.println("Sent resource request to ResourceServer: " + resource.getCode());

            // Receive the response from the ResourceServer
            String resourceResponse = resourceInput.readUTF();
            System.out.println("Resource Server response: " + resourceResponse);

            // Handle the response using the response handler
            if (responseHandler != null) {
                SwingUtilities.invokeLater(() ->
                        responseHandler.handleResponse(resourceResponse)
                );
            }
        } catch (IOException e) {
            System.out.println("Failed to connect to resource server: " + e.getMessage());

            // Notify the response handler of the connection failure
            if (responseHandler != null) {
                SwingUtilities.invokeLater(() ->
                        responseHandler.handleResponse("Failed to connect to resource server")
                );
            }
        }
    }
}