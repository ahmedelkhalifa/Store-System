
package Supplier;

import SupClass.Dealer;

public class Supplier extends Dealer {
    private String name;
    private String product;

    public Supplier(String name,String phone, String address) {
        this.name = name;
        this.product = product;
        this.phone = phone;
        this.address = address;
    }
    
    public Supplier()
    {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    
    
    
    
}
