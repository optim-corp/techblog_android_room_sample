package jp.co.optim.techblog_android_room_sample.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Userテーブルを定義するEntityクラス。
 */
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    public int id;

    public String name;  //ユーザーの名前
    @ColumnInfo(name = "created_at")
    public Long createdAt; //ユーザーの登録時刻
}