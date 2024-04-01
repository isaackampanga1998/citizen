package com.example.myapplication.Model;


// Dans le package com.example.myapplication.model
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}