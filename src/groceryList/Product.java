package groceryList;

public class Product {
    
    private String name;
    
    private Double price;

    private int quantity;

    private Double totalPrice;
    
    public Product(String name, Double price, int quantity)
    {
	this.name = name;
	this.price = price;
	this.quantity = quantity;
	this.totalPrice = price * quantity;
    }

    public String getName()
    {
        return name;
    }

    public Double getPrice()
    {
        return price;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public Double getTotalPrice()
    {
        return totalPrice;
    }
}
