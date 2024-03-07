package fr.devmobile.projetmobile.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.devmobile.projetmobile.database.models.User;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user")
    Single<List<User>> getUsers();

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
