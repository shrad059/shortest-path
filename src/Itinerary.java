import java.util.*;

// Itinerary class using GraphImplementation for travel planning
public class Itinerary {
    private GraphImplementation graph;

    public Itinerary(GraphImplementation graph) {
        this.graph = graph;
    }

    public void planItinerary(Province startProvince, List<Province> provincesToVisit) {
        // Add provinces to graph
        graph.addProvince(startProvince);
        for (Province province : provincesToVisit) {
            graph.addProvince(province);
        }

        // Calculate shortest paths using Dijkstra's algorithm
        Map<Province, Double> shortestDistances = graph.dijkstra(startProvince);

        // Display the shortest paths
        graph.displayDistances(shortestDistances, startProvince);
    }
}
