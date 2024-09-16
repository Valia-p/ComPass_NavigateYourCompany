package com.example.compass_navigateyourcompany;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM User WHERE login_name = :loginName AND password = :password AND auth_token = :authToken")
    User getUser(String loginName, String password, String authToken);


    @Query("SELECT * FROM User WHERE login_name = :loginName LIMIT 1")
    User findByLoginName(String loginName);


    @Query("SELECT * FROM user WHERE auth_token = :authToken AND type = 'Employee'")
    List<User> findEmployeeUsersByAuthToken(String authToken);

    @Query("SELECT id FROM User WHERE login_name = :loginName LIMIT 1")
    Integer findIdByName(String loginName);

}