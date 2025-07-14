
package Customer;

import java.util.List;

public interface CustomerDao {
    public void save(Customer customer);
    public void delete(int id);
    public void update(Customer customer);
    public Customer searchById(int id);
    public Customer searchByName(String fname, String minit, String lname);
    public List<Customer> list();
}
