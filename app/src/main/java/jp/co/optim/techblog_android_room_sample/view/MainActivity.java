package jp.co.optim.techblog_android_room_sample.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
     * DBを設定。
     */
    private void initDb() {
        userDataManager = new UserDataManager(this);
        callback = new UserDataManagerCallback(this);
        userDataManager.setCallback(callback);
        userDataManager.read();
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
                userDataManager.deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickAddUserButton(View view) {
        EditText editText = findViewById(R.id.editTextUser);
        String inputName = editText.getText().toString();
        //入力されたら、文言有無にかかわらず、文言初期化しキーボードを閉じる。
        editText.setText("");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (inputName.length() != 0) {
            //DB格納用に整形
            User user = new User();
            user.setName(inputName);
            user.setCreatedAt(System.currentTimeMillis());
            //user追加処理
            userDataManager.insert(user);
        }
    }

    /**
     * DB読み込み完了後、RecyclerViewを更新する。
     *
     * @param allUser DBから取得したUserデータ
     */
    public void updateRecyclerView(List<User> allUser) {
        if (adapter instanceof UserListAdapter) {
            ((UserListAdapter) adapter).setUsers(allUser);
        }
    }

    @Override
    protected void onDestroy() {
        //Activity破棄のタイミングで、DBを閉じる。
        userDataManager.closeDatabase();
        super.onDestroy();
    }
}
