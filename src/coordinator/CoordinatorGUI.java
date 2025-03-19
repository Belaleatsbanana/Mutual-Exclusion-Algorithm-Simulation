package coordinator;

import resource.Resource;
import shared.ResourceType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

public class CoordinatorGUI extends JFrame {

    // Table models for different sections
    private final DefaultTableModel resourceModel;     // Tracks currently allocated resources
    private final DefaultTableModel waitingModel;      // Shows processes waiting for resources
    private final DefaultTableModel serverResourcesModel;  // Displays resource server inventories

    public CoordinatorGUI() {
        // Configure main window settings
        setTitle("Coordinator GUI");
        setSize(800, 600);  // Larger window size for better data visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window on screen

        // Main container with spacing and padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top section containing two side-by-side panels
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Left panel: Resource allocation status
        JPanel resourcePanel = new JPanel(new BorderLayout());
        resourcePanel.setBorder(BorderFactory.createTitledBorder("Resource Allocation"));
        resourceModel = new DefaultTableModel(new String[]{"Resource", "Occupied By"}, 0);
        JTable resourceTable = new JTable(resourceModel);
        resourcePanel.add(new JScrollPane(resourceTable), BorderLayout.CENTER);

        // Right panel: Waiting processes queue
        JPanel waitingPanel = new JPanel(new BorderLayout());
        waitingPanel.setBorder(BorderFactory.createTitledBorder("Waiting Processes Queue"));
        waitingModel = new DefaultTableModel(new String[]{"Process", "Waiting For"}, 0);
        JTable waitingTable = new JTable(waitingModel);
        waitingPanel.add(new JScrollPane(waitingTable), BorderLayout.CENTER);

        // Add both panels to top section
        topPanel.add(resourcePanel);
        topPanel.add(waitingPanel);

        // Bottom section: Resource server inventory details
        JPanel serverResourcesPanel = new JPanel(new BorderLayout());
        serverResourcesPanel.setBorder(BorderFactory.createTitledBorder("Resource Server Inventory"));
        serverResourcesPanel.setPreferredSize(new Dimension(0, 350));  // Fixed height for consistency

        // Configure server inventory table columns
        serverResourcesModel = new DefaultTableModel(
                new String[]{"Resource Type", "Code", "Color", "Size", "Quantity"}, 0
        );
        JTable serverResourcesTable = new JTable(serverResourcesModel);
        serverResourcesPanel.add(new JScrollPane(serverResourcesTable), BorderLayout.CENTER);

        // Assemble full interface
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(serverResourcesPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Set up automatic data refresh every second
        Timer timer = new Timer(1000, e -> refreshTables());
        timer.start();
    }

    // Refresh all table data
    private void refreshTables() {
        updateOriginalTables();       // Update resource allocation and waiting queues
        updateServerResourcesTable();  // Update server inventory data
    }

    // Update core resource allocation tables
    private void updateOriginalTables() {
        // Clear existing data
        resourceModel.setRowCount(0);
        waitingModel.setRowCount(0);

        // Get current state from coordinator
        Map<ResourceType, String> currentProcesses = CoordinatorServer.getCurrentProcesses();
        Map<ResourceType, Queue<String>> waitingQueues = CoordinatorServer.getWaitingQueues();

        // Populate resource allocation table
        for (ResourceType type : ResourceType.values()) {
            resourceModel.addRow(new Object[]{type.name(), currentProcesses.getOrDefault(type, "Free")});
        }

        // Populate waiting processes table
        waitingQueues.forEach((type, queue) ->
                queue.forEach(processId ->
                        waitingModel.addRow(new Object[]{processId, type.name()})
                )
        );
    }

    // Update resource server inventory table
    private void updateServerResourcesTable() {
        serverResourcesModel.setRowCount(0);  // Clear previous data

        // Query each resource server (ports 7001-7003)
        for (int port = 7001; port <= 7003; port++) {
            try (Socket socket = new Socket("localhost", port);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dis = new DataInputStream(socket.getInputStream())) {

                // Request resource data from server
                dos.writeUTF("GET_RESOURCES");
                ObjectInputStream ois = new ObjectInputStream(dis);
                ArrayList<Resource> resources = (ArrayList<Resource>) ois.readObject();

                // Add server's resources to table
                for (Resource resource : resources) {
                    serverResourcesModel.addRow(new Object[]{
                            resource.getType().name(),
                            resource.getCode(),
                            resource.getColor().name(),
                            resource.getSize().name(),
                            resource.getQuantity()
                    });
                }
            } catch (Exception e) {
                System.out.println("Failed to fetch resources from port " + port + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Start coordinator server in background thread
        new Thread(() -> {
            try {
                CoordinatorServer.main(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Launch GUI on event dispatch thread
        SwingUtilities.invokeLater(() -> new CoordinatorGUI().setVisible(true));
    }
}