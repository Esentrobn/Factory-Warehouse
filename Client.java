import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String CLIENT_STRING = "c";
	private String name;
	private String id;
	private double invoicedAmount = 0.00;

	public Client(String name) 
	{
		this.name = name;
		this.id = CLIENT_STRING + (ClientIdServer.instance()).getId();
	}
	public void chargedInvoice(double money){
		this.invoicedAmount += money;

    }
	
	public void clearBalance(double amount) {
		this.invoicedAmount -= amount;
	}

	public double getInvoicedAmount() {
		return invoicedAmount;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getName() 
	{
		return name;
	}

	public boolean equal(String id) 
	{
		return this.id.equals(id);
	}

	public String getID() 
	{
		return id;
	}

	@Override
	public String toString() 
	{
		return "Client name: " + name + ", id: " + id;
	}
}
