package fr.devmobile.projetmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.activities.fragments.OtherProfileFragment;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context context;
    private FragmentActivity activity;

    public PostAdapter(List<Post> posts, Context context, FragmentActivity activity) {
        this.posts = posts;
        this.context = context;
        this.activity = activity;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.textViewUsername.setText(post.getUser().getUsername());
        holder.textViewDescription.setText(post.getText());

        Glide.with(context)
                .load(post.getUrls().get(0))
                .placeholder(R.drawable.logo)
                .into(holder.imageViewPost);
        Glide.with(context)
                .load(post.getUser().getAvatar())
                .placeholder(R.drawable.logo)
                .into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout userData;
        private TextView textViewUsername;
        private TextView textViewDescription;
        private ImageView imageViewPost;
        private ImageView avatarImageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userData = itemView.findViewById(R.id.userData);
            userData.setOnClickListener(userSelect);
        }

        public View.OnClickListener userSelect = (view) -> {
            Post postData = posts.get(getAdapterPosition());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, OtherProfileFragment.newInstance(postData.getUser().getId())).addToBackStack(null).commit();
        };
    }
}
