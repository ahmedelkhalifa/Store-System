
package Supplier;

import java.util.List;

public interface SupplierDao {
    public void save(Supplier sup);
    public void update(Supplier sup);
    public void delete(Supplier sup);
    public Supplier searchById(int id);
    public Supplier searchByName(String name);
    public List<Supplier> list();
}
