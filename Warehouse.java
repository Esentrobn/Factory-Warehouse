import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductList productList;
    private ClientList clientList;
    private ManufacturerList manufacturerList;
    private static Warehouse warehouse;
    private OrderList orderlist;
    // Instantiate the lists of objects.
    private Warehouse() {
        productList = ProductList.instance();
        clientList = ClientList.instance();
        orderlist = OrderList.instance();
        manufacturerList = ManufacturerList.instance();
    }

    // Get the server singleton instantiations.
    public static Warehouse instance() {
        if (warehouse == null) {
            ClientIdServer.instance();
            ProductIDServer.instance();
            OrderIdServer.instance();
            ManufacturerIDServer.instance();
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }
    
    public void acceptPaymentFromClient(Client client, double payment) {
        client.clearBalance(payment);
    }

    // Add a product to the warehouse.
    public Product addProduct(String prodName, int quantity, double price) {
        Product product = new Product(prodName, quantity, price);
        if (productList.insertProduct(product)) {
            return (product);
        }
        return null;
    }

    // Add a client to the warehouse.
    public Client addClient(String name, String address, String phone) {
        Client client = new Client(name);
        if (clientList.insertClient(client)) {
            return (client);
        }
        return null;
    }

    
    public Iterator WaitlistedForProduct(String productID) {
    	Product product = productList.search(productID);
    	return product.getWaitList();
    }

    public Product assignProduct(String manID, String productID) {

        System.out.println("Error 0");

        // Check if product with ID exists.
        Product product = productList.search(productID);
        if (product == null) {
            System.out.println("Error 1");
            return null;
        }

        // Check if supplier with ID exists.
        Manufacturer manufacturer = manufacturerList.search(manID);
        if (manufacturer == null) {
            System.out.println("Error 2");
            return null;
        }

        // Both objects exist, attempt to link them.
        if (!(product.assign(manufacturer) && manufacturer.assign(product))) {
            System.out.println("Error 3");
            return null;
        }
        System.out.println("Error 4");

        return product;
    }

    public void priceCheck(String productID){
		Product tempProduct = productList.search(productID);
		System.out.println(tempProduct.getCost());
	}

  

    public Order processOrder(Order order, String newClient, int RequesetedQuantity, Product product) {
    	
    	int availabeQuantity = product.getQuantity();
    	
    	if(RequesetedQuantity > availabeQuantity) {
    		int balance = RequesetedQuantity - availabeQuantity;
    		OrderedItem oi = new OrderedItem(product, balance);
    		oi.setOrderID(order.getId());
    		product.addWaitListItem(oi);
    	}

        Order newOrder = order;
        
        newOrder.setClientReferenceId(newClient);

        order.createWaitList();

        double total = newOrder.getTotals();
        clientList.chargeClient(newClient, total);
        if (orderlist.insertOrderNode(newOrder)) {
            return newOrder;
        } else {
            return null;

        }

    }

    public boolean changePrice(String productName, double newPrice) {
		Product tempProduct;
		tempProduct =productList.search(productName);
		
		if (tempProduct != null) {
			tempProduct.setCost(newPrice);
			return true;
		}
		
		return false;
	}
    
    public Order processOrder(Order order) {
        if (orderlist.insertOrderNode(order)) {
            return order;
        } else {
            return null;

        }
    }
    
   
    public Iterator getOrders(){
        return orderlist.getOrder();
    }

    public Product unAssignProduct(String manID, String productID) {

        Product product = productList.search(productID);
        if (product == null) {
            return null;
        }

        Manufacturer manufacturer = manufacturerList.search(manID);
        if (manufacturer == null) {
            return null;
        }

        if (!(product.unAssign(manufacturer) && manufacturer.unAssign(product))) {
            return null;
        }

        return product;
    }

    // Add a manufacturer to the warehouse.
    public Manufacturer addManufacturer(String name, String address, String phone) {
        Manufacturer manufacturer = new Manufacturer(name, address, phone);
        if (manufacturerList.insertManufacturer(manufacturer)) {
            return (manufacturer);
        }
        return null;
    }
    
    public void recieveShipment(String productID, int quantity) {
    	Product product = this.searchProduct(productID);
    	Iterator<OrderedItem> waitLists = warehouse.WaitlistedForProduct(productID);
    	int balanceQuantity = quantity;
    	if(waitLists.hasNext()) {
    		while(waitLists.hasNext()) {
    			System.out.println("above");
    			OrderedItem oi = waitLists.next();
    			if(balanceQuantity >= oi.quantity) {
    				System.out.println("here");
    				balanceQuantity -= oi.quantity;
    				oi.quantity = 0;
    				waitLists.remove();
    				product.setQuantity(balanceQuantity);
    				System.out.println("balanceQuantity: " + balanceQuantity);
    			}
//    			else if(oi.quantity >= balanceQuantity){
//    				balanceQuantity = oi.quantity - balanceQuantity;
//    				oi.quantity = balanceQuantity;
//    				balanceQuantity = 0;
//    			}
    		}
    	}
    	else {
    		product.setQuantity(quantity);
    	}
    }

    public Manufacturer searchManufacturer(String manufacturerID) {
        return manufacturerList.search(manufacturerID);
    }

    public Product searchProduct(String productID) {
        return productList.search(productID);
    }

    public Iterator getClients() {
        return clientList.getClients();
    }
    
    public Iterator getClientsWithOutstandingBalance() {
    	return clientList.getClientsWithBalance();
    }
    
    public Client findClient(String clientID) {
    	return clientList.search(clientID);
    }

    public Iterator getManufacturers() {
        return manufacturerList.getManufacturer();
    }

    public Iterator getProducts() {
        return productList.getProduct();
    }
    
    public Client searchMembership(String userID) {
		
		Client tempClient = clientList.search(userID);
		return tempClient;
	}

    public Iterator getProductsFromManufacturer(String manufacturerID) {
    	Manufacturer manufacturer = manufacturerList.search(manufacturerID);
    	if(manufacturer.isManufacturersOfProductEmpty()) {
    		return null;
    	}
    	else {
        	return manufacturer.getProducts();
    	}
    }
    
    public Iterator getOrderByManufacturer(String manufacturerID) {
    	Manufacturer manufacturer = manufacturerList.search(manufacturerID);
    	if(manufacturer.isOrdersEmpty()) {
    		return null;
    	}
    	else {
    		return manufacturer.getOrders();
    	}
    }

    public static Warehouse retrieve() {
        try {
            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream input = new ObjectInputStream(file);
            input.readObject();
            ClientIdServer.retrieve(input);
            ProductIDServer.retrieve(input);
            ManufacturerIDServer.retrieve(input);
            return warehouse;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    public static boolean save() {
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(warehouse);
            output.writeObject(ClientIdServer.instance());
            output.writeObject(ProductIDServer.instance());
            output.writeObject(ManufacturerIDServer.instance());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public void displayClients() {
		System.out.println(clientList);
	}

	public void displayProducts() {
		System.out.println(productList);
	}

	public void displayManufacturers() {
		System.out.println(manufacturerList);
	}

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return productList + "\n" + clientList;
    }
}