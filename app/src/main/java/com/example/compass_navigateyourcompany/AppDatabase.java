package com.example.compass_navigateyourcompany;

import androidx.room.Database;
import androidx.room.RoomDatabase;
//Yota is PERFECT
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}