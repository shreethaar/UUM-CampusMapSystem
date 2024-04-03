import java.util.*;

class Node {
    private String name;
    private double latitude;
    private double longtitude;

    public Node(String name, double latitude, double longtitude) {
        this.name=name;
        this.latitude=latitude;
        this.longtitude=longtitude;
    }
    public String getName() {
        return name;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongtitude() {
        return longtitude;
    }
    public double calcDistance(Node otherNode) {
        double lat1 = Math.toRadians(this.getLatitude());
        double lon1 = Math.toRadians(this.getLongtitude());
        double lat2 = Math.toRadians(otherNode.getLatitude());
        double lon2 = Math.toRadians(otherNode.getLongtitude());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat/2)*Math.sin(dlat/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin(dlon/2)*Math.sin(dlon/2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double radius = 6371.0;
        return radius*c;
    }
    public String toString() {
        return name;
    }
}

class Edge {
    private Node src;
    private Node dst;
    private double weight;

    public Edge(Node src, Node dst, double weight) {
        this.src=src;
        this.dst=dst;
        this.weight=weight;
    }
    public Node getSrc() {
        return src;
    }
    public Node getDst() {
        return dst;
    }
    public double getWeight() {
        return weight;
    }
}

public class UUMcampus {
    private Map<Node, List<Edge>> adjacencyList;

    public UUMcampus() {
        this.adjacencyList=new HashMap<>();
    }
    public void addNode(Node node) {
        adjacencyList.put(node,new ArrayList<>());
    }
    public void addEdge(Node src, Node dst, double weight) {
        Edge edge = new Edge(src, dst, weight);
        adjacencyList.get(src).add(edge);
        adjacencyList.get(dst).add(edge);
    }
    public void printAdjancencyList() {
        for(Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node node = entry.getKey();
            List<Edge> edges = entry.getValue();

            System.out.println("Node " + node.getName() + " is connected to: ");
            for (Edge edge : edges) {
                Node neighbor = (edge.getSrc() == node) ? edge.getDst() : edge.getSrc();
                System.out.println(neighbor.getName() + " ");
            }
            System.out.println();
        }

    }
    public void addEdgeWithDistance(Node src, Node dst) {
        double weight = src.calcDistance(dst);
        Edge edge = new Edge(src, dst, weight);
        adjacencyList.get(src).add(edge);
        adjacencyList.get(dst).add(edge);

    }
    public void addAllEdges() {
        List<Node> nodeList = new ArrayList<>(adjacencyList.keySet());
        for (int i=0; i<nodeList.size(); i++) {
            for (int j=i+1;j<nodeList.size(); j++) {
                Node src = nodeList.get(i);
                Node dst = nodeList.get(j);
                double weight = src.calcDistance(dst);
                addEdge(src, dst, weight);
            }
        }
    }
    public List<Node> getNodes() {
        List<Node> nodeList = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            nodeList.add(entry.getKey());
        }
        return nodeList;
    }
    public List<Node> findShortestPath(Node source, Node destination) {
        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
    
        for (Node node : adjacencyList.keySet()) {
            distance.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
            queue.add(node);
        }
    
        distance.put(source, 0.0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
    
            for (Edge edge : adjacencyList.get(current)) {
                Node neighbor = (edge.getSrc() == current) ? edge.getDst() : edge.getSrc();
                double newDistance = distance.get(current) + edge.getWeight();
    
                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        List<Node> path = new ArrayList<>();
        Node current = destination;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);
        return path.size() > 1 ? path : null; 
    }

    
    public static void main(String[] args) {
        UUMcampus landmarks = new UUMcampus();

        Node AnjungTamu = new Node("Anjung Tamu",6.44831 , 100.50929);
        Node UKBuilding = new Node ("Security Department", 6.44860, 100.50898);
        Node JPPBuilding = new Node("Development & Maintenance Department", 6.45058, 100.50829);
        Node DKG6 = new Node("DKG 6", 6.45511, 100.50633);
        Node DKG5 = new Node("DKG 5", 6.45778, 100.5659);
        Node Chancellery = new Node("Chancellery Building", 6.46213, 100.50613);
        Node MuseumCIACuAssist = new Node("Museum, CIAC & U-Assist", 6.46219, 100.50535);
        Node PerdanaSquare = new Node("Perdana Square", 6.46221, 100.50520);
        Node DKG1 = new Node("DKG 1", 6.46542, 100.50824);
        Node DKG2 = new Node("DKG 2", 6.46401, 100.50797);
        Node DKG3 = new Node("DKG 3", 6.46556, 100.50603);
        Node Library = new Node("Library",6.46290,100.50511 );
        Node AlumniJunction = new Node ("Alumni Junction", 6.44853, 100.50980);
        Node PACE = new Node("PACE office", 6.45019, 100.49617);
        Node DTSO = new Node("Tan Sri Othman Yeop Abdullah Hall",6.46539,100.50397);
        Node DMAS = new Node("Mu\'adzam Shah Hall",6.46619,100.50526);
        Node DKG4 = new Node("DKG 4",6.46741,100.50736);
        Node CactusHouse = new Node("Cactus House",6.46885,100.50831);
        Node BotanicalGarden = new Node("Botanical Garden",6.47033,100.50769);
        Node CampSite = new Node("Camp Site", 6.47448, 100.51038);
        Node MarchingGround = new Node("Marching Ground", 6.47280, 100.50795);
        Node PKU = new Node("PKU Healthcare Center UUM", 6.47159, 100.50679);
        Node SintokMemorial = new Node("Sintok Memorial Monument", 6.47311, 100.50692);
        Node PusatKOK = new Node("Co-curiculum Centre & Palapes (Reserve Officer Training Unit)",6.47456,100.50802);
        Node SUKSIS = new Node("SUKSIS Centre", 6.47499, 100.50712);
        Node GolfClub = new Node("Golf & Recretion Club",6.47577,100.50765);
        Node InasisMuamalat = new Node("Bank Muamalat Student Residence", 6.47872, 100.50918);
        Node InasisYAB = new Node("Al-Bukhary Student Residence",6.48109, 100.50962);
        Node PusatSukan = new Node("Sports Complex", 6.47362, 100.50447);
        Node InasisMISC = new Node("MISC Student Residence", 6.47139,100.49962);
        Node InasisBSN = new Node("BSN Student Residence", 6.47049, 100.50061);

        landmarks.addNode(AnjungTamu);
        landmarks.addNode(UKBuilding);
        landmarks.addNode(JPPBuilding);
        landmarks.addNode(DKG6);
        landmarks.addNode(DKG5);
        landmarks.addNode(Chancellery);
        landmarks.addNode(MuseumCIACuAssist);
        landmarks.addNode(PerdanaSquare);
        landmarks.addNode(DKG1);
        landmarks.addNode(DKG2);
        landmarks.addNode(DKG3);
        landmarks.addNode(Library);
        landmarks.addNode(AlumniJunction);
        landmarks.addNode(PACE);
        landmarks.addNode(DTSO);
        landmarks.addNode(DMAS);
        landmarks.addNode(DKG4);
        landmarks.addNode(CactusHouse);
        landmarks.addNode(BotanicalGarden);
        landmarks.addNode(CampSite);
        landmarks.addNode(MarchingGround);
        landmarks.addNode(PKU);
        landmarks.addNode(SintokMemorial);
        landmarks.addNode(PusatKOK);
        landmarks.addNode(SUKSIS);
        landmarks.addNode(GolfClub);
        landmarks.addNode(InasisMuamalat);
        landmarks.addNode(InasisYAB);
        landmarks.addNode(PusatSukan);
        landmarks.addNode(InasisMISC);
        landmarks.addNode(InasisBSN);


        landmarks.addAllEdges();
        landmarks.printAdjancencyList();

        CampusMapGUI gui = new CampusMapGUI(landmarks);
        gui.displayGUI();
        
    }

    
}

