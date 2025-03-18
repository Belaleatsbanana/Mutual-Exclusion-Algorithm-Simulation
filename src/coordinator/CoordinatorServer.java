package coordinator;

import shared.ResourceType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CoordinatorServer implements Runnable {
    private final Socket processSocket;
    private static final Map<ResourceType, String> resourceServers = Map.of(
            ResourceType.TSHIRTS, "localhost:6001",
            ResourceType.BOTTOMS, "localhost:6002",
            ResourceType.SHOES, "localhost:6003"
    );

    private static final Map<ResourceType, Boolean> resourceAvailability = new HashMap<>();
    private static final Map<ResourceType, String> currentProcesses = new HashMap<>();
    private static final Map<ResourceType, Queue<String>> waitingQueues = new HashMap<>();
    private static final Map<ResourceType, Queue<Socket>> waitingSockets = new HashMap<>();

    static {
        for (ResourceType type : ResourceType.values()) {
            resourceAvailability.put(type, true);
            waitingQueues.put(type, new LinkedList<>());
            waitingSockets.put(type, new LinkedList<>());
        }
    }

    public CoordinatorServer(Socket processSocket) {
        this.processSocket = processSocket;
    }

    @Override
    public void run() {
        DataInputStream processInput = null;
        DataOutputStream processOutput = null;

        try {
            processInput = new DataInputStream(processSocket.getInputStream());
            processOutput = new DataOutputStream(processSocket.getOutputStream());

            String request = processInput.readUTF();
            String[] parts = request.split(" ");
            String action = parts[0];
            ResourceType resourceType = ResourceType.valueOf(parts[1]);
            String processId = parts[2];

            synchronized (resourceAvailability) {
                if (action.equals("REQUEST")) {
                    if (resourceAvailability.get(resourceType)) {
                        // Resource is available: redirect immediately
                        processOutput.writeUTF("REDIRECT " + resourceServers.get(resourceType));
                        resourceAvailability.put(resourceType, false);
                        currentProcesses.put(resourceType, processId);
                        System.out.println("Assigned resource " + resourceType + " to process " + processId);

                        // Close the socket after redirecting
                        processOutput.close();
                        processInput.close();
                        processSocket.close();
                    } else {
                        // Resource is busy: add to the waiting queue
                        processOutput.writeUTF("WAIT");
                        waitingQueues.get(resourceType).add(processId);
                        waitingSockets.get(resourceType).add(processSocket);
                        System.out.println("Added process " + processId + " to waiting queue for " + resourceType);

                        // Do NOT close the socket here; it will be managed later
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Client handler exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // Notification listener for resource free events
        new Thread(() -> {
            try (ServerSocket notificationSocket = new ServerSocket(5001)) {
                System.out.println("Coordinator notification listener started on port 5001");
                while (true) {
                    Socket socket = notificationSocket.accept();
                    System.out.println("New connection from ResourceServer");
                    new Thread(new NotificationHandler(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Main server for client (process) connections
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Coordinator Server is listening on port 5000");
            while (true) {
                Socket processSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(new CoordinatorServer(processSocket)).start();
            }
        }
    }

    private static class NotificationHandler implements Runnable {
        private final Socket socket;

        public NotificationHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream())) {
                String message = input.readUTF();
                System.out.println("Received message from ResourceServer: " + message);
                String[] parts = message.split(" ");
                // In the NotificationHandler's run() method:
                if (parts[0].equals("FREE")) {
                    ResourceType resourceType = ResourceType.valueOf(parts[1]);
                    synchronized (resourceAvailability) {
                        currentProcesses.remove(resourceType);
                        Queue<String> queue = waitingQueues.get(resourceType);
                        Queue<Socket> sockets = waitingSockets.get(resourceType);
                        if (!queue.isEmpty() && !sockets.isEmpty()) {
                            String nextProcessId = queue.poll();
                            Socket nextSocket = sockets.poll();
                            try (DataOutputStream output = new DataOutputStream(nextSocket.getOutputStream())) {
                                // Send REDIRECT to the waiting process
                                output.writeUTF("REDIRECT " + resourceServers.get(resourceType));
                                resourceAvailability.put(resourceType, false);
                                currentProcesses.put(resourceType, nextProcessId);
                                System.out.println("Assigned resource " + resourceType + " to waiting process " + nextProcessId);
                            } catch (IOException e) {
                                System.out.println("Error sending redirect: " + e.getMessage());
                            }
                            // Do NOT close nextSocket here; let the process handle it
                        } else {
                            resourceAvailability.put(resourceType, true);
                            System.out.println("Resource " + resourceType + " is now free");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling notification: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    socket.close(); // Close the notification socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Thread-safe getter for current processes
    public static Map<ResourceType, String> getCurrentProcesses() {
        synchronized (resourceAvailability) {
            return new HashMap<>(currentProcesses);
        }
    }

    // Thread-safe getter for waiting queues
    public static Map<ResourceType, Queue<String>> getWaitingQueues() {
        synchronized (resourceAvailability) {
            Map<ResourceType, Queue<String>> copy = new HashMap<>();
            for (Map.Entry<ResourceType, Queue<String>> entry : waitingQueues.entrySet()) {
                copy.put(entry.getKey(), new LinkedList<>(entry.getValue()));
            }
            return copy;
        }
    }
}