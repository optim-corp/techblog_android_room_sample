package jp.co.optim.techblog_android_room_sample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Roomデータベース。
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRoomDatabase extends RoomDatabase {

    /**
     * db操作に使用するDAOの抽象メソッド。
     *
     * @return UserDaoのオブジェクト。
     */
    public abstract UserDao UserDao();

    private static UserRoomDatabase userRoomDatabase;

    /**
     * Roomデータベースを返す。存在しなければ、作成する。
     *
     * @param context
     * @return UserRoomDatabase
     */
    public static UserRoomDatabase getDatabase(final Context context) {
        if (userRoomDatabase == null) {
            synchronized (UserRoomDatabase.class) {
                if (userRoomDatabase == null) {
                    //dbを作成する。
                    userRoomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDatabase.class, "user_database")
                            .build();
                }
            }
        }
        return userRoomDatabase;
    }
}
