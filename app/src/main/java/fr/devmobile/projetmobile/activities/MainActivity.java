package fr.devmobile.projetmobile.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.fragments.HomeFragment;
import fr.devmobile.projetmobile.activities.fragments.OtherProfileFragment;
import fr.devmobile.projetmobile.activities.fragments.PostFragment;
import fr.devmobile.projetmobile.activities.fragments.ProfileFragment;
import fr.devmobile.projetmobile.activities.fragments.SearchFragment;
import fr.devmobile.projetmobile.broadcasts.TimeReceiver;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static int currentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent().getBooleanExtra("NewPhotoTakedNotification", false) == true){
            replaceFragment(new PostFragment());
            findViewById(R.id.action_post).performClick();
        }else{
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigation.setOnItemSelectedListener(navListener);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
        setupBroadcast();
        askPermisionNotifications(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment", currentFragmentId);
        switch (currentFragmentId) {
            case R.id.action_home:
                break;
            case R.id.action_post:
                outState.putString("description", ((EditText) findViewById(R.id.description_input)).getText().toString());
                break;
            case R.id.action_search:
                outState.putString("search", ((SearchView) findViewById(R.id.input_search)).getQuery().toString());
                outState.putInt("currentPagePos", ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getCurrentPagePos());
                outState.putParcelableArrayList("postData", (ArrayList<? extends Parcelable>) ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getPostData());
                outState.putParcelableArrayList("userData", (ArrayList<? extends Parcelable>) ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getUserData());
                outState.putInt("currentPageUser", ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getCurrentPageUser());
                outState.putInt("currentPagePost", ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getCurrentPagePost());
                break;
            case R.id.action_profile:
                break;
            case R.layout.fragment_other_profile:
                outState.putString("id", (((OtherProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getUser().getId()));
                outState.putInt("previousFragmentId", ((OtherProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getPreviousFragmentId());
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentFragmentId = savedInstanceState.getInt("currentFragment", R.id.action_home);
        Log.d("MainActivity", "onRestoreInstanceState: " + currentFragmentId);
        switch (currentFragmentId) {
            case R.id.action_home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.action_post:
                replaceFragment(PostFragment.newInstance(
                        savedInstanceState.getString("description", "")
                ));
                break;
            case R.id.action_search:
                replaceFragment(SearchFragment.newInstance(
                        savedInstanceState.getString("search", ""),
                        savedInstanceState.getInt("currentPagePos", 0),
                        savedInstanceState.getParcelableArrayList("postData"),
                        savedInstanceState.getParcelableArrayList("userData"),
                        savedInstanceState.getInt("currentPagePost", 1),
                        savedInstanceState.getInt("currentPageUser", 1)
                        ));

                break;
            case R.id.action_profile:
                replaceFragment(new ProfileFragment());
                break;
            case R.layout.fragment_other_profile:
                Log.d("MainActivity", Integer.toString(savedInstanceState.getInt("previousFragmentId")));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, OtherProfileFragment.newInstance(savedInstanceState.getString("id"), savedInstanceState.getInt("previousFragmentId"))).addToBackStack(null).commit();
                break;
        }
        this.currentFragmentId = currentFragmentId;
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
        this.currentFragmentId = item.getItemId();
        return true;
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void askPermisionNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && !notificationManager.areNotificationsEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Les notifications sont désactivées");
            builder.setMessage("Veuillez activer les notifications pour cette application dans les paramètres.");
            builder.setPositiveButton("Paramètres", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("Annuler", null);
            builder.show();
        }
    }

    private void setupBroadcast(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        TimeReceiver timeReceiver = new TimeReceiver();
        registerReceiver(timeReceiver, filter);
    }
}