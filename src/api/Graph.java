package api;

import java.util.HashMap;
import java.util.Iterator;

public class Graph implements DirectedWeightedGraph {

    HashMap<String, EdgeData> edges;
    HashMap<Integer, NodeData> nodes;

    static final char edgeSpaceKey = '_';

    public Graph(){
        edges = new HashMap<String, EdgeData>();
        nodes = new HashMap<Integer, NodeData>();
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);

        return edges.get(key);
    }

    @Override
    public void addNode(NodeData n) {
        int key = n.getKey();
        nodes.put(key,n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        EdgeData edge = new EdgeDataImp(src,dest, w);
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        Iterator t = nodes.values().iterator();
        return t;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        Iterator t = edges.values().iterator();
        return t;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
        NodeData nd = nodes.get(key);
        nodes.remove(key);

        return nd;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        EdgeData ed = edges.get(key);
        edges.remove(ed);

        return ed;
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int getMC() {
        return 0;
    }
}
