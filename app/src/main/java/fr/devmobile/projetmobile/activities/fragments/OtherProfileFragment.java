package fr.devmobile.projetmobile.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.LoginActivity;
import fr.devmobile.projetmobile.adapters.ImageAdapter;
import fr.devmobile.projetmobile.adapters.UserAdapter;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.session.Session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherProfileFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_USERNAME = "username";

    private static final String ARG_DISPLAYNAME = "display_name";
    private static final String ARG_AVATAR_URL = "avatar_url";

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageView profilePictureView;
    private TextView usernameView;
    private TextView displayNameView;
    private String id;
    private String username;
    private String displayName;
    private String avatarUrl;

    public OtherProfileFragment() {
        // Required empty public constructor
    }

    public static OtherProfileFragment  newInstance(UserAdapter.UserData user){
        OtherProfileFragment fragment = new OtherProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, user.getId());
        args.putString(ARG_USERNAME, user.getUsername());
        args.putString(ARG_DISPLAYNAME, user.getDisplayName());
        args.putString(ARG_AVATAR_URL, user.getUserAvatar());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            username = getArguments().getString(ARG_USERNAME);
            displayName = getArguments().getString(ARG_DISPLAYNAME);
            avatarUrl = getArguments().getString(ARG_AVATAR_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        profilePictureView = (ImageView) view.findViewById(R.id.profile_picture);
        usernameView = (TextView) view.findViewById(R.id.profile_username);
        displayNameView = (TextView) view.findViewById(R.id.profile_displayname);

        usernameView.setText(username);
        displayNameView.setText(displayName);
        Glide.with(this).load(avatarUrl).into(profilePictureView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        /*
        imageAdapter = new ImageAdapter(getContext(), posts);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
         */

        return view;
    }
}