package br.com.stevanini.espplay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.AccessToken;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.domain.UserAddressAPI;
import br.com.stevanini.espplay.fragments.HomeFragment;
import br.com.stevanini.espplay.fragments.ProfileFragment;
import br.com.stevanini.espplay.fragments.SendFragment;
import br.com.stevanini.espplay.utils.Prefs;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private String LOGTAG = "RFG MainActivity";
    public User user = new User();
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private AlertDialog dialog;
    private UserAddressAPI userAddressAPI = new UserAddressAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_custom, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );

        if (AccessToken.getCurrentAccessToken() == null && goVerifyUserPrefs()) {
            goLoginScreen();
        } else {
            try {
                printPrefs();
                user.setUserId(Prefs.getLong(getContext(), "user_id"));
            } catch (NullPointerException e) {
                log("Erro: " + e.getMessage());
            }

            if (savedInstanceState == null) {
                navigation.setSelectedItemId(R.id.navigation_home);
                //goVerifyAddress(user.getUserId());
                //replaceFragment(R.id.flcontainerMain, new HomeFragment());
            }
        }


    }

    private void printPrefs() {
        log("user_id: " + Prefs.getLong(getContext(), "user_id"));
        log("user_name: " + Prefs.getString(getContext(), "user_name"));
        log("user_last_name: " + Prefs.getString(getContext(), "user_last_name"));
        log("user_email: " + Prefs.getString(getContext(), "user_email"));
        log("addr_id: " + Prefs.getLong(getContext(), "addr_id"));
        log("addr_street: " + Prefs.getString(getContext(), "addr_street"));
        log("addr_city: " + Prefs.getString(getContext(), "addr_city"));
        log("addr_state: " + Prefs.getString(getContext(), "addr_state"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goVerifyAddress(user.getUserId());
                    replaceFragment(R.id.flcontainerMain, new HomeFragment());
                    break;
                case R.id.navigation_home_buscar:
                    startActivity(new Intent(getContext(), SearchActivity.class));
                    break;
                case R.id.navigation_home_enviar:
                    goVerifyAddress(user.getUserId());
                    replaceFragment(R.id.flcontainerMain, new SendFragment());
                    break;
//                case R.id.navigation_home_favoritos:
//                    goVerifyAddress(user.getUserId());
//                    replaceFragment(R.id.flcontainerMain, new FavoritesFragment());
//                    break;
                case R.id.navigation_home_perfil:
                    goVerifyAddress(user.getUserId());
                    replaceFragment(R.id.flcontainerMain, new ProfileFragment());
                    break;
            }
            return true;
        }

    };

    public boolean goVerifyUserPrefs() {
        if (Prefs.getLong(getContext(), "user_id") == 0 &&
                Prefs.getString(getContext(), "user_name").equals("") &&
                Prefs.getString(getContext(), "user_last_name").equals("") &&
                Prefs.getString(getContext(), "user_email").equals("")
                ) {
            return true;
        }
        return false;
    }

    public boolean verifyAddrPrefs() {
        if (Prefs.getLong(getContext(), "addr_id") == 0 &&
                Prefs.getString(getContext(), "addr_city").equals("") &&
                Prefs.getString(getContext(), "addr_state").equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void goVerifyAddress(final long id) {
        if (verifyAddrPrefs()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Call<UserAddressAPI> verifyAddress = mRetrofitHelper.getUserApi().getAddressAPI("Users", "user_verify_address", id);
                    verifyAddress.enqueue(new Callback<UserAddressAPI>() {
                        @Override
                        public void onResponse(Call<UserAddressAPI> call, Response<UserAddressAPI> response) {
                            try {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        saveAddrPrefs(response.body());//armazena no prefs
                                    } else {
                                        showDialogCreateAddr();
                                    }
                                }
                            } catch (NullPointerException e) {
                                log("Erro: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserAddressAPI> call, Throwable t) {
                            toast("Erro ao verificar endereço!");
                            Log.i(LOGTAG, "Deu errado! err: " + t.getMessage());
                        }
                    });
                }
            }).start();
        }

    }

    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goSendAddressAPI(final UserAddressAPI userAddr, final String methodAPI) {

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Call<Long> createUserAddres = mRetrofitHelper.getUserApi().setUserAddressCreateAPI(
                            "Users",
                            methodAPI,
                            userAddr.getUserId(),
                            userAddr.getAddrId(),
                            userAddr.getAddrStreet(),
                            userAddr.getAddrNumber(),
                            userAddr.getAddrCity(),
                            userAddr.getAddrState()
                    );

                    createUserAddres.enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    userAddr.setAddrId(response.body());
                                    saveAddrPrefs(userAddr);
                                    showDialogRegisterSucess();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {
                            toast("Erro ao criar endereço!");
                        }
                    });

                }
            }).start();
        } catch (NullPointerException e) {
            log("Erro: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void saveAddrPrefs(UserAddressAPI UserAdd) {
        Prefs.setLong(getContext(), "addr_id", UserAdd.getAddrId());
        Prefs.setString(getContext(), "addr_street", UserAdd.getAddrStreet());
        Prefs.setString(getContext(), "addr_number", UserAdd.getAddrNumber());
        Prefs.setString(getContext(), "addr_city", UserAdd.getAddrCity());
        Prefs.setString(getContext(), "addr_state", UserAdd.getAddrState());
    }

    private void showDialogRegisterSucess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sucesso!");
        builder.setMessage("Seu endereço foi cadastrado com sucesso!");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogCreateAddr() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_address_edit, null);

        final Spinner spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        spinnerCity.setOnItemSelectedListener(this);

        final Spinner spinnerState = (Spinner) view.findViewById(R.id.spinnerState);
        spinnerState.setOnItemSelectedListener(this);

        final EditText etStreet = (EditText) view.findViewById(R.id.etStreet);
        final EditText etNumber = (EditText) view.findViewById(R.id.etNumber);

        Button b = (Button) view.findViewById(R.id.btContinuar);
        b.setVisibility(View.GONE);

        builder.setView(view);
        builder.setTitle("Endereço");
        builder.setPositiveButton("Salvar endereço", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (user != null) {
                        userAddressAPI.setUserId(user.getUserId());

                    }
                    if (etStreet.getText().toString().equals("")) {
                        etStreet.setError("Este campo não pode ser vazio!");
                    } else {
                        userAddressAPI.setAddrStreet(etStreet.getText().toString());
                    }
                    if (etNumber.getText().toString().equals("")) {
                        etNumber.setError("Este campo não pode ser vazio!");
                    } else {
                        userAddressAPI.setAddrNumber(etNumber.getText().toString());
                    }

                    userAddressAPI.setAddrCity(String.valueOf(spinnerCity.getSelectedItem()));
                    userAddressAPI.setAddrState(String.valueOf(spinnerState.getSelectedItem()));

                    if (user != null && !etStreet.getText().toString().equals("") && !etNumber.getText().toString().equals("") && verifyAddrPrefs()) {
                        dialog.dismiss();
                        goSendAddressAPI(userAddressAPI, "user_create_address");
                    } else {
                        dialog.dismiss();
                        goSendAddressAPI(userAddressAPI, "user_update_address");
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
