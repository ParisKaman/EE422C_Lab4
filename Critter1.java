package assignment4;

//Critter1 can only run in a straight line. Every fifth time it runs, it rotates one to its left 
//It reproduces if it has energy above 50. Its child runs one direction clockwise.
//Half the time the critter fights. Half the time it runs

public class Critter1 extends Critter {

	@Override
	public String toString(){
		return "1";
	}
	
	private int rundir = 0;
	private int dir;
	private boolean fight = true;
	
	/**
	 * Constructor for Critter1
	 */
	public Critter1(){
		dir = (Critter.getRandomInt(42) % 8);
	}
	
	
		
	/**
	 * Critter runs and has chance to reproduce
	 */
	@Override
	public void doTimeStep() {
		run(dir);
		rundir++;
		if(getEnergy() > 50){
			Critter1 child = new Critter1();
			reproduce(child,(dir+1)% 8);
		}
		if(rundir%5 == 0){
			dir = (dir+1)%8;
		}
	}

	
	/**
	 * @returns true if Critter wants to fight
	 * @returns false if Critter does not want to fight
	 */
	@Override
	public boolean fight(String oponent) {
		if(fight){
			fight = false;
			return true;
		}
		else{
			fight = true;
			return false;
		}
			
	}

}
