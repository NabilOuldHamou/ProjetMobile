package fr.devmobile.projetmobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.devmobile.projetmobile.database.dao.UserDao;
import fr.devmobile.projetmobile.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
