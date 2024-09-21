package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.security.cert.Extension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()) {
                     int id = rs.getInt(1);
                     seller.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected number of rows inserted");
            }


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller getById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) { // testando se veio algum resultado
                Department department = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, department);
                return seller;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller sl = new Seller();
        sl.setId(rs.getInt("Id"));
        sl.setName(rs.getString("Name"));
        sl.setEmail(rs.getString("Email"));
        sl.setBaseSalary(rs.getDouble("BaseSalary"));
        sl.setBirthDate(rs.getDate("BirthDate"));
        sl.setDepartment(dep);
        return sl;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;

    }

    @Override
    public List<Seller> findAll() {
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = conn.prepareStatement(
                        "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON\n" +
                                "    seller.DepartmentId = department.Id\n" +
                                "ORDER BY name");


                rs = st.executeQuery();
                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map = new HashMap<>();
                while (rs.next()) {
                    Department dep = map.get(rs.getInt("DepartmentId"));
                    if (dep == null) {
                        dep = instantiateDepartment(rs);
                        map.put(rs.getInt("DepartmentId"), dep);
                    }
                    Seller obj = instantiateSeller(rs, dep);
                    list.add(obj);
                }
                return list;
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            } finally {
                DB.closeStatement(st);
                DB.closeResultSet(rs);
            }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON\n" +
                            "    seller.DepartmentId = department.Id WHERE DepartmentId = ?\n" +
                            "ORDER BY name;");
            st.setInt(1, department.getId());

            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
