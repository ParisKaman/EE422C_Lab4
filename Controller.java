package assignment4;
import java.lang.reflect.Method;
import java.util.*;

public class Controller {
	private static String myPackage;	
	private static String[] commands = {"quit","show","step","seed","stats","make"};
	
	/**Given an input string, returns an array of strings.
	 * The first index will be a command
	 * The second two indices will be any parameters to be used by the command
	 * 
	 * @param input, the raw input by the user
	 * @return result, the raw input parsed into an array of tokens
	 */
	public static String[] parse(String input){
		int index = 0;						
		String[] result = new String[4];	//array to return
		
		//Parse through input string. 
		Scanner parse = new Scanner(input);
		while(index < 4 && parse.hasNext()){	
			result[index] = parse.next();
			index++;
		}
		
		return result;
	}
	
	/**
	 * Checks to see if the input token is a valid command in this implementation
	 * of Critter
	 * 
	 * @param input, an array of tokens input by the user
	 * @return true if input[0] is a legal command
	 * @return false if input[0] is not a legal command
	 */
	public static boolean isLegal(String[] input){
		
		//see if token is a valid command		
		for(String command: commands){
			if(command.equals(input[0])){
				return true;
			}
		}
		return false;
	}		

	
	/**
	 * executes based on the command located in command[0] and the argument tokens
	 * in command[1] and command[2]
	 * 
	 * @param command, an array of tokens input by the user
	 * @param input, the raw input by the user
	 * @return false if the command 'quit' has been used
	 * @return true if the command 'quit' has not been used and execution should
	 * continue
	 */
	public static boolean execute(String[] command, String input){
		
		//If invalid command output error and start again		
		if(command[0].equals("false")){
			System.out.println("invalid command: " + input);
			return true;
		}
		
		//If command has extra junk after args
		if(command[3] != null){
			System.out.println("error processing: " + input);
			return true;
		}
				
		//If quit command, tell main to shut down
		if(command[0].equals("quit")){
			if (command[1] != null){	//check for extra args
				System.out.println("error processing: " + input);
				return true;
			}
			return false;
		}
		
		//If show command
		if(command[0].equals("show")){
			if (command[1] != null){	//check for extra args
				System.out.println("error processing: " + input);
				return true;
			}
			Critter.displayWorld();
			return true;
		}
		
		
		//If step command
		if(command[0].equals("step")){
			int value = 0;
			if(command[2] != null){		//check for extra args
				System.out.println("error processing: " + input);
				return true;
			}
			
			//if no optional int arg
			if(command[1] == null)
				value = 1;
			
			//else convert string to int
			else try{
				Integer temp = Integer.valueOf(command[1]);
				value = temp.intValue();				
			} catch(Exception NumberFormatException){
				System.out.println("error processing: " + input);
				return true;
			}
			
			//run timestep n times
			for(int i = 0; i < value; i++){
				Critter.worldTimeStep();
			}
			return true;
		}
		
		//If seed command
		if(command[0].equals("seed")){
			if(command[2] != null){		//check for extra args
				System.out.println("error processing: " + input);
				return true;
			}
			
			//Convert string to long. Throw exception if arg not a number
			try{
				Integer temp = Integer.valueOf(command[1]);
				long value = temp.longValue();
				Critter.setSeed(value);
			} catch(Exception NumberFormatException){
				System.out.println("error processing: " + input);
				return true;
			}
			return true;
		}

		//If stats command
		if(command[0].equals("stats")){
			if(command[2] != null){		//check for extra args
				System.out.println("error processing: " + input);
				return true;
			}

//			//Critter list and it's corresponding package
			java.util.List<Critter> instances;
			myPackage = Critter.class.getPackage().toString().split(" ")[1];
			
			//runStats for the specific type of Critter in command[1]
			try{
				instances = Critter.getInstances(command[1]);
				Class<?> c = Class.forName(myPackage + "." + command[1]);
				Class<?>[] types = {List.class};
				Method method = c.getMethod("runStats", types);	
				method.invoke(null, instances);
				
			} catch(Exception InvalidCritterException){
				System.out.println("error processing: " + input);
				return true;
			}
			return true;
		}
			
		//if make command
		if(command[0].equals("make")){
			int value = 0;
			
			//call make critter n times
			//if no arg specified, call it once
			if(command[2] == null){
				value = 1;
			}
			
			//else figure out how many times to call
			//catch exception if arg is junk
			else try{
				Integer temp = Integer.valueOf(command[2]);
				value = temp.intValue();
				
			} catch(Exception NumberFormatException){
				System.out.println("error processing: " + input);
				return true;
			}
			
			//call make critter 'value' times. Catch the exception if we need to
			try{
				for(int i = 0; i < value; i++){
					Critter.makeCritter(command[1]);
				}
			} catch(Exception InvalidCritterException){
				System.out.println("error processing: " + input);
				return true;
			}
		}
		
		return true;
	}
	
}
