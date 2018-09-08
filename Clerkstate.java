import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;

public class Clerkstate extends WareHouseUIState implements ActionListener{
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;
	private WarehouseUIContext context;
	private Client clientForPayment;
	
	private JFrame frame;
	private JPanel southButtons;
	private JPanel northButtons;
	
	private JTabbedPane tabbedPane;
	
	private AbstractButton loadDatabaseButton, logoutButton;
	
	private JPanel addClientPanel, addProductPanel,
	showClientPanel, showProductFrame,
	showManufacturersPanel, recievePaymentsPanel, showWaitlistedOrdersPanel;
	

	private static Clerkstate instance;

	private static final int EXIT = 0;
	private static final int addClient = 1;
	private static final int addProducts = 2;

	private static final int showClients = 3;
	private static final int showProducts = 4;
	private static final int showManufacturers = 6;

	private static final int recieveOrder = 7;
	private static final int recievePayment = 8;
	private static final int showWaitlistedOrders = 9;

	private static final int makeOrderForClient = 11;
	private static final int makeClient = 12;
	private static final int loadDatabase = 13;

	private static final int HELP = 911;
	private JLabel lblClientName;
	private JLabel lblAddress;
	private JLabel lblPhone;
	private JTextField nameTextField;
	private JTextField addressTextField;
	private JTextField phoneTextField;
	private JButton btnAddClient;
	private JLabel lblClientName_1;
	private JLabel lblAddress_1;
	private JLabel lblPhone_1;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPhone;
	private JLabel lblclientInfotext;
	private JLabel lblProductName;
	private JLabel lblQuantity;
	private JLabel lblPrice;
	private JTextField txtProductname;
	private JTextField txtQuantity;
	private JTextField txtPrice;
	private JButton btnAddProduct;
	private JLabel lblProductinfo;
	private JLabel lblClient;
	private JLabel lblSelectedClientName;
	private JLabel lblInvoiceAmount;
	private JLabel lblClientinvoiceamt;
	private JLabel lblPayment;
	private JTextField txtPayment;
	private JButton btnProcessPayment;
	private JList list;
	private JList clientGUIList;
	private JLabel lblClientName_2;
	private JLabel lblInvoiceAmount_1;
	private JLabel lblClientname;
	private JLabel lblInvoiceamt;
	private JList productJList;
	private JLabel lblProductName_1;
	private JLabel lblPerUnitPrice;
	private JLabel lblQunatity;
	private JLabel lblProductname;
	private JLabel lblCost;
	private JLabel lblQuantity_1;
	private JLabel lblManufacturer;
	private JLabel lblManufacturername;
	private JButton saveButton;
	private JButton recieveOrderButton;
	private JButton makeOrderForClientButton;
	private JLabel southButtonsInfoLabel;
	
	private DefaultListModel productListModel;
	private DefaultListModel clientLM;
	private JList OrdersListOnWaitlist;
	private JList ProductListOnWaitList;
	private JLabel lblProductlist;
	private JLabel lblWaitlistedOrders;
	private JLabel lblProductIds;
	private JLabel lblManufacturerIds;
	private JLabel lblClientIds;

	private Clerkstate() {
		super();
		warehouse = Warehouse.instance();
	}

	public static Clerkstate instance() {
		if (instance == null) {
			instance = new Clerkstate();
		}
		return instance;
	}

	public String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
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

