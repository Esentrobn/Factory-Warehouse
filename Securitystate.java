 import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Securitystate extends WareHouseUIState implements ActionListener{
	
	  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
	  private static boolean loginSuccess = false;
	  private static Securitystate instance;
	  
	  private JFrame frame;
	  private JButton btnLogin;
	  private JTextArea userNameTextArea, passwordTextArea;
	  private JTextField txtUsername;
	  private JTextField txtPassword;
	  
	  private Securitystate() {
	      super();
	  }

	  public static Securitystate instance() {
	    if (instance == null) {
	      instance = new Securitystate();
	    }
	    return instance;
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
	 
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void process() {
	    System.out.println("Called, and we are in login");
	    frame = WarehouseUIContext.instance().getFrame();
	    frame.getContentPane().removeAll();
	    frame.getContentPane().setLayout(new BorderLayout());
	    
	    JPanel loginPage = new JPanel();
	    
	    
	    frame.getContentPane().add(loginPage);
	    loginPage.setLayout(null);
	    
	    JLabel lblUsername = new JLabel("Username");
	    lblUsername.setBounds(276, 97, 76, 16);
	    loginPage.add(lblUsername);
	    
	    JLabel lblPassword = new JLabel("Password");
	    lblPassword.setBounds(276, 127, 65, 16);
	    loginPage.add(lblPassword);
	    
	    txtUsername = new JTextField();
	    txtUsername.setBounds(350, 94, 116, 22);
	    loginPage.add(txtUsername);
	    txtUsername.setColumns(10);
	    
	    txtPassword = new JTextField();
	    txtPassword.setBounds(350, 126, 116, 22);
	    loginPage.add(txtPassword);
	    txtPassword.setColumns(10);
	    
	    btnLogin = new JButton("Login");
	    btnLogin.setBounds(327, 161, 97, 25);
	    btnLogin.addActionListener(this);
	    loginPage.add(btnLogin);
	    
	    JLabel lblEnterAnyValues = new JLabel("Enter any values.");
	    lblEnterAnyValues.setHorizontalAlignment(SwingConstants.CENTER);
	    lblEnterAnyValues.setBounds(254, 220, 249, 25);
	    loginPage.add(lblEnterAnyValues);
	    
	    JPanel panel = new JPanel();
	    panel.setBackground(Color.DARK_GRAY);
	    panel.setBounds(0, 0, 245, 400);
	    loginPage.add(panel);
	    panel.setLayout(null);
	    
	    JLabel label = new JLabel("WAREHOUSE");
	    label.setHorizontalAlignment(SwingConstants.CENTER);
	    label.setForeground(Color.WHITE);
	    label.setFont(new Font("Tahoma", Font.PLAIN, 36));
	    label.setBounds(12, 141, 220, 40);
	    panel.add(label);
	    
	    JLabel label_1 = new JLabel("THE");
	    label_1.setHorizontalAlignment(SwingConstants.CENTER);
	    label_1.setForeground(Color.WHITE);
	    label_1.setFont(new Font("Tahoma", Font.PLAIN, 38));
	    label_1.setBounds(53, 97, 130, 40);
	    panel.add(label_1);
	    
	    JLabel lblemptyFieldsWill = new JLabel("Empty fields will result in failed login.");
	    lblemptyFieldsWill.setHorizontalAlignment(SwingConstants.CENTER);
	    lblemptyFieldsWill.setBounds(254, 241, 249, 16);
	    loginPage.add(lblemptyFieldsWill);
	        
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
	  }
	
	  public void clear() {
		  frame.getContentPane().removeAll();
		  frame.paint(frame.getGraphics());
	  }
	  
	  public boolean isLocked(){
		  return this.loginSuccess;		  
	  }
	  
	  public void reLock(){		  
		  this.loginSuccess = false;
	  }
	  public void run() {
	    process();
	  }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		
		System.out.println("username " + username);
		System.out.println("password " + password);
		
	    if (!username.equals("") && !password.equals("")) {
	    	System.out.println("true login!!");
			loginSuccess = true;
		}
	    else {
	    	loginSuccess = false;
	    }
		
		if(arg0.getSource().equals(this.btnLogin)) {
			
			if(loginSuccess && (WarehouseUIContext.instance().getLogin() == WarehouseUIContext.IsClerk)) {
				clear();
				WarehouseUIContext.instance().changeState(1);
				System.out.println("In Clerk");
			}
			else if(loginSuccess && (WarehouseUIContext.instance().getLogin() == WarehouseUIContext.IsClient)) {
				clear();
				WarehouseUIContext.instance().changeState(2);
				System.out.println("In Client");
			}
			else if(loginSuccess && (WarehouseUIContext.instance().getLogin() == WarehouseUIContext.IsManager)) {
				clear();
				WarehouseUIContext.instance().changeState(3);
				System.out.println("In Manager");
			}
			else {
				System.out.println("login failed");
				(WarehouseUIContext.instance()).changeState(0);
			}
		}
	}
}
