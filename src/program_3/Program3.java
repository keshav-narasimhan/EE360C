package program_3;
/*
 * Name: Keshav Narasimhan
 * EID: kn9558
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program3 extends AbstractProgram3 {

	float [][] Table_Response;		// table used for dynamic programming algorithm
    /**
     * Determines the solution of the optimal response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "response" field set to the optimal response time
     */
    @Override
    public TownPlan OptimalResponse(TownPlan town) {
    	// initialize DP Table for storing responses
        Table_Response = new float[town.getHouseCount()][town.getStationCount()];
        
        int n = town.getHouseCount();
        int k = town.getStationCount();
        
        /*
         * Base Cases:
         * 
         * One: 1 house will always have a response time of 0 since we can place the station at the same location of the house
         * Two: if the number of food stations equals or is greater than the number of houses, the response time will also equal 0
         */
        for (int i = 0; i < k; i++) {
        	// Base Case One
        	Table_Response[0][i] = (float)0;
        	
        	// Base Case Two
        	for (int j = 0; j <= i; j++) {
        		Table_Response[j][i] = (float)0;
        	}
        }
        
        // one station --> must place at midpoint of two endpoint houses 
        for (int i = 1; i < n; i++) {
        	float start = town.getPositionHouses().get(0);
        	float end = town.getPositionHouses().get(i);
        	
        	Table_Response[i][0] = ((start + end) / (float)2.0) - start;
        }
        
        // dynamic programming main algorithm using OPT recurrence
        for (int i = 1; i < n; i++) {
        	for (int j = 1; j < k; j++) {
        		float min = Float.MAX_VALUE;
        		for (int z = 0; z < i; z++) {
        			float second_param = /*town.getPositionHouses().get(i) - */((town.getPositionHouses().get(z + 1) + town.getPositionHouses().get(i)) / (float)2.0) - town.getPositionHouses().get(z + 1);
        			float check = Math.max(Table_Response[z][j - 1], second_param);
        			
        			if (check < min) {
        				min = check;
        			}
        		}
        		
        		Table_Response[i][j] = min;
        	}
        }
    	
        town.setResponse(Table_Response[n - 1][k - 1]);
    	return town;
    }

    /**
     * Determines the solution of the set of food station positions that optimize response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "position_food_stations" field set to the optimal food station positions
     */
    @Override
    public TownPlan OptimalPosFoodStations(TownPlan town) {
    	/*
    	 * repeat code from OptimalResponse()
    	 */
    	// initialize DP Table for storing responses
        Table_Response = new float[town.getHouseCount()][town.getStationCount()];
        
        int n = town.getHouseCount();
        int k = town.getStationCount();
        
        /*
         * Base Cases:
         * 
         * One: 1 house will always have a response time of 0 since we can place the station at the same location of the house
         * Two: if the number of food stations equals or is greater than the number of houses, the response time will also equal 0
         */
        for (int i = 0; i < k; i++) {
        	// Base Case One
        	Table_Response[0][i] = (float)0;
        	
        	// Base Case Two
        	for (int j = 0; j <= i; j++) {
        		Table_Response[j][i] = (float)0;
        	}
        }
        
        // one station --> must place at midpoint of two endpoint houses 
        for (int i = 1; i < n; i++) {
        	float start = town.getPositionHouses().get(0);
        	float end = town.getPositionHouses().get(i);
        	
        	Table_Response[i][0] = ((start + end) / (float)2.0) - start;
        }
        
        // dynamic programming main algorithm using OPT recurrence
        for (int i = 1; i < n; i++) {
        	for (int j = 1; j < k; j++) {
        		float min = Float.MAX_VALUE;
        		for (int z = 0; z < i; z++) {
        			float second_param = /*town.getPositionHouses().get(i) - */((town.getPositionHouses().get(z + 1) + town.getPositionHouses().get(i)) / (float)2.0) - town.getPositionHouses().get(z + 1);
        			float check = Math.max(Table_Response[z][j - 1], second_param);
        			
        			if (check < min) {
        				min = check;
        			}
        		}
        		
        		Table_Response[i][j] = min;
        	}
        }
        
        // list for optimal food station locations
        ArrayList<Float> position_food_stations = new ArrayList<>();
        n = town.getHouseCount() - 1;
        k = town.getStationCount() - 1;   
        
        // find the optimal food station locations by backtracking the Table_Response matrix
        findPositions(town, n, k, position_food_stations/*, n*/);
        
    	town.setPositionFoodStations(position_food_stations);
        return town;
    }
    
    // recursive method that backtracks the response time matrix to get the optimal food station locations
    private void findPositions(TownPlan town, int t, int j, ArrayList<Float> position_food_stations/*, int pos*/) {
    	if (t < 0 && j < 0) {
    		return;
    	} else if (j < 1) {
    		float val = (town.getPositionHouses().get(0) + town.getPositionHouses().get(t)) / (float)2;
    		position_food_stations.add(0, val);
    		return;
    	}
    	
    	float min = Float.MAX_VALUE;
    	int new_t = t;
    	for (int z = 0; z < t; z++) {
			float second_param = /*town.getPositionHouses().get(t) - */((town.getPositionHouses().get(z + 1) + town.getPositionHouses().get(t)) / (float)2.0) - town.getPositionHouses().get(z + 1);
			float check = Math.max(Table_Response[z][j - 1], second_param);
			
			if (check < min) {
				new_t = z;
				min = check;
			}
		}
    	
    	float val = (town.getPositionHouses().get(new_t + 1) + town.getPositionHouses().get(t)) / (float)2;
    	position_food_stations.add(0, val);
		findPositions(town, new_t, j - 1, position_food_stations/*, new_t*/);
    }
}
