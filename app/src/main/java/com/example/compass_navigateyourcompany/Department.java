package com.example.compass_navigateyourcompany;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Department")
public class Department {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String Cid;

    public String Name;
}
