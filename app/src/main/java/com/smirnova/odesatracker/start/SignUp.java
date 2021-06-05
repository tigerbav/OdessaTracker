package com.smirnova.odesatracker.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.smirnova.odesatracker.Constants;
import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "Tag";
    private Button signUp;
    private Button signUpGoogle;
    private TextView login;
    private TextInputLayout email, password, name;

    private final AwesomeValidation mAwesomeValidationNameEmailPassword = new AwesomeValidation(TEXT_INPUT_LAYOUT);

    private DataBase dataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = findViewById(R.id.sign_up);
        signUpGoogle = findViewById(R.id.sign_up_google);
        login = findViewById(R.id.textLogin);

        email = findViewById(R.id.loginString);
        password = findViewById(R.id.passwordString);
        name = findViewById(R.id.nameString);

        dataBase = DataBase.createOrReturn();

        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        signUpGoogle.setOnClickListener(v -> {
            DataBase.setGSO();
            dataBase.googleSignIn(SignUp.this);
        });

        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.loginString, android.util.Patterns.EMAIL_ADDRESS, R.string.error_gmail);
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.nameString, "[a-zA-ZА-Яа-я\\s]+", R.string.error_name);
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.passwordString, ".{6,}", R.string.password_error);

        signUp.setOnClickListener(v -> {
            if (mAwesomeValidationNameEmailPassword.validate()) {
                dataBase.registration(name.getEditText().getText().toString(),
                        email.getEditText().getText().toString(),
                        password.getEditText().getText().toString(),
                        SignUp.this);
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            dataBase.handleSignInResult(task, this);
        }
    }
}
