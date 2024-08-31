package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HeadDao {

    @Insert
    long insertHead(Head head);

    @Query("SELECT * FROM Head")
    List<Head> getAllHeads();

    @Query("SELECT * FROM Head WHERE id = :id")
    Head getHeadById(int id);

    @Query("SELECT * FROM Head WHERE Did = :departmentId")
    List<Head> getHeadsByDepartmentId(int departmentId);

    @Query("SELECT * FROM Head WHERE name = :name LIMIT 1")
    Head getHeadByName(String name);
}
