import java.util.*;
import java.lang.reflect.Method;
import java.awt.Color;

/**
 * @(#)Evolution2.java
 *
 * Evolution2 application
 *
 * @author
 * @version 1.00 2011/10/28
 */

public class Evolution2 {
   	private static List orgList = new LinkedList();
   	private static World world = new World(orgList);
   	private static Random rand = new Random();

   	private static String[] INSTRUCTION_SET = {
   		"DUPLICATE",
   		"EAT",
	   	"MOVE_UP",
//	   	"MOVE_UP_RIGHT",
	   	"MOVE_RIGHT",
//	   	"MOVE_RIGHT_DOWN",
	   	"MOVE_DOWN",
//	   	"MOVE_DOWN_LEFT",
	   	"MOVE_LEFT",
//	   	"MOVE_LEFT_UP",
		"PAUSE",
   	};

    public static void main(String[] args) {

		// Color tmpA = new Color(0,0,0);
		// Color tmpB = new Color(255,255,255);
		// Color tmpC = new Color(155,155,155);

		// System.out.println("Result: " + Utils.withinRGBRange(tmpA, tmpB, tmpC));

	    Organism newOrg;

		// for(int i=0;i<Constants.ORG_COUNT;i++){
			// newOrg = new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY);
			// world.mapNewOrg(newOrg);
		// }

		Organism tmpOrg;

		for(int a=0;a<Constants.LOOP_COUNT;a++){
			for(int i=0;i<world.getOrgCount();i++){
				tmpOrg = (Organism)orgList.get(i);
				callByInstrName(getInstrCode(tmpOrg), tmpOrg);
				if(world.stats.wait(Constants.REFRESH_RATE_MS)){
					world.stats.refreshReport();
					if(a%50==0){
						world.stats.printStats();
					}
				}
				throttlePopulation();
			}

			// if(a%10==0){
				// world.mapNewOrg(
					// new Organism(Constants.START_ENERGY*10, Constants.CUSTOM_ORG_2), rand.nextInt(250), 0);
			// }
			if(a%8==0&&world.getOrgCount()<1000)
			{
				world.mapNewOrg(
					new Organism(10000, Constants.SUNSHINE), rand.nextInt(250), 0);
			}
			// if(a%8==0&&world.getOrgCount()<500)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.SIMPLE_ORG_1), rand.nextInt(250), 200);
			// }
			
			// if(a%10==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.PURPLE_FOOD), 95+rand.nextInt(10), 120);
			// }
			
			// if(a%8==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.GREEN_FOOD), 185+rand.nextInt(10), 225);
			// }
			
			// if(a%8==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.BLUE_FOOD), 5+rand.nextInt(10), 200);
			// }
			
			// if(a%10==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.PURPLE_FOOD), 75+rand.nextInt(8), 35);
			// }
			
			// if(a%8==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.GREEN_FOOD), 115+rand.nextInt(12), 220);
			// }
			
			// if(a%8==0&&world.getOrgCount()<10000)
			// {
				// world.mapNewOrg(
					// new Organism(1000, Constants.BLUE_FOOD), 200+rand.nextInt(15), 200);
			// }
			
			if(a%20==0&&world.getOrgCount()<1000)
			{
				world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY), 75+rand.nextInt(95), 40 + rand.nextInt(10));
				System.out.println("adding more orgs");
			}
			
			
			// if(a%15==0){
				// world.mapNewOrg(
					// new Organism(Constants.START_ENERGY, Constants.CUSTOM_ORG_3), 125+rand.nextInt(20), 325);
			// }

//			world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY), 110, 100);
//			world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY), 15, 200);
//			world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY), 220, 300);
//			world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY), 1+rand.nextInt(248), 50);
//			world.mapNewOrg(
//				new Organism(Constants.START_ENERGY, world.stats.recordBook.getNextRecordInstr()), 105, 400);
//			world.mapNewOrg(
//				new Organism(Constants.START_ENERGY, world.stats.recordBook.getNextRecordInstr()), 110, 400);
//			world.mapNewOrg(
//				new Organism(Constants.START_ENERGY, world.stats.recordBook.getNextRecordInstr()), 115, 400);
		}
    }

	public static void throttlePopulation(){
		int orgCount = world.getOrgCount();

		if(orgCount<Constants.POPULATION_THRESHOLD){
			for(int i = 0;i<Constants.THROTTLE;i++){

//				world.mapNewOrg(new Organism(Constants.INSTRUCTION_COUNT, Constants.START_ENERGY));
			}
		}
	}

    public static String getInstrCode(Organism o){
    	byte rawInstr = o.getNextInstruction();
    	int instrSetCount = INSTRUCTION_SET.length;
    	int result = (rawInstr + 128) % instrSetCount;
    	return INSTRUCTION_SET[result];
    }

    public static boolean callByInstrName(String instrName, Organism mArg){
    	try{
			Class instrClass = Class.forName("World");
			Method instrMethod = instrClass.getMethod(instrName, Organism.class);

			instrMethod.invoke(world, mArg);

			return true;
		}
		catch (Throwable e) {
        	System.err.println(e);
        }
        return false;
    }
}
