package fr.devmobile.projetmobile.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.LoginActivity;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class ChangePasswordFragment extends Fragment {

    private Button submitButton;
    private EditText password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);


        Session session = Session.getInstance();
        User currentUser = session.getUser();

        password = view.findViewById(R.id.pwd_password);
        submitButton = view.findViewById(R.id.btn_submit_pwd);

        submitButton.setOnClickListener(v -> {

            if (password.getEditableText().toString().length() < 6) {
                Toast.makeText(requireContext(), "Le mot de passe doit faire au moins 6 caractères.", Toast.LENGTH_LONG).show();
                return;
            }

            AppHttpClient appHttpClient = new AppHttpClient(session.getToken());
            appHttpClient.changePassword(currentUser, password.getEditableText().toString())
                    .thenAccept(
                            r -> {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), "Vous venez de changer votre mot de passe, merci de vous reconnecter."
                                            , Toast.LENGTH_LONG).show();
                                    session.deleteToken();
                                    session.deleteUser();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    requireActivity().finish();
                                });
                            });
        });

        return view;
    }

}
