import java.util.*;

// GraphImplementation class for Dijkstra's algorithm
public class GraphImplementation {
    private Map<Province, Map<Province, Double>> adjacencyList;

    public GraphImplementation() {
        this.adjacencyList = new HashMap<>();
    }

    public void addProvince(Province province) {
        adjacencyList.putIfAbsent(province, new HashMap<>());
    }

    public void addEdge(Province source, Province destination, double weight) {
        // Make sure both source and destination provinces exist in the graph
        addProvince(source);
        addProvince(destination);

        // Add the edge between source and destination with weight
        adjacencyList.get(source).put(destination, weight);
        adjacencyList.get(destination).put(source, weight); // Assuming undirected graph
    }

    public Map<Province, Double> dijkstra(Province start) {
        Map<Province, Double> distances = new HashMap<>();
        PriorityQueue<Province> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Set<Province> visited = new HashSet<>();

        // Initialize distances
        for (Province province : adjacencyList.keySet()) {
            distances.put(province, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            Province current = queue.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Map.Entry<Province, Double> neighborEntry : adjacencyList.get(current).entrySet()) {
                Province neighbor = neighborEntry.getKey();
                double distance = distances.get(current) + neighborEntry.getValue();

                if (distance < distances.get(neighbor)) {
                    distances.put(neighbor, distance);
                    queue.offer(neighbor);
                }
            }
        }

        return distances;
    }

    public void displayDistances(Map<Province, Double> distances, Province start) {
        System.out.println("Shortest distances from " + start.getName() + ":");
        for (Map.Entry<Province, Double> entry : distances.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue() + " km");
        }
    }
}
