package fr.devmobile.projetmobile.database;

import fr.devmobile.projetmobile.database.dao.PostDao;
import fr.devmobile.projetmobile.database.dao.UserDao;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.session.Session;

public class AppDatabaseRepository {

    private UserDao userDao;
    private PostDao postDao;

    public AppDatabaseRepository() {
        AppDatabase db = Session.getAppDatabase();
        userDao = db.userDao();
        postDao = db.postDao();
    }

    public void insertUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }

    public void insertPost(Post post) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insert(post);
        });
    }

    public void updateUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateUser(user);
        });
    }

    public void deleteUser(User user) {
        if (user == null) {
            return;
        }
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.deleteUser(user);
        });
    }

    public void deletePost(Post post) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            postDao.deletePost(post);
        });
    }

}
