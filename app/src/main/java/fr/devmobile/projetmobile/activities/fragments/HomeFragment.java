package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.PostAdapter;

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

        // Fetch data for now use dummy data
        /*
        List<PostAdapter.PostData> data = Arrays.asList(
                new PostAdapter.PostData(Arrays.asList("https://loremflickr.com/cache/resized/65535_52893558487_b5dcac7206_n_320_240_nofilter.jpg"), "XDXD", "John Doe", "dummyData", "000-000-000-000", "https://avatars.githubusercontent.com/u/97165289"),
        new PostAdapter.PostData(Arrays.asList("https://loremflickr.com/cache/resized/65535_52893558487_b5dcac7206_n_320_240_nofilter.jpg"), "XDXD", "John Doe", "dummyData", "000-000-000-000", "https://avatars.githubusercontent.com/u/97165289")
        );

        setupRecycleView(data);

         */

        return view;
    }

    private void setupRecycleView(List<PostAdapter.PostData> posts) {
        postAdapter = new PostAdapter(posts, requireContext());
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

}