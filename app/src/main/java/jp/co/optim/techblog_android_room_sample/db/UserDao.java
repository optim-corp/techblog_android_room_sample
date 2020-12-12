package jp.co.optim.techblog_android_room_sample.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Userテーブルへのクエリを定義するDAOインターフェース。
 */
@Dao
public interface UserDao {

    @Query("SELECT user.* FROM user")
    public List<User> loadUsers();

    @Insert
    public void insertUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void deleteUser(User user);

    /**
     * Userを全て削除する。
     */
    @Query("DELETE FROM user")
    public void deleteAllUser();
}