import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class DirectedWeightedGraphImp implements api.DirectedWeightedGraph {

    HashMap<String, EdgeData> edges;
    HashMap<Integer, NodeData> nodes;
    HashMap<Integer, edgeForNode> edgePerNode;
    int mc;

    static final char edgeSpaceKey = '_';

    public DirectedWeightedGraphImp() {
        this.edges = new HashMap<String, EdgeData>();
        this.nodes = new HashMap<Integer, NodeData>();
        this.edgePerNode = new HashMap<Integer, edgeForNode>();
        this.mc = 0;
    }

    public DirectedWeightedGraphImp(DirectedWeightedGraphImp g) {
        this.edges = new HashMap<String, EdgeData>();
        this.nodes = new HashMap<Integer, NodeData>();
        this.edgePerNode = new HashMap<Integer, edgeForNode>();


        Iterator<NodeData> t1 = this.nodeIter();
        while (t1.hasNext()) {
            this.addNode(t1.next());
        }

        Iterator<EdgeData> t2 = this.edgeIter();
        while (t2.hasNext()) {
            EdgeData ed = t2.next();
            this.connect(ed.getSrc(), ed.getDest(), ed.getWeight());
        }

        this.mc = g.mc;
    }

    @Override
    public NodeData getNode(int key) {
        if (!nodes.containsKey(key)) {
            System.out.println("Key doesn't exist in the graph");
            return null;
        }
        return this.nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest)) {
            return null;
        }
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        if (!edges.containsKey(key)) {
            return null;
        }
        return this.edges.get(key);
    }

    @Override
    public void addNode(NodeData n) {
        int key = n.getKey();
        this.nodes.put(key, n);
        this.edgePerNode.put(n.getKey(), new edgeForNode());

        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest)) {
            return;
        }
        EdgeData edge = new EdgeDataImp(src, w, dest);
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        this.edges.put(key, edge);

        // add edge to nodes - if from then edgeForNode.src else edgeForNode.dst
        this.edgePerNode.get(src).src.put(dest, edge); // added to src node
        this.edgePerNode.get(dest).dst.put(src, edge); //added to dst node

        mc++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return new NodeIterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new EdgeIterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (!nodes.containsKey(node_id)) {
            System.out.println("Key doesn't exist in the graph");
            return null;
        }
        return new EdgeIteratorPerNode(node_id);
    }

    @Override
    public NodeData removeNode(int key) {
        if (!nodes.containsKey(key)) {
            System.out.println("Key doesn't exist in the graph");
            return null;
        }
        NodeData nd = this.nodes.get(key);
        if (nd == null) return null;

        Iterator<EdgeData> iter = this.edgeIter(key);
        while (iter.hasNext()) {
            EdgeData e = iter.next();
            String curKey = Integer.toString(e.getSrc()) + edgeSpaceKey + Integer.toString(e.getDest());

            this.edges.remove(curKey);
            this.edgePerNode.get(e.getDest()).dst.remove(e.getSrc());

//            this.edgePerNode.get(e.getDest()).dst.remove(key); // removing from edgeForNode in dst position.
        }
        iter = this.edgePerNode.get(key).dst.values().iterator();
        while (iter.hasNext()) {
            EdgeData e = iter.next();
            this.removeEdge(e.getSrc(), e.getDest()); // removing from edges
//            this.edgePerNode.get(e.getSrc()).src.remove(key); // removing from edgeForNode in src position.
        }
        this.edgePerNode.remove(key);
        this.nodes.remove(key);
        mc++;
        return nd;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest) || getEdge(src, dest) == null) {
            return null;
        }
        String key = Integer.toString(src) + edgeSpaceKey + Integer.toString(dest);
        EdgeData ed = this.edges.get(key);
        this.edges.remove(key);

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

    private class edgeForNode {
        public HashMap<Integer, EdgeData> src; // I'm the src
        public HashMap<Integer, EdgeData> dst; // I'm the dest

        public edgeForNode() {
            this.src = new HashMap<Integer, EdgeData>();
            this.dst = new HashMap<Integer, EdgeData>();
        }
    }

    @Override
    public String toString() {
        return "DirectedWeightedGraphImp{" +
                "edges=" + edges +
                ", nodes=" + nodes +
                ", mc=" + mc +
                '}';
    }

    private class EdgeIterator implements Iterator<EdgeData> {
        private Iterator<EdgeData> iterator;
        private EdgeData posEdge;
        private int compareMC; // compare between our MC to the main class graph MC
        public EdgeIterator() {
            this.compareMC = mc;
            this.iterator = edges.values().iterator();
        }

        @Override
        public boolean hasNext() {
            isOK();
            return this.iterator.hasNext();
        }
        @Override
        public EdgeData next() {
            isOK();
            this.posEdge = this.iterator.next();
            return this.posEdge;
        }
        @Override
        public void remove() {
            isOK();
            this.compareMC++;
            this.iterator.remove();
            removeEdge(this.posEdge.getSrc(), this.posEdge.getDest());
        }
        private void isOK() {
            if (this.compareMC != mc) {
                throw new RuntimeException("Can't continue, the graph has been changed");
            }
        }
    }
    private class EdgeIteratorPerNode implements Iterator<EdgeData> {
        private Iterator<EdgeData> iterator;
        private EdgeData posEdge;
        private int compareMC; // compare between our MC to the main class graph MC
        public EdgeIteratorPerNode(int node_id) {
            this.compareMC = mc;
            this.iterator = edgePerNode.get(node_id).src.values().iterator();
        }

        @Override
        public boolean hasNext() {
            isOK();
            return this.iterator.hasNext();
        }
        @Override
        public EdgeData next() {
            isOK();
            this.posEdge = this.iterator.next();
            return this.posEdge;
        }
        @Override
        public void remove() {
            isOK();
            this.compareMC++;
            this.iterator.remove();
            removeEdge(this.posEdge.getSrc(), this.posEdge.getDest());

        }

        private void isOK() {
            if (this.compareMC != mc) {
                throw new RuntimeException("Can't continue, the graph has been changed");
            }
        }
    }
    private class NodeIterator implements Iterator<NodeData> {
        private Iterator<NodeData> iterator;
        private NodeData posNode;
        private int compareMC; // compare between our MC to the main class graph MC
        public NodeIterator() {
            this.compareMC = mc;
            this.iterator = nodes.values().iterator();
        }
        @Override
        public boolean hasNext() {
            isOK();
            return this.iterator.hasNext();
        }
        @Override
        public NodeData next() {
            isOK();
            this.posNode = this.iterator.next();
            return this.posNode;
        }
        @Override
        public void remove() {
            isOK();
            this.compareMC++;
            this.iterator.remove();
            removeNode(this.posNode.getKey());
        }
        private void isOK() {
            if (compareMC != mc) { // if this mc is not equal to main mc than we made something to the main graph and we need a new iterator
                throw new RuntimeException("Can't continue, the graph has been changed");
            }
        }
    }

}
