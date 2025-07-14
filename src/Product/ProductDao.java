
package Product;

import Order.Order;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public interface ProductDao {
    public void buy(double money);
    public void setProductsTable(Product prd);
    public void sell(int response,Order ord,String name);
    public DefaultComboBoxModel setProducts();
    public List<Product> list();
    public void alterProduct(Product prod);
    public void deleteProduct(String name);
}
