package fr.devmobile.projetmobile.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class UpdateProfileFragment extends Fragment {

    private Button submitButton;
    private EditText email;
    private EditText displayName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        User currentUser = Session.getInstance().getUser();

        email = view.findViewById(R.id.upp_email);
        displayName = view.findViewById(R.id.upp_displayname);
        submitButton = view.findViewById(R.id.btn_submit_infos);

        email.setText(currentUser.getEmail());
        displayName.setText(currentUser.getDisplayName());

        submitButton.setOnClickListener(v -> {

            if (email.getEditableText().toString().isEmpty() || displayName.getEditableText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Les champs ne peuvent être vides.", Toast.LENGTH_LONG).show();
                return;
            }

            currentUser.setEmail(email.getEditableText().toString());
            currentUser.setDisplayName(displayName.getEditableText().toString());
            AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
            appHttpClient.updateInformations(currentUser)
                    .thenAccept(
                            r -> {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), "Vous venez de mettre à jour votre profil!"
                                            , Toast.LENGTH_LONG).show();
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                                    fragmentTransaction.commit();
                                });
                            });
        });

        return view;
    }

}
