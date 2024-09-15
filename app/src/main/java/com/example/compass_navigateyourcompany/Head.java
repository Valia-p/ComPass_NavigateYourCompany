package com.example.compass_navigateyourcompany;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Head")
public class Head {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Cid")
    public String authToken;

    @ColumnInfo(name = "Did")
    public int departmentId;

    @ColumnInfo(name = "Name")
    public String name;

    @ColumnInfo(name = "Mail")
    public String mail;

    @ColumnInfo(name = "Phone")
    public String phone;

    @ColumnInfo(name = "Years")
    public int years;

    @ColumnInfo(name = "FilePath")
    public String filePath;

    public int getDepartmentId() {
        return departmentId;
    }
}
