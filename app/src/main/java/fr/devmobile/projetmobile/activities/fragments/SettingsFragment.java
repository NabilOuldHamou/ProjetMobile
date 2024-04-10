package fr.devmobile.projetmobile.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.LoginActivity;
import fr.devmobile.projetmobile.session.Session;

public class SettingsFragment extends Fragment {

    private TextView changeProfilePicture;
    private TextView updateProfil;
    private TextView changePassword;
    private TextView logout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Session session = Session.getInstance();

        changeProfilePicture = view.findViewById(R.id.tv_change_profile_picture);
        updateProfil = view.findViewById(R.id.tv_update_profile);
        changePassword = view.findViewById(R.id.tv_change_password);
        logout = view.findViewById(R.id.tv_logout);

        changeProfilePicture.setOnClickListener(v -> {
            replaceFragment(new ChangeProfilePictureFragment());
        });

        updateProfil.setOnClickListener(v -> {
            replaceFragment(new UpdateProfileFragment());
        });

        changePassword.setOnClickListener(v -> {
            replaceFragment(new ChangePasswordFragment());
        });

        logout.setOnClickListener(v -> {
            session.deleteToken();
            session.deleteUser();
            session.clearPosts();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
