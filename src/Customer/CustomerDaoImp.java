
package Customer;

import DataBase.DB;
import com.mysql.cj.xdevapi.PreparableStatement;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDaoImp implements CustomerDao {

    @Override
    public void save(Customer customer) {
        Connection con = DB.getConnection();
        String name = customer.getFname() + " " + customer.getMinit() + " " + customer.getLname();
        String sql = "insert into customers(fname,minit,lname,phone,address,order_no) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, customer.getFname());
            ps.setString(2, customer.getMinit());
            ps.setString(3, customer.getLname());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setInt(6,customer.getOrders());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Added Successfully","Done",JOptionPane.INFORMATION_MESSAGE);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(int id) {
        Connection con = DB.getConnection();
        String sql = "delete from customers where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Deleted Successfully","Done",JOptionPane.INFORMATION_MESSAGE);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void update(Customer customer) {
        Connection con = DB.getConnection();
        String name = customer.getFname() + " " + customer.getMinit() + " " + customer.getLname();
        String sql = "Update customers set fname = ?,minit = ?,lname = ?,phone = ?, address = ? where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,customer.getFname());
            ps.setString(2, customer.getMinit());
            ps.setString(3, customer.getLname());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setInt(6, customer.getId());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Updated Successfully","Done",JOptionPane.INFORMATION_MESSAGE);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public Customer searchById(int id) {
        Connection con = DB.getConnection();
        String sql = "select * from customers where id = ?";
        Customer cr = new Customer();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cr = new Customer(rs.getString("fname"),rs.getString("minit"),
                rs.getString("lname"),rs.getString("phone"),rs.getString("address"));
                cr.setOrders(rs.getInt("order_no"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        return cr;
    }

    @Override
    public List<Customer> list() {
        List<Customer> list = new ArrayList<Customer>();
        Connection con = DB.getConnection();
        String sql = "Select * from customers";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Customer cr = new Customer(rs.getString("fname"),rs.getString("minit"),
                rs.getString("lname"),rs.getString("phone"),rs.getString("address"));
                cr.setOrders(rs.getInt("order_no"));
                cr.setId(rs.getInt("id"));
                list.add(cr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    @Override
    public Customer searchByName(String fname, String minit, String lname) {
        Connection con = DB.getConnection();
        String sql = "select * from customers where fname = ? and minit = ? and lname =?";
        Customer cr = new Customer("","","","","");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, minit);
            ps.setString(3, lname);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cr = new Customer(rs.getString("fname"),rs.getString("minit"),
                rs.getString("lname"),rs.getString("phone"),rs.getString("address"));
                cr.setOrders(rs.getInt("order_no"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        return cr;
    }
    
}
