
import java.io.Serializable;

public class OrderedItem implements Serializable {

	private static final long serialVersionUID = 1L;
	Product product;
	int quantity;
	String orderID;

	public OrderedItem(Product product, int quantity) {

		this.product = product;
		this.quantity = quantity;
	}

	public String toString() {

		return this.quantity + ": " + product.getName();
	}
	
	public Product getProduct() {
		return this.product;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}