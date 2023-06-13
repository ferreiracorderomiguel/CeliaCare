package com.mfc.celiacare.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mfc.celiacare.R;

import java.util.HashMap;

/**
 * The RegisterActivity class represents an activity that handles the user registration process.
 * It allows users to create a new account by providing their name, email, and password.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword1, editTextPassword2;

    private FirebaseAuth mAuth;

    /**
     * Called when the activity is starting or restarting.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword1 = findViewById(R.id.editTextPassword1);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        mAuth = FirebaseAuth.getInstance();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Handles the action when the "Back" button is clicked.
     *
     * @param view The view that was clicked.
     */
    public void volver(View view) {
        finish();
    }

    /**
     * Handles the action when the "Register" button is clicked.
     *
     * @param view The view that was clicked.
     */
    public void register(View view) {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password1 = editTextPassword1.getText().toString();
        String password2 = editTextPassword2.getText().toString();

        if(checkAccountFormat(name, email, password1, password2)){
            registerUser(email, password1, name);
        }
    }

    /**
     * Registers a new user with the provided email, password, and name.
     *
     * @param email     The email address of the user.
     * @param password1 The password of the user.
     * @param name      The name of the user.
     */
    private void registerUser(String email, String password1, String name) {
        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);

                            HashMap<Object, String> hashMap = new HashMap<>();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("image", "");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, R.string.successful_register, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, R.string.failed_register, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Checks the format of the user account information (name, email, password).
     *
     * @param name      The name of the user.
     * @param email     The email address of the user.
     * @param password1 The password of the user.
     * @param password2 The confirmation password of the user.
     * @return True if the format of the user account information is valid, false otherwise.
     */
    private boolean checkAccountFormat(String name, String email, String password1, String password2){
        if (name == null || name.isEmpty()){
            editTextName.setError(getString(R.string.name_required));
            editTextName.requestFocus();
            return false;
        } else if (email == null || email.isEmpty()){
            editTextEmail.setError(getString(R.string.email_required));
            editTextEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.email_format));
            editTextEmail.requestFocus();
            return false;
        } else if (password1 == null || password1.isEmpty()){
            editTextPassword1.setError(getString(R.string.password_required));
            editTextPassword1.requestFocus();
            return false;
        } else if (password1.length() < 8){
            editTextPassword1.setError(getString(R.string.password_length));
            editTextPassword1.requestFocus();
            return false;
        } else if (password2 == null || password2.isEmpty()){
            editTextPassword2.setError(getString(R.string.password_required));
            editTextPassword2.requestFocus();
            return false;
        } else if (!password1.equals(password2)){
            editTextPassword2.setError(getString(R.string.password_match));
            editTextPassword2.requestFocus();
            return false;
        }
        return true;
    }
}