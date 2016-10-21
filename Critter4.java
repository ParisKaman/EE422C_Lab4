/*  CRITTERS Critter.java
 *  EE422C Project 4 submission by
 *  Aaron Thomas North
 *  atm2324
 *  16465
 *  Paris Kaman
 *  pak679
 *  16445
 *  Slip days used: <0>
 *  Fall 2016
 *  Critter4 is an immobile fighting critter 
 *  Critter4 has the following characteristics:
 * 		-does not move
 * 		-always fights
 * 		-reproduces at 200 energy
 * 			--when it reproduces, it turns around, so it is facing "back" to "back" with its child
 * 
 */


package assignment4;

public class Critter4 extends Critter{
	
	private int dir;
	
	public Critter4(){
		dir = getRandomInt(8);
	}
	
	@Override
	public String toString(){return "4";}
	
	/** always returns that Critter4 wants to fight 
	 * 	when on the same space as another Critter
	 * 	@param String not_used -- not used for Critter4
	 * 	@return true
	 */
	@Override
	public boolean fight(String not_used) { return true; }

	/**Critter4 will reproduce if it has more than 200 energy
	 * 	he will never move but will turn away from child after reproducing
	 */
	@Override
	public void doTimeStep() {
		if(getEnergy() > 200){
			Critter4 child = new Critter4();
			reproduce(child, dir);
			dir = (dir + 4) % 8;
		}	
	}

}
