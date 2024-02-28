package fr.devmobile.projetmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import fr.devmobile.projetmobile.R;

public class InscriptionActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        this.emailText = findViewById(R.id.emailId);
        this.usernameText = findViewById(R.id.usernameId);
        this.passwordText = findViewById(R.id.passwordId);

    }
}