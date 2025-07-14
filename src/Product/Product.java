
package Product;

public class Product {
    private int id;
    private String name;
    private String supplier;
    private double price;
    private int amount;
    private double totalPrice;

    public Product(int id, String name, String supplier, double price, int amount) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.price = price;
        this.amount = amount;
    }
    
    public Product()
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
    
}
