package fr.devmobile.projetmobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.devmobile.projetmobile.database.dao.PostDao;
import fr.devmobile.projetmobile.database.dao.UserDao;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;

@Database(entities = {User.class, Post.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UserDao userDao();
    public abstract PostDao postDao();
}
