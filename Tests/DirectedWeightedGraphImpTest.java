import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphImpTest {

    @Test
    void getNode() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        graph = algo.getGraph();
        NodeData node = graph.getNode(5);
        System.out.println(node);
        assertNull(graph.getNode(82)); //if the key isn't in the graph
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        NodeData n1 = new NodeDataImp(p1, 23);
        graph.addNode(n1);
        assertEquals(graph.getNode(23),n1);
    }

    @Test
    void getEdge() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        graph = algo.getGraph();
        EdgeData edge = graph.getEdge(4,5);
        System.out.println(edge);
        System.out.println(edge.getWeight());
        assertNull(graph.getEdge(8,12)); //if there is no edge
        assertNull(graph.getEdge(4,43)); //if one of the keys isn't in the graph
    }

    @Test
    void addNode() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        NodeData n1 = new NodeDataImp(p1, 23);
        graph.addNode(n1);
        assertNotNull(graph.getNode(23));
    }

    @Test
    void connect() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(1, 2, 3);
        graph.connect(2, 3, 4);
        graph.connect(1, 3, 5);
        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while(edgeIter.hasNext()){
            System.out.println(edgeIter.next());
        }
    }

    @Test
    void nodeIter() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while(nodeIter.hasNext()) {
            System.out.println(nodeIter.next());
        }
    }

    @Test
    void edgeIter() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(1, 2, 3);
        graph.connect(2, 3, 4);
        graph.connect(1, 3, 5);
        Iterator<EdgeData> edgeIter = graph.edgeIter();
        while(edgeIter.hasNext()){
            System.out.println(edgeIter.next());
        }
    }

    @Test
    void testEdgeIter() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(1, 2, 3);
        graph.connect(2, 3, 4);
        graph.connect(1, 3, 5);
        Iterator<EdgeData> edgeIter = graph.edgeIter(1);
        while(edgeIter.hasNext()){
            System.out.println(edgeIter.next());
        }
    }

    @Test
    void removeNode() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        Iterator<NodeData> nodeIter = graph.nodeIter();
        while(nodeIter.hasNext()) {
            System.out.println(nodeIter.next());
        }
        //graph.removeNode(n1.getKey()); //problem
        assertNull(graph.removeNode(5));
    }

    @Test
    void removeEdge() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.connect(1, 2, 3);
        graph.connect(2, 3, 4);
        graph.connect(1, 3, 5);
        Iterator<EdgeData> edgeIter1 = graph.edgeIter();
        while(edgeIter1.hasNext()){
            System.out.println(edgeIter1.next());
        }
        graph.removeEdge(1,2);
        Iterator<EdgeData> edgeIter2 = graph.edgeIter();
        while(edgeIter2.hasNext()){
            System.out.println(edgeIter2.next());
        }
    }

    @Test
    void nodeSize() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        graph = algo.getGraph();
        assertEquals(17,graph.nodeSize());
    }

    @Test
    void edgeSize() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        graph = algo.getGraph();
        assertEquals(36,graph.edgeSize());
    }

    @Test
    void getMC() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.load(".\\data\\G1.json");
        graph = algo.getGraph();
        assertEquals(53,graph.getMC());
        graph.connect(3,7,1.4);
        assertEquals(54,graph.getMC());
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        NodeData n1 = new NodeDataImp(p1, 23);
        graph.addNode(n1);
        assertEquals(55,graph.getMC());
    }
}