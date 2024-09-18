package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DepartmentDao {
    @Insert
    void insert(Department department);

    @Query("SELECT * FROM Department WHERE Name = :name")
    Department findByName(String name);

    @Query("SELECT * FROM Department WHERE Cid = :cid")
    List<Department> findByCid(String cid);

    @Query("SELECT * FROM Department WHERE id = :id")
    Department findById(Integer id);
}
