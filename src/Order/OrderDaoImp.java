
package Order;

import DataBase.DB;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDaoImp implements OrderDao {

    @Override
    public Order search(int id) {
        Connection con = DB.getConnection();
        String sql = "select * from orders where id = ?";
        Order order = new Order();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                order.setId(id);
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("Address"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderDate(rs.getDate("order_Date").toLocalDate());
                order.setProducts(rs.getString("products"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return order;
    }

    @Override
    public Order search(String name) {
        Connection con = DB.getConnection();
        String sql = "select * from orders where name = ?";
        Order order = new Order();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                order.setId(rs.getInt("id"));
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("Address"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderDate(rs.getDate("order_Date").toLocalDate());
                order.setProducts(rs.getString("products"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return order;
    }

    @Override
    public void cancel(Order order) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Order> list() {
        Connection con = DB.getConnection();
        String sql = "select * from orders";
        List<Order> list = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Order ord = new Order(rs.getString("name"),rs.getString("address"),
                rs.getString("customer_name"),rs.getInt("customer_id"),rs.getDouble("price"));
                ord.setId(rs.getInt("id"));
                ord.setOrderDate(rs.getDate("order_date").toLocalDate());      
                ord.setProducts(rs.getString("products"));
                list.add(ord);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Order> searchs(String customerName) {
        Connection con = DB.getConnection();
        String sql = "select * from orders where customer_name = ?";
        List<Order> list = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("Address"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderDate(rs.getDate("order_Date").toLocalDate());
                order.setProducts(rs.getString("products"));
                list.add(order);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(OrderDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Order> searchs(int customerId) {
        Connection con = DB.getConnection();
        String sql = "select * from orders where customer_id = ?";
        List<Order> list = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("Address"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderDate(rs.getDate("order_Date").toLocalDate());
                order.setProducts(rs.getString("products"));
                list.add(order);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(OrderDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
}
