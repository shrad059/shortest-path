import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class CanadianDeliveryMap extends JFrame {
    private Map<String, double[]> provinceCoordinates = new HashMap<>();
    private List<double[]> pathCoordinates = new ArrayList<>();

    public CanadianDeliveryMap(String startProvinceName, List<String> provincesToVisit) throws IOException {
        super("Canadian Delivery Map");
        readProvinceCoordinates("map_provinces_data.csv");

        setResizable(false);
        setUndecorated(true);
        setBounds(100, 100, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel mapBackGround = new JLabel();
        ImageIcon backgroundImageIcon = new ImageIcon(getClass().getClassLoader().getResource("images/canadian-map.jpg"));

        if (backgroundImageIcon.getIconWidth() == -1) {
            System.out.println("Error: Background image not found or could not be loaded.");
        } else {
            Image backgroundImage = backgroundImageIcon.getImage();
            Image scaledImage = backgroundImage.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
            mapBackGround.setIcon(new ImageIcon(scaledImage));
            mapBackGround.setBounds(0, 0, 1200, 800);
            getContentPane().add(mapBackGround);
        }

        JLabel closeIcon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/closeIcon.png")));
        closeIcon.setBounds(1136, 0, 64, 64);
        closeIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.exit(0);
            }
        });
        getContentPane().add(closeIcon);

        addMarker(startProvinceName, Color.RED);

        for (String province : provincesToVisit) {
            addMarker(province, Color.BLUE);
        }
    }

    private void addMarker(String provinceName, Color color) {
        double[] coordinates = provinceCoordinates.get(provinceName.toLowerCase());
        if (coordinates != null && coordinates.length >= 2) {
            int x = (int) coordinates[0];
            int y = (int) coordinates[1];

            if (provinceName.equalsIgnoreCase("start")) {
                color = Color.RED;
            }

            pathCoordinates.add(new double[]{x, y});

            Color finalColor = color;
            JPanel marker = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(finalColor);
                    g.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            marker.setBackground(null);
            marker.setBounds(x - 10, y - 10, 20, 20);
            marker.setOpaque(false);

            marker.setToolTipText(provinceName);

            getContentPane().add(marker);
            getContentPane().setComponentZOrder(marker, 0);
        } else {
            System.out.println("Warning: No coordinates found for " + provinceName);
        }
    }


    private void readProvinceCoordinates(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0].trim().toLowerCase();
                    double latitude = Double.parseDouble(data[1].trim());
                    double longitude = Double.parseDouble(data[2].trim());
                    provinceCoordinates.put(name, new double[]{latitude, longitude});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawPathLines(g);
    }

    private void drawPathLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        for (int i = 0; i < pathCoordinates.size() - 1; i++) {
            double[] start = pathCoordinates.get(i);
            double[] end = pathCoordinates.get(i + 1);
            g2d.drawLine((int) start[0], (int) start[1], (int) end[0], (int) end[1]);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                List<String> provincesToVisit = new ArrayList<>();
//                provincesToVisit.add("Ontario");
//                provincesToVisit.add("Quebec");
//                provincesToVisit.add("Alberta");
//
//                CanadianDeliveryMap map = new CanadianDeliveryMap("British Columbia", provincesToVisit);
//                map.setVisible(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
}
