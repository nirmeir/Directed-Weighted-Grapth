import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.*;

public class Algorithms implements DirectedWeightedGraphAlgorithms {

    private MyDirectedWeightedGraph graph;

    public Algorithms() {
        this.graph = null;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = new MyDirectedWeightedGraph();
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
        Iterator<EdgeData> iterator = graph.edgeIter(x.getKey());
        while (iterator.hasNext()) {
            NodeData next = graph.getNode(iterator.next().getDest());
            if (next.getTag() == 0) {
                DFS(next, graph);
            }
        }
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<NodeData[], Double> res = DijkstraAlgo(this.graph, src, dest);
        Map.Entry<NodeData[], Double> entry = res.entrySet().iterator().next();
        double dist= entry.getValue();
        return dist;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<NodeData[], Double> res = DijkstraAlgo(this.graph, src, dest);
        Map.Entry<NodeData[], Double> entry = res.entrySet().iterator().next();
        NodeData[] prev = entry.getKey();
        NodeData nd = prev[dest];
        List<NodeData> path = new ArrayList<>();

        while(prev[nd.getKey()] != null){
            path.add(0, nd);
            nd = prev[nd.getKey()];
        }
        if(nd != null) path.add(0, nd);
        return path;
    }

    public HashMap<NodeData[], Double> DijkstraAlgo(MyDirectedWeightedGraph graph, int src, int dest){

        List<Integer> visit = new ArrayList<>();
        double dist[] = new double[graph.nodeSize()];
        NodeData prev[] = new NodeData[graph.nodeSize()];
        for (int i =0; i<dist.length; i++){
            visit.add(i);
            dist[i] = Integer.MAX_VALUE;
            prev[i] = null;
        }
        dist[src] = 0;

        while (!visit.isEmpty()){
            int lowerIndex = visit.get(0);
            double lowerValue = dist[visit.get(0)];
            for(int i = 1; i<visit.size(); i++){
                if(lowerValue > dist[visit.get(i)]){
                    lowerIndex = visit.get(i);
                    lowerValue = dist[visit.get(i)];
                }
            }

            Iterator<EdgeData> t = graph.edgeIter(lowerIndex);
            while(t.hasNext()){
                EdgeData ed = t.next();
                double alt = dist[lowerIndex] + ed.getWeight();
                if(alt < dist[ed.getDest()]){
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

/*    public void Dijkstra_Algo(MyDirectedWeightedGraph graph, NodeData src){
        PriorityQueue<NodeData> nodeQ = new PriorityQueue<>();
        src.setTag(0);
        src.setWeight(0);
        nodeQ.add(src);

        Iterator<NodeData> nodeIter = graph.nodeIter();
        while(nodeIter.hasNext()){
            NodeData node = nodeIter.next();
            node.setWeight(Integer.MAX_VALUE);
            node.setTag(0);
            //prev vertex
            if (node != src){
                nodeQ.add(node);
            }
        }
        while(!nodeQ.isEmpty()){
            NodeData currNode = nodeQ.remove();
            Iterator<EdgeData> edgeIter = graph.edgeIter(currNode.getKey());
            while(edgeIter.hasNext()){
                double EdgeWeight = edgeIter.next().getWeight();
                double checkDis = EdgeWeight + currNode.getWeight();
                NodeData neighbor = graph.getNode(edgeIter.next().getDest());
                if (neighbor.getWeight() > checkDis){
                    neighbor.setWeight(checkDis);

                }
            }
        }
    }*/

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
