
package Supplier;

import DataBase.DB;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SupplierDaoImp implements SupplierDao {

    @Override
    public void save(Supplier sup) {
        Connection con = DB.getConnection();
        String sql = "insert into suppliers(name,phone,address) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sup.getName());
            ps.setString(2, sup.getPhone());
            ps.setString(3, sup.getAddress());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Added Successfully", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Supplier sup) {
        Connection con = DB.getConnection();
        String sql = "update suppliers set name = ?, product = ?, phone = ?, address = ? where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sup.getName());
            ps.setString(2, sup.getProduct());
            ps.setString(3, sup.getPhone());
            ps.setString(4, sup.getAddress());
            ps.setInt(5, sup.getId());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Updated Successfully", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }       
    }

    @Override
    public void delete(Supplier sup) {
        Connection con = DB.getConnection();
        String sql = "delete from suppliers where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sup.getId());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Deleted Successfully", "done", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error occurred","Oops",JOptionPane.ERROR_MESSAGE);
        }
        String delete = "delete from products where supplier = ?";
        try {
            PreparedStatement st = con.prepareStatement(delete);
            st.setString(1, sup.getName());
            st.execute();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    @Override
    public Supplier searchById(int id) {
        Connection con = DB.getConnection();
        String sql = "select * from suppliers where id = ?";
        Supplier sup = new Supplier("", "", "");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                sup = new Supplier(rs.getString("name"),rs.getString("phone"), rs.getString("address"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sup;
    }

    @Override
    public Supplier searchByName(String name) {
        Connection con = DB.getConnection();
        String sql = "select * from suppliers where name = ?";
        Supplier sup = new Supplier("", "", "");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                sup = new Supplier(rs.getString("name"),rs.getString("phone"), rs.getString("address"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sup;
    }

    @Override
    public List<Supplier> list() {
        Connection con = DB.getConnection();
        String sql = "select * from suppliers";
        List<Supplier> list = new ArrayList<Supplier>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Supplier sup = new Supplier(rs.getString("name"), rs.getString("phone"), rs.getString("address"));
                sup.setId(rs.getInt("id"));
                list.add(sup);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDaoImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
}
