import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;

public class OrderState extends WareHouseUIState implements ActionListener{

	private static OrderState instance;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;
	private JFrame frame;
	private JPanel southPanel;
	private JButton btnCreateOrder;
	private JButton btnBackToClerk;
	private JLabel lblNotifications;
	private JTextField txtClientidfield;
	private JTextField txtProductidfield;
	private JTextField txtQuantityfield;
	private JLabel lblErrornoteClientID;
	private JLabel lblErrornoteproductid;
	
	private OrderState(){
		super();
		warehouse = Warehouse.instance();
	}
	
	@Override
	public void run() {
		makeOrderForClient();
	}
	
	public static OrderState instance(){
		if(instance == null){
			instance = new OrderState();
		}
		return instance;
	}
	
	private void processMatch(String tempClient2){
		String productStringId;
		Product tempProduct;
		int tempQuantity = 0;
		boolean addItemsToOrder;
		String tempString;
		Order createdOrder = new Order();
		Order result;
		String tempClient = tempClient2;
		char cont;

		productStringId = this.txtProductidfield.getText();
		tempProduct = warehouse.searchProduct(productStringId);
		if (tempProduct != null) {
			this.lblErrornoteproductid.setText("");
			tempQuantity = Integer.parseInt(this.txtQuantityfield.getText());

			addItemsToOrder = createdOrder.insertlistedItem(tempProduct, tempQuantity);
			if (!addItemsToOrder) {
				this.lblNotifications.setText("Failed to add item to order");

			} else
				this.lblNotifications.setText("Added Item");

		} else {
			this.lblErrornoteproductid.setText("Could not find item");
		}

		this.lblNotifications.setText("Processing order");
		result = warehouse.processOrder(createdOrder, tempClient, tempQuantity, tempProduct);

		if (result == null) {
			this.lblNotifications.setText("Could not add order");
		} else {
			this.lblNotifications.setText("Added product [" + result + "]");
			warehouse.save();
		}

	}
	
	private void createOrder() {

		String ClientID = this.txtClientidfield.getText();
		if (warehouse.searchMembership(ClientID) != null) {
			processMatch(ClientID);
			this.lblErrornoteClientID.setText("");
		}else{
			this.lblErrornoteClientID.setText("Couldn't find client to associate");
		}
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
	private void makeOrderForClient() {
		frame = WarehouseUIContext.instance().getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		southPanel = new JPanel();
		panel.setLayout(null);
		
		JLabel lblCreateOrder = new JLabel("Order");
		lblCreateOrder.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCreateOrder.setBounds(12, 13, 54, 27);
		panel.add(lblCreateOrder);
		
		btnCreateOrder = new JButton("Create Order");
		btnCreateOrder.setBounds(108, 194, 113, 25);
		btnCreateOrder.addActionListener(this);
		panel.add(btnCreateOrder);
		
		btnBackToClerk = new JButton("Back to Clerk Menu");
		btnBackToClerk.setBounds(126, 236, 157, 25);
		btnBackToClerk.addActionListener(this);
		southPanel.add(btnBackToClerk);
		
		frame.getContentPane().add(panel);
		
		lblNotifications = new JLabel("");
		lblNotifications.setBounds(78, 24, 360, 16);
		panel.add(lblNotifications);
		
		JLabel lblClientId = new JLabel("Client ID:");
		lblClientId.setBounds(36, 68, 56, 16);
		panel.add(lblClientId);
		
		txtClientidfield = new JTextField();
		txtClientidfield.setBounds(105, 65, 116, 22);
		panel.add(txtClientidfield);
		txtClientidfield.setColumns(10);
		
		JLabel lblProductId = new JLabel("Product ID:");
		lblProductId.setBounds(22, 110, 71, 16);
		panel.add(lblProductId);
		
		txtProductidfield = new JTextField();
		txtProductidfield.setBounds(105, 107, 116, 22);
		panel.add(txtProductidfield);
		txtProductidfield.setColumns(10);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setBounds(32, 151, 56, 16);
		panel.add(lblQuantity);
		
		txtQuantityfield = new JTextField();
		txtQuantityfield.setBounds(105, 148, 116, 22);
		panel.add(txtQuantityfield);
		txtQuantityfield.setColumns(10);
		
		lblErrornoteClientID = new JLabel("");
		lblErrornoteClientID.setBounds(233, 68, 205, 16);
		panel.add(lblErrornoteClientID);
		
		lblErrornoteproductid = new JLabel("");
		lblErrornoteproductid.setBounds(233, 110, 205, 16);
		panel.add(lblErrornoteproductid);
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
	}
	
	public void clear() {
		frame.getContentPane().removeAll();
		frame.paint(frame.getGraphics());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(btnCreateOrder)) {
			createOrder();
		}
		else if (arg0.getSource().equals(btnBackToClerk)) {
			System.out.println("back to clerk");
			clear();
			WarehouseUIContext.instance().changeState(1);
		}
		
	}
}
