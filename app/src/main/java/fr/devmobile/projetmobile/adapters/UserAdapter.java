package fr.devmobile.projetmobile.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.fragments.OtherProfileFragment;
import fr.devmobile.projetmobile.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserData> users;
    private Context context;

    private FragmentActivity activity;

    public UserAdapter(List<UserData> users, Context context, FragmentActivity activity) {
        this.users = users;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserData user = users.get(position);

        holder.textViewUsername.setText(user.getUsername());
        Glide.with(context).load(user.getUserAvatar()).into(holder.avatarImageView);
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private ImageView avatarImageView;
        private FragmentActivity activity;


        public UserViewHolder(@NonNull View itemView, FragmentActivity activity) {
            super(itemView);
            itemView.setOnClickListener(userSelect);
            textViewUsername = itemView.findViewById(R.id.user_username);
            avatarImageView = itemView.findViewById(R.id.user_userpicture);
            this.activity = activity;
        }

        public View.OnClickListener userSelect = (view) -> {
            UserData userData = users.get(getAdapterPosition());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, OtherProfileFragment.newInstance(userData.getId())).addToBackStack(null).commit();
        };

    }

    public static class UserData {

        private String id;
        private String username;
        private String displayName;
        private String userAvatar;

        public UserData(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.displayName = user.getDisplayName();
            this.userAvatar = user.getAvatar();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }
}
