
package Customer;

import SupClass.Dealer;

public class Customer extends Dealer {
    
    private String fname;
    private String minit;
    private String lname;
    private int orders;

    public Customer(String fname, String minit, String lname, String phone, String address) {
        this.phone = phone;
        this.address = address;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
    }

    public Customer(String fname, String minit, String lname) {
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
    }
    
    public Customer()
    {
        
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMinit() {
        return minit;
    }

    public void setMinit(String minit) {
        this.minit = minit;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
    
    

}
