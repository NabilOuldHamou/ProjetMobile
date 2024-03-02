package fr.devmobile.projetmobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.devmobile.projetmobile.database.dao.PostDao;
import fr.devmobile.projetmobile.database.dao.UserDao;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;

@Database(entities = {User.class, Post.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();
}
