package com.example.project_temp2.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.example.project_temp2.R;
import com.example.project_temp2.Validations;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.databinding.FragmentProfileBinding;
import com.example.project_temp2.shared.SignedUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;
    ProgressDialog pd;
    Uri imageUri;
    private Uri selectedImageUri;
    private FragmentProfileBinding binding;
    private DataBaseHelper dataBaseHelper;
    private String firstName;
    private String lastName;
    private static String hashed_password;
    TextView nameProfileView, phoneView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pd = new ProgressDialog(requireContext());
        pd.setCanceledOnTouchOutside(false);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dataBaseHelper = new DataBaseHelper(requireContext(), "project", null, 1);

        CircleImageView profileImage = (CircleImageView) root.findViewById(R.id.profileImage);
        CircleImageView img1 = (CircleImageView) root.findViewById(R.id.img1);
        CircleImageView img2 = (CircleImageView) root.findViewById(R.id.img2);
        CircleImageView editImage = (CircleImageView) root.findViewById(R.id.clickToEdit);
        TextView emailProfileView = (TextView) root.findViewById(R.id.emailProfileView);
        nameProfileView = (TextView) root.findViewById(R.id.nameProfileView);
        phoneView = (TextView) root.findViewById(R.id.phoneView);


        ImageView editName = (ImageView) root.findViewById(R.id.edit1);
        ImageView editPassword = (ImageView) root.findViewById(R.id.edit2);
        ImageView editPhone = (ImageView) root.findViewById(R.id.edit3);


        img1.setImageResource(R.color.white);
        img2.setImageResource(R.color.cyan);
        editImage.setImageResource(R.drawable.gallery);
        dataBaseHelper = new DataBaseHelper(requireContext(), "project", null, 1);

        Cursor cursor = dataBaseHelper.getUserData(SignedUser.getInstance().user.getEmail());
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
            @SuppressLint("Range") String lastname = cursor.getString(cursor.getColumnIndex("last_name"));
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
            @SuppressLint("Range") String profile_img_path = cursor.getString(cursor.getColumnIndex("profileURI"));

            int hashedPasswordIndex = cursor.getColumnIndex("hashed_password");
            if (hashedPasswordIndex >= 0) {
                hashed_password = cursor.getString(hashedPasswordIndex);
            } else {
                Log.e("YourTag", "Column 'hashed_password' not found in the cursor");
            }

            emailProfileView.setText(email);
            nameProfileView.setText(firstName + " " + lastname);
            phoneView.setText(phoneNumber);
            if (profile_img_path != null && !profile_img_path.isEmpty()) {
                Glide.with(this)
                        .load(profile_img_path)
                        .into(profileImage);
            } else {
                // Set a default image or handle the absence of an image
                profileImage.setImageResource(R.drawable.khaled);
            }

            this.firstName = firstName;
            this.lastName = lastname;
        }

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root.getContext(), "Click me to edit your picture", Toast.LENGTH_SHORT).show();
                pd.setMessage("Updating Profile Picture");
                showImagePicDialog();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root.getContext(), "Click me to edit your name", Toast.LENGTH_SHORT).show();
                pd.setMessage("Updating First Name");
                showEditNameChangeDialog();
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root.getContext(), "Click me to edit your password", Toast.LENGTH_SHORT).show();
                pd.setMessage("Changing Password");
                showPasswordChangeDialog();
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root.getContext(), "Click me to edit your phone number", Toast.LENGTH_SHORT).show();
                pd.setMessage("Updating Phone Number");
                showPhoneNumberChangeDialog();
            }
        });


        return root;
    }


    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void showEditNameChangeDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_name, null);
        final EditText firstNameEditText = view.findViewById(R.id.newFirstNameLog);
        final EditText lastNameEditText = view.findViewById(R.id.newLastNameLog);
        firstNameEditText.setText(this.firstName);
        lastNameEditText.setText(this.lastName);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFirstName = firstNameEditText.getText().toString().trim();
                String newlastName = lastNameEditText.getText().toString().trim();

                if (!newFirstName.isEmpty() && !lastName.isEmpty()) {
                    if (Validations.isValidName(newFirstName) && Validations.isValidName(newlastName))
                        updateName(newFirstName, newlastName);
                    else
                        Toast.makeText(requireContext(), " Name Not Valid", Toast.LENGTH_SHORT).show();
                } else if (newFirstName.isEmpty()) {
                    Toast.makeText(requireContext(), "First Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Last Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showPasswordChangeDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_password, null);
        final EditText oldPassEditText = view.findViewById(R.id.oldpasslog);
        final EditText newPassEditText = view.findViewById(R.id.newpasslog);
        final EditText ConfirmNewPassEditText = view.findViewById(R.id.confirmnewpasslog);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPass = oldPassEditText.getText().toString().trim();
                String newPass = newPassEditText.getText().toString().trim();
                String confirmNewPass = ConfirmNewPassEditText.getText().toString().trim();

                if (areFieldsNotEmpty(oldPass, newPass, confirmNewPass)) {
                    if (Validations.checkPassword(oldPass, hashed_password)) {
                        if (Validations.isValidPassword(newPass)) {
                            if (newPass.equals(confirmNewPass)) {
                                updatePassword(newPass);
                            } else {
                                showToast("Passwords do not match");
                            }
                        } else {
                            showToast("Invalid password format");
                        }
                    } else {
                        showToast("Incorrect old password");
                    }
                } else {
                    showToast("Passwords cannot be empty");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showPhoneNumberChangeDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_phone, null);
        final EditText phoneNumberEditText = view.findViewById(R.id.newphonelog);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPhoneNumber = phoneNumberEditText.getText().toString().trim();

                if (!newPhoneNumber.isEmpty()) {
                    updatePhoneNumber(newPhoneNumber);
                } else {
                    Toast.makeText(requireContext(), "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // Method to pick image from gallery
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_REQUEST);
    }


    private void pickFromCamera() {
        try {
            File photoFile = createImageFile();
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.project_temp2.fileprovider",
                        photoFile
                );

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, IMAGE_PICKCAMERA_REQUEST);

            } else {
                Log.e("ImageCreation", "Failed to create image file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageCreation", "IOException during image file creation: " + e.getMessage());
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_REQUEST) {
                // User picked an image from the gallery
                selectedImageUri = data.getData();
                uploadProfileImage(selectedImageUri);
            } else if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                // User captured an image from the camera
                uploadProfileImage(imageUri);
            }
        }
    }

    private void uploadProfileImage(Uri imageUri) {
        if (imageUri != null) {
            String imageUriString = imageUri.toString();
            updateProfileImage(imageUriString);
        }
    }

    private void updateProfileImage(String imagePath) {
        CircleImageView profileImage = (CircleImageView) getView().findViewById(R.id.profileImage);

        // Use Glide to load and display the image
        Glide.with(this)
                .load(imagePath)
                .into(profileImage);
        dataBaseHelper.updateProfileImagePath(SignedUser.getInstance().user.getEmail(), imagePath);
        Toast.makeText(requireContext(), "Profile image has been successfully updated!", Toast.LENGTH_SHORT).show();

    }

    private void updateName(String newFirstName, String newLastName) {
        ContentValues newValues = new ContentValues();
        newValues.put("first_name", newFirstName);
        newValues.put("last_name", newLastName);
        dataBaseHelper.updateUserProfile(SignedUser.getInstance().user.getEmail(), newValues);
        Toast.makeText(requireContext(), "Your name has been successfully updated!", Toast.LENGTH_SHORT).show();
        this.firstName = newFirstName;
        this.lastName = newLastName;
        nameProfileView.setText(newFirstName + " " + newLastName);
    }

    public void updatePassword(String newHashedPassword) {
        ContentValues newPassword = new ContentValues();
        String newPasswordHased = Validations.hashPassword(newHashedPassword.toString());
        newPassword.put("hashed_password", newPasswordHased);
        dataBaseHelper.updateUserProfile(SignedUser.getInstance().user.getEmail(), newPassword);
        Toast.makeText(requireContext(), "Your Password name has been successfully updated!", Toast.LENGTH_SHORT).show();

    }

    private void updatePhoneNumber(String newPhoneNumber) {
        ContentValues newValues = new ContentValues();
        newValues.put("phone_number", newPhoneNumber);
        dataBaseHelper.updateUserProfile(SignedUser.getInstance().user.getEmail(), newValues);
        Toast.makeText(requireContext(), "Your Phone Number has been successfully updated!", Toast.LENGTH_SHORT).show();
        phoneView.setText(newPhoneNumber);

    }

    private boolean checkCameraPermission() {


        int cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private boolean checkStoragePermission() {
        int storagePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private boolean areFieldsNotEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}