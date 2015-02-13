/*
 * Sudoku Solver using backtracking
 * Author: Youssef ElGhareeb
 * This solution was adapted from the algorihtm described by Peter Norvig
 * Reference: http://norvig.com/sudoku.html
 */

package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Solver implements Runnable {
	private int [][] solution;
	private int [][] grid;
	
	private List<Integer>[][] parseGrid(int[][] grid) {
		List <Integer> values [][] = new List [9][9];
		
		//Initialize values
		for (int i=0;i<9;i++) {
			for (int j=0;j<9;j++) {
				values[i][j] = new ArrayList <Integer>();
				if(grid[i][j]==-1) for (int k=1;k<=9;k++) values[i][j].add(k);
				else values[i][j].add(grid[i][j]);
			}
		}
		//Elminate the values
		
		//1. Rows first
		for (int i=0;i<9;i++) {
			//collect removal values
			List <Integer> removals = new ArrayList<Integer> ();
			for (int j=0;j<9;j++) if (values[i][j].size()==1) removals.add((Integer) values[i][j].get(0));
			
			//Remove
			for (int j=0;j<9;j++) {
				if (values[i][j].size()>1) {
					for (int k=0;k<removals.size();k++) {
						values[i][j].remove((Object)removals.get(k));
					}
				}
			}
		}
		
		//2. Columns
		for (int j=0;j<9;j++) {
			//collect removal values
			List <Integer> removals = new ArrayList<Integer> ();
			for (int i=0;i<9;i++) if (values[i][j].size()==1) removals.add((Integer) values[i][j].get(0));
			
			//Remove
			for (int i=0;i<9;i++) {
				if (values[i][j].size()>1) {
					for (int k=0;k<removals.size();k++) {
						values[i][j].remove((Object)removals.get(k));
					}
				}
			}
		}
		
		//3. loop on blocks
		for (int i=0;i<3;i++) {
			for (int j=0;j<3;j++) {
				List <Integer> removals = new ArrayList<Integer> ();
				for (int ii=0;ii<3;ii++) {
					for (int jj=0;jj<3;jj++) {
						if (values[i*3+ii][j*3+jj].size()==1) 
							removals.add(values[i*3+ii][j*3+jj].get(0));
					}
				}
				
				//Remove
				for (int ii=0;ii<3;ii++) {
					for (int jj=0;jj<3;jj++) {
						if (values[i*3+ii][j*3+jj].size()>1) 
							for (int k=0;k<removals.size();k++) values[i*3+ii][j*3+jj].remove((Object)removals.get(k));
					}
				}
			}
		}
		
		//Debug
		Integer testSquares[] = {0,1,5,4,8,0,7,5};
		for (int tt=0;tt<testSquares.length;tt+=2) {
			System.out.print ("Square [" +testSquares[tt]+","+testSquares[tt+1]+"]: ");
			for (int k=0;k<values[testSquares[tt]][testSquares[tt+1]].size(); k++) {
				System.out.print (values[testSquares[tt]][testSquares[tt+1]].get(k));
			}
			System.out.println();
		}
		
		return values;
	}
	
	private boolean eliminate (List<Integer>[][] values, int i, int j, int val) {
		if (!values[i][j].contains(val)) {
			return true; //Already eliminated
		}
		
		values[i][j].remove((Object)val);
		if (values[i][j].isEmpty()) {
			return false; //Contradiction
		}
		
		else if (values[i][j].size()==1) {
			int d2 = values[i][j].get(0);
			//Loop on all peers of (i,j) & eliminate d2
			for (int ii=0;ii<9;ii++) if(i!=ii) if (eliminate (values,ii,j,d2)==false) return false;
			for (int jj=0;jj<9;jj++) if(j!=jj) if (eliminate (values,i,jj,d2)==false) return false;
			for (int ii=0;ii<3;ii++) {
				for (int jj=0;jj<3;jj++) {
					if (3*(i/3)+ii == i && 3*(j/3)+jj==j) continue;
					if (eliminate (values, 3*(i/3)+ii, 3*(j/3)+jj, d2)==false) return false;
				}
			}
		}	
		return true;
	}
	
	private boolean assign (List<Integer>[][] values, int i, int j, int val) {
		//Elminate this value from neighbors
		List<Integer> others = new ArrayList <Integer> ();
		for (Integer n: values[i][j]) {
			if (n==val) continue;
			else others.add (n);
		}
		
		for (Integer n: others) {
			if (eliminate (values, i,j,n)==false) return false;
		}
		return true;
	}
	
	private int [][] search (List<Integer>[][] values) {
		//Check for termination condition
		boolean isEnd = true;
		for (int i=0;i<9 && isEnd;i++) {
			for (int j=0;j<9;j++) {
				if (values[i][j].size()>1) {
					isEnd = false;
					break;
				}
			}
		}
		
		if (isEnd==true) {
			//Return the solution
			int [][] ret = new int[9][9];
			for (int i=0;i<9;i++) {
				for (int j=0;j<9;j++) {
					ret[i][j] = values[i][j].get(0);
				}
			}
			return ret;
		}
		
		//Get the square with the minimum remaining possibilties
		int minRem = 10, mini=-1, minj=-1;
		for (int i=0;i<9;i++) {
			for (int j=0;j<9;j++) {
				if (values[i][j].size()<minRem && values[i][j].size()>1) {
					minRem = values[i][j].size();
					mini = i;	minj=j;
				}
			}
		}
		
		//Try all possible assignments for the minimum square and recurse
		assert (mini!=-1 && minj!=-1);
		for (int k=0; k<values[mini][minj].size();k++) {
			//1. Clone 
			List<Integer>[][] newVals = new List [9][9];
			for (int i=0;i<9;i++) {
				for (int j=0;j<9;j++) {
					newVals[i][j] = new ArrayList<Integer>();
					for (int n: values[i][j]) newVals[i][j].add(n);
				}
			}
			//2. Assign
			if (assign (newVals, mini, minj, values[mini][minj].get(k))==false) continue;
			int [][] sol = search (newVals);
			if (sol!=null) return sol;
		}
		return null;
	}
	
	public void solve () {
		solution = search (parseGrid (grid));
	}

	public void setGrid (int[][] grid ) {
		this.grid = grid;
	}
	
	public int[][] getSolution () {
		return solution;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		solve ();
	}
}
