package jp.co.optim.techblog_android_room_sample.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import jp.co.optim.techblog_android_room_sample.R;
import jp.co.optim.techblog_android_room_sample.UserDataManager;
import jp.co.optim.techblog_android_room_sample.UserDataManagerCallback;
import jp.co.optim.techblog_android_room_sample.db.User;

public class MainActivity extends AppCompatActivity {
    private UserDataManager userDataManager;
    private UserDataManagerCallback callback;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbarを設定
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        initDb();
        initRecyclerView();
    }

    /**
     * dbを設定。
     */
    private void initDb() {
        userDataManager = new UserDataManager(this);
        callback = new UserDataManagerCallback(this);
        userDataManager.setCallback(callback);
    }

    /**
     * RecyclerViewを設定。
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserListAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all:
                //AllDelete処理
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickAddUserButton(View view) {
    }

    /**
     * DB読み込み完了後、RecyclerViewを更新する。
     *
     * @param allUser dbから取得したUserデータ
     */
    public void updateRecyclerView(List<User> allUser) {
        if (adapter instanceof UserListAdapter) {
            ((UserListAdapter) adapter).setUsers(allUser);
        }
    }
}
