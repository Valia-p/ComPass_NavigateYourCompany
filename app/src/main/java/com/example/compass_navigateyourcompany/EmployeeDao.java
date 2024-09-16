package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface EmployeeDao {

    @Insert
    void insert(Employee employee);

    @Update
    void updateEmployee(Employee employee);

    @Query("SELECT * FROM Employee WHERE Name = :name LIMIT 1")
    Employee findByName(String name);

    @Query("SELECT COUNT(*) FROM Employee WHERE Cid = :authToken AND Did = :departmentId")
    int countEmployees(String authToken, int departmentId);

    @Query("SELECT * FROM Employee WHERE Did = :departmentId")
    List<Employee> findByDepartmentId(int departmentId);

    @Query("SELECT Did FROM Employee WHERE name = :loginName LIMIT 1")
    Integer findDepartmentIdByLoginName(String loginName);


}
