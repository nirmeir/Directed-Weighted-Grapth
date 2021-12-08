import api.*;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Algorithms implements DirectedWeightedGraphAlgorithms {

    private MyDirectedWeightedGraph graph;

    public Algorithms() {
        this.graph = null;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = new MyDirectedWeightedGraph((MyDirectedWeightedGraph) g);
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new MyDirectedWeightedGraph(this.graph);
    }

    @Override
    public boolean isConnected() {

        if (this.graph.edgeSize() < this.graph.nodeSize()) { // there are less than n edges;
            return false;
        }

        MyDirectedWeightedGraph reverse = new MyDirectedWeightedGraph(); //reversing the graph
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) { //adding the nodes
            reverse.addNode(nodeIter.next());
        }
        Iterator<EdgeData> edgeIter = this.graph.edgeIter();
        while (edgeIter.hasNext()) { //adding the edges but in a reversed way
            EdgeData edge = edgeIter.next();
            reverse.connect(edge.getDest(), edge.getSrc(), edge.getWeight()); // src = dest, dest = src
        }

        return runDFS(this.graph) && runDFS(reverse);
    }

    private boolean runDFS(MyDirectedWeightedGraph graph) {
        Iterator<NodeData> iterator = graph.nodeIter();
        NodeData x = iterator.next();
        x.setTag(0);
        while (iterator.hasNext()) { //setting the tag of all nodes to be 0
            iterator.next().setTag(0);
        }
        DFS(x, graph); //running DFS on the graph

        iterator = graph.nodeIter();
        while (iterator.hasNext()) { //if one of the nodes has a tag==0 the graph is not connected
            if (iterator.next().getTag() == 0)
                return false;
        }

        return true;
    }

    private void DFS(NodeData x, MyDirectedWeightedGraph graph) {
        x.setTag(1);
        Iterator<EdgeData> edgeIter = graph.edgeIter(x.getKey());
        while (edgeIter.hasNext()) {
            NodeData next = graph.getNode(edgeIter.next().getDest());
            if (next.getTag() == 0) {
                DFS(next, graph);
            }
        }
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<NodeData[], Double> res = DijkstraAlgo(this.graph, src, dest);
        Map.Entry<NodeData[], Double> entry = res.entrySet().iterator().next();
        return entry.getValue();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<NodeData[], Double> res = DijkstraAlgo(this.graph, src, dest);
        Map.Entry<NodeData[], Double> entry = res.entrySet().iterator().next();
        NodeData[] prev = entry.getKey();
        NodeData nd = prev[dest];
        List<NodeData> path = new ArrayList<>();

        while (prev[nd.getKey()] != null) {
            path.add(0, nd);
            nd = prev[nd.getKey()];
        }
        if (nd != null) path.add(0, nd);
        return path;
    }

    public HashMap<NodeData[], Double> DijkstraAlgo(MyDirectedWeightedGraph graph, int src, int dest) {

        List<Integer> visit = new ArrayList<>();
        double[] dist = new double[graph.nodeSize()];
        NodeData[] prev = new NodeData[graph.nodeSize()];
        for (int i = 0; i < dist.length; i++) {
            visit.add(i);
            dist[i] = Integer.MAX_VALUE;
            prev[i] = null;
        }
        dist[src] = 0;

        while (!visit.isEmpty()) {
            int lowerIndex = visit.get(0);
            double lowerValue = dist[visit.get(0)];
            for (int i = 1; i < visit.size(); i++) {
                if (lowerValue > dist[visit.get(i)]) {
                    lowerIndex = visit.get(i);
                    lowerValue = dist[visit.get(i)];
                }
            }

            Iterator<EdgeData> edgeIter = graph.edgeIter(lowerIndex);
            while (edgeIter.hasNext()) {
                EdgeData ed = edgeIter.next();
                double alt = dist[lowerIndex] + ed.getWeight();
                if (alt < dist[ed.getDest()]) {
                    dist[ed.getDest()] = alt;
                    prev[ed.getDest()] = graph.getNode(lowerIndex);
                }
            }
            visit.remove(lowerIndex);
        }
        HashMap<NodeData[], Double> ret = new HashMap<NodeData[], Double>();
        ret.put(prev, dist[dest]);
        return ret;
    }

    @Override
    public NodeData center() {
        if (!isConnected()) { //if the graph isn't connected then we return null (there is no center)
            return null;
        }
        double maxDis = Double.MAX_VALUE; //max value so we can find the shortest path
        int nodeKey = 0;
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) { //finding the max shortest path to all others nodes
            int src = nodeIter.next().getKey();
            double maxShortPath = 0;
            Iterator<NodeData> nodeIter2 = this.graph.nodeIter();
            while (nodeIter2.hasNext()) { //finding the shortest path for each node
                NodeData dst = nodeIter2.next();
                if (dst != graph.getNode(src)) {
                    double checkPath = shortestPathDist(src, dst.getKey());
                    if (checkPath > maxShortPath) {
                        maxShortPath = checkPath;
                    }
                }
            }
            if (maxShortPath < maxDis) {
                maxDis = maxShortPath;
                nodeKey = src; // setting the center node of the graph
            }
        }
        return this.graph.getNode(nodeKey);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities.isEmpty()) { //if cities list is empty return null
            return null;
        }
        List<NodeData> salesman = new ArrayList<>();
        NodeData start = cities.get(0);
        // maybe https://www.geeksforgeeks.org/travelling-salesman-problem-set-2-approximate-using-mst/
        // and prim's algorithm https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/

        //garbage
        salesman.add(start); //adding to the list the starting city
        for (int i = 1; i < cities.size(); i++) { //loop on all the cities and find for them the shortest path
            NodeData dst = cities.get(i);
            List<NodeData> path = shortestPath(start.getKey(), dst.getKey());
            for (int j = 0; j < path.size(); j++) { //loop on all nodes that are part of the path from one city to another
                if (path.get(i) != start) {
                    salesman.add(path.get(i)); //add them to the list
                }
            }
            start = dst; //update the starting city for the next iteration
        }
        return salesman;
    }

    @Override
    public boolean save(String file) {
         GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        JsonSerializer<MyDirectedWeightedGraph> serializer = new JsonSerializer<MyDirectedWeightedGraph>() {
            @Override
            public JsonElement serialize(MyDirectedWeightedGraph graph, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonObject jsonGraph = new JsonObject();
                jsonGraph.add("Edges", new JsonArray());
                jsonGraph.add("Nodes", new JsonArray());
                Iterator<EdgeData> edgeIter = graph.edgeIter();
                while(edgeIter.hasNext()) {
                    JsonObject jsonEdgeObject = new JsonObject();
                    EdgeData edge = edgeIter.next();
                    jsonEdgeObject.addProperty("src", edge.getSrc());
                    jsonEdgeObject.addProperty("w", edge.getWeight());
                    jsonEdgeObject.addProperty("dest", edge.getDest());
                    jsonGraph.get("Edges").getAsJsonArray().add(jsonEdgeObject);
                }

                Iterator<NodeData> nodeIter = graph.nodeIter();
                while(nodeIter.hasNext()) {
                    JsonObject jsonNodeObject = new JsonObject();
                    NodeData node = nodeIter.next();
                    String pos = "" + node.getLocation().x() +
                            ',' +
                            node.getLocation().y() +
                            ',' +
                            node.getLocation().z();
                    jsonNodeObject.addProperty("pos", pos);
                    jsonNodeObject.addProperty("id", node.getKey());
                    jsonGraph.get("Nodes").getAsJsonArray().add(jsonNodeObject);

                }
                return jsonGraph;
            }
        };
        gsonBuilder.registerTypeAdapter(MyDirectedWeightedGraph.class, serializer);
        Gson graphGson = gsonBuilder.create();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.write(graphGson.toJson(this.graph));
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            line = br.readLine();
            while (line != null) {
                jsonString.append(line);
                line = br.readLine();
            }
            br.close();

            GsonBuilder gsonBuilder = new GsonBuilder();
            // change serialization for specific types
            JsonDeserializer<MyDirectedWeightedGraph> deserializer = new JsonDeserializer<MyDirectedWeightedGraph>() {
                @Override
                public MyDirectedWeightedGraph deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonObject jsonObject = json.getAsJsonObject();
                    MyDirectedWeightedGraph graph = new MyDirectedWeightedGraph();
                    JsonArray Edges = jsonObject.getAsJsonArray("Edges");
                    JsonArray Nodes = jsonObject.getAsJsonArray("Nodes");
                    Iterator<JsonElement> iterNodes = Nodes.iterator();
                    while (iterNodes.hasNext()) {
                        JsonElement node = iterNodes.next();
                        graph.addNode(new MyNodeData(node.getAsJsonObject().get("id").getAsInt()));
                        String coordinates = node.getAsJsonObject().get("pos").getAsString();
                        GeoLocation pos = new MyGeoLocation(coordinates);
                        graph.getNode(node.getAsJsonObject().get("id").getAsInt()).setLocation(pos);
                    }

                    Iterator<JsonElement> iterEdges = Edges.iterator();
                    int src, dest;
                    double w;
                    while (iterEdges.hasNext()) {
                        JsonElement edge = iterEdges.next();
                        src = edge.getAsJsonObject().get("src").getAsInt();
                        dest = edge.getAsJsonObject().get("dest").getAsInt();
                        w = edge.getAsJsonObject().get("w").getAsDouble();
                        graph.connect(src, dest, w);
                    }
                    return graph;
                }
            };

            gsonBuilder.registerTypeAdapter(MyDirectedWeightedGraph.class, deserializer);
            Gson graphGson = gsonBuilder.create();
            this.graph = graphGson.fromJson(jsonString.toString(), MyDirectedWeightedGraph.class);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        algo.save("G4.json");
    }
}

