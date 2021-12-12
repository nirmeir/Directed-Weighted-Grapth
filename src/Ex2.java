import Graphics.GUI;
import api.Algorithms;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.DirectedWeightedGraphImp;

import java.io.IOException;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = new DirectedWeightedGraphImp();
        // ****** Add your code here ******
        Algorithms algo = new Algorithms();
        algo.init(ans);
        algo.load(json_file);
        // ********************************
        return algo.getGraph();
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) throws IOException {
        DirectedWeightedGraphAlgorithms ans = new Algorithms();
        // ****** Add your code here ******
        DirectedWeightedGraph graph = new DirectedWeightedGraphImp();
        ans.init(graph);
        ans.load(json_file);
        // ********************************
        return ans;
    }

    /**
     * This static function will run your Graphics.GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) throws IOException {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        // ****** Add your code here ******
        GUI gui = new GUI(alg);
        gui.setVisible(true);

        // ********************************
    }

    public static void main(String[] args) throws IOException {
        runGUI(".\\data\\G1.json");
        //runGUI(".\\data\\" + args[0]);

    }
}