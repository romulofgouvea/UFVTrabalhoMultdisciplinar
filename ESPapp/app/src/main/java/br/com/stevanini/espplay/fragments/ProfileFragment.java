package br.com.stevanini.espplay.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.activity.LoginActivity;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.domain.UserAddressAPI;
import br.com.stevanini.espplay.utils.Prefs;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragments implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView tvName, tvEmail, tvStreet, tvCityState;
    private AppCompatButton btChangePassword, btLogout, btAddress;
    private android.support.v7.app.AlertDialog dialog;

    private User user = new User();
    private UserAddressAPI userAddressAPI = new UserAddressAPI();
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPrefs();
    }

    private void initViews(View view) {
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvStreet = (TextView) view.findViewById(R.id.tvStreet);
        tvCityState = (TextView) view.findViewById(R.id.tvCityState);

        btAddress = (AppCompatButton) view.findViewById(R.id.btAddress);
        btAddress.setOnClickListener(this);


        btChangePassword = (AppCompatButton) view.findViewById(R.id.btChangePassword);
        btChangePassword.setOnClickListener(this);

        btLogout = (AppCompatButton) view.findViewById(R.id.btLogout);
        btLogout.setOnClickListener(this);

    }

    private void getUserPrefs() {
        try {
            if (user == null || userAddressAPI == null || !goVerifyUserPrefs() && !goVerifyAddrPrefs()) {
                user.setUserId(Prefs.getLong(getContext(), "user_id"));
                user.setUserName(Prefs.getString(getContext(), "user_name"));
                user.setUserLastname(Prefs.getString(getContext(), "user_last_name"));
                user.setUserEmail(Prefs.getString(getContext(), "user_email"));

                userAddressAPI.setUserId(Prefs.getLong(getContext(), "user_id"));
                userAddressAPI.setAddrId(Prefs.getLong(getContext(), "addr_id"));
                userAddressAPI.setAddrStreet(Prefs.getString(getContext(), "addr_street"));
                userAddressAPI.setAddrNumber(Prefs.getString(getContext(), "addr_number"));
                userAddressAPI.setAddrCity(Prefs.getString(getContext(), "addr_city"));
                userAddressAPI.setAddrState(Prefs.getString(getContext(), "addr_state"));
            }

            tvName.setText(user.getUserName() + " " + user.getUserLastname());
            tvEmail.setText(user.getUserEmail());

            tvStreet.setText(userAddressAPI.getAddrStreet() + ", " + userAddressAPI.getAddrNumber());
            tvCityState.setText(userAddressAPI.getAddrCity() + " - " + userAddressAPI.getAddrState());

        } catch (NullPointerException e) {
            log("Erro: Campos nulos " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btAddress:
                showDialogCreateAddrProf();
                break;
            case R.id.btChangePassword:
//                showDialog();
                break;
            case R.id.btLogout:
                logout();
                break;
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout() {
        Prefs.removePreferences(getContext(), "user_id");
        Prefs.removePreferences(getContext(), "user_name");
        Prefs.removePreferences(getContext(), "user_last_name");
        Prefs.removePreferences(getContext(), "user_email");

        Prefs.removePreferences(getContext(), "addr_id");
        Prefs.removePreferences(getContext(), "addr_street");
        Prefs.removePreferences(getContext(), "addr_city");
        Prefs.removePreferences(getContext(), "addr_state");

        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    public void showDialogCreateAddrProf() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    etStreet.setText(userAddressAPI.getAddrStreet());
//                    etNumber.setText(userAddressAPI.getAddrNumber());

                    if (user != null) {
                        userAddressAPI.setUserId(user.getUserId());
                        userAddressAPI.setAddrId(Prefs.getLong(getContext(), "addr_id"));
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

                    if (userAddressAPI.getAddrCity().equals("") && userAddressAPI.getAddrState().equals("")) {
                        userAddressAPI.setAddrCity(String.valueOf(spinnerCity.getSelectedItem()));
                        userAddressAPI.setAddrState(String.valueOf(spinnerState.getSelectedItem()));
                    }

                    if (user != null && !goVerifyAddrPrefs()) {
                        dialog.dismiss();
                        goSendAddressAPIProf(userAddressAPI, "user_update_address");
                    } else {
                        dialog.dismiss();
                        goSendAddressAPIProf(userAddressAPI, "user_create_address");
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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

    public boolean goVerifyAddrPrefs() {
        if (Prefs.getLong(getContext(), "addr_id") == 0 &&
                Prefs.getString(getContext(), "addr_city").equals("") &&
                Prefs.getString(getContext(), "addr_state").equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void goSendAddressAPIProf(final UserAddressAPI userAddr, final String methodAPI) {

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
                                    saveAddrPrefsProf(userAddr);
                                    showDialogRegisterSucessProf();
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

    public void saveAddrPrefsProf(UserAddressAPI UserAdd) {
        Prefs.setLong(getContext(), "addr_id", UserAdd.getAddrId());
        Prefs.setString(getContext(), "addr_street", UserAdd.getAddrStreet());
        Prefs.setString(getContext(), "addr_number", UserAdd.getAddrNumber());
        Prefs.setString(getContext(), "addr_city", UserAdd.getAddrCity());
        Prefs.setString(getContext(), "addr_state", UserAdd.getAddrState());
    }

    private void showDialogRegisterSucessProf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sucesso!");
        builder.setMessage("Seu endereço foi atualizado com sucesso!");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                replaceFragment(R.id.flcontainerMain, new ProfileFragment());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.navigation_support:
                showDialogSupport();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialogSupport() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_support, null);

        final EditText etTelefone = (EditText) view.findViewById(R.id.etTelefone);
        final EditText etEmail = (EditText) view.findViewById(R.id.etEmail);

        builder.setView(view);
        builder.setTitle("Suporte");
        builder.setPositiveButton("Pedir suporte", new DialogInterface.OnClickListener() {
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
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etTelefone.equals(" ")){
                    etTelefone.setError("Este campo nao pode ser vazio!");
                }
                if(etEmail.equals(" ")){
                    etEmail.setError("Este campo nao pode ser vazio!");
                }

                if(!etEmail.equals(" ") || !etTelefone.equals(" ")){
                    toast("Pedido enviado com sucesso!");
                }
            }
        });
    }
}