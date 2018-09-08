
public class OrderTest {

	
	  public static void main(String[] args) {
	  
	  Product product = new Product("Headphones", 50,0); 
      Product product1 = new Product("Laptops", 10,0);
      Product product2 = new Product("Phones",24,0);
	  
      OrderedItem Items = new OrderedItem(product, 20); 
      Order order1 = new Order(); 
      Order order2 = new Order();
	  
	  OrderList list = new OrderList();
	  
      order1.insertlistedItem(product1, 90);
      order1.insertlistedItem(product, 20); 
      order2.insertlistedItem(product1, 5);
      order2.insertlistedItem(product2,23);
      list.insertOrderNode(order1); 
      list.insertOrderNode(order2);	  
	  System.out.println(list);
	  
	  }
	 

}