import java.util.*;
import java.io.*;

public class ProductList implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private List product = new LinkedList();
    private static ProductList productList;

    public static ProductList instance()
    {
        if (productList == null) 
        {
            return (productList = new ProductList());
        }
        else
        {
            return productList;
        }
    }

    public boolean insertProduct(Product prod) 
    {
        product.add(prod);
        return true;
    }

    public Iterator getProduct() 
    {
        return product.iterator();
    }

    public Product search(String productID)
    {
        for (Iterator iterator = product.iterator(); iterator.hasNext(); )
        {
            Product product = (Product) iterator.next();
            if (product.getID().equals(productID)) 
            {
                return product;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output)
    {
        try
           {
            output.defaultWriteObject();
            output.writeObject(productList);
            }
        catch(IOException ioe) 
           {
            System.out.println(ioe);
           }
    }

    private void readObject(java.io.ObjectInputStream input) 
    {
        try 
          {
            if (productList != null)
            {
                return;
            }
            else 
             {
                input.defaultReadObject();
                if (productList == null)
                {
                    productList = (ProductList) input.readObject();
                }
                else 
                   {
                    input.readObject();
                   }
            }
        }
        catch(IOException ioe)
        {
            System.out.println("in ProductList readObject \n" + ioe);
        }
        catch(ClassNotFoundException cnfe) 
        {
            cnfe.printStackTrace();
        }
    }

    public String toString()
    {
        return product.toString();
    }
}
