package com.example.compass_navigateyourcompany;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, Employer.class, Head.class, Employee.class, Department.class, Pass.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EmployerDao employerDao();
    public abstract HeadDao headDao();
    public abstract EmployeeDao employeeDao();
    public abstract DepartmentDao departmentDao();
    public abstract PassDao passDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user-database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
