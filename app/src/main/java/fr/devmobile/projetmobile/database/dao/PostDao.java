package fr.devmobile.projetmobile.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.devmobile.projetmobile.database.models.Post;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);

    @Query("SELECT * FROM post")
    Single<List<Post>> getPosts();

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

}
