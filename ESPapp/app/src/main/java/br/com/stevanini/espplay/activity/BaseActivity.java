package br.com.stevanini.espplay.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public abstract class BaseActivity extends android.support.v7.app.AppCompatActivity {
    private static final String TAG = "RFG BaseActivity";

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
//    protected void setUpToolbar() {
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }
//    }

    public void replaceFragmentStack(int idContainer, Fragment frag) {//MUDAR O FRAGMENT NA MAIN
        getSupportFragmentManager().beginTransaction().replace(idContainer, frag, "TAG").addToBackStack(null).commit();
    }

    public void replaceFragment(int idContainer, Fragment frag) {//MUDAR O FRAGMENT NA MAIN
        getSupportFragmentManager().beginTransaction().replace(idContainer, frag, "TAG").commit();
    }

    public void replaceFragment(int idContainer, Fragment frag, Bundle bundle) {//MUDAR O FRAGMENT NA MAIN
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(idContainer, frag, "TAG").addToBackStack(null).commit();
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
