package fr.devmobile.projetmobile.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.fragments.HomeFragment;
import fr.devmobile.projetmobile.activities.fragments.PostFragment;
import fr.devmobile.projetmobile.activities.fragments.ProfileFragment;
import fr.devmobile.projetmobile.activities.fragments.SearchFragment;
import fr.devmobile.projetmobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(navListener);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
    }

    private BottomNavigationView.OnItemSelectedListener navListener = item -> {
        switch (item.getItemId()) {
            case R.id.action_home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.action_post:
                replaceFragment(new PostFragment());
                break;
            case R.id.action_search:
                replaceFragment(new SearchFragment());
                break;
            case R.id.action_profile:
                replaceFragment(new ProfileFragment());
                break;
        }
        return true;
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}