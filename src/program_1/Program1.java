package program_1;
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
 * grading your solution. However, do not add extra import statements to this file.
 */
public class Program1 extends AbstractProgram1 {

    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching problem) {
        /* TODO implement this function */
    	
    	ArrayList <Integer> currMatching = problem.getInternMatching();
    	
    	/*
    	 * checks for the instabilities outlined in the program doc
    	 */
    	boolean checkInstabilityOne = false;
    	boolean checkInstabilityTwo = false;
    	
    	/*
    	 * Instability 1
    	 */
    	for (int intern_ind = 0; intern_ind < currMatching.size(); intern_ind++) {
    		/*
    		 * loop through all the interns in the matching
    		 * check for intern i' that is not matched to any company
    		 */
    		if (currMatching.get(intern_ind) == -1) {
    			/*
    			 * intern i' is intern #intern_ind
    			 */
    			for (int index = 0; index < currMatching.size(); index++) {
    				/*
    				 * loop through rest of interns who ARE matched to a company
    				 */
    				if (currMatching.get(index) != -1) {
    					ArrayList <Integer> currCompanyPref = problem.getCompanyPreference().get(currMatching.get(index));
    					
    					int index_of_matchedIntern = currCompanyPref.indexOf(index);
    					int index_of_unmatchedIntern = currCompanyPref.indexOf(intern_ind);
    					
    					if (index_of_matchedIntern > index_of_unmatchedIntern) {
    						/*
    						 * curr company prefers unmatched intern over current intern --> unstable match
    						 */
    						checkInstabilityOne = true;
    					}
    					
    					if (checkInstabilityOne) {break;}
    				}
    			}
    			
    			if (checkInstabilityOne) {break;}
    		}
    	}
    	
    	/*
    	 * Instability 2
    	 */
    	for (int i = 0; i < currMatching.size(); i++) {
    		int intern_i = i;
    		
    		if (currMatching.get(intern_i) != -1) {
    			
    			for (int j = 0; j < currMatching.size(); j++) {
    				int intern_j = j;
    				
    				if (intern_i != intern_j && currMatching.get(intern_j) != -1 && currMatching.get(intern_i) != currMatching.get(intern_j)) {
    					// have ensured that intern_i and intern_j are different interns, both assigned to different companies
    					int companyOfI = currMatching.get(intern_i);
    					int companyOfJ = currMatching.get(intern_j);
    					
    					int rankInternI = problem.getCompanyPreference().get(companyOfI).indexOf(intern_i);
    					int rankInternJ = problem.getCompanyPreference().get(companyOfI).indexOf(intern_j);
    					
    					int rankCompanyOfI = problem.getInternPreference().get(intern_j).indexOf(companyOfI);
    					int rankCompanyOfJ = problem.getInternPreference().get(intern_j).indexOf(companyOfJ);
    					
    					if (rankInternJ < rankInternI && rankCompanyOfI < rankCompanyOfJ) {
    						// if i's company prefers j over i AND if j prefers i's company over its company
    						checkInstabilityTwo = true;
    					}
    					
    					if (checkInstabilityTwo) { break; }
    					
    					
    				}
    			}
    			
    			if (checkInstabilityTwo) { break; }
    			
    		}
    	}
    	
    	if (checkInstabilityOne || checkInstabilityTwo) {
    		return false;
    	} else {
    		return true;
    	}
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_companyoptimal(Matching problem) {
        /* TODO implement this function */
    	
    	/*
    	 * generate a 2d matrix, inverse_pref, that will represent company prefs in a more efficient way
    	 * this provides constant lookup times for determining if an intern prefers one company over another
    	 */
    	int [][] inverse_prefs = new int[problem.getInternCount()][problem.getCompanyCount()];
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		for (int c = 0; c < problem.getCompanyCount(); c++) {
    			int getPref = problem.getInternPreference().get(i).indexOf(c);
    			inverse_prefs[i][c] = getPref;
    		}
    	}
    	
    	/*
    	 * 2d matrix that keeps track of if a company has proposed to all the prospective interns
    	 */
    	boolean [][] hasProposed = new boolean[problem.getCompanyCount()][problem.getInternCount()];
    	for (int c = 0; c < problem.getCompanyCount(); c++) {
    		for (int i = 0; i < problem.getInternCount(); i++) {
    			hasProposed[c][i] = false;
    		}
    	}
    	
    	/*
    	 * keep track of the number of job openings for each company
    	 */
    	ArrayList <Integer> numOpenings = problem.getCompanyPositions();
    	
    	/*
    	 * Queue to keep track of companies
    	 */
    	LinkedList <Integer> companies = new LinkedList<>();
    	for (int c = 0; c < problem.getCompanyCount(); c++) {
    		companies.add(c);
    	}
    	
    	/*
    	 * keeps track of if an intern is employed at the moment
    	 */
    	boolean [] isEmployed = new boolean[problem.getInternCount()];
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		isEmployed[i] = false;
    	}
    	
    	/*
    	 * finalMatching will hold the final intern matching that will be passed to setInternMatching at the end of the algorithm
    	 */
    	ArrayList <Integer> finalMatching = new ArrayList<>();
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		finalMatching.add(-1);
    	}
    	
    	/*
    	 * Preprocessing above
    	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    	 * While loop/Algorithm below
    	 */
    	
    	while(!companies.isEmpty()) {
    		
    		// obtain the current company
    		int curr_company = companies.peek();
    		int curr_company_positions = numOpenings.get(curr_company);
    		
    		if (curr_company_positions > 0) {
    			
    			for (int i = 0; i < problem.getCompanyPreference().get(curr_company).size(); i++) {
        			
        			int curr_intern = problem.getCompanyPreference().get(curr_company).get(i);
        			
        			if (!hasProposed[curr_company][curr_intern]) {
            			
            			hasProposed[curr_company][curr_intern] = true;
            			
            			/*
            			 * check if curr_intern is currently employed with another company
            			 */
            			boolean internEmployed = isEmployed[curr_intern];
            			
            			if (internEmployed) {
            				
            				// check to see whether the intern prefers current matching with curr_company
            				int intern_curr_comp = finalMatching.get(curr_intern);
            				
            				int rankOfInternCurrComp = inverse_prefs[curr_intern][intern_curr_comp];
            				int rankOfCurrComp = inverse_prefs[curr_intern][curr_company];
            				
            				if (rankOfInternCurrComp < rankOfCurrComp) {
            					// prefers current matching over proposal from curr_company, nothing to do
            				}
            				
            				/*
            				 * prefers proposal from curr_company over current matching
            				 */
            				else {
            					// add this company to the queue so it can get a new matching
            					companies.add(intern_curr_comp); 
            					
            					// increase the number of positions left for intern_curr_comp
            					numOpenings.set(intern_curr_comp, numOpenings.get(intern_curr_comp) + 1);
            					
            					// set the new matching btwn curr_company & curr_intern
            					finalMatching.set(curr_intern, curr_company);
            					
            					// reduce the number of positions still available at curr_company
            					numOpenings.set(curr_company, curr_company_positions - 1);
            					
            					break;
            				}
            			}
            			
            			/*
            			 * the current intern is not yet matched with a company 
            			 */
            			else {
            				// can match current company to current intern
            				numOpenings.set(curr_company, curr_company_positions - 1);
            				isEmployed[curr_intern] = true;
            				finalMatching.set(curr_intern, curr_company);
            				
            				break;
            			}
        			}
        		}
    			
    		}
    		
    		if (numOpenings.get(curr_company) <= 0) {
    			companies.remove();
    		}
    		
    	}
    	
    	problem.setInternMatching(finalMatching);
    	
        return problem;
    	
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_internoptimal(Matching problem) {
        /* TODO implement this function */
    	
    	/*
    	 * generate 2D matrix that makes comparing a company's preferred interns easier and more efficient
    	 */
    	int [][] inverse_prefs = new int[problem.getCompanyCount()][problem.getInternCount()];
    	for (int c = 0; c < problem.getCompanyCount(); c++) {
    		for (int i = 0; i < problem.getInternCount(); i++) {
    			int getPref = problem.getCompanyPreference().get(c).indexOf(i);
    			inverse_prefs[c][i] = getPref;
    		}
    	}
    	
    	/*
    	 * generate 2D ArrayList that keep track of whether an intern has proposed to a company
    	 */
    	ArrayList <ArrayList<Boolean>> hasProposed = new ArrayList<>();
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		hasProposed.add(new ArrayList<>());
    		for (int c = 0; c < problem.getCompanyCount(); c++) {
    			hasProposed.get(i).add(false);
    		}
    	}
    	
    	/*
    	 * Queue that will hold all interns
    	 */
    	LinkedList <Integer> interns = new LinkedList<>();
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		interns.add(i);
    	}
    	
    	/*
    	 * a 2D ArrayList of ArrayLists that will hold all the interns currently working at a company
    	 */
    	ArrayList <ArrayList<Integer>> interns_at_company = new ArrayList<>();
    	for (int c = 0; c < problem.getCompanyCount(); c++) {
    		interns_at_company.add(new ArrayList<>());
    	}
    	
    	/*
    	 * boolean array that specifies whether an intern is employed at a company or not
    	 */
    	boolean [] isEmployed = new boolean[problem.getInternCount()];
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		isEmployed[i] = false;
    	}
    	
    	/*
    	 * keep track of the number of job openings with an ArrayList that holds the number of available jobs per company
    	 */
    	ArrayList <Integer> numOpenings = problem.getCompanyPositions();
    	
    	/*
    	 * finalMatching will hold the final intern matching that will be passed to setInternMatching at the end of the algorithm
    	 */
    	ArrayList <Integer> finalMatching = new ArrayList<>();
    	for (int i = 0; i < problem.getInternCount(); i++) {
    		finalMatching.add(-1);
    	}
    	
    	
    	/*
    	 * Preprocessing above
    	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    	 * While loop/Algorithm below
    	 */
    	
    	
    	/*
    	 * loop through the intern queue while there are still interns to check
    	 */
    	while (!interns.isEmpty()) {
    		
    		int curr_intern = interns.peek();
    		boolean foundMatch = false;
    		
    		/*
    		 * loop through the popped intern's preference list
    		 */
    		for (int c = 0; c < problem.getInternPreference().get(curr_intern).size(); c++) {
    			
    			int curr_company = problem.getInternPreference().get(curr_intern).get(c);
    			int curr_company_positions = numOpenings.get(curr_company);
    			
    			/*
    			 * ensure that the intern has not proposed to the company in some past iteration
    			 */
    			if (!hasProposed.get(curr_intern).get(curr_company)) {
    				
    				hasProposed.get(curr_intern).set(curr_company, true);
    				
    				if (!isEmployed[curr_intern]) {
    					
    					/*
        				 * check to see if the current company has any open positions
        				 * if it does, then we can match up the intern to the company
        				 */
        				if (curr_company_positions > 0) {
        					// indicate there is one less job opening for there is now an intern working for curr_company
        					numOpenings.set(curr_company, curr_company_positions - 1);
        					
        					// set the matching btwn curr_intern and curr_company 
        					finalMatching.set(curr_intern, curr_company);
        					
        					// mark that the intern is now employed
        					isEmployed[curr_intern] = true;
        					
        					// add curr_intern to log of all interns curr_company currently has
        					interns_at_company.get(curr_company).add(curr_intern);

        					foundMatch = true;
        					
        					break;
        				} 
        				
        				/*
        				 * the current company has no open positions
        				 */
        				else {
        					
        					/*
        					 * check to see what the least preferred intern at the current company is & its rank
        					 */
        					int minRankCurrInterningAtCompany = Integer.MIN_VALUE;
        					int internThatIsLeastPrefByCompany = -1;
        					int indexOfLeastPrefIntern = -1;
        					
        					for (int i = 0; i < interns_at_company.get(curr_company).size(); i++) {
        						if (inverse_prefs[curr_company][interns_at_company.get(curr_company).get(i)] > minRankCurrInterningAtCompany) {
        							
        							minRankCurrInterningAtCompany = inverse_prefs[curr_company][interns_at_company.get(curr_company).get(i)];
        							internThatIsLeastPrefByCompany = interns_at_company.get(curr_company).get(i);
        							indexOfLeastPrefIntern = i;
        						}
        					}
        					
        					int rankCurrIntern = inverse_prefs[curr_company][curr_intern];
        					
        					if (minRankCurrInterningAtCompany < rankCurrIntern) {
        						// company prefers the lowest preferred intern currently interning there over curr_intern
        					}
        					
        					/*
        					 * company prefers curr_intern over one of its current interns
        					 */
        					else {
        						// remove the unpreferred intern from the current list of interns at curr_company
        						interns_at_company.get(curr_company).remove(interns_at_company.get(curr_company).get(indexOfLeastPrefIntern));
        						
        						// add preferred intern to curr_company's list of interns
        						interns_at_company.get(curr_company).add(curr_intern);
        						
        						// re-add the unpreferred intern back into the queue to potentially find another company
        						interns.add(internThatIsLeastPrefByCompany);
        						
        						// show that the unpreferred intern is no longer matched to any company
        						finalMatching.set(internThatIsLeastPrefByCompany, -1);
        						
        						// show that curr_intern now interns at curr_company
        						finalMatching.set(curr_intern, curr_company);
        						
        						// mark the intern is now employed
        						isEmployed[curr_intern] = true;
        						
        						// mark unpreferred intern as unemployed
        						isEmployed[internThatIsLeastPrefByCompany] = false;
        						
        						foundMatch = true;
        						break;
        					}
        					
        				}
    					
    				}
    			}
    			
    		}
    		
    		if (foundMatch) {
    			// curr_intern found a company to match with
    			interns.remove();
    		}
    		
    		else if (!hasProposed.get(curr_intern).contains(false)) {
    			// curr_intern went through every company and was rejected by all of them
    			interns.remove();
    		}
    		
    	}
    	
    	problem.setInternMatching(finalMatching);
    	
        return problem;
    }
}