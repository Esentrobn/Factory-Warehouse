

import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class WarehouseUIContext extends Context {
 
  private static JFrame warehouseFrame;
  private static Warehouse warehouse;
  private static WarehouseUIContext instance;
  private int currentUser;
  private String userID;
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
  public static final int IsClerk = 1;
  public static final int IsClient = 2;
  public static final int IsManager = 3;
  
  public static void main (java.lang.String[] args){
    WarehouseUIContext.instance().start(); 
    states[currentState].run();
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  
  public void load(){
	  retrieve();
	  
  }
  private void retrieve() {
	  
        
        try {
			Warehouse newWarehouse = Warehouse.retrieve();

			if (newWarehouse != null) {
				System.out.println("Database loaded \n");
				warehouse = newWarehouse;
			} else {
				System.out.println("No database exists, creating new one");
				warehouse = Warehouse.instance();

				
			}
		} catch (Exception cnfe) {
			cnfe.printStackTrace();
        }
	}

  public void setLogin(int code)
  {currentUser = code;}

  public void setUser(String uID)
  { userID = uID;}

  public int getLogin()
  { return currentUser;}

  public String getUser()
  { return userID;}

  private WarehouseUIContext() { //constructor
	super();
    System.out.println("In WarehouseContext constructor");
    
    retrieve();
       
    // set up the FSM and transition table;
    Context.addState(Loginstate.instance());//0
    Context.addState(Clerkstate.instance());//1
    Context.addState(Clientstate.instance());//2
    Context.addState(Managerstate.instance());//3
    Context.addState(Securitystate.instance());//4
    Context.addState(OrderState.instance());//5
    Context.addState(ReceiveOrderState.instance());//6
    
    Context.addTransition(0, -1, 0);//fixed
    Context.addTransition(1, 0, 0);
    Context.addTransition(2, 0, 0);
    Context.addTransition(3, 0, 0);
    Context.addTransition(5, 1, 1);//fixed
    Context.addTransition(6, 1, 1);//fixed
    Context.addTransition(0, 1, 1);//fixed
    Context.addTransition(1, 0, 1);
    Context.addTransition(2, 1, 1);
    Context.addTransition(3, -2, 1);
    Context.addTransition(0, 2, 2);//fixed
    Context.addTransition(1, -2, 2);
    Context.addTransition(2, -1, 2);
    Context.addTransition(3, 2, 2);
    Context.addTransition(0, 3, 3);
    Context.addTransition(1, 3, 3);
    Context.addTransition(2, 3, 3);
    Context.addTransition(3, -1, 3);
    Context.addTransition(1, 5, 5);//Clerk to create order
    Context.addTransition(3, 4, 4);
    Context.addTransition(0, 6, 6);
    Context.addTransition(4, 3, 3);
    Context.addTransition(0, 4, 4);//added for securityState
    Context.addTransition(4, 0, 0);
    Context.addTransition(1, 4, 4);//Clerk to Security
    Context.addTransition(4, 1, 1);//Security to Clerk
    Context.addTransition(1, 6, 6);//Clerk to Recieve order
    
    warehouseFrame = new JFrame("Warehouse GUI");
    warehouseFrame.addWindowListener(new WindowAdapter() {
    	public void windowClosing (WindowEvent e) {
    		System.exit(0);
    	}
    });
    warehouseFrame.setSize(515,400);
    warehouseFrame.setResizable(false);
    warehouseFrame.setLocation(200, 400);
  }
  
  public JFrame getFrame() {
	  return warehouseFrame;
  }

  public static WarehouseUIContext instance() {
	  if (instance == null) {
		  System.out.println("Calling constructor");
		  instance = new WarehouseUIContext();
	  	}
	  return instance;
  }
  
  public void process() {
	  states[currentState].run();
  }

}
