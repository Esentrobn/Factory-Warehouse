
public abstract class Context {
	public static WareHouseUIState[] states;
	protected static int[][] transitions;
	private static int previousState;
	protected static int currentState;
	private static int totalStates = 0;
	
	public Context() {
		states = new WareHouseUIState[10];
		transitions = new int[7][7];
	}
	
	public static void addState(WareHouseUIState newState) {
		states[totalStates] = newState;
		totalStates++;
	}
	  
	public static void addTransition(int initialState, int finalState, int exitCode) {
		transitions[initialState][exitCode] = finalState;
	}
	
	public void start(){
		currentState = 0;
		states[currentState].run();
	}
	
	public void changeState(int exitCode)
	{
//		System.out.println("current state " + currentState + " \n \n ");
//		System.out.println("exit code " + exitCode + " \n \n ");
		previousState = currentState;
		currentState = transitions[currentState][exitCode];
		if(currentState == -1) {
			Warehouse.save();
			System.out.println("Warehouse saved successfully");
			System.exit(0);
		}
		//System.out.println("current state " + currentState + " \n \n ");
		states[currentState].run();
	}
	
	public int getPreviousState() {
		return previousState;
	}

}
