package com.mfc.celiacare.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mfc.celiacare.R;

public class AccountFragment extends Fragment {

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDatabase;
    DatabaseReference databaseReference;

    TextView nameTv;
    TextView emailTv;
    Button signOutButton, changePasswordButton;

    public AccountFragment() {

    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();
        databaseReference = fDatabase.getReference("Users");

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        getUserData();
        signOutButton = view.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        changePasswordButton = view.findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void getUserData() {
        if (fUser != null) {
            String name = fUser.getDisplayName();
            String email = fUser.getEmail();

            nameTv.setText(name);
            emailTv.setText(email);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void checkUserStatus() {
        if (fUser == null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_navigation_account_to_navigation_login);
        }
    }

    private void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.change_password));

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 0);

        final EditText currentPasswordEditText = new EditText(requireContext());
        currentPasswordEditText.setHint(getString(R.string.current_password));
        layout.addView(currentPasswordEditText);

        final EditText newPasswordEditText = new EditText(requireContext());
        newPasswordEditText.setHint(getString(R.string.new_password));
        layout.addView(newPasswordEditText);

        final EditText newPassword2EditText = new EditText(requireContext());
        newPassword2EditText.setHint(getString(R.string.repite_contra));
        layout.addView(newPassword2EditText);

        builder.setView(layout);

        builder.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentPassword = currentPasswordEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();
                String newPassword2 = newPassword2EditText.getText().toString().trim();

                String action = checkPassword(currentPassword, newPassword, newPassword2);
                if (action.isEmpty()) {
                    // Cambiar la contraseña del usuario
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                        user.reauthenticate(credential)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        user.updatePassword(newPassword)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(requireContext(), getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(requireContext(), "Error al cambiar la contraseña.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(requireContext(), "Asegúrese de que su contraseña actual esté bien", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(requireContext(), action, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String checkPassword(String currentPassword, String newPassword, String newPassword2) {
        if (currentPassword.isEmpty()) {
            return "Introduzca su contraseña actual.";
        }
        if (newPassword.isEmpty()) {
            return "Introduzca su nueva contraseña.";
        }
        if (newPassword2.isEmpty()) {
            return "Vuelva a introducir su nueva contraseña.";
        }
        if (newPassword.length() < 8) {
            return "Su contraseña nueva tiene que tener más de 8 caracteres.";
        }
        if (!newPassword.equals(newPassword2)) {
            return "Las nuevas contraseñas no coinciden.";
        }
        return ""; // No hay errores
    }


    private void logout() {
        fAuth.signOut();
        fUser = null;
        checkUserStatus();
    }
}