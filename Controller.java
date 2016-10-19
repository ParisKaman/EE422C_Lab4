package assignment4;
import java.util.*;

public class Controller {
		
	private static String[] commands = {"quit","show","step","seed","stats","make"};
	
	/**Given an input string, returns an array of strings.
	 * The first index will be a command
	 * The second two indices will be any parameters to be used by the command
	 * 
	 * @param input
	 * @return result
	 */
	public static String[] parse(String input){
		int index = 0;						//index of line
		//boolean legal = false;				//determines if command is legal
		String[] result = new String[4];	//array to return
		
		//Parse through input string. 
		Scanner parse = new Scanner(input);
		while(index < 4 && parse.hasNext()){	
			result[index] = parse.next();
			index++;
		}
		if(index > 3){
			result[1] = "false";
		}
		return result;
	}
	
	public static boolean isLegal(String[] input){
		int index = 0;
		
		//see if token is a valid command		
		for(String command: commands){
			if(command.equals(input[0])){
				return true;
			}
		}
		return false;
	}		

	public static boolean execute(String[] command, String input){

		//If invalid command output error and start again		
		if(command[0].equals("false")){
			System.out.println("invalid command: " + input);
			return true;
		}
		
		//If quit command, tell main to shut down
		if(command[0].equals("quit")){
			return false;
		}
		
		//If show command
		if(command[0].equals("show")){
			Critter.displayWorld();
			return true;
		}
		
		
		//If step command
		if(command[0].equals("step")){
			int value = 0;
			if(command[1] == null)
				value = 1;
			else try{
				Integer temp = Integer.valueOf(command[1]);
				value = temp.intValue();				
			} catch(Exception NumberFormatException){
				System.out.println("error processing: " + input);
				return true;
			}
			for(int i = 0; i < value; i++){
				Critter.worldTimeStep();
			}
			return true;
		}
		
		//If seed command
		if(command[0].equals("seed")){
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
			java.util.List<Critter> instances;
			try{
				instances = Critter.getInstances("assignment4." + command[1]);
				
				//Critter.runStats(instances);
				//need to invoke this method with the critter in command[1]
				//rather than "Critter" but don't know how yet
				
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
					Critter.makeCritter("assignment4." + command[1]);
				}
			} catch(Exception InvalidCritterException){
				System.out.println("error processing: " + input);
				return true;
			}
		}
		
		return true;
}
	
	public static boolean isNumeric(String str){
	    for (char c : str.toCharArray()){
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
