//Ananda CSCI 430
import java.io.Serializable;
import java.util.*;
import java.io.*;


public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final String Order_STRING = "O";
	private String id;
	private List<OrderedItem> orderItems = new LinkedList<OrderedItem>();
    private List<OrderedItem> waitlistItems = new LinkedList<OrderedItem>();
	private String clientReferenceId;
	private String manufacturerReferenceID;

    public Order(){
        this.id = Order_STRING + (OrderIdServer.instance()).getId();
    }
    
    public void createWaitList() {

		Iterator it = getOrderProducts();

		while (it.hasNext()) {
			OrderedItem orderItem = (OrderedItem) it.next();
			int oldProductQuantity = orderItem.product.getQuantity();

			if (orderItem.product.getQuantity() >= orderItem.quantity) {
				orderItem.product.setQuantity(orderItem.product.getQuantity() - orderItem.quantity);

			}

			else if (orderItem.product.getQuantity() < orderItem.quantity) {
				int tempQuantityFornewWaitlist = orderItem.quantity - orderItem.product.getQuantity();
				OrderedItem newWaitListedItem = new OrderedItem(orderItem.product, tempQuantityFornewWaitlist);
				newWaitListedItem.setOrderID(this.id);
				waitlistItems.add(newWaitListedItem);
				orderItem.quantity = oldProductQuantity;
				orderItem.product.setQuantity(0);
			}
		}
    }

    public String getClientReferenceId() {
		return manufacturerReferenceID;
	}
    
    public String getManufacturerReferenceId() {
		return clientReferenceId;
	}

	public String getId() {
		return id;
	}

	public Iterator getOrderProducts() {
		return orderItems.iterator();
	}
    
    public List<OrderedItem> getWaitlistItems() {
		return waitlistItems;
	}

	public Iterator getWaitListIterator() {

		return waitlistItems.iterator();
	}

	public boolean insertlistedItem(Product product, int amount) {

		return orderItems.add(new OrderedItem(product, amount));
	}

//	public Iterator findWaitListWithProduct(Product product) {
////		while (iterate.hasNext()) {
////			OrderedItem object = (OrderedItem) iterate.next();
////
////			if (object.product.equals(product) && object.quantity > 0) {
////				System.out.println("Order " + id + " is waiting for " + object.quantity + ": " + product.getName());
////
////			}
////
////		}
//		return waitlistItems.iterator();
//
//    }

    public void setClientReferenceId(String clientReferenceId) {
		this.clientReferenceId = clientReferenceId;
		if (this.clientReferenceId == clientReferenceId) {

		}
	}
    
    public void setManufacturerReferenceId(String manufacturerReferenceID) {
 		this.manufacturerReferenceID = manufacturerReferenceID;
 	}
    
	public String toString() {
		String referenceID = clientReferenceId != null ? clientReferenceId : manufacturerReferenceID;
		return "Id=" + id + "\nOrderItems=" + orderItems + "\n waitlistItems=" + waitlistItems + "\n ReferenceId=" + referenceID + "\n\n";
	}

	public double getTotals() {
		Iterator it = orderItems.iterator();
		double total = 0.00;

		while (it.hasNext()) {
			OrderedItem orderLine = (OrderedItem) it.next();
			total += orderLine.product.getCost() * orderLine.quantity;
		}
		System.out.println("Totals are: " + total);
		return total;
	}


}