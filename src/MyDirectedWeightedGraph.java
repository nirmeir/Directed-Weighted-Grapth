import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MyDirectedWeightedGraph implements api.DirectedWeightedGraph {

    HashMap<String, EdgeData> edges;
    HashMap<Integer, NodeData> nodes;
    HashMap<Integer, edgeForNode> edgePerNode;
    int mc;

    static final char edgeSpaceKey = '_';

    public MyDirectedWeightedGraph(){
        this.edges = new HashMap<String, EdgeData>();
        this.nodes = new HashMap<Integer, NodeData>();
        this.edgePerNode = new HashMap<Integer, edgeForNode>();
        this.mc = 0;
    }

    public MyDirectedWeightedGraph (MyDirectedWeightedGraph g){
        this.edges = new HashMap<String, EdgeData>();
        this.nodes = new HashMap<Integer, NodeData>();
        this.edgePerNode = new HashMap<Integer, edgeForNode>();


        Iterator<NodeData> t1 = this.nodeIter();
        while(t1.hasNext()){
            this.addNode( t1.next());
        }

        Iterator<EdgeData> t2=this.edgeIter();
        while (t2.hasNext()){
            EdgeData ed = t2.next();
            this.connect(ed.getSrc(),ed.getDest(),ed.getWeight());
        }

        this.mc = g.mc;
    }

    @Override
    public NodeData getNode(int key) {
        return this.nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);

        return this.edges.get(key);
    }

    @Override
    public void addNode(NodeData n) {
        int key = n.getKey();
        this.nodes.put(key,n);
        this.edgePerNode.put(n.getKey(), new edgeForNode());

        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        EdgeData edge = new MyEdgeData(src,dest, w);
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        this.edges.put(key,edge);

        // add edge to nodes - if from then edgeForNode.src else edgeForNode.dst
        this.edgePerNode.get(src).src.put(dest,edge); // added to src node
        this.edgePerNode.get(dest).dst.put(src,edge); //added to dst node

        mc++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        Iterator t = this.nodes.values().iterator();
        return t;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        Iterator t = this.edges.values().iterator();
        return t;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return this.edgePerNode.get(node_id).src.values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        NodeData nd = this.nodes.get(key);
        this.nodes.remove(key);

        if(nd == null) return null;

        Iterator<EdgeData> iter = this.edgeIter(key);
        while(iter.hasNext()){
            EdgeData e = iter.next();
            this.edges.remove(e); // removing from edges
            this.edgePerNode.get(e.getDest()).dst.remove(key); // removing from edgeForNode in dst position.
        }

        iter = this.edgePerNode.get(key).dst.values().iterator();
        while(iter.hasNext()){
            EdgeData e = iter.next();
            this.edges.remove(e); // removing from edges
            this.edgePerNode.get(e.getSrc()).src.remove(key); // removing from edgeForNode in src position.
        }

        this.edgePerNode.remove(key);

        mc++;

        return nd;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        EdgeData ed = this.edges.get(key);
        this.edges.remove(ed);

        this.edgePerNode.get(src).src.remove(dest);
        this.edgePerNode.get(dest).dst.remove(dest);

        mc++;

        return ed;
    }

    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    @Override
    public int edgeSize() {
        return this.edges.size();
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    private class edgeForNode{
        public HashMap <Integer,  EdgeData> src; // I'm the src
        public HashMap <Integer,  EdgeData> dst; // I'm the dest
        public edgeForNode() {
            this.src = new HashMap<Integer,  EdgeData>();
            this.dst = new HashMap<Integer,  EdgeData>();
        }
    }
}
