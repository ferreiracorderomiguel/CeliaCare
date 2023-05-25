package com.mfc.celiacare.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword1, editTextPassword2;

    // Declaration of FirebaseAuth instance
    private FirebaseAuth mAuth;

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


    public void volver(View view) {
        finish();
    }

    public void register(View view) {
        // Inputs de la vista
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password1 = editTextPassword1.getText().toString();
        String password2 = editTextPassword2.getText().toString();

        if(checkAccountFormat(name, email, password1, password2)){
            registerUser(email, password1, name);
        }
    }

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