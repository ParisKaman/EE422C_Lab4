/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */



public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static String[][] world = new String[Params.world_width][Params.world_height];
	private static boolean fight = false; //How to know if critters are moving or fleeing
	
	
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	public boolean hasMoved; 
	private int x_coord;
	private int y_coord;
	private int fightRoll;
	private boolean wantsFight;
	public boolean isBaby = false;
	public boolean isDead = false;
	
	
	/**Helper method for walk or run. Given a direction and a number of spaces, it will move a critter to a new spot
	 * 
	 * @param direction is the direction the critter will move
	 * @param spaces is the number of spaces the critter will move
	 */
	private void move(int direction, int spaces) {
		
		//check to see if the Critter has already moved
		if(this.hasMoved){
			return;
		}
		
		//Temp x,y coordinates
		int x = this.x_coord;
		int y = this.y_coord;
				
			switch (direction){
				case 0:	x += spaces;
						break;
				case 1: x += spaces;
						y -= spaces;
						break;
				case 2: y -= spaces;
						break;
				case 3: x -= spaces;
						y -= spaces;
						break;
				case 4: x -= spaces;
						break;
				case 5: x -= spaces;
						y += spaces;
						break;
				case 6: y += spaces;
						break;
				case 7: x += spaces;
						y += spaces;
						break;
			}
		
		//wrap the world
		x = (x + Params.world_width) % Params.world_width;
		y = (y + Params.world_height) % Params.world_height;
		
		//check that it is able to move if fighting
		if(fight && !isBaby){
			for(Critter crit: population){
				if(!crit.equals(this))
					if((crit.x_coord == x) && (crit.y_coord == y))
						return;
			}
		}
		
		//update coordinates if move was successful
		this.x_coord = x;
		this.y_coord = y;
		this.hasMoved = true;
	}
	
	/**Given a direction, moves a critter one space in that direction
	 * 
	 * @param direction is the direction the critter should walk in
	 */
	protected final void walk(int direction) {
		//move the critter
		//it may kill itself by trying
		move(direction, 1);
		this.energy -= Params.walk_energy_cost;
	}
	
	
	/**Given a direction, moves a critter two spaces in that direction
	 * 
	 * @param direction is the direction the critter should run in
	 */
	protected final void run(int direction){
		//move the critter
		//it may kill itself by trying
		move(direction, 2);
		this.energy -= Params.run_energy_cost;
		
	}
	
	
	/**Creates a new critter the same type as offspring and gives it a new direction
	 * 
	 * @param offspring is the type of critter to be "reproduced"
	 * @param direction is the direction the offspring will move in
	 */
	protected final void reproduce(Critter offspring, int direction) {
		
		//check if it is able to reproduce
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		
		//if so reproduce. catch exceptions thrown
		Class<? extends Critter> a = this.getClass();
		try {
			Critter child = (Critter) a.newInstance();
			child.energy = this.energy/2;
			this.energy = (this.energy + 1) / 2;
			child.x_coord = this.x_coord;
			child.y_coord = this.y_coord;
			child.hasMoved = false;
			child.move(direction, 1);
			child.isBaby = true;
			babies.add(child);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try{
			Class<?> c = Class.forName(myPackage + "." + critter_class_name);
			Critter crit = (Critter)c.newInstance();
			crit.x_coord = getRandomInt(Params.world_width);
			crit.y_coord = getRandomInt(Params.world_height);
			crit.energy = Params.start_energy;
			population.add(crit);
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException ex){
			throw new InvalidCritterException(critter_class_name);
		}			
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try{
			Class<?> c = Class.forName(myPackage + "." + critter_class_name);
			for(int i = 0; i < population.size(); i++){
				if(population.get(i).getClass() == c){
					result.add(population.get(i));
				}
			}
		}
		catch(ClassNotFoundException ex){
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	/**
	 * Clear the world of all dead critters
	 */
	private static void clearDead(){
		int i = 0;
		while(i<population.size()){
			if(population.get(i).isDead)
				population.remove(i);
			else i++;
		}
	}
	
	/**
	 * moves the world a single timestep
	 * Critters do their timestep, then encounters, then add babies to world, then clear dead
	 */
	public static void worldTimeStep() {
		
		//All critters do timestep
		for(Critter crit: population){
			crit.doTimeStep();
			if(crit.energy <= 0)
				crit.isDead = true;
		}
				
		//check encounters
		fight = true;
		for(Critter crit: population){
			for(int i = population.indexOf(crit) + 1; i < population.size(); i++){
				Critter other = population.get(i);
				
				//if two critters are in the same spot, both are not dead, run an encounter
				if(other != null && other.x_coord == crit.x_coord 
						&& other.y_coord == crit.y_coord && !other.isDead){
					
					//see if crit wants to fight. (May try to run)
					crit.wantsFight = crit.fight(other.toString());
					if(crit.energy <= 0)		//check that crit is alive
						crit.isDead = true;
					
					//see if other wants to fight. (May try to run)
					other.wantsFight = other.fight(crit.toString());
					if(other.energy <=0)		//check that other is alive
						other.isDead = true;
					
					//check that crits are still in same position
					//if not, don't fight
					if((other.x_coord == crit.x_coord 
							&& other.y_coord == crit.y_coord) && !crit.isDead && !other.isDead){
						
						
						//get rolls for each critter
						if(crit.wantsFight){
							crit.fightRoll = getRandomInt(crit.energy);
						}else{
							crit.fightRoll = 0;
						}
						if(other.wantsFight){
							other.fightRoll = getRandomInt(other.energy);
						}else{
							other.fightRoll = 0;
						}
						
						//if crit rolls higher or equal to other, he kills other and gets half his energy
						// other only wins if he rolls higher than crit
						if(crit.fightRoll >= other.fightRoll){
							crit.energy += (other.energy/2);
							other.energy = 0;
						}else{
							other.energy += (crit.energy/2);
							crit.energy = 0;
						}
						
						//check if either is dead
						if(crit.energy <= 0)
							crit.isDead = true;
						if(other.energy <= 0)
							other.isDead = true;
					}
				}	
			}
		}
		
		//fights are over
		fight = false;
		
		//add babies and empty baby array
		for(Critter crit: babies){
			crit.isBaby = false;
			population.add(crit);
		}
		babies.clear();
		
		//get rid of rest energy cost
		for(Critter crit: population){
			crit.energy -= Params.rest_energy_cost;
			crit.hasMoved = false;
			if(crit.energy <= 0){        						//throws concurrentmodificationexception
				crit.isDead = true;
			}
		}
		
		//clear all the dead
		clearDead();
		
		for(int i = 0; i< Params.refresh_algae_count; i++){
			try	{
				Critter.makeCritter("Algae");
			} catch(InvalidCritterException e){
				System.out.println("error processing: Algae" );  				//Check this line
			}
		}
	}
	
	
	/**
	 * Prints an ascii representation of the world to the console
	 */
	public static void displayWorld() {
		//Clear the world
		for(int i = 0; i<Params.world_height;i++){
			for(int j = 0; j<Params.world_width; j++){
				world[j][i] = " ";					
			}
		}
		
		//populate the world
		for (Critter critter: population){
			world[critter.x_coord][critter.y_coord] = critter.toString();
		}
		
		//Print top border
		System.out.print("+");
		for(int i = 0; i<Params.world_width; i++){	
			System.out.print("-");
		}
		System.out.println("+");
		
		//Print world
		for(int i = 0; i<Params.world_height; i++){	
			System.out.print("|");
			for(int j = 0; j < Params.world_width; j++){
				System.out.print(world[j][i]);
				}
			System.out.println("|");
		}
		
		//Print bottom border
		System.out.print("+");					
		for(int i = 0; i<Params.world_width; i++){	
			System.out.print("-");
		}
		System.out.println("+");
	}
}
