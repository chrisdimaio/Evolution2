
public final class Stats {
	public class RecordBook{
		private Organism[] recordBook = new Organism[Constants.RECORDS_TO_KEEP];
		private int recordCount = 0;
		private int recordIndex = 0;

		public byte[] getNextRecordInstr(){
			byte[] result = Utils.randInstructionSet(Constants.INSTRUCTION_COUNT);
			if((recordCount>0)&&(recordIndex<recordCount)){
				Organism o = recordBook[recordIndex];
				if(o!=null){
					result = o.getInstructions();
					if(recordIndex==recordCount-1){
						recordIndex = 0;
					}
					else{
						recordIndex++;
					}
				}
			}
			return result;
		}

		public void addRecord(Organism o){
			int record = o.getGeneration();
			for(int i=0;i<Constants.RECORDS_TO_KEEP;i++){
				if(recordBook[i]==null){
					recordBook[i] = o;
					recordCount++;
					break;
				}
				if(recordCount==Constants.RECORDS_TO_KEEP){
					int r = recordBook[i].getGeneration();
					if(r<record){
						recordBook[i] = o;
						break;
					}
				}
			}
		}
	}

	private ReportWindow reportWindow;

	private World worldRef;

	private long orgsRemoved = 0;
	private long orgsAdded	 = 0;

	private int reportNumber = 0;

	private long worldEnergyAddedLast = 0;
	private long worldEnergyAdded	 = 0;
	private int worldEnergyContained = 0;

	private long generationsTotal 	= 0;
	private long generationsCounted = 0;
	private long mostGensRecord;
	public Organism mostGensRecordOrg;

	private long touchesTotal 	= 0;
	private long touchesCounted = 0;
	private long mostTouchesRecord;
	private Organism mostTouchesRecordOrg;

	private long lastTime;
	private long startTimeInMillis;

	public RecordBook recordBook;

	public Stats(World w){
		reportWindow = new ReportWindow(this, w);
		worldRef = w;
		startTimeInMillis = System.currentTimeMillis();
		recordBook = new RecordBook();
	}

	public void orgRemoved(){ orgsRemoved++;}
	public void orgAdded(){ orgsAdded++;}

	public void incWorldEnergyAdded(long  e){ worldEnergyAdded+=e;}
	public void incWorldEnergyContained(int e){ worldEnergyContained+=e;}

	private long getWorldEnergyAdded(){
		worldEnergyAddedLast = worldEnergyAdded;
		return worldEnergyAdded;
	}

	private String worldEnergyIndicator(){
		String result = "   ";
		if(worldEnergyAdded>worldEnergyAddedLast){ result = " UP";}
		return result;
	}

	private void recordTouches(Organism o){
		int t = o.getTouches();

		touchesTotal += t;
		touchesCounted++;

		if(t>mostTouchesRecord){
			mostTouchesRecord 	 = t;
			mostTouchesRecordOrg = o;
		}
	}

	private void recordGenerations(Organism o){
		int g = o.getGeneration();

		generationsTotal += g;
		generationsCounted++;

		if(g>mostGensRecord){
			mostGensRecord 		= g;
			mostGensRecordOrg 	= o;
			recordBook.addRecord(o);
		}
	}

	public void processOrg(Organism o){
		recordGenerations(o);
		recordTouches(o);
	}

	public void refreshReport(){
		reportWindow.refresh();
	}

	public void printStats(){
		long worldAgeInMillis = (System.currentTimeMillis()-startTimeInMillis);

		int population = worldRef.getOrgCount();

		int worldArea = Constants.MAX_WORLD_X*Constants.MAX_WORLD_Y;

		float populationToSpace = (float)worldArea/(float)population;

		String worldEnergyIndicator = worldEnergyIndicator();

		System.out.println("\n::Statistics Report # " + reportNumber + "::");
		System.out.println("World Age: " + Utils.millisToHMS(worldAgeInMillis));
		System.out.println("World Area: " + worldArea);
		System.out.println("Population: " + population);
		System.out.println("Space/Population: " + populationToSpace);
		System.out.println("Energy Added: " + getWorldEnergyAdded() + worldEnergyIndicator);
		System.out.println("Orgs Removed: " + orgsRemoved);
		System.out.println("Orgs Added: " + orgsAdded);
		System.out.println("-\nAverage Touches: " + Utils.average(touchesTotal, touchesCounted));
		System.out.println("Most Touches: " + mostTouchesRecord);
		System.out.println("-\nAverage Generations: " + Utils.average(generationsTotal, generationsCounted));
		System.out.println("Most Generations: " + mostGensRecord);
		System.out.println(":::\n");

		reportNumber++;
	}

	public boolean wait( int d){
		boolean result = false;

		long currentTime = System.currentTimeMillis();
		long difference = currentTime - lastTime;
//		long durationInMillis = d * 1000;
		long durationInMillis = d;

		if(difference>=durationInMillis){
			lastTime = currentTime;
			result = true;
		}

		return result;
	}

	public void logStats(){
//		FileWriter file = new FileWriter("out.txt");
//		BufferedWriter out = new BufferedWriter(file);
//		try{
//
//			out.write("Hello Java");
//			//Close the output stream
//			out.close();
//		}
//		catch (Exception e){//Catch exception if any
//			System.err.println("Error: " + e.getMessage());
//		}
	}
}
