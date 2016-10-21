/*
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
	
	@Override
	public boolean fight(String not_used) { return true; }

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
