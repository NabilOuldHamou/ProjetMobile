package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.ImageAdapter;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.UserRequest;
import fr.devmobile.projetmobile.session.Session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherProfileFragment extends Fragment {

    private static final String ARG_ID = "id";

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageView profilePictureView;
    private TextView usernameView;
    private TextView displayNameView;
    private ImageView returnButton;
    private String id;
    private User user;

    private List<Post> posts;

    public OtherProfileFragment() {
        // Required empty public constructor
    }

    public static OtherProfileFragment newInstance(String id){
        OtherProfileFragment fragment = new OtherProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        profilePictureView = view.findViewById(R.id.profile_picture);
        usernameView = view.findViewById(R.id.profile_username);
        displayNameView = view.findViewById(R.id.profile_displayname);
        returnButton = view.findViewById(R.id.return_fragment_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile);

        // RETURN BUTTON LOGIC
        returnButton.setOnClickListener(v -> returnToPreviousFragment());

        requestUser(this.id, view);
        return view;
    }

    private void requestUser(String id, View view){
        new UserRequest().findUserById(id, new Callback() {
            @Override
            public void onResponse(Object data) {
                List<Object> objects = (List<Object>) data;
                User user = (User) objects.get(0);
                List<Post> posts = (List<Post>) objects.get(1);
                setUser(user, posts);
            }

            @Override
            public void onError(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setUser(User user, List<Post> posts){
        this.user = user;
        this.posts = posts;
        usernameView.setText(user.getUsername());
        displayNameView.setText(user.getDisplayName());
        Glide.with(this).load(user.getAvatar()).into(profilePictureView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageAdapter = new ImageAdapter(getContext(), posts);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }

    public void returnToPreviousFragment(){
        requireActivity().getSupportFragmentManager().popBackStack();

    }
}