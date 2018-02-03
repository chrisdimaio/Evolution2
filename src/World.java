import java.util.*;
import java.lang.Math;

public class World {
	private List orgList;

	private Organism[][] worldMap = new Organism[Constants.MAX_WORLD_X][Constants.MAX_WORLD_Y];

	public Stats stats = new Stats(this);

	private int organismCount = 0;

	public World(List ol){
		orgList = ol;
	}

	public int getOrgCount(){ return organismCount;}

	public Organism getWorldMapAt(int x, int y){return worldMap[x][y];}

	private void touchOrg(Organism o)
	{
		int dF = o.getDownForce();
		int x = o.getX();
		int y = o.getY();

		o.setDownForce(++dF);
		if(dF==Constants.DOWN_FORCE){
			o.setDownForce(0);
			moveOrg(o, x, y, x, y+Constants.DOWN_FORCE_VALUE);
		}
	}

	private void moveOrg(Organism o, int oX, int oY, int nX, int nY)
	{
		if(spotFree(nX,nY)){
			worldMap[oX][oY] = null;
			worldMap[nX][nY] = o;
			o.setX(nX);
			o.setY(nY);
		}
	}

	private boolean checkEnergy(Organism o)
	{
		boolean result = false;
		if(o.getEnergy() >= 0){
			result = true;
		}else{
			removeOrg(o);
		}

		return result;
	}

	private void removeOrg(Organism o)
	{
		int x = o.getX();
		int y = o.getY();

		worldMap[x][y] = null;
		stats.processOrg(o);
		orgList.remove(o);
		organismCount--;
		stats.orgRemoved();
	}

	private boolean inBounds(int x, int y)
	{
		if((x>-1)&&(x<Constants.MAX_WORLD_X)&&(y>-1)&&(y<Constants.MAX_WORLD_Y)){
			return true;
		}
		return false;
	}

	private boolean isEmpty(int x, int y)
	{

		if(worldMap[x][y]==null){
			return true;
		}
		return false;
	}

	private boolean occupied(int x, int y)
	{
		boolean result = false;
		if(inBounds(x,y)&&(!isEmpty(x,y))){result=true;}
		return result;
	}

	private boolean spotFree(int x, int y)
	{
		boolean result = false;
		if(inBounds(x,y)&&(isEmpty(x,y))){result=true;}
		return result;
	}

	public void mapNewOrg(Organism org, int x, int y)
	{
		stats.incWorldEnergyAdded(org.getEnergy());
		mapOrg(org, x, y);
		
	}

	public void mapNewOrg(Organism org)
	{
		stats.incWorldEnergyAdded(org.getEnergy());
		mapOrg(org);
	}

	private void mapOrg(Organism org){
		Random rand = new Random();
		mapOrg(org, rand.nextInt(Constants.MAX_WORLD_X), rand.nextInt(Constants.MAX_WORLD_Y));
	}

	private void mapOrg(Organism org, int x, int y){
		for(int i=0;i<(Constants.MAX_WORLD_X * Constants.MAX_WORLD_Y);i++){
			if(isEmpty(x,y)){
				worldMap[x][y] = org;
				org.setX(x);
				org.setY(y);
				orgList.add(org);
				organismCount++;
				stats.orgAdded();
				break;
			}
		}
	}

	public void printMap(){
		Utils.printN(" - ",Constants.MAX_WORLD_X);
		System.out.print("\n");
		for(int y=0;y<Constants.MAX_WORLD_Y;y++){
			for(int x=0;x<Constants.MAX_WORLD_X;x++){
				if(isEmpty(x,y)){
					System.out.print(" . ");
				}else{
					System.out.print(" x ");
				}

			}
			System.out.print("|\n");
		}
		Utils.printN(" - ",Constants.MAX_WORLD_X);
		System.out.print("\n");
	}

	public void DUPLICATE(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.DUPLICATE_COST);

