package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.ImageAdapter;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.UserRequest;
import fr.devmobile.projetmobile.session.Session;

public class ProfileFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageView profilePictureView;
    private TextView usernameView;
    private TextView displayNameView;
    private ImageView settingsButton;

    private User user;

    private List<Post> posts;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Session session = Session.getInstance();
        User user = session.getUser();
        List<Post> posts = session.getPosts();

        // Affichage des informations de l'utilisateur en haut de la page

        profilePictureView = view.findViewById(R.id.profile_picture);
        usernameView = view.findViewById(R.id.profile_username);
        displayNameView = view.findViewById(R.id.profile_displayname);
        recyclerView = view.findViewById(R.id.recycler_view_profile);
        progressBar = view.findViewById(R.id.progress_bar_profile);

        // SETTINGS
        settingsButton = (ImageView) view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> {
            //session.deleteToken();
            //session.deleteUser();
            //startActivity(new Intent(getActivity(), LoginActivity.class));
            //requireActivity().finish();

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
            fragmentTransaction.commit();
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageAdapter = new ImageAdapter(getContext(), posts);
        recyclerView.setAdapter(imageAdapter);

        requestUser(user.getId(), view);

        return view;
    }

    private void requestUser(String id, View view){
        progressBar.setVisibility(View.VISIBLE);
        new UserRequest().findUserById(id, new Callback() {
            @Override
            public void onResponse(Object data) {
                requireActivity().runOnUiThread(() -> {
                    List<Object> objects = (List<Object>) data;
                    User user = (User) objects.get(0);
                    List<Post> posts = (List<Post>) objects.get(1);
                    setUser(user, posts);
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }

            @Override
            public void onError(Object data) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    private void setUser(User user, List<Post> posts){
        this.user = user;
        this.posts = posts;
        usernameView.setText(user.getUsername());
        displayNameView.setText(user.getDisplayName());
        Glide.with(this).load(user.getAvatar()).into(profilePictureView);

    }

}