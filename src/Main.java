import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Parse provinces from CSV
        List<Province> provinces = readProvincesFromCSV("provinces_data.csv");

        if (provinces.isEmpty()) {
            System.out.println("Error: No provinces found in CSV file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Ask the user for the starting province
        System.out.print("Enter the province you are starting from: ");
        String startProvinceName = scanner.nextLine().trim();
        Province startProvince = findProvinceByName(provinces, startProvinceName);

        if (startProvince == null) {
            System.out.println("Error: Starting province not found.");
            return;
        }

        // Ask the user for the number of provinces to visit
        System.out.print("Enter the number of provinces you want to visit: ");
        int numProvinces = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        List<Province> provincesToVisit = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        // Enter connected provinces and distances
        for (int i = 0; i < numProvinces; i++) {
            System.out.println("Province " + (i + 1) + ":");
            System.out.print("Enter name of connected province: ");
            String connectedProvinceName = scanner.nextLine().trim();
            Province connectedProvince = findProvinceByName(provinces, connectedProvinceName);

            if (connectedProvince == null) {
                System.out.println("Province '" + connectedProvinceName + "' not found.");
                return;
            }

            // Calculate distance using Haversine formula
            double distance = calculateDistance(startProvince.getLatitude(), startProvince.getLongitude(),
                    connectedProvince.getLatitude(), connectedProvince.getLongitude());
            distances.add(distance);
            provincesToVisit.add(connectedProvince);
        }

        // Sort provincesToVisit based on distances in ascending order
        sortItinerary(provincesToVisit, distances);

        // Display the sorted itinerary
        System.out.println("Your travel itinerary:");
        System.out.println("Starting from: " + startProvince.getName());
        for (int i = 0; i < provincesToVisit.size(); i++) {
            Province province = provincesToVisit.get(i);
            double distance = distances.get(i);
            System.out.println((i + 1) + ". Province: " + province.getName());
            System.out.printf("   Distance: %.2f kilometers%n", distance);
        }

        // Close the scanner
        scanner.close();
    }

    // Method to read provinces from CSV
    private static List<Province> readProvincesFromCSV(String filename) {
        List<Province> provinces = new ArrayList<>();
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
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

    // Method to find a province by name
    private static Province findProvinceByName(List<Province> provinces, String name) {
        for (Province province : provinces) {
            if (province.getName().equalsIgnoreCase(name)) {
                return province;
            }
        }
        return null;
    }

    // Method to calculate distance between two points given their latitude and longitude
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        final int R = 6371; // Radius of the Earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in kilometers
    }

    // Method to sort the itinerary based on distances in ascending order
    private static void sortItinerary(List<Province> provincesToVisit, List<Double> distances) {
        // Use Comparator to sort provincesToVisit based on distances list
        provincesToVisit.sort(Comparator.comparingDouble(p -> distances.get(provincesToVisit.indexOf(p))));
    }
}
