
package Product;

import Customer.Customer;
import DataBase.DB;
import Order.Order;
import Supplier.SupplierDaoImp;
import View.BillForm;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import java.sql.Date;
import javax.swing.DefaultComboBoxModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductDaoImp implements ProductDao {

    @Override
    public void buy(double money) {
        List<Product> list = list();
        Connection con = DB.getConnection();
        String check = "select * from financial";
        double totalMoney = 0;
        int amount = 0;
        try {
            PreparedStatement stmt = con.prepareStatement(check);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                totalMoney = rs.getDouble("total_money");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(money <= totalMoney)
        {
            for(Product prod : list)
            {
                String am = "select amount from products where name = ?";
                try {
                    PreparedStatement amo = con.prepareStatement(am);
                    amo.setString(1, prod.getName());
                    ResultSet rs = amo.executeQuery();
                    while(rs.next())
                    {
                        amount = rs.getInt("amount");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String sql = "update products set price = ?, amount = ? where name =?";
                try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setDouble(1, prod.getPrice());
                    ps.setInt(2, amount + prod.getAmount());
                    ps.setString(3, prod.getName());
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String update = "update financial set total_money = ?";
                try {
                    PreparedStatement ps = con.prepareStatement(update);
                    ps.setDouble(1, totalMoney - money);
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }         
            JOptionPane.showMessageDialog(null, "Bought successfully", "Done", JOptionPane.INFORMATION_MESSAGE);
        } else
        {
            JOptionPane.showMessageDialog(null, "No enough money", "Oops", JOptionPane.ERROR_MESSAGE);
            System.out.println("im inside the else");
        }
    }

    @Override
    public void sell(int response, Order ord,String name) 
    {
        Connection con = DB.getConnection();
        String select = "select sum(total_price) from sell_products";
        
        // confirm payint total price
        double totalPrice = 0;
        try {
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                totalPrice = rs.getDouble("sum(total_price)");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        int confirm = JOptionPane.showConfirmDialog(null, "Confirm paying: " + totalPrice, "Confirm", JOptionPane.YES_NO_OPTION);
        
        
        
        // sell
        if(confirm == 0)
        {

                boolean isAvailable = false;
                List<Product> sell = listSell();
                List<Product> all = listProduct();
                List<Product> amount = new ArrayList();
                
                for(Product prod : sell)
                {
                    for(Product pr : all)
                    {
                        if(prod.getName().equals(pr.getName()))
                        {
                            if(prod.getAmount() > pr.getAmount())
                            {
                                JOptionPane.showMessageDialog(null, "no enough quantity from: " + prod.getName(), "Oops", JOptionPane.WARNING_MESSAGE);
                                isAvailable = false;
                            } else
                                isAvailable = true;
                        }
                    }
                }
            // to customer
            if(response == 0)
            {
                
                if(isAvailable)
                {    
                    String products = "";
                    for(Product prod : sell)
                    {
                        products += prod.getAmount() + " " + prod.getName() + " - ";
                    }                    
                String order = "insert into orders(name,address,customer_name,customer_id,price,order_date,products) values(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement stmt = con.prepareStatement(order);
                    stmt.setString(1, ord.getName());
                    stmt.setString(2, ord.getAddress());
                    stmt.setString(3, ord.getCustomerName());
                    stmt.setInt(4, ord.getCustomerId());
                    stmt.setDouble(5, totalPrice);
                    stmt.setString(6, ord.getOrderDate().toString());
                    stmt.setString(7, products);
                    stmt.execute();
                    stmt.close();
                    JOptionPane.showMessageDialog(null, "Done,order added!", "Done", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Oops", JOptionPane.ERROR_MESSAGE);
                }
                char[] chars = name.toCharArray();
                List<String> list = new ArrayList();
                String names = "";
                int index = 0;
                for(char x : chars)
                {
                    names += x;
                    if(x == ' ' || chars.length-3 == index)
                    {
                        list.add(names);
                        names = "";
                        continue;
                    }
                    index++;
                }
                String get = "select id,order_no from customers where fname = ? and minit = ? and lname = ?";
                int id = 0;
                int order_no = 0;
                try {
                    PreparedStatement stmt = con.prepareStatement(get);
                    stmt.setString(1, list.get(0).trim());
                    stmt.setString(2, list.get(1).trim());
                    stmt.setString(3, list.get(2).trim());
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next())
                    {
                        id = rs.getInt("id");
                        order_no = rs.getInt("order_no");
                    }
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String increase = "update customers set order_no = ? where id = ?";
                try {
                    PreparedStatement stmt = con.prepareStatement(increase);
                    stmt.setInt(1, order_no + 1);
                    stmt.setInt(2, id);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String getMoney = "select * from financial";
                double totalMoney = 0;
                try {
                    PreparedStatement ps = con.prepareStatement(getMoney);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        totalMoney = rs.getDouble("total_money");
                    }
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String update = "update financial set total_money = ?";
                try {
                    PreparedStatement ps = con.prepareStatement(update);
                    ps.setDouble(1, totalMoney + totalPrice);
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            for(Product prod : sell)
            {
                int amounts = 0;
                String am = "select amount from products where name = ?";
                try {
                    PreparedStatement amo = con.prepareStatement(am);
                    amo.setString(1, prod.getName());
                    ResultSet rs = amo.executeQuery();
                    while(rs.next())
                    {
                        amounts = rs.getInt("amount");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String sql = "update products set amount = ? where name =?";
                try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, amounts - prod.getAmount());
                    ps.setString(2, prod.getName());
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
                }
            }
            else if(response == 1)
            {
                if(isAvailable)
                {  
                    String products = "";
                    for(Product prod : sell)
                    {
                        products += prod.getAmount() + " " + prod.getName() + " - ";
                    }
                String order = "insert into orders(price,order_date,products) values(?,?,?)";
                try {
                    PreparedStatement stmt = con.prepareStatement(order);
                    stmt.setDouble(1, totalPrice);
                    stmt.setString(2, ord.getOrderDate().toString());
                    stmt.setString(3, products);
                    stmt.execute();
                    stmt.close();
                    JOptionPane.showMessageDialog(null, "Done,order added!", "Done", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error occurred!", "Oops", JOptionPane.ERROR_MESSAGE);
                } 
                String getMoney = "select * from financial";
                double totalMoney = 0;
                try {
                    PreparedStatement ps = con.prepareStatement(getMoney);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        totalMoney = rs.getDouble("total_money");
                    }
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String update = "update financial set total_money = ?";
                try {
                    PreparedStatement ps = con.prepareStatement(update);
                    ps.setDouble(1, totalMoney + totalPrice);
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            for(Product prod : sell)
            {
                int amounts = 0;
                String am = "select amount from products where name = ?";
                try {
                    PreparedStatement amo = con.prepareStatement(am);
                    amo.setString(1, prod.getName());
                    ResultSet rs = amo.executeQuery();
                    while(rs.next())
                    {
                        amounts = rs.getInt("amount");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                String sql = "update products set amount = ? where name =?";
                try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, amounts - prod.getAmount());
                    ps.setString(2, prod.getName());
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
                }
                
            }
        }
    }
    
    public DefaultComboBoxModel setProducts()
    {
        Connection con = DB.getConnection();
        String sql = "select name from products";
        DefaultComboBoxModel dft = new DefaultComboBoxModel();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Product prod = new Product();
                prod.setName(rs.getString("name"));
                dft.addElement(prod.getName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dft;
    }
    
    public DefaultComboBoxModel setCustomers()
    {
        Connection con = DB.getConnection();
        String sql = "select fname,minit,lname from customers";
        DefaultComboBoxModel dft = new DefaultComboBoxModel();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Customer prod = new Customer(rs.getString("fname"),rs.getString("minit"),rs.getString("lname"));
                dft.addElement(prod.getFname() + " " + prod.getMinit() + " " + prod.getLname());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dft;
    }    
    
    public static String setSupplier(String product)
    {
        Connection con = DB.getConnection();
        String sql = "select supplier from products where name = ?";
        String supplier = "";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, product);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                supplier = rs.getString("supplier");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supplier;
    }
    
    public List<Product> list()
    {
        Connection con = DB.getConnection();
        List<Product> list = new ArrayList<Product>();
        String sql = "select * from temporary_products";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Product prod = new Product();
                prod.setName(rs.getString("name"));
                prod.setPrice(rs.getDouble("price"));
                prod.setSupplier(rs.getString("supplier"));
                prod.setAmount(rs.getInt("amount"));
                prod.setTotalPrice(prod.getPrice() * prod.getAmount());
                list.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public void setProductsTable(Product prod)
    {
        Connection con = DB.getConnection();
        String sql = "insert into temporary_products values(?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prod.getName());
            ps.setDouble(2, prod.getPrice());
            ps.setString(3, prod.getSupplier());
            ps.setInt(4, prod.getAmount());
            ps.setDouble(5, prod.getTotalPrice());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterProduct(Product prod)
    {
        Connection con = DB.getConnection();
        String sql = "update temporary_products set amount = ?, price = ?, total_price = ? where name = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, prod.getAmount());
            ps.setDouble(2, prod.getPrice());
            ps.setDouble(3, prod.getPrice() * prod.getAmount());
            ps.setString(4, prod.getName());
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Altered!", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Product not found!", "Oops", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteProduct(String name)
    {
        Connection con = DB.getConnection();
        String sql = "delete from temporary_products where name = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null, "deleted!", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Product not found!", "Oops", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void addProduct(Product prod)
    {
        Connection con = DB.getConnection();
        String store = "insert into products(name,price,amount,supplier) values(?,0,0,?)";
        try {
            PreparedStatement st = con.prepareStatement(store);
            st.setString(1, prod.getName());
            st.setString(2, prod.getSupplier());
            st.execute();
            st.close();
            JOptionPane.showMessageDialog(null, "Added!", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DefaultComboBoxModel setSuppliers()
    {
        Connection con = DB.getConnection();
        DefaultComboBoxModel dft = new DefaultComboBoxModel();
        String sql = "select name from suppliers";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                dft.addElement(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dft;
    }
    
    public Product searchById(int id)
    {
        Connection con = DB.getConnection();
        String sql = "select * from products where id = ?";
        Product prod = new Product();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                prod.setId(id);
                prod.setName(rs.getString("name"));
                prod.setPrice(rs.getDouble("price"));
                prod.setSupplier(rs.getString("supplier"));
                prod.setAmount(rs.getInt("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prod;
    }
    
        public Product searchByName(String name)
    {
        Connection con = DB.getConnection();
        String sql = "select * from products where name = ?";
        Product prod = new Product();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                prod.setId(rs.getInt("id"));
                prod.setName(rs.getString("name"));
                prod.setPrice(rs.getDouble("price"));
                prod.setAmount(rs.getInt("amount"));
                prod.setSupplier(rs.getString("supplier"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prod;
    }
        
    public void update(Product prod)
    {
        Connection con = DB.getConnection();
        String store = "update products set name = ?, supplier = ?, price = ? where id = ?";
        try {
            PreparedStatement st = con.prepareStatement(store);
            st.setString(1, prod.getName());
            st.setString(2, prod.getSupplier());
            st.setDouble(3, prod.getPrice());
            st.setInt(4, prod.getId());
            st.execute();
            st.close();
            JOptionPane.showMessageDialog(null, "Updated!", "Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void delete(Product prod)
    {
        Connection con = DB.getConnection();
        String sql = "delete from products where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, prod.getId());
            int response = JOptionPane.showConfirmDialog(null, "Confirm deleting this item?","Confirm",JOptionPane.YES_NO_OPTION);
            if(response == 0)
            {
                ps.execute();
                ps.close();
                JOptionPane.showMessageDialog(null, "deleted!", "Done", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Canceled!", "Done", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static double setPrice(String name)
    {
        Connection con = DB.getConnection();
        String sql = "select price from products where name = ?";
        double price = 0;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                price = rs.getDouble("price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return price * 1.25;
    }
    
    public void addSellProduct(Product prod)
    {
        Connection con = DB.getConnection();
        String sql = "insert into sell_products values(?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prod.getName());
            ps.setDouble(2, prod.getPrice());
            ps.setInt(3, prod.getAmount());
            ps.setDouble(4, prod.getTotalPrice());
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterSell(Product prod)
    {
        Connection con = DB.getConnection();
        String select = "select price from sell_products where product = ?";
        double price = 0;
        try {
            PreparedStatement ps = con.prepareStatement(select);
            ps.setString(1, prod.getName());
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                price = rs.getDouble("price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "update sell_products set amount = ?, total_price = ? where product = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, prod.getAmount());
            stmt.setDouble(2, prod.getAmount() * price);
            stmt.setString(3, prod.getName());
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Altered!","Done",JOptionPane.INFORMATION_MESSAGE);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred!","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteSell(Product prod)
    {
        Connection con = DB.getConnection();
        String sql = "delete from sell_products where product = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prod.getName());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Deleted!","Done",JOptionPane.INFORMATION_MESSAGE);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred!","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Customer setIdAddress(String name)
    {
        Connection con = DB.getConnection();
        char[] chars = name.toCharArray();
        List<String> list = new ArrayList();
        String names = "";
        int index = 0;
        for(char x : chars)
        {
            names += x;
            if(x == ' ' || chars.length-3 == index)
            {
                list.add(names);
                names = "";
                continue;
            }
            index++;
        }
        String sql = "select id,address from customers where fname = ? and minit = ? and lname = ?";
        Customer cr = new Customer();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, list.get(0).trim());
            ps.setString(2, list.get(1).trim());
            ps.setString(3, list.get(2).trim());
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cr.setId(rs.getInt("id"));
                cr.setAddress(rs.getString("address"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cr;
    }
    
    public List<Product> listSell()
    {
        Connection con = DB.getConnection();
        String sql = "select * from sell_products";
        List<Product> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Product prod = new Product();
                prod.setAmount(rs.getInt("amount"));
                prod.setName(rs.getString("product"));
                prod.setPrice(rs.getDouble("price"));
                prod.setTotalPrice(rs.getDouble("total_price"));
                list.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public List<Product> listProduct()
    {
        Connection con = DB.getConnection();
        String sql = "select * from products";
        List<Product> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Product prod = new Product();
                prod.setAmount(rs.getInt("amount"));
                prod.setName(rs.getString("name"));
                list.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;        
    }
    
    public Customer getCustomer(String name)
    {
        Connection con = DB.getConnection();
        String names = "";
        int index = 0;
        List<String> list = new ArrayList();
        char[] chars = name.toCharArray();
        for(char x : chars)
        {
            names += x;
            if(x == ' ' || chars.length-3 == index)
            {
                list.add(names);
                names = "";
                continue;
            }
            index++;
        }  
        String sql = "select * from customers where fname = ? and minit = ? and lname = ?";
        Customer cr = new Customer();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, list.get(0).trim());
            ps.setString(2, list.get(1).trim());
            ps.setString(3, list.get(2).trim());
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cr.setFname(rs.getString("fname"));
                cr.setMinit(rs.getString("minit"));
                cr.setLname(rs.getString("lname"));
                cr.setAddress(rs.getString("address"));
                cr.setPhone(rs.getString("phone"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cr;
    }
    
    public void showBill()
    {
        new BillForm().setVisible(true);
    }
    public void showBill(Customer cr)
    {
        new BillForm(cr).setVisible(true);
    }
    
    public static int checkQuantity(String name)
    {
        Connection con = DB.getConnection();
        String sql = "select * from products where name = ?";
        int totalAmount = 0;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                totalAmount = rs.getInt("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalAmount;
    }
}
