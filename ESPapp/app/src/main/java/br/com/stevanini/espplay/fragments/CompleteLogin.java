package br.com.stevanini.espplay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;

public class CompleteLogin extends BaseFragments {
//
//    private User userF = new User();
//    private EditText etEmail;
//    EditText etNome;
//    EditText etSobrenome;
//    EditText etSenha;
//    EditText etTelefone;
//    EditText etbirthday;
//
//    private String etGender;
//    RadioButton rbMasculino;
//    RadioButton rbFeminino;
//
//    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //fazer outro layout para quem logou na plataforma
//        View view = inflater.inflate(R.layout.fragment_complete_login, container, false);
//
//        initViews(view);
//
//        userF = (User) getArguments().getSerializable("user");
//        if (userF != null) {
//            etNome.setText(userF.getFirst_name());
//            etSobrenome.setText(userF.getLast_name());
//            etEmail.setText(userF.getEmail());
//            etbirthday.setText(userF.getBirthday());
//
//            if (userF.getGender().equals("male")) {
//                rbMasculino.setChecked(true);
//                etGender = "M";
//            } else if (userF.getGender().equals("female")) {
//                rbFeminino.setChecked(true);
//                etGender = "F";
//            }
//
//        }
//
//        return view;
//    }
//
//    private void initViews(final View view) {
//        etNome = (EditText) view.findViewById(R.id.etNome);
//        etSobrenome = (EditText) view.findViewById(R.id.etSobrenome);
//        etEmail = (EditText) view.findViewById(R.id.etEmail);
//        etSenha = (EditText) view.findViewById(R.id.etSenha);
//        etTelefone = (EditText) view.findViewById(R.id.etTelefone);
//        etbirthday = (EditText) view.findViewById(R.id.etbirthday);
//
//        rbMasculino = (RadioButton) view.findViewById(R.id.rbMasculino);
//        rbFeminino = (RadioButton) view.findViewById(R.id.rbFeminino);
//
//        Button btContinuar = (Button) view.findViewById(R.id.btContinuar);
//        btContinuar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                userF.setFirst_name(etNome.getText().toString());
////                userF.setLast_name(etSobrenome.getText().toString());
////                userF.setEmail(etEmail.getText().toString());
////                userF.setPassword(etSenha.getText().toString());
////                onRadioButtonClicked(view);
////                userF.setGender(etGender);
////                userF.setBirthday(etbirthday.getText().toString());
//
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Call<Double> setUserAPI = mRetrofitHelper.getItemApi().setUserAPI(
////                                "Users",
////                                "user_create",
////                                userF.getFirst_name(),
////                                userF.getLast_name(),
////                                userF.getEmail(),
////                                userF.getPassword(),
////                                userF.getGender(),
////                                userF.getBirthday()
////                        );
////                        setUserAPI.enqueue(new Callback<Double>() {
////                            @Override
////                            public void onResponse(Call<Double> call, Response<Double> response) {
////                                if (response.isSuccessful()) {
////                                    log("Resultado > " + response.body());
////                                }
////                            }
////
////                            @Override
////                            public void onFailure(Call<Double> call, Throwable t) {
////                                toast("Erro, Tente Novamente! :)");
////                            }
////                        });
////
////                    }
////                }).start();
//            }
//        });
//    }
//
//    public void onRadioButtonClicked(View view) {
//        RadioGroup rgButton = (RadioGroup) view.findViewById(R.id.rgButton);
//
//        switch (rgButton.getCheckedRadioButtonId()) {
//            case R.id.rbMasculino:
//                rbMasculino.setChecked(true);
//                etGender = "M";
//                break;
//            case R.id.rbFeminino:
//                rbFeminino.setChecked(true);
//                etGender = "F";
//                break;
//        }
//    }

}
