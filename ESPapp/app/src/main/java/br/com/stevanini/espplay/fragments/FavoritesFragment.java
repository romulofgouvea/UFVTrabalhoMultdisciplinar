package br.com.stevanini.espplay.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.stevanini.espplay.R;

public class FavoritesFragment extends BaseFragments {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}