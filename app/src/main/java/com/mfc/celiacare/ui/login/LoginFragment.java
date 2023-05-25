package com.mfc.celiacare.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mfc.celiacare.R;

import java.util.HashMap;

public class LoginFragment extends Fragment {

    EditText editTextCorreo, editTextContra;
    TextView textoRegistro, textViewPassword;
    Button botonInicioSesion;
    CheckBox checkBoxVerContra;
    private FirebaseAuth fAuth;
    private String lastEmailNoAuth = "";
    private int triesLoginNoAuth = 0;

    public LoginFragment() {
        super();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //checkUserStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextCorreo = view.findViewById(R.id.editTextCorreo);
        editTextContra = view.findViewById(R.id.editTextContra);

        textoRegistro = view.findViewById(R.id.textoRegistro);
        textoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAFragmentRegistro();
            }
        });

        textViewPassword = view.findViewById(R.id.textForgotPassword);
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        checkBoxVerContra = view.findViewById(R.id.checkBoxVerContra);
        final EditText editTextContra = view.findViewById(R.id.editTextContra);

        checkBoxVerContra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextContra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editTextContra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        botonInicioSesion = view.findViewById(R.id.botonInicioSesion);
        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void irAFragmentRegistro() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    private void iniciarSesion() {
        String email = editTextCorreo.getText().toString();
        String password = editTextContra.getText().toString();

        if (checkAccountValidation(email, password)) {
            fAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = fAuth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    hashMap.put("email", email);
                                    hashMap.put("uid", uid);
                                    hashMap.put("name", user.getDisplayName());
                                    hashMap.put("image", "");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users");
                                    reference.child(uid).setValue(hashMap);
                                    getActivity().onBackPressed();
                                } else {
                                    if(checkNoVerification(email)){
                                        user.sendEmailVerification();
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.email_not_verified), Toast.LENGTH_SHORT).show();
                                    }
                                    fAuth.signOut();
                                    editTextContra.setText("");
                                }
                            }
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), getString(R.string.email_password_incorrect), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean checkNoVerification(String email) {
        if (lastEmailNoAuth.equals(email)) {
            if (triesLoginNoAuth > 2) {
                Toast.makeText(getActivity(), getString(R.string.email_verification_sent), Toast.LENGTH_SHORT).show();
                triesLoginNoAuth = 0;
                return true;
            } else {
                triesLoginNoAuth++;
                return false;
            }
        } else {
            lastEmailNoAuth = email;
            triesLoginNoAuth = 0;
            return false;
        }
    }

    private boolean checkAccountValidation(String email, String password) {
        if (email.isEmpty()) {
            editTextCorreo.setError(getString(R.string.email_required));
            editTextCorreo.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextCorreo.setError(getString(R.string.email_format));
            editTextCorreo.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            editTextContra.setError(getString(R.string.password_required));
            editTextContra.requestFocus();
            return false;
        } else if (password.length() < 8) {
            editTextContra.setError(getString(R.string.password_length));
            editTextContra.requestFocus();
            return false;
        }
        return true;
    }

    private void forgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.recover_password));

        LinearLayout linearLayout = new LinearLayout(getActivity());
        EditText editTextEmail = new EditText(getActivity());
        editTextEmail.setHint("Email");
        editTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(editTextEmail);
        linearLayout.setPadding(10, 10, 10, 10);
        editTextEmail.setMinEms(16);

        builder.setView(linearLayout);

        builder.setPositiveButton(getString(R.string.recover_password), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = editTextEmail.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void beginRecovery(String email) {
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), getString(R.string.email_not_sent), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.email_sent), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getString(R.string.email_not_sent), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void checkUserStatus() {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_navigation_login_to_navigation_account);
        }
    }*/
}