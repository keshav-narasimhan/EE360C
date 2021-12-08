package program_2;
/*
 * Name: Keshav Narasimhan
 * EID: kn9558
 */

// Implement your algorithms here
// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.ArrayList;

public class Program2 {
    private ArrayList<City> cities;  // this is a list of all Cities, populated by Driver class
    private Heap minHeap;

    // additional constructor fields may be added, but don't delete or modify anything already here
    public Program2(int numCities) {
        minHeap = new Heap();
        cities = new ArrayList<City>();
    }

    /**
     * findMinimumRouteDistance(City start, City dest)
     *
     * @param start - the starting City.
     * @param dest  - the end (destination) City.
     * @return the minimum distance possible to get from start to dest.
     * Assume the given graph is always connected.
     */
    public int findMinimumRouteDistance(City start, City dest) {
        // TODO: implement this function
    	
    	// preprocessing
    	for (int i = 0; i < cities.size(); i++) {
    		cities.get(i).setMinDist(Integer.MAX_VALUE);
    	}
    	
    	// indicate that the start city should be the head of the min_heap
    	start.setMinDist(0);
    	
    	// build a heap out of all the cities
    	minHeap.buildHeap(cities);
    	
    	// loop through the min-heap while there still exists a city in it
    	while(!minHeap.isEmpty()) {
    		
    		// extract the city with the smallest minDist
    		City curr = this.minHeap.extractMin();
    		
    		// show that it has been extracted from the queue
    		curr.setCityConnect(-1);
    		
    		// loop through the current city's neighbors and focus on the edge weight
    		for (int i = 0; i < curr.getNeighbors().size(); i++) {
    			City neighbor = curr.getNeighbors().get(i);
    			int edgeWeight = curr.getWeights().get(i);
    			
    			// call the helper function to update minDist and the heap if needed
    			DijkstraRelax(curr, neighbor, edgeWeight);
    		}
    		
    	}
    	
    	// return the minDist from the start to dest
        return dest.getMinDist();
    }
    
    /**
     * Helper method that performs the RELAX operation from the Dijkstra Algorithm shown in class
     * @param curr -- the current city extracted from the min-heap
     * @param neighbor -- the neighboring city connected to curr
     * @param edgeWeight -- the weight of the edge connecting neighbor to curr
     */
    private void DijkstraRelax(City curr, City neighbor, int edgeWeight) {
    	
    	// if neighbor's minDist is greater than the minDist to curr added to the weight of the edge connecting curr to neighbor
    	if (neighbor.getMinDist() > curr.getMinDist() + edgeWeight) {
    		
    		// if the neighbor is still in the queue
    		if (neighbor.getCityConnect() != -1) {
    			
    			// update neighbor's minDist
        		int newDist = curr.getMinDist() + edgeWeight;
        		neighbor.setMinDist(newDist);
        		
        		// rearrange the heap since we updated neighbor's minDist
    			this.minHeap.HeapifyUp(neighbor.getIndex());
    		}
    	}
    }

    /**
     * findMinimumLength()
     *
     * @return The minimum total optical line length required to connect (span) each city on the given graph.
     * Assume the given graph is always connected.
     */
    public int findMinimumLength() {
        // TODO: implement this function
    	
    	// preprocessing
    	for (int i = 0; i < cities.size(); i++) {
    		cities.get(i).setMinDist(Integer.MAX_VALUE);
    	}
    	
    	// label a start node
    	cities.get(0).setMinDist(0);
    	
    	// build a heap out of the cities arrayList
    	minHeap.buildHeap(cities);
    	
    	// set a local variable to keep track of minLength
    	int minLength = 0;
    	
    	// loop through the heap while it still contains a node
    	while (!minHeap.isEmpty()) {
    		
    		// extract the city with the smallest minDist and set a helper field to -1 (out of the queue)
    		City smallestDist = minHeap.extractMin();
    		smallestDist.setCityConnect(-1);
    		minLength += smallestDist.getMinDist();
    		
    		// loop through the current city's neighbors and focus on the edge weight
    		for (int i = 0; i < smallestDist.getNeighbors().size(); i++) {
    			City neighbor = smallestDist.getNeighbors().get(i);
    			int edge_weight = smallestDist.getWeights().get(i);
    			
    			// if the neighbor still exists in the queue
    			if(neighbor.getCityConnect() != -1) {
    				
    				// check to see if the distance from the current MST to the neighbor is smaller than minDist
    				if (neighbor.getMinDist() > edge_weight) {
    					
    					// if so, update minDist
    					neighbor.setMinDist(edge_weight);
    					
    					// run HeapifyUp to rearrange the min-heap
    					this.minHeap.HeapifyUp(neighbor.getIndex());
    				}
    			}
    		}
    	}
    	
    	// return the minLength
    	return minLength;
    }

    //returns edges and weights in a string.
    public String toString() {
        String o = "";
        for (City v : cities) {
            boolean first = true;
            o += "City ";
            o += v.getName();
            o += " has neighbors ";
            ArrayList<City> ngbr = v.getNeighbors();
            for (City n : ngbr) {
                o += first ? n.getName() : ", " + n.getName();
                first = false;
            }
            first = true;
            o += " with distances ";
            ArrayList<Integer> wght = v.getWeights();
            for (Integer i : wght) {
                o += first ? i : ", " + i;
                first = false;
            }
            o += System.getProperty("line.separator");

        }

        return o;
    }

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public Heap getHeap() {
        return minHeap;
    }

    public ArrayList<City> getAllCities() {
        return cities;
    }

    // used by Driver class to populate each City with correct neighbors and corresponding weights
    public void setEdge(City curr, City neighbor, Integer weight) {
        curr.setNeighborAndWeight(neighbor, weight);
    }

    // used by Driver.java and sets cities to reference an ArrayList of all RestStops
    public void setAllNodesArray(ArrayList<City> x) {
        cities = x;
    }
}
