package com.example.project_temp2;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.User;
import com.example.project_temp2.shared.SignedUser;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class Signup extends AppCompatActivity {

    private TextInputLayout emailLayout, firstNameLayout, lastNameLayout, passwordLayout, confirmPasswordLayout, phoneNumberLayout, genderLayout, countryLayout, cityLayout;
    private EditText email, firstName, lastName, password, confirmPassword, phoneNumber;
    private AutoCompleteTextView countrySpinner, citySpinner, genderSpinner;

    private ImageView back;
    private String selectedCountry;

    private DataBaseHelper databaseHelper;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (getIntent().getBooleanExtra("fromAdminFragment", false)) {
            TextView title = findViewById(R.id.textView4);
            title.setText("Admin");
            btn = findViewById(R.id.signupButton);
            btn.setTextSize(16);
            btn.setText("Add Admin");
        }


        databaseHelper = new DataBaseHelper(Signup.this, "project", null, 1);

        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        firstNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        lastNameLayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        confirmPasswordLayout = (TextInputLayout) findViewById(R.id.confirmPasswordLayout);
        phoneNumberLayout = (TextInputLayout) findViewById(R.id.phoneLayout);
        countryLayout = (TextInputLayout) findViewById(R.id.countryLayout);
        genderLayout = (TextInputLayout) findViewById(R.id.genderLayout);
        cityLayout = (TextInputLayout) findViewById(R.id.cityLayout);


        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        phoneNumber = (EditText) findViewById(R.id.phone);


        citySpinner = (AutoCompleteTextView) findViewById(R.id.city);
        genderSpinner = (AutoCompleteTextView) findViewById(R.id.gender);
        countrySpinner = (AutoCompleteTextView) findViewById(R.id.country);

        back = (ImageView) findViewById(R.id.backLogin);

        email.setImeActionLabel("", EditorInfo.IME_ACTION_NEXT);

        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        genderSpinner.setAdapter(dataAdapter);

        List<String> countries = databaseHelper.getAllCountries();

        ArrayAdapter<String> spinner2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(spinner2);

        countrySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                citySpinner.setText("");
                selectedCountry = countrySpinner.getText().toString().trim();
                populateCitySpinner(selectedCountry);
                setPhoneCode(selectedCountry);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                if (getIntent().getBooleanExtra("fromAdminFragment", false)) {
                    intent = new Intent(Signup.this, Admin.class);
                } else
                    intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        clearStatus();


        Button btn = (Button) findViewById(R.id.signupButton);
        btn.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void populateCitySpinner(String selectedCountry) {
        List<String> cities = databaseHelper.getCitiesByCountryName(selectedCountry);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        citySpinner.setAdapter(cityAdapter);

    }


    private void setPhoneCode(String selectedCountry) {
        String phoneCode = databaseHelper.getPhoneCodeByCountryName(selectedCountry);
        phoneNumberLayout.setPrefixText(phoneCode);
    }

    public void signUp() {
        String userEmail = email.getText().toString().trim();
        String userFirstName = firstName.getText().toString().trim();
        String userLastName = lastName.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();
        String userPhoneNumber = phoneNumber.getText().toString().trim();
        String userCountry = countrySpinner.getText().toString().trim();
        String userGender = genderSpinner.getText().toString().trim();
        String userCity = citySpinner.getText().toString().trim();


        if (!Validations.isValidEmail(userEmail)) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Invalid email address");
            return;
        } else {
            emailLayout.setError(null);
        }

        if (!Validations.isValidName(userFirstName)) {
            firstNameLayout.setErrorEnabled(true);
            firstNameLayout.setError("Invalid first name");
            return;
        } else {
            firstNameLayout.setError(null);
        }

        if (!Validations.isValidName(userLastName)) {
            lastNameLayout.setErrorEnabled(true);
            lastNameLayout.setError("Invalid last name");
            return;
        } else {
            lastNameLayout.setError(null);
        }

        if (TextUtils.isEmpty(userGender)) {
            genderLayout.setErrorEnabled(true);
            genderLayout.setError("Choose your gender");
            return;
        } else {
            genderLayout.setError(null);
        }

        if (!Validations.isValidPassword(userPassword)) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Invalid password format");
            showDetailedErrorMessage("Password must be at least 5 characters, starts with alphabetic and contain at least 1 character, 1 number, and 1 special character.\n", passwordLayout);
            return;
        } else {
            passwordLayout.setError(null);
        }

        if (!userPassword.equals(userConfirmPassword)) {
            confirmPasswordLayout.setErrorEnabled(true);
            confirmPasswordLayout.setError("Passwords do not match");
            return;
        } else {
            confirmPasswordLayout.setError(null);
        }

        if (TextUtils.isEmpty(userCountry)) {
            countryLayout.setErrorEnabled(true);
            countryLayout.setError("Choose your country");
            return;
        } else {
            countryLayout.setError(null);
        }

        if (TextUtils.isEmpty(userCity)) {
            cityLayout.setErrorEnabled(true);
            cityLayout.setError("Choose your city");
            return;
        } else {
            cityLayout.setError(null);
        }

        if (!Validations.isValidPhoneNumber(userPhoneNumber)) {
            phoneNumberLayout.setErrorEnabled(true);
            phoneNumberLayout.setError("Invalid phone number");
            return;
        } else {
            phoneNumberLayout.setError(null);
        }


        String hashedPassword = Validations.hashPassword(userPassword);
        String phoneNumberWithCode = addPrefix(userPhoneNumber);

        if (getIntent().getBooleanExtra("fromAdminFragment", false)) {
            User admin = new User(userEmail, userFirstName, userLastName, userGender, hashedPassword, userCountry, userCity, phoneNumberWithCode, 2);
            admin.setSupervisor(SignedUser.getInstance().user.getEmail());
            databaseHelper.insertUser(admin);
            showToast("Add Admin successful");
            Intent intent = new Intent(Signup.this, Admin.class);
            startActivity(intent);
        } else {
            User user = new User(userEmail, userFirstName, userLastName, userGender, hashedPassword, userCountry, userCity, phoneNumberWithCode, 0);
            databaseHelper.insertUser(user);
            showToast("Registration successful");
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
        }

    }

    private void clearStatus() {
        email.addTextChangedListener(new TextWatcher() {
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

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstNameLayout.setErrorEnabled(false);
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastNameLayout.setErrorEnabled(false);
            }
        });

        genderSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                genderLayout.setErrorEnabled(false);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
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

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirmPasswordLayout.setErrorEnabled(false);
            }
        });

        countrySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                countryLayout.setErrorEnabled(false);
            }
        });

        citySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                cityLayout.setErrorEnabled(false);
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phoneNumberLayout.setErrorEnabled(false);
            }
        });

    }

    private void showDetailedErrorMessage(String detailedErrorMessage, View anchorView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showAlertDialog(detailedErrorMessage, anchorView);
        } else {
            showToast(detailedErrorMessage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAlertDialog(String detailedErrorMessage, View anchorView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password format")
                .setMessage(detailedErrorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String addPrefix(String phoneNumber) {
        String phoneCode = phoneNumberLayout.getPrefixText().toString();
        return phoneCode + phoneNumber;
    }
}