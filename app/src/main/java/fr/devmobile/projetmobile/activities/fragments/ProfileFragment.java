package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import fr.devmobile.projetmobile.session.Session;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageView profilePicture;
    private TextView username;
    private TextView displayName;
    private ImageView settingsButton;

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

        profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
        username = (TextView) view.findViewById(R.id.profile_username);
        displayName = (TextView) view.findViewById(R.id.profile_displayname);

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

        username.setText("@" + user.getUsername());
        displayName.setText(user.getDisplayName());
        Glide.with(this).load(user.getAvatar()).into(profilePicture);

        // Affichage des posts de l'utilisateur
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        imageAdapter = new ImageAdapter(getContext(), posts);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        return view;
    }

}