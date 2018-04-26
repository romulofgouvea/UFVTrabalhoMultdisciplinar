package br.com.stevanini.espplay.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.activity.LoginActivity;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends BaseFragments implements View.OnClickListener {

    private AppCompatButton btn_register;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private EditText et_email, et_password, et_name;
    private TextView tv_login;
    private ImageButton ibRegisterFacebook;
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private User user = new User();
    private LoginActivity loginActivity = new LoginActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        fbLogin(view);
        return view;
    }

    private void initViews(View view) {
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_password = (EditText) view.findViewById(R.id.et_password);

        btn_register = (AppCompatButton) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        ibRegisterFacebook = (ImageButton) view.findViewById(R.id.ibRegisterFacebook);
        ibRegisterFacebook.setOnClickListener(this);

        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                toast("Regisgter API ESPplay (Em construção) Somente LOGIN com FACEBOOK");
//                goRegisterAPIESP();
                break;
            case R.id.ibRegisterFacebook:
                loginButton.performClick();
                break;
            case R.id.tv_login:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    public void fbLogin(View view) {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.loginButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginActivity.getUserDetailsFromFB(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goRegisterAPIESP() {
        user.setUserName(et_name.getText().toString());
        user.setUserEmail(et_email.getText().toString());
        user.setUserPassword(et_password.getText().toString());

        if (!user.getUserName().isEmpty() && !user.getUserEmail().isEmpty() && !user.getUserPassword().isEmpty()) {
            goregisterAPIESPVerify(user.getUserEmail(), user.getUserPassword());
        } else {
            Snackbar.make(getView(), "Os campos nome, email e senha não podem ser vazios!", Snackbar.LENGTH_LONG).show();
        }

    }

    public void goregisterAPIESPVerify(final String email, final String pass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> verifyuser = mRetrofitHelper.getUserApi().getVerifyUserAPI("Users", "user_verify_email_pass", email, pass);
                verifyuser.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loginActivity.goRegisterUser(user);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                    }
                });
            }
        }).start();
    }

}
