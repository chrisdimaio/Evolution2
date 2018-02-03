import java.util.Random;
import java.awt.Color;

public class Organism {
	private byte[] instructions;

	public int energy;
	private int generation;

	private int touches = 0;
	private int instrPosition = Constants.FIRST_INSTRUCTION;

	private int x;
	private int y;
	private int downForce = 0;

	// Organism specific properties defined by the
	// first 2 instructions.
	private int P_FOOD_START_RANGE;
	private int P_FOOD_END_RANGE;
	private int P_DUPLICATE_MARK;

	// Organism specific properties defined by a hash
	// of all organism instructions.
	private int P_ORG_COLOR;
	private Color ORG_COLOR_OBJ;

	public Organism(int instrCount, int initialEnergy){
		this(initialEnergy, Utils.randInstructionSet(instrCount));
	}

	public Organism(int initialEnergy, byte[] instrs){
		instructions = instrs;

		// Generation is set to zero by default.
		// Use get and setGeneration to change.
		generation = 0;

		// Set organsim properties.
		energy = initialEnergy;

		int rangeOffSet = instructions[3] + 128;
		Color tmpColorObj = new Color(instructions[0]+ 128, instructions[1]+ 128, instructions[2]+ 128);
	    P_FOOD_START_RANGE 	= tmpColorObj.getRGB();
		P_FOOD_END_RANGE 	= P_FOOD_START_RANGE + rangeOffSet;

		ORG_COLOR_OBJ = new Color(instructions[2]+ 128, instructions[3]+ 128, instructions[4]+ 128);
		
		// Org's color to other orgs is defined only by red.
		P_ORG_COLOR = instructions[2]+ 128;

		int rangeA = instructions[0] + 128;
		int rangeB = instructions[1] + 128;
		if(rangeA<rangeB){
			P_FOOD_START_RANGE 	= rangeA;
			P_FOOD_END_RANGE	= rangeB;
		}else{
			P_FOOD_START_RANGE 	= rangeB;
			P_FOOD_END_RANGE	= rangeA;
		}
		P_DUPLICATE_MARK = (instructions[6] + 128);



//		P_ORG_COLOR	= Utils.byteHash(instructions) + 128;
	}

	public int getDuplicateMark(){ return P_DUPLICATE_MARK;}
//	public Color getOrgColorObj(){ return new Color(P_FOOD_START_RANGE, P_FOOD_END_RANGE, P_ORG_COLOR);}
	public Color getOrgColorObj(){ return ORG_COLOR_OBJ;}

	public int getOrgColor(){ return P_ORG_COLOR;}
	public int getOrgFoodStartRange(){ return P_FOOD_START_RANGE;}
	public int getOrgFoodEndRange(){ return P_FOOD_END_RANGE;}

	public int getEnergy(){ return energy;}
	public void setEnergy(int e){ energy = e;}

	public int getTouches(){ return touches;}

	public int getGeneration(){ return generation;}
	public void setGeneration(int g){ generation = g;}

	public void increaseEnergy(int e){energy+=e;}
	public void decreaseEnergy(int e){energy-=e;}

	public int getX(){ return x;}
	public void setX(int nX){ x = nX;}

	public int getY(){ return y;}
	public void setY(int nY){ y = nY;}

	public int getDownForce(){return downForce;}
	public void setDownForce(int d){downForce = d;}

	public byte[] getInstructions(){ return instructions;}

	public byte getNextInstruction(){
		byte result = instructions[instrPosition];

		if(instrPosition<instructions.length-1){
			instrPosition++;
		}else{
			instrPosition = Constants.FIRST_INSTRUCTION;
		}
		touches++;
		return result;
	}

	public int getInstrCount(){ return instructions.length;}

	public String toString(){
		int len = instructions.length;

		String str = " [";
		for(int i=0;i<len;i++){
			str += instructions[i] + ((i+1==len)?"":",");
		}
		str += "]";

		return str;
	}

}
