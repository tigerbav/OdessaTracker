package com.smirnova.odesatracker.start;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.smirnova.odesatracker.DataBase;
import com.smirnova.odesatracker.R;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class Login extends AppCompatActivity {
    private Button login;
    private ImageView backBtn;
    private TextInputLayout email, password;

    final AwesomeValidation mAwesomeValidationEmailPassword = new AwesomeValidation(TEXT_INPUT_LAYOUT);

    private DataBase dataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.log_in);
        backBtn = findViewById(R.id.back_btn);
        email = findViewById(R.id.loginString);
        password = findViewById(R.id.passwordString);

        dataBase = DataBase.createOrReturn();

        mAwesomeValidationEmailPassword.addValidation(Login.this, R.id.loginString, android.util.Patterns.EMAIL_ADDRESS, R.string.error_gmail);
        mAwesomeValidationEmailPassword.addValidation(Login.this, R.id.passwordString, ".{6,}", R.string.password_error);

        backBtn.setOnClickListener(v -> Login.super.onBackPressed());

        login.setOnClickListener(v -> {
            if (mAwesomeValidationEmailPassword.validate()) {
                dataBase.signIn(email.getEditText().getText().toString(), password.getEditText().getText().toString(), Login.this);
            }
        });


    }
}
