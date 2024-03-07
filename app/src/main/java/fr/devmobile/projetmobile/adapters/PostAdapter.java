package fr.devmobile.projetmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostData> posts;
    private Context context;

    public PostAdapter(List<PostData> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    public void setPosts(List<PostData> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostData post = posts.get(position);
        holder.textViewUsername.setText(post.getUsername());
        holder.textViewDescription.setText(post.getDescription());

        Glide.with(context).load(post.getUrls().get(0)).into(holder.imageViewPost);
        Glide.with(context).load(post.getAuthorProfilePic()).into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewDescription;
        private ImageView imageViewPost;
        private ImageView avatarImageView;

        public PostViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
        }
    }

    public static class PostData {
        private List<String> urls;
        private String description;
        private String author;
        private String username;
        private String authorId;
        private String authorProfilePic;

        public PostData(Post post, User user) {
            this.urls = post.getUrls();
            this.description = post.getText();
            this.author = user.getDisplayName();
            this.username = user.getUsername();
            this.authorId = user.getId();
            this.authorProfilePic = user.getAvatar();
        }

        public String getDescription() {
            return description;
        }

        public String getAuthor() {
            return author;
        }

        public String getUsername() {
            return username;
        }

        public String getAuthorId() {
            return authorId;
        }

        public String getAuthorProfilePic() {
            return authorProfilePic;
        }

        public List<String> getUrls() {
            return urls;
        }

    }
}
