package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HeadDao {

    @Insert
    long insert(Head head);

    @Query("SELECT * FROM Head")
    List<Head> getAllHeads();

    @Query("SELECT * FROM Head WHERE Did = :departmentId")
    Head getHeadByDepartmentId(int departmentId);

    @Query("SELECT * FROM Head WHERE name = :name LIMIT 1")
    Head getHeadByName(String name);

    @Query("SELECT Name FROM Head WHERE Cid = :authToken AND Did = :departmentId LIMIT 1")
    String findSupervisor(String authToken, int departmentId);

    @Query("SELECT COUNT(*) > 0 FROM head WHERE Did = :departmentId")
    boolean isDepartmentExists(int departmentId);

}
