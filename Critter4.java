/*
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
	
	@Override
	public boolean fight(String not_used) { return true; }

	@Override
	public void doTimeStep() {
		if(getEnergy() > 200){
			Critter4 child = new Critter4();
			reproduce(child, dir);
			dir = (dir + 4) % 8;
		}	
	}

}
