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


public class ReceiveOrderState extends WareHouseUIState implements ActionListener{

	private static ReceiveOrderState instance;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;
	private JFrame frame;
	private JPanel southFrame;
	private JButton btnBackToClerk;
	private JTextField txtProductidfield;
	private JTextField txtQuantityfield;
	private JButton btnAddShipment;
	private JLabel lblErrorfieldproductid;
	private JLabel lblNotificationfield;
	
	public ReceiveOrderState(){
		super();
		warehouse = Warehouse.instance();
	}
	
	@Override
	public void run() {
		recieveOrder();

	}
	
	public static ReceiveOrderState instance(){
		if(instance == null){
			instance = new ReceiveOrderState();
		}
		return instance;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void recieveOrder() {
		
		frame = WarehouseUIContext.instance().getFrame();
		
		JPanel panel = new JPanel();
		southFrame = new JPanel();

		panel.setLayout(null);
		
		btnBackToClerk = new JButton("Back to Clerk Menu");
		southFrame.add(btnBackToClerk);
		btnBackToClerk.addActionListener(this);
		JLabel lblRecieveShipment = new JLabel("Recieve Shipment");
		lblRecieveShipment.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblRecieveShipment.setBounds(12, 13, 171, 25);
		panel.add(lblRecieveShipment);
		
		frame.getContentPane().add(southFrame, BorderLayout.SOUTH);
		frame.getContentPane().add(panel);
		
		JLabel lblProductId = new JLabel("Product ID:");
		lblProductId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductId.setBounds(22, 62, 72, 16);
		panel.add(lblProductId);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuantity.setBounds(22, 91, 72, 16);
		panel.add(lblQuantity);
		
		txtProductidfield = new JTextField();
		txtProductidfield.setBounds(112, 59, 116, 22);
		panel.add(txtProductidfield);
		txtProductidfield.setColumns(10);
		
		txtQuantityfield = new JTextField();
		txtQuantityfield.setBounds(112, 88, 116, 22);
		panel.add(txtQuantityfield);
		txtQuantityfield.setColumns(10);
		
		lblErrorfieldproductid = new JLabel("");
		lblErrorfieldproductid.setBounds(240, 62, 198, 16);
		panel.add(lblErrorfieldproductid);
		
		lblNotificationfield = new JLabel("");
		lblNotificationfield.setBounds(195, 20, 243, 16);
		panel.add(lblNotificationfield);
		
		btnAddShipment = new JButton("Add Shipment");
		btnAddShipment.setBounds(112, 123, 116, 25);
		btnAddShipment.addActionListener(this);
		panel.add(btnAddShipment);
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
	}
	
	private void addShipment() {
		String productID;
		productID = this.txtProductidfield.getText();
		if (warehouse.searchProduct(productID) != null) {
			int productQuantity = Integer.parseInt(this.txtQuantityfield.getText());
			warehouse.recieveShipment(productID, productQuantity);
			this.lblNotificationfield.setText("Shipment successfully filled");
			this.txtProductidfield.setText("");
			this.txtQuantityfield.setText("");
	
		} else {
			this.lblErrorfieldproductid.setText("Couldn't find product");
		}
	}
	
	public void clear() {
		frame.getContentPane().removeAll();
		frame.paint(frame.getGraphics());
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(btnBackToClerk)) {
			clear();
			WarehouseUIContext.instance().changeState(1);
		}
		else if(arg0.getSource().equals(btnAddShipment)) {
			addShipment();
			warehouse.save();
		}
		
	}
}
