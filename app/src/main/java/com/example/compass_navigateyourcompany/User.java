package com.example.compass_navigateyourcompany;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity (tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "login_name")
    public String loginName;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "auth_token")
    public String authToken;

    @ColumnInfo(name = "type")
    public String type; // "Employer", "Head", or "Employee"
}
