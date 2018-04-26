package br.com.stevanini.espplay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.fragments.RegisterFragment;
import br.com.stevanini.espplay.utils.Prefs;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.stevanini.espplay.utils.Utils.stringCryptografy;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private String LOGTAG = "RFG LoginActivity";
    private LoginButton loginButton;
    private TextView tv_register, tv_forgot_pass;
    private CallbackManager callbackManager;
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private User user = new User();
    private boolean loginOrRegister = false;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        intiMethods();
    }

    private void initViews() {
        //BOTAO LOGIN ESP API
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);

        //BOTAO LOGIN FACEBOOK
        ImageButton ibLoginFacebook = (ImageButton) findViewById(R.id.ibLoginFacebook);
        ibLoginFacebook.setOnClickListener(this);

        //BOTAO LOGIN GMAIL
        ImageButton ibLoginGoogle = (ImageButton) findViewById(R.id.ibLoginGoogle);
        ibLoginGoogle.setOnClickListener(this);

        //BOTAO JA TEM CONTA?
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);

        //BOTAO ESQUECEU SENHA
        tv_forgot_pass = (TextView) findViewById(R.id.tv_forgot_pass);
        tv_forgot_pass.setOnClickListener(this);
    }

    private void intiMethods() {
        fbLogin();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btLogin:
                toast("Login API ESPplay (Em construção) Somente LOGIN com FACEBOOK");
                break;
            case R.id.tv_register:
                loginOrRegister = false;
                replaceFragmentStack(R.id.rvContainerLogin, new RegisterFragment());
                break;
            case R.id.ibLoginFacebook:
                loginOrRegister = true;
                loginButton.performClick();
                break;
            case R.id.ibLoginGoogle:
                toast("Login Google (Em construção) Somente LOGIN com FACEBOOK");
                break;
            case R.id.tv_forgot_pass:
                toast("Esqueci minha senha! (Em construção) Somente LOGIN com FACEBOOK");
                break;
        }

    }

    public void fbLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetailsFromFB(loginResult);
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

    public void getUserDetailsFromFB(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    user.setUserIdFb(Long.valueOf(object.getString("id")));

                    if (user.getUserIdFb() != null)
                        user.setUserCover("http://graph.facebook.com/" + user.getUserIdFb() + "/picture?type=large");

                    user.setUserName(object.getString("first_name"));
                    user.setUserLastname(object.getString("last_name"));
                    user.setUserEmail(object.getString("email"));

                    String userGenre = null;
                    if (object.getString("gender").equals("male")) {
                        userGenre = "M";
                    } else if (object.getString("gender").equals("female")) {
                        userGenre = "F";
                    }
                    user.setUserGenre(userGenre);

                    user.setUserDatebirth(object.getString("birthday"));
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                verifyEmailAPI(user);//veiricar email,senha e se ele é do facebook;
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void verifyEmailAPI(final User userM) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> verifyemail = mRetrofitHelper.getUserApi().getEmailAPI("Users", "user_verify_email", userM.getUserEmail());
                verifyemail.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
//                            log("loginOrRegister: " + loginOrRegister);
//                            log("response.body(): " + response.body());
                            if (loginOrRegister && response.body()) {
                                getUserAPI(user.getUserEmail());//login
                            } else if (!loginOrRegister && response.body()) {
                                LoginManager.getInstance().logOut();
                                showDialogEmailTrue("Verificação", "Este email ja se encontra em nosso sistema, deseja logar?", "Sim", "Não");
                            } else if (loginOrRegister && !response.body()) {
                                LoginManager.getInstance().logOut();
                                showDialogRegister();
                            } else if (!loginOrRegister && !response.body()) {
                                LoginManager.getInstance().logOut();
                                if (userM.getUserPassword() == null) {
                                    userM.setUserPassword(stringCryptografy("ESPplayPassword"));
                                }
                                goRegisterUser(userM);
                            }
                        } else {
                            LoginManager.getInstance().logOut();
                            toast("Ocorreu um erro!");
//
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        log("Erro ao verificar email, Tente Novamente! :)");
                        LoginManager.getInstance().logOut();
                    }
                });
            }
        }).start();
    }

    private void getUserAPI(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<User> userAPI = mRetrofitHelper.getUserApi().getUserAPI("Users", "user_get", email);
                userAPI.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                response.body().setUserIdFb(user.getUserIdFb());
                                response.body().setUserCover("http://graph.facebook.com/" + user.getUserIdFb() + "/picture?type=large");
                                response.body().setUserGenre(user.getUserGenre());

                                goMainScreen(response.body());
                            } else {
                                LoginManager.getInstance().logOut();
                                showDialogRegister();
                                log("Erro ao buscar dados do usuário!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        toast("Erro ao buscar dados do usuário!");
                    }
                });
            }
        }).start();
    }

    public void goMainScreen(User userParam) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        goSaveUserPrefs(userParam);
        intent.putExtra("user", userParam);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goSaveUserPrefs(User userParam) {
        Prefs.setLong(getContext(), "user_id", userParam.getUserId());
        Prefs.setString(getContext(), "user_name", userParam.getUserName());
        Prefs.setString(getContext(), "user_last_name", userParam.getUserLastname());
        Prefs.setString(getContext(), "user_email", userParam.getUserEmail());
    }

    public void goRegisterUser(final User userF) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Double> setUserAPI = mRetrofitHelper.getItemApi().setUserCreateAPI(
                        "Users",
                        "user_create",
                        userF.getUserIdFb(),
                        user.getUserCover(),
                        userF.getUserName(),
                        userF.getUserLastname(),
                        userF.getUserEmail(),
                        userF.getUserPassword(),
                        userF.getUserGenre(),
                        userF.getUserDatebirth()
                );
                setUserAPI.enqueue(new Callback<Double>() {
                    @Override
                    public void onResponse(Call<Double> call, Response<Double> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != 0) {
                                showDialogRegisterSucess();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Double> call, Throwable t) {
                        log("Erro ao registrar, Tente Novamente! :)");
                        LoginManager.getInstance().logOut();
                    }
                });

            }
        }).start();
    }

    private void showDialogRegisterSucess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sucesso!");
        builder.setMessage("Seu cadastro no sistema foi realizado com sucesso, agora voce deve logar com seu usuario!");
        builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginOrRegister = true;
                getActivity().onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogRegister() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Aviso");
        builder.setMessage("Esse usuario ainda nao é cadastrado em nosso sitema, deseja se cadastrar agora?");

        builder.setPositiveButton("Quero me cadastrar agora!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginOrRegister = false;
                replaceFragmentStack(R.id.rvContainerLogin, new RegisterFragment());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogEmailTrue(String title, String msg, String pButton, String nButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        if (!pButton.equals("")) {
            builder.setPositiveButton(pButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loginOrRegister = true;
                    getActivity().onBackPressed();
                }
            });
        }
        if (!nButton.equals("")) {
            builder.setNegativeButton(nButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

//
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}