package jp.co.optim.techblog_android_room_sample.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.optim.techblog_android_room_sample.R;

/**
 * RecyclerViewに表示するViewアイテムを生成するViewHolderクラス。
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    public final TextView userNameView;
    public final TextView userCreatedAtView;

    /**
     * コンストラクタ
     *
     * @param itemView
     */
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userNameView = itemView.findViewById(R.id.listUserName);
        userCreatedAtView = itemView.findViewById(R.id.listUserCreatedAt);
    }
}
