package program_2;
/*
 * Name: Keshav Narasimhan
 * EID: kn9558
 */

// Implement your heap here
// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.ArrayList;

public class Heap {
	
	/**
	 * Private fields of Heap class
	 * 
	 * minHeap -- a minimum heap of the cities 
	 */
    private ArrayList<City> minHeap;
    
    /**
     * Constructor to initialize fields of Heap class
     */
    public Heap() {
        minHeap = new ArrayList<City>();
    }
    
    /**
     * Helper method that performs the 'Heapify-Up' functionality discussed in class
     * Time complexity - O(log(n)) in the worst case because you may need to perform log(n) swaps
     * 
     * @param cityIndex -- index of city to check to see if needs to propagate up the heap
     */
    public void HeapifyUp(int cityIndex) {
    	if (cityIndex == 0) {
    		return;
    	} 
    	
    	int parentCityIndex = ((cityIndex + 1) / 2) - 1;
    	City parent = this.minHeap.get(parentCityIndex);
    	City currCity = this.minHeap.get(cityIndex);
    	
    	int parentCityMinDist = this.minHeap.get(parentCityIndex).getMinDist();
    	int cityMinDist = this.minHeap.get(cityIndex).getMinDist();
    	
    	if (parentCityMinDist > cityMinDist) {
    		
    		this.minHeap.add(currCity);
    		this.minHeap.set(cityIndex, parent);
    		this.minHeap.set(parentCityIndex, currCity);
    		this.minHeap.remove(this.minHeap.size() - 1);
    		
    		currCity.setIndex(parentCityIndex);
    		parent.setIndex(cityIndex);
    		
    		HeapifyUp(parentCityIndex);
    	} else if (parentCityMinDist == cityMinDist && currCity.getName() < parent.getName()) {
    		
    		this.minHeap.add(currCity);
    		this.minHeap.set(cityIndex, parent);
    		this.minHeap.set(parentCityIndex, currCity);
    		this.minHeap.remove(this.minHeap.size() - 1);
    		
    		currCity.setIndex(parentCityIndex);
    		parent.setIndex(cityIndex);
    		
    		HeapifyUp(parentCityIndex);
    	}
    }
    
    /**
     * Helper method that performs the 'Heapify-Down' functionality discussed in class
     * Time complexity - O(log(n)) in the worst case because you may need to perform log(n) swaps
     * 
     * @param cityIndex -- index of city to check to see if needs to propagate down the heap
     */
    public void HeapifyDown(int cityIndex) {
    	if (cityIndex >= this.minHeap.size() || ((cityIndex + 1) * 2) - 1 >= this.minHeap.size()) {
    		return;
    	}
    	
    	int cityChildOneIndex = ((cityIndex + 1) * 2) - 1;
    	int cityChildTwoIndex = (cityIndex + 1) * 2;
    	
    	City currCity = this.minHeap.get(cityIndex);
    	
    	City smallerChildCity;
    	int smallerChildCityIndex;
    	
    	if (cityChildTwoIndex >= this.minHeap.size()) {
    		smallerChildCity = this.minHeap.get(cityChildOneIndex);
    		smallerChildCityIndex = cityChildOneIndex;
    	} else {
    		if (this.minHeap.get(cityChildOneIndex).getMinDist() > this.minHeap.get(cityChildTwoIndex).getMinDist()) {
        		smallerChildCity = this.minHeap.get(cityChildTwoIndex);
        		smallerChildCityIndex = cityChildTwoIndex;
        	} else if (this.minHeap.get(cityChildOneIndex).getMinDist() == this.minHeap.get(cityChildTwoIndex).getMinDist()) {
        		if (this.minHeap.get(cityChildOneIndex).getName() < this.minHeap.get(cityChildTwoIndex).getName()) {
        			smallerChildCity = this.minHeap.get(cityChildOneIndex);
        			smallerChildCityIndex = cityChildOneIndex;
        		} else {
        			smallerChildCity = this.minHeap.get(cityChildTwoIndex);
        			smallerChildCityIndex = cityChildTwoIndex;
        		}
        	} else {
        		smallerChildCity = this.minHeap.get(cityChildOneIndex);
        		smallerChildCityIndex = cityChildOneIndex;
        	}
    	}
    	
    	if (currCity.getMinDist() > smallerChildCity.getMinDist()) {
    		
    		this.minHeap.add(currCity);
    		this.minHeap.set(cityIndex, smallerChildCity);
    		this.minHeap.set(smallerChildCityIndex, currCity);
    		this.minHeap.remove(this.minHeap.size() - 1);
    		
    		currCity.setIndex(smallerChildCityIndex);
    		smallerChildCity.setIndex(cityIndex);
    		
    		HeapifyDown(smallerChildCityIndex);
    	} else if ((currCity.getMinDist() == smallerChildCity.getMinDist()) && currCity.getName() > smallerChildCity.getName()) {
    		
    		this.minHeap.add(currCity);
    		this.minHeap.set(cityIndex, smallerChildCity);
    		this.minHeap.set(smallerChildCityIndex, currCity);
    		this.minHeap.remove(this.minHeap.size() - 1);
    		
    		currCity.setIndex(smallerChildCityIndex);
    		smallerChildCity.setIndex(cityIndex);
    		
    		HeapifyDown(smallerChildCityIndex);
    	}
    }

