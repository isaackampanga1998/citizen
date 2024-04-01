package com.example.myapplication;

import android.app.Application;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Model.AppDatabase;

public class MyApp extends Application {
    public static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        // Ajoutez la gestion de la migration ici
        Migration migration1to2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                // Ajoutez votre logique de migration ici si nécessaire
                // database.execSQL("ALTER TABLE user_table ADD COLUMN new_column_name TEXT");
            }
        };

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Citezens")
                .addMigrations(migration1to2)  // Ajoutez cette ligne pour la gestion de la migration
                .build();


    }
}
