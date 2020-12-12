package jp.co.optim.techblog_android_room_sample.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.co.optim.techblog_android_room_sample.R;
import jp.co.optim.techblog_android_room_sample.db.User;

/**
 * UserデータとRecyclerViewを繋ぐAdapterクラス。
 */
public class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {

    /**
     * 日時フォーマット
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);

    private List<User> users;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * データとViewHolderを結びつける。
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameView.setText(user.getName());
        holder.userCreatedAtView.setText(dateFormat.format(user.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