    /**
     * buildHeap(ArrayList<City> cities)
     * Given an ArrayList of Cities, build a min-heap keyed on each City's minDist
     * Time Complexity - O(nlog(n)) or O(n)
     *
     * @param cities
     */
    public void buildHeap(ArrayList<City> cities) {
        // TODO: implement this method
    	this.minHeap = new ArrayList<>();
    	for (int i = 0; i < cities.size(); i++) {
    		this.minHeap.add(cities.get(i));
    		cities.get(i).setCityConnect(cities.get(i).getName());
    		cities.get(i).setIndex(i);
    	}
    	
    	int lastElemSecondToLastLayer = (this.minHeap.size() / 2) - 1;
    	
    	for (int i = lastElemSecondToLastLayer; i >= 0; i--) {
    		HeapifyDown(i);
    	}
    }

    /**
     * insertNode(City in)
     * Insert a City into the heap.
     * Time Complexity - O(log(n))
     *
     * @param in - the City to insert.
     */
    public void insertNode(City in) {
        // TODO: implement this method
    	
    	this.minHeap.add(in);
    	in.setIndex(this.minHeap.size() - 1);
    	in.setCityConnect(in.getName());
    	
    	HeapifyUp(this.minHeap.size() - 1);
    }

    /**
     * findMin()
     * Time Complexity - O(1)
     *
     * @return the minimum element of the heap.
     */
    public City findMin() {
        // TODO: implement this method
        
    	return this.minHeap.get(0);
    }

    /**
     * extractMin()
     * Time Complexity - O(log(n))
     *
     * @return the minimum element of the heap, AND removes the element from said heap.
     */
    public City extractMin() {
        // TODO: implement this method
        
    	City min = this.findMin();
    	this.delete(0);
    	return min;
    }

    /**
     * delete(int index)
     * Deletes an element in the min-heap given an index to delete at.
     * Time Complexity - O(log(n))
     *
     * @param index - the index of the item to be deleted in the min-heap.
     */
    public void delete(int index) {
        // TODO: implement this method
    	
    	int minDistCityRemove = this.minHeap.get(index).getMinDist();
    	int minDistLastCity = this.minHeap.get(this.minHeap.size() - 1).getMinDist();

    	City last = this.minHeap.get(this.minHeap.size() - 1);
    	last.setIndex(index);
    	this.minHeap.set(index, last);
    	this.minHeap.remove(this.minHeap.size() - 1);
    	
    	if (minDistLastCity < minDistCityRemove) {
    		HeapifyUp(index);
    	} else {
    		HeapifyDown(index);
    	}
    }
    
    /*
     * is the Heap empty?
     */
    public boolean isEmpty () {
    	return this.minHeap.isEmpty();
    }
    
    /*
     * Getter 
     */
    public ArrayList<City> getHeap() {
    	return this.minHeap;
    }

    /**
     * changeKey(City r, int newDist)
     * Changes minDist of City s to newDist and updates the heap.
     * Time Complexity - O(log(n))
     *
     * @param r       - the City in the heap that needs to be updated.
     * @param newDist - the new cost of City r in the heap (note that the heap is keyed on the values of minDist)
     */
    public void changeKey(City r, int newDist) {
        // TODO: implement this method
    	
    	int oldDist = r.getMinDist();
    	int index = r.getIndex();
    	
    	this.minHeap.get(index).setMinDist(newDist);
    	
    	if (newDist > oldDist) {
    		HeapifyDown(index);
    	} else {
    		HeapifyUp(index);
    	}
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < minHeap.size(); i++) {
            output += minHeap.get(i).getName() + " ";
        }
        return output;
    }

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public ArrayList<City> toArrayList() {
        return minHeap;
    }
}
