package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface EmployeeDao {

    @Insert
    void insert(Employee employee);

    @Query("SELECT * FROM Employee WHERE id = :id LIMIT 1")
    Employee findById(int id);

    @Query("SELECT * FROM Employee WHERE Name = :name LIMIT 1")
    Employee findByName(String name);

    @Query("SELECT * FROM Employee WHERE Did = :departmentId")
    List<Employee> findByDepartmentId(int departmentId);

    @Query("SELECT * FROM Employee WHERE Cid = :authToken LIMIT 1")
    Employee findByAuthToken(String authToken);

    @Query("SELECT * FROM Employee")
    List<Employee> getAllEmployees();
}
