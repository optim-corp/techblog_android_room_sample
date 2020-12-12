package jp.co.optim.techblog_android_room_sample;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.optim.techblog_android_room_sample.db.User;
import jp.co.optim.techblog_android_room_sample.db.UserDao;
import jp.co.optim.techblog_android_room_sample.db.UserRoomDatabase;

/**
 * Userテーブル操作用のDataManagerクラス。
 */
public class UserDataManager {

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int READ = 100;
    private static final int DELETE_ALL = 102;

    private UserDao userDao;

    /**
     * 全ユーザーのデータを格納するList型オブジェクト。
     */
    private List<User> allUser;

    /**
     * 処理結果を通知するcallbackクラス。
     */
    private UserDataManagerCallback callback;

    /**
     * 非同期処理を行うworkerスレッド用のシングルスレッド。
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * コンストラクタ
     *
     * @param context Application Context
     */
    public UserDataManager(Context context) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(context);
        userDao = db.UserDao();
    }

    public void setCallback(UserDataManagerCallback callback) {
        this.callback = callback;
        callback.setUserDataManager(this);
    }

    /**
     * insert処理
     *
     * @param user 追加するUser
     */
    public void insert(User user) {
        asyncExecute(user, INSERT);
    }

    /**
     * read処理
     */
    public void read() {
        asyncRead();
    }

    /**
     * delete処理
     * Userテーブルのデータ全てを削除する。
     */
    public void deleteAll() {
        asyncExecute(null, DELETE_ALL);
    }

    /**
     * backgroundタスクを作り、非同期処理を行う。
     *
     * @param user ToDebugItemsのアイテム。
     */
    @UiThread
    private void asyncExecute(User user, int type) {
        //ワーカースレッドから処理完了通知を受け取る。
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case INSERT:
                        callback.onAddUserCompleted();
                        break;
                    case DELETE:
                    case DELETE_ALL:
                        callback.onDeleteUserCompleted();
                        break;
                    default:
                        break;
                }
            }
        };
        BackgroundTask backgroundTask = new BackgroundTask(handler, userDao, user, type);
        //ワーカースレッドで実行する。
        executorService.submit(backgroundTask);
    }

    /**
     * 非同期でdb読み込みを行う。
     *
     * @return 読み込み結果
     */
    @UiThread
    private void asyncRead() {
        //ワーカースレッドからdb読み込み結果を受け取る。
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.obj != null) {
                    allUser = (List<User>) msg.obj;
                    //処理が完了したら、callbackに処理を返す。
                    callback.onReadUserCompleted(allUser);
                }
            }
        };
        BackgroundTaskRead backgroundTaskRead = new BackgroundTaskRead(handler, userDao, allUser);
        //ワーカースレッドで実行する。
        executorService.submit(backgroundTaskRead);
    }

    /**
     * insert, deleteの非同期処理を行うためのインナークラス。
     */
    private static class BackgroundTask implements Runnable {
        private final Handler handler;
        private UserDao userDao;
        private User user;
        private int type;

        BackgroundTask(Handler handler, UserDao userDao, User user, int type) {
            this.handler = handler;
            this.userDao = userDao;
            this.user = user;
            this.type = type;
        }

        @WorkerThread
        @Override
        public void run() {
            //非同期処理を開始する。
            switch (type) {
                case INSERT:
                    userDao.insertUser(user);
                    handler.sendMessage(handler.obtainMessage(INSERT));
                    break;
                case DELETE:
                    userDao.deleteUser(user);
                    handler.sendMessage(handler.obtainMessage(DELETE));
                    break;
                case DELETE_ALL:
                    userDao.deleteAllUser();
                    handler.sendMessage(handler.obtainMessage(DELETE_ALL));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * readの非同期処理を行うためのインナークラス。
     */
    private static class BackgroundTaskRead implements Runnable {
        private final Handler handler;
        private UserDao userDao;
        private List<User> user;

        BackgroundTaskRead(Handler handler, UserDao userDao, List<User> user) {
            this.handler = handler;
            this.userDao = userDao;
            this.user = user;
        }

        @WorkerThread
        @Override
        public void run() {
            //非同期処理を開始する。
            user = userDao.loadUsers();
            handler.sendMessage(handler.obtainMessage(READ, user));
        }
    }
}
