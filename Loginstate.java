

import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Loginstate extends WareHouseUIState implements ActionListener{
  private static final int CLERK_LOGIN = 0;
  private static final int USER_LOGIN = 1;
  private static final int Manager_Login = 3;
  private static final int EXIT = 2;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private WarehouseUIContext context;
  private static Loginstate instance;
  private JFrame frame;
  private AbstractButton clerkButton, userButton, managerButton;
  JButton btnClerk;
  JButton btnUser;
  JButton btnManager;
  
  private Loginstate() {
      super();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new Loginstate();
    }
    return instance;
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" ));
        if (value <= Manager_Login && value >= CLERK_LOGIN) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
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
 
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  private void clerk(){
    (WarehouseUIContext.instance()).setLogin(WarehouseUIContext.IsClerk);
    clear();
    (WarehouseUIContext.instance()).changeState(4);
  }

  private void user(){
	 //For testing only
	 Iterator<Client> users = Warehouse.instance().getClients();
	 System.out.println("\n");
	 while(users.hasNext()) {
		 Client user = users.next();
		 System.out.println(user.toString());
	}
	System.out.println("\n");
	//For testing only
    String userID = getToken("Please input the user id: ");
    if (Warehouse.instance().searchMembership(userID) != null){
      (WarehouseUIContext.instance()).setLogin(WarehouseUIContext.IsClient);
      (WarehouseUIContext.instance()).setUser(userID);   
      clear();
      (WarehouseUIContext.instance()).changeState(4);
    }
    else 
      System.out.println("Invalid user id.");
  } 
  private void manager(){
	  System.out.println("ManagerState");
	  (WarehouseUIContext.instance()).setLogin(WarehouseUIContext.IsManager);
	  clear();
	  (WarehouseUIContext.instance()).changeState(4);
  }

  /**
   * @wbp.parser.entryPoint
   */
  public void process() {
	frame = WarehouseUIContext.instance().getFrame();
	frame.getContentPane().removeAll();
	frame.getContentPane().setLayout(null);

	
	JPanel panel = new JPanel();
	panel.setBackground(Color.DARK_GRAY);
	panel.setBounds(0, 0, 244, 400);
	frame.getContentPane().add(panel);
	panel.setLayout(null);
	
	JLabel lblWarehouse = new JLabel("WAREHOUSE");
	lblWarehouse.setForeground(Color.WHITE);
	lblWarehouse.setHorizontalAlignment(SwingConstants.CENTER);
	lblWarehouse.setFont(new Font("Tahoma", Font.PLAIN, 36));
	lblWarehouse.setBounds(12, 140, 220, 40);
	panel.add(lblWarehouse);
	
	JLabel lblThe = new JLabel("THE");
	lblThe.setForeground(Color.WHITE);
	lblThe.setHorizontalAlignment(SwingConstants.CENTER);
	lblThe.setFont(new Font("Tahoma", Font.PLAIN, 38));
	lblThe.setBounds(53, 96, 130, 40);
	panel.add(lblThe);
	
	btnClerk = new JButton("Clerk");
	btnClerk.setFont(new Font("Tahoma", Font.PLAIN, 20));
	btnClerk.setBounds(317, 102, 127, 41);
	btnClerk.addActionListener(this);
	frame.getContentPane().add(btnClerk);
	
	btnUser = new JButton("User");
	btnUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
	btnUser.setBounds(317, 166, 127, 41);
	btnUser.addActionListener(this);
	frame.getContentPane().add(btnUser);
	
	btnManager = new JButton("Manager");
	btnManager.setFont(new Font("Tahoma", Font.PLAIN, 20));
	btnManager.setBounds(317, 229, 127, 41);
	btnManager.addActionListener(this);
	frame.getContentPane().add(btnManager);

	frame.setVisible(true);
	frame.paint(frame.getGraphics());
	frame.toFront();
	frame.requestFocus();

  }

  public void run() {
    process();
  }
  public void clear() {
	  frame.getContentPane().removeAll();
	  frame.paint(frame.getGraphics());
  }

@Override
public void actionPerformed(ActionEvent arg0) {
	if(arg0.getSource().equals(this.btnClerk)) {
		System.out.println("Clerk");
		this.clerk();
	}
	else if(arg0.getSource().equals(this.btnUser)) {
		System.out.println("User");
		this.user();
	}
	else if(arg0.getSource().equals(this.btnManager)) {
		System.out.println("Manager");
		this.manager();
	}
}
}
