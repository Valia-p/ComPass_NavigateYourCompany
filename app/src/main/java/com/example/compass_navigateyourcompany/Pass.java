package com.example.compass_navigateyourcompany;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Pass")
public class Pass {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Uid")
    public String userID;

    @ColumnInfo(name = "Type")
    public String type;

    @ColumnInfo(name = "FromDate")
    public Date fromDate;

    @ColumnInfo(name = "ToDate")
    public Date toDate;

    @ColumnInfo(name = "Approved")
    public int approved; // values are 1, 0, -1

}
