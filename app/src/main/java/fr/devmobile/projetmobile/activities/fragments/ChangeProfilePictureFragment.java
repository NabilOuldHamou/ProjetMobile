package fr.devmobile.projetmobile.activities.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class ChangeProfilePictureFragment extends Fragment {

    private Button selectPicture;
    private Button submitButton;
    private Uri selectedUri;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_profile_picture, container, false);

        selectPicture = view.findViewById(R.id.btn_select_pfp);
        submitButton = view.findViewById(R.id.btn_submit_pfp);
        
        selectPicture.setOnClickListener(v -> openGallery());
        submitButton.setOnClickListener(v -> {
            if (selectedUri != null) {
                submitAvatar();
            } else {
                Toast.makeText(requireContext(), "Veuillez sélectionner une image", Toast.LENGTH_LONG).show();
            }
        });



        return view;
    }

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            uri -> {
                if (uri != null) {
                    selectedUri = uri;
                    selectPicture.setBackgroundColor(getResources().getColor(R.color.md_theme_light_tertiary, null));
                }
            });

    private void openGallery() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void submitAvatar() {
        byte[] file = uriToFile();

        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.uploadProfilePicture(Session.getInstance().getUser(), file)
                .thenAccept(r -> {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Vous venez de changer votre photo de profil!"
                                , Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                        fragmentTransaction.commit();
                    });
                });
    }

    private byte[] uriToFile() {
        try (InputStream is = requireActivity().getContentResolver().openInputStream(selectedUri)) {
            assert is != null;
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
