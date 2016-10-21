/*	CRITTERS Critter.java
 *	EE422C Project 4 submission by
 *	Aaron Thomas North
 *	atm2324
 *	16465
 *	Paris Kaman
 *	pak679
 *	16445
 *	Slip days used: <0>
 *	Fall 2016
 * 	Critter3 is a critter that does not reproduce
 * 	Critter3 has the following characteristics:
 * 		-never reproduces
 * 		-under 200 energy it will walk
 * 		-over 200 energy it will run
 * 		-chooses a new random direction each time step 
 * 
 */



package assignment4;

public class Critter3 extends Critter{
	
	private int dir;
	
	public Critter3(){
		dir = getRandomInt(8);
	}
	
	@Override
	public String toString(){return "3";}
	
	/**	always returns that Critter3 wants to fight 
	 * 	when on the same space as another Critter
	 * 	@param String not_used -- not used for Critter3
	 * 	@return true
	 */ 
	@Override
	public boolean fight(String not_used) { return true; }

	/**	Critter3 will walk when under 200 energy 
	 * 	and run when 200 or over 200
	 */
	@Override
	public void doTimeStep() {
		if(getEnergy() < 200){
			walk(dir);
		}else{
			run(dir);
		}
		
		dir = getRandomInt(8);
	}

}
