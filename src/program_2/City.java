package program_2;
/*
 * Name: Keshav Narasimhan
 * EID: kn9558
 */

// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.*;

public class City {
    private int minDist;
    private int name;
    private ArrayList<City> neighbors;
    private ArrayList<Integer> weights;

    /**
     * Added field to keep track of index in minHeap
     */
    private int indexHeap;
    
    /**
     * Added field to keep track of whether the city is in the min-heap or not
     */
    private int cityConnect;

    public City(int x) {
        name = x;
        minDist = Integer.MAX_VALUE;
        neighbors = new ArrayList<City>();
        weights = new ArrayList<Integer>();
        
        indexHeap = 0;
        cityConnect = this.name;
    }

    public void setNeighborAndWeight(City n, Integer w) {
        neighbors.add(n);
        weights.add(w);
    }

    public ArrayList<City> getNeighbors() {
        return neighbors;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    public int getMinDist() { return minDist; }

    public void setMinDist(int x) {
        minDist = x;
    }

    public void resetMinDist() {
        minDist = Integer.MAX_VALUE;
    }

    public int getName() {
        return name;
    }
    
    /**
     * Added Setters to Help Swap Cities in Heapify
     */
    
    public void setName(int name) {
		this.name = name;
	}

	public void setNeighbors(ArrayList<City> neighbors) {
		this.neighbors = neighbors;
	}

	public void setWeights(ArrayList<Integer> weights) {
		this.weights = weights;
	}
	
	/**
	 * Added Getter + Setter for indexHeap
	 */
	public int getIndex() {
		return this.indexHeap;
	}
	
	public void setIndex(int index) {
		this.indexHeap = index;
	}

	public int getCityConnect() {
		return cityConnect;
	}

	public void setCityConnect(int cityConnect) {
		this.cityConnect = cityConnect;
	}
	
	
}
