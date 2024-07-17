import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Province> provinces = readProvincesFromCSV("provinces_data.csv");

        if (provinces.isEmpty()) {
            System.out.println("Error: No provinces found in CSV file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the province you are starting from: ");
        String startProvinceName = scanner.nextLine().trim();
        Province startProvince = findProvinceByName(provinces, startProvinceName);

        if (startProvince == null) {
            System.out.println("Error: Starting province not found.");
            return;
        }

        System.out.print("Enter the number of provinces you want to visit: ");
        int numProvinces = scanner.nextInt();
        scanner.nextLine();

        List<Province> provincesToVisit = new ArrayList<>();

        for (int i = 0; i < numProvinces; i++) {
            System.out.println("Province " + (i + 1) + ":");
            System.out.print("Enter name of connected province: ");
            String connectedProvinceName = scanner.nextLine().trim();
            Province connectedProvince = findProvinceByName(provinces, connectedProvinceName);

            if (connectedProvince == null) {
                System.out.println("Province '" + connectedProvinceName + "' not found.");
                return;
            }

            provincesToVisit.add(connectedProvince);
        }

        GraphImplementation graph = new GraphImplementation();

        for (Province province1 : provinces) {
            for (Province province2 : provinces) {
                if (!province1.equals(province2)) {
                    double distance = calculateDistance(province1.getLatitude(), province1.getLongitude(),
                            province2.getLatitude(), province2.getLongitude());
                    graph.addEdge(province1, province2, distance);
                }
            }
        }

        List<Province> shortestPath = graph.findShortestPath(startProvince, provincesToVisit);
        System.out.println("**********************************");
        System.out.println("**********************************");
        System.out.println("Your travel itinerary in Canada:");
        for (int i = 0; i < shortestPath.size(); i++) {
            System.out.println((i + 1) + " Province to visit:" + shortestPath.get(i).getName());
        }
        System.out.println("**********************************");
        System.out.println("**********************************");

        SwingUtilities.invokeLater(() -> {
            try {
                CanadianDeliveryMap frame = new CanadianDeliveryMap(startProvinceName, extractProvinceNames(shortestPath));
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        scanner.close();
    }

    private static List<Province> readProvincesFromCSV(String filename) {
        List<Province> provinces = new ArrayList<>();
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    double latitude = Double.parseDouble(data[1].trim());
                    double longitude = Double.parseDouble(data[2].trim());
                    Province province = new Province(name, latitude, longitude);
                    provinces.add(province);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provinces;
    }

    private static Province findProvinceByName(List<Province> provinces, String name) {
        for (Province province : provinces) {
            if (province.getName().equalsIgnoreCase(name)) {
                return province;
            }
        }
        return null;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private static List<String> extractProvinceNames(List<Province> provinces) {
        List<String> names = new ArrayList<>();
        for (Province province : provinces) {
            names.add(province.getName());
        }
        return names;
    }
}