	public int getNumber(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				Integer num = Integer.valueOf(item);
				return num.intValue();
			} catch (NumberFormatException nfe) {
				System.out.println("Please input a number ");
			}
		} while (true);
	}

	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number");
			}
		} while (true);
	}
	
	private void SecurityCheck() {
		WarehouseUIContext.instance().changeState(4);
	}
	 
	public void help() {
		System.out.println("Enter a number between 0 and 99 as explained below:");
		System.out.println(EXIT + " to exit");
		System.out.println(addClient + " to add client");
		System.out.println(addProducts + " to add products");
		System.out.println();

		System.out.println(showClients + " to show clients");
		System.out.println(showProducts + " to show products");
		System.out.println(showManufacturers + " to show manufacturers");
		System.out.println();

		System.out.println(recieveOrder + " to recieve order");
		System.out.println(recievePayment + " to recieve payment");
		System.out.println(showWaitlistedOrders + " to show waitlisted orders");
		System.out.println();

		System.out.println(makeOrderForClient + " to create an order for a client");

		System.out.println(makeClient + " to change to client menu");
		System.out.println(loadDatabase + " to load database");
		System.out.println();

		System.out.println(HELP + " to display help menu");
	}

	public void usermenu() {
		String userID = getToken("Please input the user id: ");
		if (Warehouse.instance().searchMembership(userID) != null) {
			(WarehouseUIContext.instance()).setUser(userID);
			(WarehouseUIContext.instance()).changeState(1);
		} else
			System.out.println("Invalid user id.");
	}

	public void logout() {
		if (WarehouseUIContext.instance().getLogin() == WarehouseUIContext.IsManager) {
			WarehouseUIContext.instance().changeState(3);
		} else if ((WarehouseUIContext.instance()).getLogin() == WarehouseUIContext.IsClerk) {
			WarehouseUIContext.instance().changeState(0); // exit with a code 0
		} else {
			WarehouseUIContext.instance().changeState(2); // exit code 2, indicates error
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void process() {
		
		frame = WarehouseUIContext.instance().getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
		
		
		southButtons = new JPanel();
		northButtons = new JPanel();
		
		tabbedPane = new JTabbedPane();
		
		addClientPanel = new JPanel();
		addProductPanel = new JPanel();
		showClientPanel = new JPanel();
		showProductFrame = new JPanel();
		showManufacturersPanel = new JPanel();
		recievePaymentsPanel = new JPanel();
		showWaitlistedOrdersPanel = new JPanel();

		
		tabbedPane.addTab("Add Client", addClientPanel);
		setupAddClientMenu();
		
		tabbedPane.addTab("Add Product", addProductPanel);
		setupAddProductsMenu();
		
		tabbedPane.addTab("Show Client", showClientPanel);
		setupShowClientsMenu();
		
		tabbedPane.addTab("Show Products", showProductFrame);
		showProducts();
		
		tabbedPane.addTab("Show Manufacturers", showManufacturersPanel);
		showManufacturers();
		
		tabbedPane.addTab("Payments", recievePaymentsPanel);
		setupRecievePaymentMenu();
		
		tabbedPane.addTab("Waitlisted Orders", showWaitlistedOrdersPanel);
		showWaitlistedOrders();

		frame.getContentPane().add(tabbedPane);
		
		recieveOrderButton = new JButton("Recieve Order");
		recieveOrderButton.addActionListener(this);
		
		makeOrderForClientButton = new JButton("Create Order");
		makeOrderForClientButton.addActionListener(this);
		
		northButtons.add(recieveOrderButton);
		northButtons.add(makeOrderForClientButton);
		
		
		southButtonsInfoLabel = new JLabel();
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		loadDatabaseButton = new JButton("Load Database");
		loadDatabaseButton.addActionListener(this);
		
		logoutButton= new JButton("Logout");
		logoutButton.addActionListener(this);
		
		southButtons.add(southButtonsInfoLabel);
		southButtons.add(saveButton);
		southButtons.add(loadDatabaseButton); 
		southButtons.add(logoutButton);
		
		frame.getContentPane().add(northButtons, BorderLayout.NORTH);
		frame.getContentPane().add(southButtons, BorderLayout.SOUTH);
		
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
		
//		int command;
//		help();
//		while ((command = getCommand()) != EXIT) {
//			switch (command) {
//			case recieveOrder:
//				WarehouseUIContext.instance().changeState(6);
//				break;
//			case makeOrderForClient:
//				WarehouseUIContext.instance().changeState(5);
//				break;
//			}
//		}
	}
	
	public void clear() {
		frame.getContentPane().removeAll();
		frame.paint(frame.getGraphics());
	}
	  
	private void setupAddClientMenu() {
			addClientPanel.setLayout(null);
			
			lblClientName_1 = new JLabel("Client Name");
			lblClientName_1.setBounds(138, 79, 80, 16);
			addClientPanel.add(lblClientName_1);
			
			lblAddress_1 = new JLabel("Address");
			lblAddress_1.setBounds(138, 108, 56, 16);
			addClientPanel.add(lblAddress_1);
			
			lblPhone_1 = new JLabel("Phone");
			lblPhone_1.setBounds(138, 137, 56, 16);
			addClientPanel.add(lblPhone_1);
			
			txtName = new JTextField();
			txtName.setBounds(230, 76, 116, 22);
			addClientPanel.add(txtName);
			txtName.setColumns(10);
			
			txtAddress = new JTextField();
			txtAddress.setBounds(230, 105, 116, 22);
			addClientPanel.add(txtAddress);
			txtAddress.setColumns(10);
			
			txtPhone = new JTextField();
			txtPhone.setBounds(230, 134, 116, 22);
			addClientPanel.add(txtPhone);
			txtPhone.setColumns(10);
			
			btnAddClient = new JButton("Add");
			btnAddClient.addActionListener(this);
			btnAddClient.setBounds(249, 167, 97, 25);
			addClientPanel.add(btnAddClient);
			
			lblclientInfotext = new JLabel("");
			lblclientInfotext.setBounds(153, 205, 168, 16);
			addClientPanel.add(lblclientInfotext);
	  }

	private void addClient() {
		Client result;
		String name = txtName.getText();
		String address = txtAddress.getText();
		String phone = txtPhone.getText();
		System.out.println(name + " " + address  + " " + phone);
		if(!name.equals("") && !address.equals("") && !phone.equals("")) {
			result = warehouse.addClient(name, address ,phone);
			if (result == null) {
				lblclientInfotext.setText("Failed to add client, canceling input");
				txtName.setText("");
				txtAddress.setText("");
				txtPhone.setText("");
			}
			lblclientInfotext.setText("Added client " + txtName.getText());
			clientLM.addElement(result.getID());
		}
		else {
			lblclientInfotext.setText("Please fill all the fields");
		}

	}
	
	private void setupAddProductsMenu() {
		addProductPanel.setLayout(null);
		
		lblProductName = new JLabel("Product Name");
		lblProductName.setBounds(139, 79, 90, 16);
		addProductPanel.add(lblProductName);
		
		lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(139, 108, 56, 16);
		addProductPanel.add(lblQuantity);
		
		lblPrice = new JLabel("Price");
		lblPrice.setBounds(139, 137, 56, 16);
		addProductPanel.add(lblPrice);
		
		txtProductname = new JTextField();
		txtProductname.setBounds(230, 76, 116, 22);
		addProductPanel.add(txtProductname);
		txtProductname.setColumns(10);
		
		txtQuantity = new JTextField();
		txtQuantity.setText("");
		txtQuantity.setBounds(230, 105, 116, 22);
		addProductPanel.add(txtQuantity);
		txtQuantity.setColumns(10);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(230, 134, 116, 22);
		addProductPanel.add(txtPrice);
		txtPrice.setColumns(10);
		
		btnAddProduct = new JButton("Add");
		btnAddProduct.setBounds(249, 169, 97, 25);
		btnAddProduct.addActionListener(this);
		addProductPanel.add(btnAddProduct);
		
		lblProductinfo = new JLabel("");
		lblProductinfo.setBounds(154, 207, 174, 16);
		addProductPanel.add(lblProductinfo);
	}

	private void addProducts() {
		String name = txtProductname.getText();
		int quantity = Integer.parseInt(txtQuantity.getText());
		double cost = Double.parseDouble(txtPrice.getText());
		Product result;

		if(!name.equals("") && !txtQuantity.getText().equals("") && !txtPrice.getText().equals("")) {
			result = warehouse.addProduct(name, quantity, cost);
			if (result == null) {
				lblProductinfo.setText("Failed to add client, canceling input");
				txtProductname.setText("");
				txtQuantity.setText("");
				txtPrice.setText("");
			}
			lblProductinfo.setText("Added product [" + name + "]");
			productListModel.addElement(result.getId());
		}
		else {
			lblProductinfo.setText("Please fill all the fields");
		}
	}

	private void setupShowClientsMenu() {
		showClientPanel.setLayout(null);
		
		clientLM = new DefaultListModel();
		
		Iterator<Client> clients = warehouse.getClients();
		while(clients.hasNext()) {
			Client c = clients.next();
			clientLM.addElement(c.getID());
		}

		clientGUIList = new JList(clientLM);
		clientGUIList.setBounds(12, 32, 162, 231);
		showClientPanel.add(clientGUIList);
		
		lblClientName_2 = new JLabel("Client Name");
		lblClientName_2.setBounds(199, 14, 89, 16);
		showClientPanel.add(lblClientName_2);
		
		lblInvoiceAmount_1 = new JLabel("Invoice Amount");
		lblInvoiceAmount_1.setBounds(199, 43, 89, 16);
		showClientPanel.add(lblInvoiceAmount_1);
		
		lblClientname = new JLabel("");
		lblClientname.setBounds(300, 13, 152, 16);
		showClientPanel.add(lblClientname);
		
		lblInvoiceamt = new JLabel("");
		lblInvoiceamt.setBounds(300, 43, 152, 16);
		showClientPanel.add(lblInvoiceamt);
		
		lblClientIds = new JLabel("Client IDs");
		lblClientIds.setBounds(12, 14, 56, 16);
		showClientPanel.add(lblClientIds);
		
		clientGUIList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Client client = warehouse.findClient(clientGUIList.getSelectedValue().toString());
				lblClientname.setText(client.getName());
				double invoiceAmt = client.getInvoicedAmount();
				lblInvoiceamt.setText(String.valueOf(invoiceAmt));
			}
		});
	}
	
	private void showClients() {
		warehouse.displayClients();
	}

	private void showProducts() {
		
		showProductFrame.setLayout(null);
		
		productListModel = new DefaultListModel();
		
		Iterator<Product> products = warehouse.getProducts();
		while(products.hasNext()) {
			Product p = products.next();
			productListModel.addElement(p.getId());
		}
		
		productJList = new JList(productListModel);
		productJList.setBounds(12, 32, 162, 231);
		showProductFrame.add(productJList);
		
		lblProductName_1 = new JLabel("Product Name");
		lblProductName_1.setBounds(200, 13, 101, 16);
		showProductFrame.add(lblProductName_1);
		
		lblPerUnitPrice = new JLabel("Per Unit Price");
		lblPerUnitPrice.setBounds(200, 42, 101, 16);
		showProductFrame.add(lblPerUnitPrice);
		
		lblQunatity = new JLabel("Qunatity");
		lblQunatity.setBounds(200, 71, 101, 16);
		showProductFrame.add(lblQunatity);
		
		lblProductname = new JLabel("");
		lblProductname.setBounds(313, 13, 162, 16);
		showProductFrame.add(lblProductname);
		
		lblCost = new JLabel("");
		lblCost.setBounds(313, 42, 162, 16);
		showProductFrame.add(lblCost);
		
		lblQuantity_1 = new JLabel("");
		lblQuantity_1.setBounds(313, 71, 162, 16);
		showProductFrame.add(lblQuantity_1);
		
		lblProductIds = new JLabel("Product IDs");
		lblProductIds.setBounds(12, 13, 83, 16);
		showProductFrame.add(lblProductIds);
		
		productJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Product product = warehouse.searchProduct(productJList.getSelectedValue().toString());
				lblProductname.setText(product.getName());
				lblQuantity_1.setText(String.valueOf(product.getQuantity()));
				lblCost.setText(String.valueOf(product.getCost()));
			}
		});
	}

	private void showManufacturers() {
		showManufacturersPanel.setLayout(null);
		
		DefaultListModel manufacturerLM = new DefaultListModel();
		
		Iterator<Manufacturer> manufacturers = warehouse.getManufacturers();
		while(manufacturers.hasNext()) {
			Manufacturer manufactuer = manufacturers.next();
			manufacturerLM.addElement(manufactuer.getID());
		}
		
		JList manufacturerGUIList = new JList(manufacturerLM);
		manufacturerGUIList.setBounds(12, 32, 162, 231);
		showManufacturersPanel.add(manufacturerGUIList);
		
		JLabel lblManufacturerName = new JLabel("Manufacturer Name");
		lblManufacturerName.setBounds(199, 14, 127, 16);
		showManufacturersPanel.add(lblManufacturerName);
		
		JLabel lblManufacturerAddress = new JLabel("Address");
		lblManufacturerAddress.setBounds(199, 43, 127, 16);
		showManufacturersPanel.add(lblManufacturerAddress);
		
		JLabel lblPhone_2 = new JLabel("Phone");
		lblPhone_2.setBounds(199, 72, 56, 16);
		showManufacturersPanel.add(lblPhone_2);
		
		JLabel lblPrintmanufacturername = new JLabel("");
		lblPrintmanufacturername.setBounds(338, 14, 160, 16);
		showManufacturersPanel.add(lblPrintmanufacturername);
		
		JLabel lblPrintmanufactureraddress = new JLabel("");
		lblPrintmanufactureraddress.setBounds(338, 43, 148, 16);
		showManufacturersPanel.add(lblPrintmanufactureraddress);
		
		JLabel lblPrintmanufactuerphone = new JLabel("");
		lblPrintmanufactuerphone.setBounds(338, 72, 148, 16);
		showManufacturersPanel.add(lblPrintmanufactuerphone);
		
		lblManufacturerIds = new JLabel("Manufacturer IDs");
		lblManufacturerIds.setBounds(12, 14, 133, 16);
		showManufacturersPanel.add(lblManufacturerIds);
		
		manufacturerGUIList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Manufacturer m = warehouse.searchManufacturer(manufacturerGUIList.getSelectedValue().toString());
				lblPrintmanufacturername.setText(m.getName());
				lblPrintmanufactureraddress.setText(m.getAddress());
				lblPrintmanufactuerphone.setText(m.getPhone());
			}
		});
	}

	private void recieveOrder() {
		String productName;
		productName = getToken("Enter Product id to recieve");

		if (warehouse.searchProduct(productName) != null) {
			String productQuantity = getToken("Enter amount to receive");
            //After writing code to recieve the product
            //warehouse.recieveProduct(productName, Integer.parseInt(productQuantity), reader);

		} else {
			System.out.println("Couldn't find product");

		}
	}
	
	private void setupRecievePaymentMenu() {
		recievePaymentsPanel.setLayout(null);
		
		DefaultListModel clientLM = new DefaultListModel();
		
		Iterator<Client> clients = warehouse.getClients();
		while(clients.hasNext()) {
			Client c = clients.next();
			clientLM.addElement(c.getID());
		}
		
		list = new JList(clientLM);
		list.setBounds(12, 13, 162, 250);
		recievePaymentsPanel.add(list);
		
		lblClient = new JLabel("Client:");
		lblClient.setBounds(213, 58, 56, 16);
		recievePaymentsPanel.add(lblClient);
		
		lblSelectedClientName = new JLabel("");
		lblSelectedClientName.setBounds(318, 58, 152, 16);
		recievePaymentsPanel.add(lblSelectedClientName);
		
		lblInvoiceAmount = new JLabel("Invoice Amount:");
		lblInvoiceAmount.setBounds(213, 87, 104, 16);
		recievePaymentsPanel.add(lblInvoiceAmount);
		
		lblClientinvoiceamt = new JLabel("");
		lblClientinvoiceamt.setBounds(324, 86, 146, 16);
		recievePaymentsPanel.add(lblClientinvoiceamt);
		
		lblPayment = new JLabel("Payment");
		lblPayment.setBounds(213, 140, 56, 16);
		recievePaymentsPanel.add(lblPayment);
		
		txtPayment = new JTextField();
		txtPayment.setBounds(281, 137, 147, 22);
		recievePaymentsPanel.add(txtPayment);
		txtPayment.setColumns(10);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				clientForPayment = warehouse.findClient(list.getSelectedValue().toString());
				System.out.println("Selected item " + clientForPayment.toString());
				lblSelectedClientName.setText(clientForPayment.getName());
				double invoiceAmt = clientForPayment.getInvoicedAmount();
				lblClientinvoiceamt.setText(String.valueOf(invoiceAmt));
			}
		});
		
		btnProcessPayment = new JButton("Process Payment");
		btnProcessPayment.setBounds(281, 180, 147, 25);
		btnProcessPayment.addActionListener(this);
		recievePaymentsPanel.add(btnProcessPayment);
	}
	
	private void recievePayment() {
		double payment = Double.parseDouble(txtPayment.getText());
		warehouse.acceptPaymentFromClient(clientForPayment, payment);	
		txtPayment.setText("");
		lblClientinvoiceamt.setText(String.valueOf(clientForPayment.getInvoicedAmount()));
	}

	private void showWaitlistedOrders() {
		
		
		showWaitlistedOrdersPanel.setLayout(null);
		
		OrdersListOnWaitlist = new JList();
		OrdersListOnWaitlist.setBounds(124, 32, 279, 231);
		showWaitlistedOrdersPanel.add(OrdersListOnWaitlist);
		
		ProductListOnWaitList = new JList(productListModel);
		ProductListOnWaitList.setBounds(12, 32, 100, 231);
		showWaitlistedOrdersPanel.add(ProductListOnWaitList);
		
		lblProductlist = new JLabel("Product IDs");
		lblProductlist.setBounds(12, 13, 80, 16);
		showWaitlistedOrdersPanel.add(lblProductlist);
		
		lblWaitlistedOrders = new JLabel("Waitlisted Order");
		lblWaitlistedOrders.setBounds(124, 13, 127, 16);
		showWaitlistedOrdersPanel.add(lblWaitlistedOrders);
		String productId;
		ProductListOnWaitList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String productID = ProductListOnWaitList.getSelectedValue().toString();
				Iterator<OrderedItem> waitLists = warehouse.WaitlistedForProduct(productID);
				DefaultListModel waitlistedOrdersLM = new DefaultListModel();
				if(waitLists.hasNext()) {
					while(waitLists.hasNext()) {
						OrderedItem oi = waitLists.next();
						waitlistedOrdersLM.addElement(oi.orderID + ", Product Name: " + oi.product.getName()+ ", Quantity: " + oi.quantity);
					}
				}
				else {
					waitlistedOrdersLM.addElement("No Waitlist");
				}
				
				OrdersListOnWaitlist.setModel(waitlistedOrdersLM);

			}
		});

	}

	private void makeClient() {
		String clientID = getToken("Please input the client id: ");
		if (warehouse.searchMembership(clientID) != null) {
			(WarehouseUIContext.instance()).setUser(clientID);
			(WarehouseUIContext.instance()).changeState(1);
		} else
			System.out.println("Invalid client id.");
	}

	private void loadDatabase() {
		WarehouseUIContext.instance().load();

	}

	public void run() {
		process();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(this.btnAddProduct)) {
			addProducts();
		}
		else if(arg0.getSource().equals(this.btnAddClient)) {
			addClient();
		}
		else if(arg0.getSource().equals(this.btnProcessPayment)) {
			recievePayment();
		}
		else if(arg0.getSource().equals(this.saveButton)) {
			warehouse.save();
			Date newDate = new Date();
			southButtonsInfoLabel.setText("Last Saved: " + new SimpleDateFormat("h:mm a").format(newDate));
		}
		else if(arg0.getSource().equals(this.loadDatabaseButton)) {
			warehouse.retrieve();
			southButtonsInfoLabel.setText("Database Reloaded");
		}
		else if(arg0.getSource().equals(this.makeOrderForClientButton)) {
			clear();
			WarehouseUIContext.instance().changeState(5);
		}
		else if(arg0.getSource().equals(this.recieveOrderButton)) {
			clear();
			WarehouseUIContext.instance().changeState(6);
		}
		else if(arg0.getSource().equals(this.logoutButton)) {
			clear();
			logout();
		}
		
	}
}
