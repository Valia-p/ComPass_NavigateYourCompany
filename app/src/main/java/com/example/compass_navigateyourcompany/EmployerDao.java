package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface EmployerDao {
    @Insert
    void insert(Employer employer);

    @Update
    void updateEmployer(Employer employer);

    @Query("SELECT * FROM Employer WHERE Cid = :authToken")
    Employer findByAuthToken(String authToken);

    @Query("SELECT * FROM Employer WHERE Name = :name")
    Employer findByName(String name);

    // check if the authToken exists
    @Query("SELECT COUNT(*) FROM Employer WHERE Cid = :authToken")
    int countByAuthToken(String authToken);

}

