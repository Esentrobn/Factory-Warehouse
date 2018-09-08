import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface {
    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_PRODUCT = 2;
    private static final int ADD_MANUFACTURER = 3;
    private static final int SHOW_CLIENTS = 4;
    private static final int SHOW_MANUFACTURER = 5;
    private static final int SAVE = 6;
    private static final int ASSIGN_PRODUCT = 7;
    private static final int UNASSIGN_PRODUCT = 8;
    private static final int SHOW_PRODUCTS = 9;
    private static final int MANUFACTURER_SPECIFIC_PRODUCT = 10;
    private static final int LIST_OF_PRODUCTS_BY_MANUFACTURER = 11;
    private static final int SHOW_WAITLISTED_ORDERS = 12;
    private static final int MENU = 20;
    private static final int ACCEPT_ORDER = 15;
    private static final int ACCEPT_PAYMENT = 16;
    private static final int LIST_OF_CLIENTS_WITH_BALANCE = 17;
    private static final int SHOW_ALL_ORDERS_AND_WAITLIST = 18;
    private static final int LIST_OF_ORDERS_BY_MANUFACTURER = 19;
    private static final int PLACE_AN_ORDER_WITH_A_MANUFACTURER = 13;
    private static final int RECEIVE_SHIPMENT = 14;

    private UserInterface() {
        if (yesOrNo("Look for saved data and use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
    }

    public static UserInterface instance() {
        if (userInterface == null) {
            return userInterface = new UserInterface();
        } else {
            return userInterface;
        }
    }

    // String prompt used to capture info.
    public String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
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

    // Integer prompt using token method.
    public int getInt(String prompt) {
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

    // Float prompt using token method.
    public double getDouble(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                double f = Double.parseDouble(item);
                return f;
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public void displayWaitlistedOrdersforProducts() {
        String productID;
        productID = getToken("Please, enter the product id to show waitlisted items for");
        Product product = warehouse.searchProduct(productID);
//        if (productID != null)
        		//Depricated function call
//            warehouse.displayWaitlistedForProduct(product);
//        else
//            System.out.println("Couldn't find the desired product");
    }

    // Yes or no prompt.
    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no: ");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }

    // Menu prompt.
    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("> "));
                if (value >= EXIT && value <= MENU) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void menu() {

        System.out.println(EXIT + " To exit the program");
        System.out.println(ADD_CLIENT + " To add a client");
        System.out.println(ADD_PRODUCT + " To add a product");
        System.out.println(ADD_MANUFACTURER + " To add a manufactuerer");
        System.out.println(SHOW_CLIENTS + " List all clients");
        System.out.println(SHOW_MANUFACTURER + " List all manufacturers");
        System.out.println(SAVE + " To save the program");
        System.out.println(MENU + " Go back to menu");
        System.out.println(ASSIGN_PRODUCT + " Assign a product");
        System.out.println(UNASSIGN_PRODUCT + " Unassign a product");
        System.out.println(SHOW_PRODUCTS + " Show all products");
        System.out.println(MANUFACTURER_SPECIFIC_PRODUCT + " List all products by manufacturer");
        System.out.println(SHOW_WAITLISTED_ORDERS + " Show all waitlisted orders");
        System.out.println(ACCEPT_ORDER + " Accept an order");
        System.out.println(ACCEPT_PAYMENT + " Accept a payment");
        System.out.println(LIST_OF_CLIENTS_WITH_BALANCE + " List all the clients with an outstanding balance");
        System.out.println(SHOW_ALL_ORDERS_AND_WAITLIST + " Show all orders");
        System.out.println(LIST_OF_ORDERS_BY_MANUFACTURER + " Show list of orders by manufacturer");
        System.out.println(PLACE_AN_ORDER_WITH_A_MANUFACTURER + " Place and order with a manufacturer");
        System.out.println(RECEIVE_SHIPMENT + " Receive shipment");
        //System.out.println(LIST_OF_PRODUCTS_BY_MANUFACTURER + " ");

    }

    // Capture tokens for adding a client.
    public void addClient() {
        String name = getToken("Enter client company: ");
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone: ");
        Client result;
        result = warehouse.addClient(name, address, phone);
        if (result == null) {
            System.out.println("Client could not be added.");
        }
        System.out.println(result);
    }

    // Capture tokens for adding a product.
    public void addProduct() {
        Product result;
        String prodName = getToken("Enter product name: ");
        int quantity = getInt("Enter quantity: ");
        double price = getDouble("Enter price per unit: $");
        result = warehouse.addProduct(prodName, quantity, price);
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Product could not be added.");
        }
    }

    // Capture tokens for adding a manufacturer.
    public void addManufacturer() {
        String name = getToken("Enter manufacturer company: ");
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone: ");
        Manufacturer result;
        result = warehouse.addManufacturer(name, address, phone);
        if (result == null) {
            System.out.println("Manufacturer could not be added.");
        }
        System.out.println(result);
    }

    // Queries.
    public void showClients() {
        Iterator allClients = warehouse.getClients();
        while (allClients.hasNext()) {
            Client client = (Client) (allClients.next());
            System.out.println(client.toString());
        }
    }

    public void createOrder() {

        System.out.println("TEST");
        String tempClient;
        int tempQuantity;
        char proceed;
        boolean match = false;
        Order createdOrder = new Order();
        String productStringId;
        String tempString;
        Product tempProduct;
        boolean addItemsToOrder;
        Order result;

        tempClient = getToken("Enter client id to create order for ");
        Iterator i = warehouse.getClients();

        while (i.hasNext()) {
            Client client = (Client) i.next();
            if (client.equal(tempClient)) {
                match = true;
            }

        }

        if (match) {

            do {

                productStringId = getToken("Enter First id of product to be added to the list: ");
                tempProduct = warehouse.searchProduct(productStringId);
            
                if (tempProduct != null) {

                    tempQuantity = Integer.parseInt(getToken("Enter the quantity of that item: "));

                    addItemsToOrder = createdOrder.insertlistedItem(tempProduct, tempQuantity);
                    if (!addItemsToOrder) {
                        System.out.println("Failed to add item to order");

                    } else
                        System.out.println("Added Item");

                } else {
                    System.out.println("Could not find item");
                }
               

                tempString = getToken("Continue adding items? Y to proceedinue: ");
                proceed = tempString.charAt(0);

                
            } while (proceed == 'y' || proceed == 'Y');

            System.out.println("Processing order");

            result = warehouse.processOrder(createdOrder, tempClient);

            if (result == null) {
                System.out.println("Could not add order");
            } else {
                System.out.println("Added product [" + result + "]");
                
                Iterator<OrderedItem> products = result.getOrderProducts();
                
                while(products.hasNext()) {
                	OrderedItem orderdItem = (OrderedItem)(products.next());
                	Product product = orderdItem.getProduct();
                	Iterator<Manufacturer> manufacturers = product.getManufacturers();
                	
                    while(manufacturers.hasNext()) {
                    	Manufacturer manufacturer = manufacturers.next();
                    	if(manufacturer != null) {
                        	manufacturer.addOrder(result);
                    	}
                    }
                }

            }
        } else
            System.out.println("Couldn't find client to associate");

    }
    
    public void receiveShipment() {
    	System.out.println("1 - Large group shipments");
    	System.out.println("2 - Small group shipments");
    	int typeSelection = getInt("Please select the type of shipment: ");
    	switch(typeSelection) {
	    	case 1:
	    		largeGroupShipment();
	    		break;
	    	case 2:
	    		smallGroupShipment();
	    		break;
    	}
    }
    
    public void largeGroupShipment() {
    	System.out.println("Large group");
    }
    
    public void smallGroupShipment() {
    	System.out.println("small group");
    }
    
    public void placeAnOrderWithManufacturer() {
    	String manufacturerID = getToken("Please enter the manufacturer ID ");
    	Manufacturer manufacturer = warehouse.searchManufacturer(manufacturerID);
    	Iterator<Product> productsByManufacturer = warehouse.getProductsFromManufacturer(manufacturerID);
    	if(productsByManufacturer != null) {
    		System.out.println("Products by " + manufacturer.getName());
    		Order newOrder = new Order();
    		boolean createWaitListFlag = false;
    		while(productsByManufacturer.hasNext()) {
    			Product product = productsByManufacturer.next();
    			System.out.println(product.toString());
    		}
    		do {
	    		String productID = getToken("Please enter the product ID of the product you want from the above list: ");
	    		Product product = manufacturer.getProduct(productID);
	    		while(product == null) {
	    			productID = getToken("Please enter the product ID of the product you want from the above list: ");
	    			product = manufacturer.getProduct(productID);
	    		}
	    		int orderQuantity = getInt("Please enter the quantity: ");
	    		if(orderQuantity > product.getQuantity()) {
	    			System.out.println("Your order is short of " + (orderQuantity - product.getQuantity()) + " " + (product.getName()) + "s. " + " These orders are added to the waitlist");
	    			createWaitListFlag = true;
	    		}
	    		newOrder.insertlistedItem(product, orderQuantity);
	            if (!yesOrNo("Do you want to add more products?")) {
	                break;
	            }
    		} while(true);
    		newOrder.setManufacturerReferenceId(manufacturerID);
            if(createWaitListFlag) {
            	newOrder.createWaitList();
            }
            warehouse.processOrder(newOrder);
    		System.out.println("Your order is being processed!");
    	}
    	else {
    		System.out.println("This manufacturer doesn't have any products assigned to it");
    	}
    }

    public void acceptPayment() {
        String clientID = getToken("Please enter a client id: ");
        Client client = warehouse.findClient(clientID);
        if (client != null) {
            if (client.getInvoicedAmount() > 0) {
                double balance = client.getInvoicedAmount();
                System.out.println("Client's outstanding balance is: " + balance);
                double amount = getDouble("Please enter the payment amount: ");
                while(amount > balance) {
                	amount = getDouble("Please enter an appropriate payment amount: ");
                }
                client.clearBalance(amount);
                System.out.println("Payment amount accepted!");
                double newBalance = client.getInvoicedAmount();
                System.out.println("Client's outstanding balance is: " + newBalance);
            } else {
                System.out.println("This client don't have an outstanding balance");
            }
        } else {
            System.out.println("Invalid client ID");
        }
    }
    
    public void listProductsByManufacturer() {
    	String manufacturerID = getToken("Please enter the manufacturer's ID ");
    	if(warehouse.searchManufacturer(manufacturerID) != null) {
    		Iterator products = warehouse.getProductsFromManufacturer(manufacturerID);
    		if(products != null) {
        		while(products.hasNext()) {
        			Product product = (Product)(products.next());
        			System.out.println(product.toString());
        		}
    		}
    		else {
    			System.out.println("This manufacturer doesn't have any products assigned yet");
    		}
    	}
    	else {
    		System.out.println("Invalid manufacturerID!");
    	}
    }
    
    public void listOrdersByManufacturer(){
    	String manufacturerID = getToken("Please enter the manufacturer's ID ");
    	if(warehouse.searchManufacturer(manufacturerID) != null) {
    		Iterator ordersByManufacturer = warehouse.getOrderByManufacturer(manufacturerID);
    		if(ordersByManufacturer != null) {
        		while(ordersByManufacturer.hasNext()) {
        			Order order = (Order)(ordersByManufacturer.next());
        			System.out.println(order.toString());
        		}
    		}
    		else {
    			System.out.println("This manufacturer doesn't have any orders associated with");
    		}
    	}
    	else {
    		System.out.println("Invalid manufacturerID!");
    	}
    }

    public void listClientsWithBalance() {
        Iterator clientsWithBalance = warehouse.getClientsWithOutstandingBalance();
        if(clientsWithBalance.hasNext()) {
            while (clientsWithBalance != null) {
                Client client = (Client) (clientsWithBalance.next());
                System.out.println(client.toString() + ", Outstanding Balance: " + client.getInvoicedAmount());
            }
        }
        else {
        	System.out.println("No clients with outstanding balance");
        }

    }
    

    public void showManufacturers() {
        Iterator allManufacturers = warehouse.getManufacturers();
        while (allManufacturers.hasNext()) {
            Manufacturer manufacturer = (Manufacturer) (allManufacturers.next());
            System.out.println(manufacturer.toString());
        }
    }

    public void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println(product.toString());
        }
    }

    private void save() {
        if (warehouse.save()) {
            System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            System.out.println(" There has been an error in saving \n");
        }
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesnt exist; creating new warehouse");
                warehouse = Warehouse.instance();
            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    private void assignProductToManufacturer() {
        Product result;
        String manufacturerID = getToken("Enter Manufacturer ID: ");
        if (warehouse.searchManufacturer(manufacturerID) == null) {
            System.out.println("No such Manufacturer!");
            return;
        }
        do {
            String productID = getToken("Enter product ID: ");
            result = warehouse.assignProduct(manufacturerID, productID);
            if (result != null) {
                System.out.println("Product [" + productID + "] assigned to Manufacturer: [" + manufacturerID + "]");
            } else {
                System.out.println("Product could not be assigned");
            }
            if (!yesOrNo("Assign more products to Manufacturer: [" + manufacturerID + "]")) {
                break;
            }
        } while (true);
    }

    private void unAssignProductFromManufacturer() {
        Product result;
        String manufacturerID = getToken("Enter Manufacturer ID: ");
        if (warehouse.searchManufacturer(manufacturerID) == null) {
            System.out.println("No such Manufacturer!");
            return;
        }
        do {
            String productID = getToken("Enter product ID: ");
            result = warehouse.unAssignProduct(manufacturerID, productID);
            if (result != null) {
                System.out.println("Product [" + productID + "] unassigned to Manufacturer: [" + manufacturerID + "]");
            } else {
                System.out.println("Product could not be unassigned");
            }
            if (!yesOrNo("Unassign more products to Manufacturer: [" + manufacturerID + "]")) {
                break;
            }
        } while (true);
    }


    public void listOrders(){
        
        Iterator order = warehouse.getOrders();

        while(order.hasNext()){
            Order myOrder = (Order) order.next();
            System.out.println(myOrder.toString());
        }


    }

    public void process() {
        int command;
        menu();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
            case ADD_CLIENT:
                addClient();
                break;

            case ADD_PRODUCT:
                addProduct();
                break;

            case ADD_MANUFACTURER:
                addManufacturer();
                break;

            case SHOW_CLIENTS:
                showClients();
                break;

            case SHOW_MANUFACTURER:
                showManufacturers();
                break;

            case SAVE:
                save();
                break;

            case MENU:
                menu();
                break;

            case SHOW_PRODUCTS:
                showProducts();
                break;

            case ASSIGN_PRODUCT:
                assignProductToManufacturer();
                break;

            case UNASSIGN_PRODUCT:
                unAssignProductFromManufacturer();
                break;

            case SHOW_WAITLISTED_ORDERS:
                displayWaitlistedOrdersforProducts();
                break;

            case ACCEPT_ORDER:
                createOrder();
                break;

            case ACCEPT_PAYMENT:
                acceptPayment();
                break;

            case LIST_OF_CLIENTS_WITH_BALANCE:
                listClientsWithBalance();
                break;
            case SHOW_ALL_ORDERS_AND_WAITLIST:
                listOrders();
                break;
            case LIST_OF_ORDERS_BY_MANUFACTURER:
            	listOrdersByManufacturer();
            	break;
            case MANUFACTURER_SPECIFIC_PRODUCT:
            	listProductsByManufacturer();
            	break;
            case PLACE_AN_ORDER_WITH_A_MANUFACTURER:
            	placeAnOrderWithManufacturer();
            	break;
            case RECEIVE_SHIPMENT:
            	receiveShipment();
            	break;
            }
        }
    }

    public static void main(String[] s) {
        UserInterface.instance().process();
    }
}