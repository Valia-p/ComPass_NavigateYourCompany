package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PassDao {
    @Insert
    void insert(Pass pass);

    @Update
    void updatePass(Pass pass);

    @Query("SELECT COUNT(*) FROM Pass WHERE Uid = :userId AND approved = 1")
    int countApprovedLeaves(int userId);

    @Query("SELECT * FROM pass WHERE Uid IN (:userIds) AND approved = 0")
    List<Pass> getPassesByUserIds(List<Integer> userIds);

    @Query("SELECT * FROM pass WHERE Uid = :userId AND approved = 1")
    List<Pass> getApprovedPassesByUserId(int userId);

}
