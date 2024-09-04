package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PassDao {
    @Insert
    void insert(Pass pass);

}
