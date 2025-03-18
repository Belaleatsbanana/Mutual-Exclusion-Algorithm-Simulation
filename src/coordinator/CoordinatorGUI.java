package coordinator;

import shared.ResourceType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

public class CoordinatorGUI extends JFrame {

    private final DefaultTableModel resourceModel;
    private final DefaultTableModel waitingModel;

    public CoordinatorGUI() {
        setTitle("Coordinator GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Resource Allocation Panel (center)
        JPanel resourcePanel = new JPanel(new BorderLayout());
        resourcePanel.setBorder(BorderFactory.createTitledBorder("Resource Allocation"));

        resourceModel = new DefaultTableModel(new String[]{"Resource", "Occupied By"}, 0);
        JTable resourceTable = new JTable(resourceModel);
        JScrollPane resourceScroll = new JScrollPane(resourceTable);
        resourcePanel.add(resourceScroll, BorderLayout.CENTER);

        // Waiting Queue Panel (east)
        JPanel waitingPanel = new JPanel(new BorderLayout());
        waitingPanel.setBorder(BorderFactory.createTitledBorder("Waiting Processes Queue"));

        waitingModel = new DefaultTableModel(new String[]{"Process", "Waiting For"}, 0);
        JTable waitingTable = new JTable(waitingModel);
        JScrollPane waitingScroll = new JScrollPane(waitingTable);
        waitingPanel.add(waitingScroll, BorderLayout.CENTER);
        waitingPanel.setPreferredSize(new Dimension(250, 0));

        // Add panels to main layout
        mainPanel.add(resourcePanel, BorderLayout.CENTER);
        mainPanel.add(waitingPanel, BorderLayout.EAST);
        add(mainPanel);

        // Refresh data every second
        Timer timer = new Timer(1000, e -> refreshTables());
        timer.start();
    }

    private void refreshTables() {
        // Update resource table
        resourceModel.setRowCount(0);
        Map<ResourceType, String> currentProcesses = CoordinatorServer.getCurrentProcesses();
        for (ResourceType type : ResourceType.values()) {
            String processId = currentProcesses.getOrDefault(type, "Free");
            resourceModel.addRow(new Object[]{type.name(), processId});
        }

        // Update waiting queue table
        waitingModel.setRowCount(0);
        Map<ResourceType, Queue<String>> waitingQueues = CoordinatorServer.getWaitingQueues();
        waitingQueues.forEach((resourceType, queue) -> {
            queue.forEach(processId -> {
                waitingModel.addRow(new Object[]{processId, resourceType.name()});
            });
        });
    }

    public static void main(String[] args) {
        // Start the CoordinatorServer in the background
        new Thread(() -> {
            try {
                CoordinatorServer.main(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Start the GUI
        SwingUtilities.invokeLater(() -> {
            CoordinatorGUI gui = new CoordinatorGUI();
            gui.setVisible(true);
        });
    }
}