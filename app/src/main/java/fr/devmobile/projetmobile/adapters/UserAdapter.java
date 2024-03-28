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
import fr.devmobile.projetmobile.database.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private Context context;

    private FragmentActivity activity;

    public UserAdapter(List<User> users, Context context, FragmentActivity activity) {
        this.users = users;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.textViewUsername.setText(user.getUsername());
        Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.logo)
                .into(holder.avatarImageView);
    }

    public void setUsers(List<User> users) {
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


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(userSelect);
            textViewUsername = itemView.findViewById(R.id.user_username);
            avatarImageView = itemView.findViewById(R.id.user_userpicture);
        }

        public View.OnClickListener userSelect = (view) -> {
            User userData = users.get(getAdapterPosition());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, OtherProfileFragment.newInstance(userData.getId())).addToBackStack(null).commit();
        };

    }
}
