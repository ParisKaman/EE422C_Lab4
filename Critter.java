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


//Thing's I've added(Aaron)
//String[][] world
//Finished displayWorld
//Added "myPackage." to certain lines

public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static String[][] world = new String[Params.world_height][Params.world_width];
	
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
	
	private int x_coord;
	private int y_coord;
	
	protected void move(int direction, int spaces) {
			switch (direction){
				case 0:	this.x_coord += spaces;
						break;
				case 1: this.x_coord += spaces;
						this.y_coord -= spaces;
						break;
				case 2: this.y_coord -= spaces;
						break;
				case 3: this.x_coord -= spaces;
						this.y_coord -= spaces;
						break;
				case 4: this.x_coord -= spaces;
						break;
				case 5: this.x_coord -= spaces;
						this.y_coord += spaces;
						break;
				case 6: this.y_coord += spaces;
						break;
				case 7: this.x_coord += spaces;
						this.y_coord += spaces;
						break;
			}
		this.x_coord = (this.x_coord + Params.world_width) % Params.world_width;
		this.y_coord = (this.y_coord + Params.world_height) % Params.world_height;
	}
	
	protected final void walk(int direction) {
		if(this.energy >= Params.walk_energy_cost){
			move(direction, 1);
		}

	}
	
	protected final void run(int direction){
		if(this.energy >= Params.run_energy_cost){
			move(direction, 2);
		}
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		Class<? extends Critter> a = this.getClass();
		try {
			Critter child = (Critter) a.newInstance();
			child.energy = this.energy/2;
			child.x_coord = this.x_coord;
			child.y_coord = this.y_coord;
			child.move(direction, 1);
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
//		
//		try{
//			Class<?> c = Class.forName(myPackage + "." + critter_class_name);
//			for(int i = 0; i < population.size(); i++){
//				Critter a = population.get(i);
//				if(a.getClass().isInstance(c)){
//					result.add(population.get(i));
//				}
//			}
//		}
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
	}
	
	public static void worldTimeStep() {
		for(Critter crit: population){
			crit.doTimeStep();
		}
		for(Critter crit: population){
			
		}
	}
	
	public static void displayWorld() {
		//Clear the world
		for(int i = 0; i<Params.world_height;i++){
			for(int j = 0; j<Params.world_width; j++){
				world[i][j] = " ";					
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
				System.out.print(world[i][j]);
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
