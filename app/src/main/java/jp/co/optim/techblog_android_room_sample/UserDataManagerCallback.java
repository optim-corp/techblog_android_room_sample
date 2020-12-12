package jp.co.optim.techblog_android_room_sample;

import android.content.Context;

import java.util.List;

import jp.co.optim.techblog_android_room_sample.db.User;
import jp.co.optim.techblog_android_room_sample.view.MainActivity;

/**
 * db処理完了通知を受け、次の処理を開始するcallbackクラス。
 */
public class UserDataManagerCallback {
    private MainActivity activity;
    private UserDataManager userDataManager;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public UserDataManagerCallback(Context context) {
        activity = (MainActivity) context;
    }

    public void setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    public void onReadUserCompleted(List<User> allUser) {
        activity.updateRecyclerView(allUser);
    }

    public void onAddUserCompleted() {
        userDataManager.read();
    }

    public void onDeleteUserCompleted() {
        userDataManager.read();
    }
}
