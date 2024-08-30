package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface EmployerDao {
    @Insert
    void insert(Employer employer);

    @Query("SELECT * FROM Employer WHERE Cid = :authToken")
    Employer findByAuthToken(String authToken);

    @Query("SELECT * FROM Employer WHERE Name = :name")
    Employer findByName(String name);

}

