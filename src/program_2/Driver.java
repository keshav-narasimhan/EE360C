package program_2;
// This Driver file will be replaced by ours during grading
// Do not include this file in your final submission

import java.io.File;
import java.util.*;

public class Driver {
    private static String filename; // input file name
    private static boolean testHeap; // set to true by -h flag
    private static boolean testDijkstra; // set to true by -d flag
    private static boolean testMST; // set to true by -m flag
    private static Program2 testProgram2; // instance of your graph
    private static ArrayList<City> cities;

    private static void usage() { // error message
        System.err.println("usage: java Driver [-h] [-d] [-m] <filename>");
        System.err.println("\t-h\tTest Heap implementation");
        System.err.println("\t-d\tTest Dijkstra implementation");
        System.err.println("\t-m\tTest MST implementation");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        cities = new ArrayList<City>();
        parseArgs(args);
        parseInputFile(filename);
        testRun();
    }

    public static void parseArgs(String[] args) {
        boolean flagsPresent = false;
        if (args.length == 0) {
            usage();
        }

        filename = "";

        testMST = false;
        for (String s : args) {
            if (s.equals("-h")) {
                flagsPresent = true;
                testHeap = true;
            } else if (s.equals("-d")) {
                flagsPresent = true;
                testDijkstra = true;
            } else if (s.equals("-m")) {
                flagsPresent = true;
                testMST = true;
            } else if (!s.startsWith("-")) {
                filename = s;
            } else {
                System.err.printf("Unknown option: %s\n", s);
                usage();
            }
        }

        if (!flagsPresent) {
            testHeap = true;
            testDijkstra = true;
            testMST = true;
        }
    }

    public static void parseInputFile(String filename)
            throws Exception {
        int numV = 0, numE = 0;
        Scanner sc = new Scanner(new File(filename));
        String[] inputSize = sc.nextLine().split(" ");
        numV = Integer.parseInt(inputSize[0]);
        numE = Integer.parseInt(inputSize[1]);
        HashMap<Integer, ArrayList<NeighborWeightTuple>> tempNeighbors = new HashMap<>();
        testProgram2 = new Program2(numV);
        for (int i = 0; i < numV; ++i) {

            String[] pairs = sc.nextLine().split(" ");
            String[] weightPairs = sc.nextLine().split(" ");

            Integer currNode = Integer.parseInt(pairs[0]);
            City currCity = new City(currNode);
            cities.add(currNode, currCity);
            ArrayList<NeighborWeightTuple> currNeighbors = new ArrayList<>();
            tempNeighbors.put(currNode, currNeighbors);

            for (int k = 1; k < pairs.length; k++) {
                Integer neighborVal = Integer.parseInt(pairs[k]);
                Integer weightVal = Integer.parseInt(weightPairs[k]);
                currNeighbors.add(new NeighborWeightTuple(neighborVal, weightVal));
            }
        }
        for (int i = 0; i < cities.size(); ++i) {
            City currCity = cities.get(i);
            ArrayList<NeighborWeightTuple> neighbors = tempNeighbors.get(i);
            for (NeighborWeightTuple neighbor : neighbors) {
                testProgram2.setEdge(currCity, cities.get(neighbor.neighborID), neighbor.weight);
            }
        }

        testProgram2.setAllNodesArray(cities);
    }

    // feel free to alter this method however you wish, we will replace it with our own version during grading
    public static void testRun() {
        if (testHeap) {
            // test out Heap.java here
        	Heap h = new Heap();
        	ArrayList <City> c = new ArrayList<>();
        	for (int i = 0; i < cities.size(); i++) {
        		c.add(cities.get(i));
        	}
        	c.get(2).setMinDist(25);
        	c.get(4).setMinDist(24);
        	c.get(0).setMinDist(23);
        	c.get(1).setMinDist(99);
        	c.get(3).setMinDist(50);
        	c.get(5).setMinDist(9);
        	h.buildHeap(c);
        	City ci = new City(6);
        	City ci2 = new City(7);
        	ci.setMinDist(2);
        	ci2.setMinDist(25);
        	h.insertNode(ci);
        	h.insertNode(ci2);
        	h.delete(3);
        	ArrayList<City> test = h.toArrayList();
        	for (int i = 0; i < test.size(); i++) {
        		System.out.print(test.get(i).getMinDist() + "-Name:" + test.get(i).getName() + " ");
        	}
        }

        if (testDijkstra) {
            // test out Program2.java findMinimumRouteDistance here
            System.out.println("\nGiven graph: ");
            System.out.println(testProgram2);
            System.out.println("Length of shortest path from start to dest: \n" +
                    testProgram2.findMinimumRouteDistance(cities.get(7), cities.get(2)));
        }

        if (testMST) {
            // test out Program2.java findMinimumLength here
            System.out.println("\nGiven graph: ");
            System.out.println(testProgram2);
            System.out.println("Minimum total optical line distance: \n" + testProgram2.findMinimumLength());
        }
    }

    private static class NeighborWeightTuple {
        public Integer neighborID;
        public Integer weight;

        NeighborWeightTuple(Integer neighborID, Integer weight) {
            this.neighborID = neighborID;
            this.weight = weight;
        }
    }
}
