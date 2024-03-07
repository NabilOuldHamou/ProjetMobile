package fr.devmobile.projetmobile.activities.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class PostFragment extends Fragment {

    private EditText description;
    private Button postSelection;
    private Button submitButton;
    private TextView selectedMedia;

    private List<Uri> selectedUris;

    public PostFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        description = view.findViewById(R.id.description_input);
        postSelection = view.findViewById(R.id.post_select_image_button);
        submitButton = view.findViewById(R.id.post_submit_button);
        selectedMedia = view.findViewById(R.id.post_selected_media_text);

        postSelection.setOnClickListener(v -> openGallery());
        submitButton.setOnClickListener(v -> submitPost());

        return view;
    }

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickMultipleVisualMedia(5),
            uris -> {
                if (!uris.isEmpty()) {
                    Log.d("Azote", "pickMedia: Selected URI size: " + uris.size());
                    selectedMedia.setText("Contenu sélectionné : " + uris.size());
                    selectedUris = uris;
                } else {
                    Log.d("Azote", "pickMedia: No media selected.");
                }
            });

    private void openGallery() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void submitPost() {
        List<byte[]> files = urisToFiles();
        String desc = description.getEditableText().toString();

        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.uploadPost(desc, files)
                .thenAccept(r -> {

                    Toast.makeText(requireContext(), "Vous venez de mettre en ligne un nouveau post!"
                            , Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                    fragmentTransaction.commit();
                });
    }

    private List<byte[]> urisToFiles() {
        List<byte[]> files = new ArrayList<>();
        for (Uri u : selectedUris) {
            try (InputStream is = requireActivity().getContentResolver().openInputStream(u)) {
                assert is != null;
                files.add(getBytes(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return files;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}