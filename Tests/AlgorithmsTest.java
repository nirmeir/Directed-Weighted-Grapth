import api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @Test
    void init() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
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
        graph.connect(2, 1, 6);
        algo.init(graph);
        System.out.println(algo.getGraph());
    }

    @Test
    void getGraph() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        System.out.println(algo.getGraph().toString());
    }

    @Test
    void copy() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        DirectedWeightedGraph copy = algo.copy();
        assertNotEquals(copy, algo.getGraph());
    }

    @Test
    void isConnected() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        assertTrue(algo.isConnected());
        algo.load(".\\data\\G2.json");
        assertTrue(algo.isConnected());
        algo.load(".\\data\\G3.json");
        assertTrue(algo.isConnected());
    }

    @Test
    void shortestPathDist() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        assertEquals(10.52002088011531, algo.shortestPathDist(1, 11));
        assertEquals(0.0, algo.shortestPathDist(1, 1));
    }

    @Test
    void shortestPath() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        List<NodeData> list1 = new ArrayList<>();
        list1.add(algo.getGraph().getNode(3));
        list1.add(algo.getGraph().getNode(2));
        list1.add(algo.getGraph().getNode(6));
        list1.add(algo.getGraph().getNode(7));
        list1.add(algo.getGraph().getNode(8));
        list1.add(algo.getGraph().getNode(9));
        assertEquals(list1, algo.shortestPath(3, 9));
        assertNull(algo.shortestPath(5, 5));
        List<NodeData> list2 = new ArrayList<>();
        list2.add(algo.getGraph().getNode(3));
        list2.add(algo.getGraph().getNode(4));
        assertEquals(list2, algo.shortestPath(3, 4));
    }

    @Test
    void center() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        assertEquals(8, algo.center().getKey());
        algo.load(".\\data\\G2.json");
        assertEquals(0, algo.center().getKey());
        algo.load(".\\data\\G3.json");
        assertEquals(40, algo.center().getKey());
        algo.load(".\\data\\G4.json");
        assertEquals(2, algo.center().getKey());
        algo.load(".\\data\\G5.json");
        assertEquals(6, algo.center().getKey());
        algo.load(".\\data\\G6.json");
        assertEquals(7, algo.center().getKey());
    }

    @Test
    void tsp() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        algo.load(".\\data\\G1.json");
        List<NodeData> city1 = new ArrayList<>();
        city1.add(algo.getGraph().getNode(0));
        city1.add(algo.getGraph().getNode(1));
        city1.add(algo.getGraph().getNode(2));
        city1.add(algo.getGraph().getNode(5));
        city1.add(algo.getGraph().getNode(7));
        List<NodeData> tsp1 = new ArrayList<>();
        tsp1.add(algo.getGraph().getNode(0));
        tsp1.add(algo.getGraph().getNode(1));
        tsp1.add(algo.getGraph().getNode(2));
        tsp1.add(algo.getGraph().getNode(6));
        tsp1.add(algo.getGraph().getNode(5));
        tsp1.add(algo.getGraph().getNode(6));
        tsp1.add(algo.getGraph().getNode(7));
        assertEquals(tsp1, algo.tsp(city1));

        assertNull(algo.tsp(null));
        assertNull(algo.tsp(new ArrayList<>()));

        algo.load(".\\data\\G4.json");
        List<NodeData> city2 = new ArrayList<>();
        city2.add(algo.getGraph().getNode(12));
        city2.add(algo.getGraph().getNode(9));
        city2.add(algo.getGraph().getNode(1));
        city2.add(algo.getGraph().getNode(16));
        city2.add(algo.getGraph().getNode(21));
        List<NodeData> tsp2 = new ArrayList<>();
        tsp2.add(algo.getGraph().getNode(12));
        tsp2.add(algo.getGraph().getNode(11));
        tsp2.add(algo.getGraph().getNode(10));
        tsp2.add(algo.getGraph().getNode(9));
        tsp2.add(algo.getGraph().getNode(8));
        tsp2.add(algo.getGraph().getNode(26));
        tsp2.add(algo.getGraph().getNode(1));
        tsp2.add(algo.getGraph().getNode(0));
        tsp2.add(algo.getGraph().getNode(16));
        tsp2.add(algo.getGraph().getNode(0));
        tsp2.add(algo.getGraph().getNode(21));
        assertEquals(tsp2, algo.tsp(city2));
    }

    @Test
    void save() {
        DirectedWeightedGraphImp graph = new DirectedWeightedGraphImp();
        Algorithms algo = new Algorithms();
        algo.init(graph);
        GeoLocation p1 = new GeoLocationImp(1, 1, 0);
        GeoLocation p2 = new GeoLocationImp(4, 2, 0);
        GeoLocation p3 = new GeoLocationImp(7, 10, 0);
        NodeData n1 = new NodeDataImp(p1, 1);
        NodeData n2 = new NodeDataImp(p2, 2);
        NodeData n3 = new NodeDataImp(p3, 3);
        algo.getGraph().addNode(n1);
        algo.getGraph().addNode(n2);
        algo.getGraph().addNode(n3);
        algo.getGraph().connect(1, 2, 3);
        algo.getGraph().connect(2, 3, 4);
        algo.getGraph().connect(1, 3, 5);
        algo.getGraph().connect(2, 1, 6);
        algo.save("save.json");
    }

    @Test
    void load() {
        //used everywhere in the test
    }
}