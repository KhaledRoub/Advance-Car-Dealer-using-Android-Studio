package com.example.project_temp2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.User;
import com.example.project_temp2.shared.SignedUser;
import com.google.android.material.textfield.TextInputLayout;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.loginEmail);
        final TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.loginPassword);


        final EditText emailEditText = (EditText) findViewById(R.id.email);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final TextView signup = (TextView) findViewById(R.id.signupMain);
        final Button login = (Button) findViewById(R.id.login);
        final CheckBox rememberMeCheckBox = findViewById(R.id.rememberMe);
        SharedPreferences sharedUser = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String storedEmail = sharedUser.getString("userEmail", "");
        if (!storedEmail.isEmpty()) {
            emailEditText.setText(storedEmail);
            rememberMeCheckBox.setChecked(true);
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordLayout.setErrorEnabled(false);
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailLayout.setErrorEnabled(false);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(Login.this, "project", null, 1);

                Cursor userInfo = dataBaseHelper.getUserByEmail(email);

                if (userInfo.moveToFirst()) {
                    @SuppressLint("Range") String hashedPassword = userInfo.getString(userInfo.getColumnIndex("hashed_password"));

                    if (Validations.checkPassword(password, hashedPassword)) {
                        @SuppressLint("Range") String emailU = (userInfo.getString(userInfo.getColumnIndex("email")).trim());
                        @SuppressLint("Range") String firstName = (userInfo.getString(userInfo.getColumnIndex("first_name")).trim());
                        @SuppressLint("Range") String lastName = (userInfo.getString(userInfo.getColumnIndex("last_name")).trim());
                        @SuppressLint("Range") String gender = (userInfo.getString(userInfo.getColumnIndex("gender")).trim());
                        @SuppressLint("Range") String country = (userInfo.getString(userInfo.getColumnIndex("country")).trim());
                        @SuppressLint("Range") String city = (userInfo.getString(userInfo.getColumnIndex("city")).trim());
                        @SuppressLint("Range") String phoneNumber = (userInfo.getString(userInfo.getColumnIndex("phone_number")).trim());
                        @SuppressLint("Range") String profileURI = (userInfo.getString(userInfo.getColumnIndex("profileURI")));
                        @SuppressLint("Range") int role = (userInfo.getInt(userInfo.getColumnIndex("role")));
                        @SuppressLint("Range") String supervisor = (userInfo.getString(userInfo.getColumnIndex("supervisor")));

                        User userData = new User(emailU, firstName, lastName, gender, hashedPassword, country, city, phoneNumber, role);
                        userData.setSupervisor(supervisor);
                        userData.setProfileURI(profileURI);

                        try {
                            SignedUser.getInstance().user = (User) userData.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }

                        Intent intent;

                        if (rememberMeCheckBox.isChecked()) {
                            SharedPreferences.Editor editor = sharedUser.edit();
                            editor.putString("userEmail", email);
                            editor.apply();
                        } else {
                            SharedPreferences.Editor editor = sharedUser.edit();
                            editor.putString("userEmail", null);
                            editor.apply();
                        }

                        if (role == 1 || role == 2) {
                            intent = new Intent(Login.this, Admin.class);
                        } else {
                            intent = new Intent(Login.this, NormalCustomer.class);
                        }

                        Login.this.startActivity(intent);
                        finish();
                    } else {
                        passwordLayout.setErrorEnabled(true);
                        if (passwordEditText.getText().toString().equals(""))
                            passwordLayout.setError("Enter your password");
                        else
                            passwordLayout.setError("Wrong password");
                    }
                } else {
                    if (!isValidEmail(email)) {
                        emailLayout.setErrorEnabled(true);
                        if (emailEditText.getText().toString().equals(""))
                            emailLayout.setError("Enter your email");
                        else
                            emailLayout.setError("Wrong email");
                    } else
                        Toast.makeText(getApplicationContext(), "You are not registered, click on sign up to register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}