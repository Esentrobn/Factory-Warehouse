import java.io.Serializable;
import java.util.*;
import java.io.*;

public class Product implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String PRODUCT_STRING = "P";
    private List productManufacturers = new LinkedList();
    private String productName;
    private String id;
    private double cost;
    private int quantity;  
    private LinkedList<OrderedItem> waitList = new LinkedList<OrderedItem>();
    public Product(String productName,int quantity, double cost)
    {
        this.cost = cost;
        this.productName = productName;
        this.quantity = quantity;
        this.id = PRODUCT_STRING + (ProductIDServer.instance()).getID();
    }
    
    public void addWaitListItem(OrderedItem item) {
    	try {
    		waitList.add(item);
    		System.out.println("in try");
    	}
    	catch(NullPointerException e) {
    		System.out.println("in catch");
    		waitList = new LinkedList<OrderedItem>();
    		waitList.add(item);
    	}
    }
    
    public Iterator getWaitList() {
    	return waitList.iterator();
    }

    public String getProductName()
    {
        return productName;
    }

    public String getId(){
        return this.id;
    }

    public double getCost() {
		return this.cost;
    }
    
    public void setCost(double cost){
        this.cost = cost;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
  
    public boolean equals(String id) 
    {
        return this.id.equals(id);
    }
    
    public String getID() 
    {
		return id;
    }
        
    public String toString() 
    {
        return "Product name: " + productName + ", id: " + id + ", Quantity: " + quantity;

    }

	public String getName() {
		return this.productName;
	}
	
	public Iterator getManufacturers() {
		return productManufacturers.iterator();
	}


    public boolean assign(Manufacturer manufacturer) {
        return productManufacturers.add(manufacturer) ? true : false;
    }

    public boolean unAssign(Manufacturer manufacturer) {
        return productManufacturers.remove(manufacturer);
    }
}
