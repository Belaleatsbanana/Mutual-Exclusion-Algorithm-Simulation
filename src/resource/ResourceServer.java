package resource;

import shared.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ResourceServer class handles resource management and client connections.
 * It listens for client requests, processes resource reservations, and notifies the Coordinator.
 */
public class ResourceServer implements Runnable {

    // List of resources managed by this server
    private final ArrayList<Resource> resources;

    // Port for client connections
    private final int port;

    // Port for admin connections (used for inventory queries)
    private final int adminPort;

    // Type of resources managed by this server
    private final ResourceType resourceType;

    /**
     * Constructor for ResourceServer.
     *
     * @param port The port on which the server will listen for client connections.
     */
    public ResourceServer(int port) {
        this.port = port;
        this.adminPort = port + 1000; // Admin port is offset by 1000
        this.resourceType = determineResourceType(port); // Determine resource type based on port
        this.resources = new ArrayList<>();
        initializeResources(); // Initialize the resources for this server
    }

    /**
     * Determines the resource type based on the port number.
     *
     * @param port The port number.
     * @return The ResourceType corresponding to the port.
     * @throws IllegalArgumentException If the port is invalid.
     */
    private ResourceType determineResourceType(int port) {
        switch (port) {
            case 6001: return ResourceType.TSHIRTS;
            case 6002: return ResourceType.BOTTOMS;
            case 6003: return ResourceType.SHOES;
            default: throw new IllegalArgumentException("Invalid port for resource type");
        }
    }

    /**
     * Initializes the resources for this server based on its resource type.
     */
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

    /**
     * Main run method for the ResourceServer.
     * Listens for client connections and handles them in a separate thread.
     */
    @Override
    public void run() {
        // Start the admin handler in a separate thread
        new Thread(new AdminHandler()).start();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Resource Server (" + resourceType + ") is listening on port " + port);

            while (true) {
                // Accept a new client connection
                Socket socket = serverSocket.accept();

                // Simulate processing delay
                Thread.sleep(2000);
                System.out.println("New client connected to " + resourceType + " server");

                // Handle the client request
                handleClient(socket);
            }
        } catch (Exception e) {
            System.out.println("Resource Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles a client request.
     *
     * @param socket The client socket.
     */
    private void handleClient(Socket socket) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            // Read the Resource object from the client
            Resource requestedResource = (Resource) input.readObject();
            System.out.println("Received request for resource: " + requestedResource.getCode());

            // Process the request and get the response
            String response = processRequest(requestedResource);
            output.writeUTF(response); // Send the response back to the client

            // Notify the Coordinator that the resource is free
            notifyCoordinator();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client handler exception: " + ex.getMessage());
        } finally {
            try {
                socket.close(); // Close the client socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes a resource reservation request.
     *
     * @param requestedResource The resource requested by the client.
     * @return A response message indicating success or failure.
     */
    private String processRequest(Resource requestedResource) {
        // Find the resource in the server's list by all attributes except quantity
        Resource serverResource = findResource(requestedResource);
        if (serverResource == null) {
            return "Error: Resource not found.";
        }

        // Check if the server has enough quantity
        int requestedQuantity = requestedResource.getQuantity();
        if (serverResource.getQuantity() >= requestedQuantity) {
            // Deduct the requested quantity from the server's resource
            serverResource.setQuantity(serverResource.getQuantity() - requestedQuantity);
            return "Reservation successful. Remaining quantity: " + serverResource.getQuantity();
        } else {
            return "Error: Not enough quantity available. Available: " + serverResource.getQuantity();
        }
    }

    /**
     * Finds a resource in the server's list by matching all attributes except quantity.
     *
     * @param requestedResource The resource to find.
     * @return The matching resource, or null if not found.
     */
    private Resource findResource(Resource requestedResource) {
        System.out.println(requestedResource.toString());
        for (Resource resource : resources) {
            // Compare all attributes except quantity
            if (resource.getCode().equals(requestedResource.getCode()) &&
                    resource.getColor().equals(requestedResource.getColor()) &&
                    resource.getSize().equals(requestedResource.getSize())) {
                return resource;
            }
        }
        return null;
    }

    /**
     * Notifies the Coordinator that the resource is free.
     */
    private void notifyCoordinator() {
        try (Socket coordinatorSocket = new Socket("localhost", 5001);
             DataOutputStream out = new DataOutputStream(coordinatorSocket.getOutputStream())) {

            // Send a message to the Coordinator
            String message = "FREE " + resourceType.name();
            out.writeUTF(message);
            System.out.println("Notified Coordinator that " + resourceType + " is free");
        } catch (IOException e) {
            System.out.println("Failed to notify Coordinator: " + e.getMessage());
        }
    }

    /**
     * The AdminHandler class handles admin requests for resource inventory.
     */
    private class AdminHandler implements Runnable {
        @Override
        public void run() {
            try (ServerSocket adminSocket = new ServerSocket(adminPort)) {
                System.out.println("Admin Server (" + resourceType + ") listening on port " + adminPort);

                while (true) {
                    // Accept an admin connection
                    Socket socket = adminSocket.accept();
                    handleAdminRequest(socket);
                }
            } catch (IOException e) {
                System.out.println("Admin Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * Handles an admin request.
         *
         * @param socket The admin socket.
         */
        private void handleAdminRequest(Socket socket) {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                // Read the admin command
                String command = dis.readUTF();
                if ("GET_RESOURCES".equals(command)) {
                    synchronized (resources) {
                        // Send a copy of the resources list to the admin
                        ObjectOutputStream oos = new ObjectOutputStream(dos);
                        oos.writeObject(new ArrayList<>(resources));
                    }
                }
            } catch (IOException e) {
                System.out.println("Admin handler error: " + e.getMessage());
            } finally {
                try {
                    socket.close(); // Close the admin socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Main method to start the ResourceServer.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Start ResourceServers for different resource types
        new Thread(new ResourceServer(6001)).start(); // TSHIRTS
        new Thread(new ResourceServer(6002)).start(); // BOTTOMS
        new Thread(new ResourceServer(6003)).start(); // SHOES
    }
}