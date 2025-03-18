package resource;

import shared.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ResourceServer implements Runnable {

    private final ArrayList<Resource> resources;
    private final int port;
    private final ResourceType resourceType;

    public ResourceServer(int port) {
        this.port = port;
        this.resourceType = determineResourceType(port);
        this.resources = new ArrayList<>();
        initializeResources();
    }

    private ResourceType determineResourceType(int port) {
        switch (port) {
            case 6001: return ResourceType.TSHIRTS;
            case 6002: return ResourceType.BOTTOMS;
            case 6003: return ResourceType.SHOES;
            default: throw new IllegalArgumentException("Invalid port for resource type");
        }
    }

    private void initializeResources() {
        if (resourceType == ResourceType.TSHIRTS) {
            resources.add(new Resource("TS001", "T-shirt", ResourceType.TSHIRTS, Color.RED, Size.M, 100));
            resources.add(new Resource("TS002", "T-shirt", ResourceType.TSHIRTS, Color.BLUE, Size.L, 50));
        } else if (resourceType == ResourceType.BOTTOMS) {
            resources.add(new Resource("BT001", "Jeans", ResourceType.BOTTOMS, Color.BLUE, Size.M, 75));
        } else if (resourceType == ResourceType.SHOES) {
            resources.add(new Resource("SH001", "Sneakers", ResourceType.SHOES, Color.BLACK, Size.S, 50));
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Resource Server (" + resourceType + ") is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                Thread.sleep(3000);
                System.out.println("New client connected to " + resourceType + " server");
                handleClient(socket);
            }
        } catch (Exception e) {
            System.out.println("Resource Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            // Read the Resource object from the client
            Resource requestedResource = (Resource) input.readObject();
            System.out.println("Received request for resource: " + requestedResource.getCode());

            // Process the request
            String response = processRequest(requestedResource);
            output.writeUTF(response);

            // Notify the Coordinator
            notifyCoordinator();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client handler exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processRequest(Resource requestedResource) {
        // Find the resource in the server's list by code
        Resource serverResource = findResourceByCode(requestedResource.getCode());
        if (serverResource == null) {
            return "Error: Resource not found.";
        }

        // Check if the server has enough quantity
        int requestedQuantity = requestedResource.getQuantity();
        if (serverResource.getQuantity() >= requestedQuantity) {
            serverResource.setQuantity(serverResource.getQuantity() - requestedQuantity);
            return "Reservation successful. Remaining quantity: " + serverResource.getQuantity();
        } else {
            return "Error: Not enough quantity available. Available: " + serverResource.getQuantity();
        }
    }

    private Resource findResourceByCode(String code) {
        for (Resource resource : resources) {
            if (resource.getCode().equals(code)) {
                return resource;
            }
        }
        return null;
    }

    private void notifyCoordinator() {
        try (Socket coordinatorSocket = new Socket("localhost", 5001);
             DataOutputStream out = new DataOutputStream(coordinatorSocket.getOutputStream())) {

            String message = "FREE " + resourceType.name();
            out.writeUTF(message);
            System.out.println("Notified Coordinator that " + resourceType + " is free");
        } catch (IOException e) {
            System.out.println("Failed to notify Coordinator: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Thread(new ResourceServer(6001)).start(); // TSHIRTS
        new Thread(new ResourceServer(6002)).start(); // BOTTOMS
        new Thread(new ResourceServer(6003)).start(); // SHOES
    }
}