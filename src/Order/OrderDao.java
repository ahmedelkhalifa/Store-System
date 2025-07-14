
package Order;

import java.util.List;

public interface OrderDao {
    public Order search(int id);
    public Order search(String name);
    public void cancel(Order order);
    public List<Order> list();
    public List<Order> searchs(String customerName);
    public List<Order> searchs(int customerId);
}
