package com.example.compass_navigateyourcompany;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employer {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Cid")
    public String authToken;

    @ColumnInfo(name = "Name")
    public String name;

    @ColumnInfo(name = "CompanyName")
    public String companyName;

    @ColumnInfo(name = "Address")
    public String address;

    @ColumnInfo(name = "Phone")
    public String phone;

    @ColumnInfo(name = "Mail")
    public String mail;
}
