package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.PostAdapter;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recycler_view);

        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/posts")
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            return;
                        }

                        List<PostAdapter.PostData> data = new ArrayList<>();
                        JSONArray posts = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject p = posts.getJSONObject(i);
                            String postId = p.getString("id");
                            String text = p.getString("text");

                            ArrayList<String> urls = new ArrayList<>();
                            JSONArray files = p.getJSONArray("files");
                            for (int j = 0; j < files.length(); j++) {
                                urls.add("https://oxyjen.io/assets/" +
                                        files.getJSONObject(j).getString("FileName"));
                            }

                            JSONObject userObject = p.getJSONObject("user");
                            String userId = userObject.getString("id");
                            String username = userObject.getString("username");
                            String displayName = userObject.getString("display_name");
                            String profile_picture = userObject.getString("profile_picture").equals("") ?
                                    "https://oxyjen.io/assets/default.jpg" :
                                    "https://oxyjen.io/assets/" + userObject.getString("profile_picture");

                            Post post = new Post(postId, text, urls);
                            User user = new User(userId, profile_picture, username, displayName, "");

                            data.add(new PostAdapter.PostData(post, user));
                        }

                        requireActivity().runOnUiThread(() -> setupRecycleView(data));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });

        return view;
    }

    private void setupRecycleView(List<PostAdapter.PostData> posts) {
        postAdapter = new PostAdapter(posts, requireContext(), requireActivity());
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

}