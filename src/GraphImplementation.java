import java.util.*;

public class GraphImplementation {
    private Map<Province, List<Edge>> adjacencyMap;

    public GraphImplementation() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addEdge(Province from, Province to, double distance) {
        this.adjacencyMap.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, distance));
        this.adjacencyMap.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, distance));
    }

    public Map<Province, Double> dijkstra(Province start) {
        Map<Province, Double> distances = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        pq.add(new Edge(start, 0));
        distances.put(start, 0.0);

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            Province currentProvince = current.getTarget();

            for (Edge neighbor : this.adjacencyMap.getOrDefault(currentProvince, new ArrayList<>())) {
                double newDist = distances.get(currentProvince) + neighbor.getWeight();
                if (newDist < distances.getOrDefault(neighbor.getTarget(), Double.MAX_VALUE)) {
                    distances.put(neighbor.getTarget(), newDist);
                    pq.add(new Edge(neighbor.getTarget(), newDist));
                }
            }
        }

        return distances;
    }

    public void displayDistances(Map<Province, Double> distances, Province start) {
        System.out.println("Shortest distances from " + start.getName() + ":");
        for (Map.Entry<Province, Double> entry : distances.entrySet()) {
            System.out.printf("To %s: %.2f kilometers%n", entry.getKey().getName(), entry.getValue());
        }
    }

    public List<Province> findShortestPath(Province start, List<Province> provincesToVisit) {
        List<Province> allProvinces = new ArrayList<>(provincesToVisit);
        allProvinces.add(0, start);
        List<Province> shortestPath = new ArrayList<>();
        double shortestDistance = Double.MAX_VALUE;

        List<List<Province>> permutations = generatePermutations(allProvinces.subList(1, allProvinces.size()));

        for (List<Province> permutation : permutations) {
            double currentDistance = 0;
            Province current = start;
            for (Province province : permutation) {
                currentDistance += getDistance(current, province);
                current = province;
            }
            currentDistance += getDistance(current, start);

            System.out.print("Permutation: " + start.getName() + " -> ");
            for (Province province : permutation) {
                System.out.print(province.getName() + " -> ");
            }
            System.out.println(start.getName());
            System.out.println("Total Distance: " + currentDistance + " km");

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                shortestPath = new ArrayList<>(permutation);
                shortestPath.add(0, start);
                shortestPath.add(start);
            }
        }

        return shortestPath;
    }


    private double getDistance(Province from, Province to) {
        for (Edge edge : this.adjacencyMap.get(from)) {
            if (edge.getTarget().equals(to)) {
                return edge.getWeight();
            }
        }
        return Double.MAX_VALUE;
    }

    private List<List<Province>> generatePermutations(List<Province> provinces) {
        if (provinces.isEmpty()) {
            List<List<Province>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        Province first = provinces.remove(0);
        List<List<Province>> remainingPermutations = generatePermutations(provinces);
        List<List<Province>> result = new ArrayList<>();

        for (List<Province> permutation : remainingPermutations) {
            for (int i = 0; i <= permutation.size(); i++) {
                List<Province> temp = new ArrayList<>(permutation);
                temp.add(i, first);
                result.add(temp);
            }
        }

        provinces.add(0, first);
        return result;
    }

    private static class Edge implements Comparable<Edge> {
        private Province target;
        private double weight;

        public Edge(Province target, double weight) {
            this.target = target;
            this.weight = weight;
        }

        public Province getTarget() {
            return target;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }
}
