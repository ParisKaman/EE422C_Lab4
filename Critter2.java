package assignment4;

//Critter2 will fight half the time and run half the time.
//Each time step it turns 1 to its left and takes a step.
//it reproduces if it has at least 75 energy
//its child walks the opposite direction


public class Critter2 extends Critter{
	@Override
	public String toString(){
		return "2";
	}
	
	
	private int dir;
	private boolean fight = false;
	
	/**
	 * Constructor for Critter2
	 */
	public Critter2(){
		dir = (Critter.getRandomInt(16) % 8);
	}
	
	
	/**
	 * Critter walks and has chance to reproduce
	 */
	@Override
	public void doTimeStep() {
		dir = (dir + 2)%8;
		walk(dir);		
		if(getEnergy() > 75){
			Critter2 child = new Critter2();
			reproduce(child,(dir+4)% 8);
		}
	}

	/**
	 * Critter fights half the time, runs half the time
	 * 
	 * @return true if Critter wants to fight
	 * @return false if Critter does not want to fight
	 */
	@Override
	public boolean fight(String oponent) {
		if(fight){
			fight = false;
			return true;
		}
		else{
			run(dir);
			return false;
		}
			
	}

}