		if(checkEnergy(org)&&(org.getEnergy()>=org.getDuplicateMark())){
			// Look for a spot.
			boolean spotFound = false;

			int oX = org.getX();
			int oY = org.getY();

			int cX = 0;
			int cY = 0;

			// Right
			if(occupied(oX+1,oY)){
				cX = oX+1;
				cY = oY;
				spotFound = true;
			}
			// Left
			else if(spotFree(oX-1,oY)){
				cX = oX-1;
				cY = oY;
				spotFound = true;
			}
			// Up
			else if(spotFree(oX,oY-1)){
				cX = oX;
				cY = oY-1;
				spotFound = true;
			}
			// Down
			else if(spotFree(oX,oY+1)){
				cX = oX;
				cY = oY+1;
				spotFound = true;
			}
			// Right, Up
			else if(spotFree(oX+1,oY-1)){
				cX = oX+1;
				cY = oY-1;
				spotFound = true;
			}
			// Right, Down
			else if(spotFree(oX+1,oY+1)){
				cX = oX+1;
				cY = oY+1;
				spotFound = true;
			}
			// Left, Up
			else if(spotFree(oX-1,oY-1)){
				cX = oX-1;
				cY = oY-1;
				spotFound = true;
			}
			// Left, Down
			else if(spotFree(oX-1,oY+1)){
				cX = oX-1;
				cY = oY+1;
				spotFound = true;
			}

			// we found a spot.
			if(spotFound){
				Random rand = new Random();

				byte[] parentInstrs = org.getInstructions();
				byte[] childInstrs = Utils.copyInstructionSet(parentInstrs);

				// Energy is split between parent and child.
				// the child gets bigger of halves.
				int parentEnergy 	= org.getEnergy();
				int childEnergy 	= (int)Math.ceil(parentEnergy/2.0);
				int parentNewEnergy = (int)Math.floor(parentEnergy/2.0);

				// There is a one out of MUTATION_PROBABILITY
				if(rand.nextInt(Constants.MUTATION_PROBABILITY)==
					Constants.MUTATION_PROBABILITY-1)
				{
					System.out.println("mutation");
					// Mutate one byte by one bit.
					int mByte 	= childInstrs[rand.nextInt(Constants.INSTRUCTION_COUNT)];
					int mBit 	= rand.nextInt(8);
					byte mutatedByte = Utils.flipBit(mByte, mBit);
					childInstrs[mBit] = mutatedByte;
				}

				// Set the new energy levels.
				org.setEnergy(parentNewEnergy);
				Organism childOrg = new Organism(childEnergy, childInstrs);
				childOrg.setGeneration(org.getGeneration()+1);
				mapOrg(childOrg, cX, cY);
			}
		}
	}

	public void EAT(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.EATING_COST);

		if(checkEnergy(org)){
			int myX = org.getX();
			int myY = org.getY();
			int foodX = -1;
			int foodY = -1;

			boolean foodFound = false;

			// Right
			if(occupied(myX+1,myY)){
				foodFound = true;
				foodX=myX+1;
				foodY=myY;
			}
			// Left
			else if(occupied(myX-1,myY)){
				foodFound = true;
				foodX=myX-1;
				foodY=myY;
			}
			// Up
			else if(occupied(myX,myY-1)){
				foodFound = true;
				foodX=myX;
				foodY=myY-1;
			}
			// Down
			else if(occupied(myX,myY+1)){
				foodFound = true;
				foodX=myX;
				foodY=myY+1;
			}
			// Right, Up
			else if(occupied(myX+1,myY-1)){
				foodFound = true;
				foodX=myX+1;
				foodY=myY-1;
			}
			// Right, Down
			else if(occupied(myX+1,myY+1)){
				foodFound = true;
				foodX=myX+1;
				foodY=myY+1;
			}
			// Left, Up
			else if(occupied(myX-1,myY-1)){
				foodFound = true;
				foodX=myX-1;
				foodY=myY-1;
			}
			// Left, Down
			else if(occupied(myX-1,myY+1)){
				foodFound = true;
				foodX=myX-1;
				foodY=myY+1;
			}

			if(foodFound){
				Organism foodOrg = worldMap[foodX][foodY];

				int foodColor = foodOrg.getOrgColor();
				int orgStartRange 	= org.getOrgFoodStartRange();
				int orgEndRange		= org.getOrgFoodEndRange();

				// System.out.println("Found something");
				// System.out.println("foodColor: " + foodColor);
				// System.out.println("orgStartRange: " + orgStartRange);
				// System.out.println("orgEndRange: " + orgEndRange);
				// Utils.waitMS(250);
				
				// if((orgStartRange<=28)&&(28<=orgEndRange))
				// {
					// System.out.println("Good");
				// }
				
				
				// Check if other org is actually food.
				// Compares orgs food range to foodOrg's red value.
				if((orgStartRange<=foodColor)&&(foodColor<=orgEndRange))
				// if((orgStartRange<=foodColor)&&(foodColor<=orgStartRange+50))
				{
					int foodsEnergy = foodOrg.getEnergy();

					org.increaseEnergy(foodsEnergy);

					// remove org from map & list.
					removeOrg(foodOrg);
				}
			}
		}
	}

	public void MOVE_UP(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.MOVEMENT_COST);

		if(checkEnergy(org)){
			int oldX = org.getX();
			int oldY = org.getY();

			int newY = oldY-1;
			moveOrg(org, oldX, oldY, oldX, newY);
		}
	}
	public void MOVE_UP_RIGHT(Organism org){
	}
	public void MOVE_RIGHT(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.MOVEMENT_COST);

		if(checkEnergy(org)){
			int oldX = org.getX();
			int oldY = org.getY();

			int newX = oldX+1;
			moveOrg(org, oldX, oldY, newX, oldY);
//			if(inBounds(newX,oldY)&&isEmpty(newX,oldY)){
//				worldMap[oldX][oldY] = null;
//				worldMap[newX][oldY] = org;;
//				org.setX(newX);
//			}
		}
	}
	public void MOVE_RIGHT_DOWN(Organism org){
	}
	public void MOVE_DOWN(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.MOVEMENT_COST);

		if(checkEnergy(org)){
			int oldX = org.getX();
			int oldY = org.getY();

			int newY = oldY+1;
			moveOrg(org, oldX, oldY, oldX, newY);
		}
	}
	public void MOVE_DOWN_LEFT(Organism org){
	}
	public void MOVE_LEFT(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.MOVEMENT_COST);

		if(checkEnergy(org)){
			int oldX = org.getX();
			int oldY = org.getY();

			int newX = oldX-1;
			moveOrg(org, oldX, oldY, newX, oldY);
		}
	}
	public void MOVE_LEFT_UP(Organism org){
	}
	public void PAUSE(Organism org){
		touchOrg(org);
		org.decreaseEnergy(Constants.PAUSE_COST);

		if(checkEnergy(org)){

		}
	}
}





