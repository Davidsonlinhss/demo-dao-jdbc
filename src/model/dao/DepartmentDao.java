package model.dao;

import model.entities.Department;
import java.util.List;

public interface DepartmentDao {

    // operação para inserir o departament ao banco de dados
    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
