/*
 * Created by JFormDesigner on Sat Mar 15 15:17:53 EET 2025
 */

package process;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.Border;

/**
 * @author belal
 */
public class BranchGUI extends JFrame {
    public BranchGUI() {
        initComponents();
        setupRadioButtonGroups();
        styleRadioButtons();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Belal Mohamed Ahmed Taha
        headPanel = new JPanel();
        title = new JLabel();
        categoryPanel = new JPanel();
        SHIRT = new JRadioButton();
        PANT = new JRadioButton();
        SHOE = new JRadioButton();
        selectionPanel = new JPanel();
        colorPanel = new JPanel();
        red = new JRadioButton();
        orange = new JRadioButton();
        yellow = new JRadioButton();
        green = new JRadioButton();
        blue = new JRadioButton();
        white = new JRadioButton();
        gray = new JRadioButton();
        black = new JRadioButton();
        sizePanel = new JPanel();
        small = new JRadioButton();
        medium = new JRadioButton();
        large = new JRadioButton();
        xLarge = new JRadioButton();
        reservePanel = new JPanel();
        itemCode = new JTextField();
        reserveButton = new JButton();

        //======== this ========
        setSize(new java.awt.Dimension(800, 600));
        setBackground(Color.darkGray);
        setResizable(false);
        setForeground(new Color(0xffff99));
        var contentPane = getContentPane();

        //======== headPanel ========
        {
            headPanel.setBackground(new Color(0xccccff));
            headPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder
                    (0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing.border
                    .TitledBorder.BOTTOM,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt
                    .Color.red),headPanel. getBorder()));headPanel. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void
        propertyChange(java.beans.PropertyChangeEvent e){if("\u0062order".equals(e.getPropertyName()))throw new RuntimeException()
                ;}});
            headPanel.setLayout(new BorderLayout());

            //---- title ----
            title.setText("BananaClothing");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setFont(new Font("sansserif", Font.BOLD, 20));
            headPanel.add(title, BorderLayout.CENTER);
        }

        //======== categoryPanel ========
        {
            categoryPanel.setLayout(new GridLayout(1, 3, 10, 10));

            //---- SHIRT ----
            SHIRT.setText("Boott");
            SHIRT.setIcon(null);
            SHIRT.setVerticalAlignment(SwingConstants.BOTTOM);
            categoryPanel.add(SHIRT);

            //---- PANT ----
            PANT.setText("SHit");
            PANT.setIcon(null);
            PANT.setVerticalAlignment(SwingConstants.BOTTOM);
            categoryPanel.add(PANT);

            //---- SHOE ----
            SHOE.setText("SHoe");
            SHOE.setIcon(null);
            SHOE.setVerticalAlignment(SwingConstants.BOTTOM);
            categoryPanel.add(SHOE);
        }

        //======== selectionPanel ========
        {
            selectionPanel.setPreferredSize(new Dimension(625, 50));
            selectionPanel.setLayout(new GridLayout(1, 2, 5, 5));

            //======== colorPanel ========
            {
                colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

                //---- red ----
                red.setText("text");
                red.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(red);

                //---- orange ----
                orange.setText("text");
                orange.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(orange);

                //---- yellow ----
                yellow.setText("text");
                yellow.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(yellow);

                //---- green ----
                green.setText("text");
                green.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(green);

                //---- blue ----
                blue.setText("text");
                blue.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(blue);

                //---- white ----
                white.setText("text");
                white.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(white);

                //---- gray ----
                gray.setText("text");
                gray.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(gray);

                //---- black ----
                black.setText("text");
                black.setPreferredSize(new Dimension(25, 25));
                colorPanel.add(black);
            }
            selectionPanel.add(colorPanel);

            //======== sizePanel ========
            {
                sizePanel.setPreferredSize(new Dimension(250, 45));
                sizePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

                //---- small ----
                small.setText("text");
                small.setPreferredSize(new Dimension(50, 25));
                sizePanel.add(small);

                //---- medium ----
                medium.setText("text");
                medium.setPreferredSize(new Dimension(50, 25));
                sizePanel.add(medium);

                //---- large ----
                large.setText("text");
                large.setPreferredSize(new Dimension(50, 25));
                sizePanel.add(large);

                //---- xLarge ----
                xLarge.setText("text");
                xLarge.setPreferredSize(new Dimension(50, 25));
                sizePanel.add(xLarge);
            }
            selectionPanel.add(sizePanel);
        }

        //======== reservePanel ========
        {
            reservePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

            //---- itemCode ----
            itemCode.setPreferredSize(new Dimension(150, 28));
            reservePanel.add(itemCode);

            //---- reserveButton ----
            reserveButton.setText("text");
            reservePanel.add(reserveButton);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addComponent(headPanel, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(categoryPanel, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(selectionPanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                .addGap(40, 40, 40))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(reservePanel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(63, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(headPanel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(categoryPanel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(reservePanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(23, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    private void setupRadioButtonGroups() {
        // Category group
        ButtonGroup categoryGroup = new ButtonGroup();
        categoryGroup.add(SHIRT);
        categoryGroup.add(PANT);
        categoryGroup.add(SHOE);

        // Color group
        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(red);
        colorGroup.add(orange);
        colorGroup.add(yellow);
        colorGroup.add(green);
        colorGroup.add(blue);
        colorGroup.add(white);
        colorGroup.add(gray);
        colorGroup.add(black);

        // Size group
        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(small);
        sizeGroup.add(medium);
        sizeGroup.add(large);
        sizeGroup.add(xLarge);
    }

    private void styleRadioButtons() {
        // Custom styling for all radio buttons
        Component[] components = {
                SHIRT, PANT, SHOE, red, orange, yellow, green, blue, white, gray, black,
                small, medium, large, xLarge
        };

        for (Component comp : components) {
            if (comp instanceof JRadioButton) {
                JRadioButton radio = (JRadioButton) comp;
                radio.setIcon(new EmptyIcon()); // Remove default circle
                radio.setBorder(new RoundedBorder(10, Color.GRAY));
                radio.setOpaque(true);
                radio.setBackground(Color.WHITE);
                radio.setFocusPainted(false);

                // Add hover/selection effects
                radio.addItemListener(e -> {
                    if (radio.isSelected()) {
                        radio.setBorder(new RoundedBorder(10, new Color(0, 120, 215)));
                        radio.setBackground(new Color(230, 240, 255));
                    } else {
                        radio.setBorder(new RoundedBorder(10, Color.GRAY));
                        radio.setBackground(Color.WHITE);
                    }
                });
            }
        }
    }

    // Custom empty icon to hide radio button circle
    private static class EmptyIcon implements Icon {
        public int getIconWidth() { return 0; }
        public int getIconHeight() { return 0; }
        public void paintIcon(Component c, Graphics g, int x, int y) {}
    }

    // Custom rounded border
    private static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+1, radius+1);
        }

        public boolean isBorderOpaque() {
            return false;
        }
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Belal Mohamed Ahmed Taha
    private JPanel headPanel;
    private JLabel title;
    private JPanel categoryPanel;
    private JRadioButton SHIRT;
    private JRadioButton PANT;
    private JRadioButton SHOE;
    private JPanel selectionPanel;
    private JPanel colorPanel;
    private JRadioButton red;
    private JRadioButton orange;
    private JRadioButton yellow;
    private JRadioButton green;
    private JRadioButton blue;
    private JRadioButton white;
    private JRadioButton gray;
    private JRadioButton black;
    private JPanel sizePanel;
    private JRadioButton small;
    private JRadioButton medium;
    private JRadioButton large;
    private JRadioButton xLarge;
    private JPanel reservePanel;
    private JTextField itemCode;
    private JButton reserveButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public static void main(String[] args) {
        BranchGUI gui = new BranchGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
}
