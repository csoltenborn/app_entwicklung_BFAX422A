package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class SignInFragment extends Fragment {

    private GoogleSignInClient googleSignInClient;

    private ActivityResultLauncher<Intent> googleSignInLauncher;
    public SignInFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Context context = getContext();
        if(context == null) return;

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

        googleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != AppCompatActivity.RESULT_OK) {
                    openErrorMessage(new Exception("Result Code isn't OK. Result Code is: " + result.getResultCode()));
                    return;
                }

                Intent data = result.getData();

                if(data == null) {
                    openErrorMessage(new Exception("Data from sign in is null!"));
                    return;
                }

                GoogleSignIn
                    .getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(this::openMainActivity)
                    .addOnFailureListener(this::openErrorMessage);
            }
        );
    }

    private void openErrorMessage(Exception e) {
        View view = getView();
        if(view == null) return;

        getErrorView(view).setVisibility(View.VISIBLE);
        Log.e("SignIn", "Couldn't sign in correctly: ", e);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignInButton signInButton = getSignInButton(view);

        getErrorView(view).setVisibility(View.INVISIBLE);

        signInButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    private void openMainActivity(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.putExtra("USER_NAME", googleSignInAccount.getDisplayName());
        startActivity(intent);
    }

    public SignInButton getSignInButton (View view) {
        return view.findViewById(R.id.sign_in_button);
    }

    public TextView getErrorView (View view) {
        return view.findViewById(R.id.sign_in_error_text);
    }
}