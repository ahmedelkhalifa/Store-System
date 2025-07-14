
package Order;

import Product.Product;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private String name;
    private String address;
    private String customerName;
    private int customerId;
    private double price;
    private LocalDate orderDate;
    private List<Product> list;
    private String products;

    public Order(String name, String address, String customerName, int customerId, double price) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.customerName = customerName;
        this.customerId = customerId;
        this.price = price;
        this.orderDate = orderDate;
    }

    public Order(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public Order()
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
    
    
    
}
