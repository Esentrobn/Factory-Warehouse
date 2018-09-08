import java.util.*;
import java.io.*;

public class Manufacturer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private String phone;
    private String id;
    private List<Product> ManufacturersOfProduct = new LinkedList<Product>();
    private static List<Order> orders = new LinkedList<Order>();
    private static final String Manufacturer_STRING = "S";

    public Manufacturer(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        id = Manufacturer_STRING + (ManufacturerIDServer.instance()).getID();
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String nName) {
        name = nName;
    }

    public String getID() {
        return id;
    }

    public void setAddress(String nAddress) {
        address = nAddress;
    }

    public void setPhone(String nPhone) {
        phone = nPhone;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public boolean assign(Product product) {
        return ManufacturersOfProduct.add(product);
    }

    public boolean unAssign(Product product) {
        return ManufacturersOfProduct.remove(product);
    }
    
    public Iterator getProducts() {
    	return ManufacturersOfProduct.iterator();
    }
    
    public Product getProduct(String productID) {
        for (Iterator<Product> iterator = ManufacturersOfProduct.iterator(); iterator.hasNext(); )
        {
            Product product = iterator.next();
            if (product.getID().equals(productID)) 
            {
                return product;
            }
        }
        return null;
    }
    
    public boolean isManufacturersOfProductEmpty() {
    	return ManufacturersOfProduct.isEmpty();
    }
    
    public boolean addOrder(Order order) {
    	return orders.add(order);
    }
    
    public boolean removeOrder(Order order) {
    	return orders.remove(order);
    }
    
    public Iterator getOrders() {
    	return orders.iterator();
    }
    
    public boolean isOrdersEmpty() {
    	return orders.isEmpty();
    }


    public String toString() {
        String string = "Manufacturer name " + name + " address " + address + " id " + id + "phone " + phone;
        return string;
    }
}
