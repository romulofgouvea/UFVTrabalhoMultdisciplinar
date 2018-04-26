package br.com.stevanini.espplay.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.activity.PlaceInfoActivity;
import br.com.stevanini.espplay.adapter.PlacesAdapter;
import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.helpers.Constantes;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragments {

    private RecyclerView mRecyclerView;
    private List<PlaceAPI> placeAPIList = new ArrayList<PlaceAPI>();
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private TextView tvInfoHome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPlaces);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlacesAPI();
    }

    public PlacesAdapter.PlaceOnClickListener onClickPlace() {
        return new PlacesAdapter.PlaceOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                PlaceAPI p = placeAPIList.get(idx);
                log("onClickItem: " + p.toString());

                Intent intent = new Intent(getContext(), PlaceInfoActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("place_api", p);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onLongClickItem(View itemView, int position) {
                toast("onLongClickItem" + position);
            }
        };
    }

    private void initViews(View view) {
        tvInfoHome = (TextView) view.findViewById(R.id.tvInfoHome);
    }

    public void getPlacesAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<PlaceAPI>> listCall = mRetrofitHelper.getItemApi().getListPlaces(
                        Constantes.API_KEY_PLACES,
                        Constantes.API_TOKEN_PLACES
                );
                listCall.enqueue(new Callback<List<PlaceAPI>>() {
                    @Override
                    public void onResponse(Call<List<PlaceAPI>> call, Response<List<PlaceAPI>> response) {
                        if (response.isSuccessful()) {
                            log("response: " + response.body().toString());
                            if (response.body() != null) {
                                tvInfoHome.setVisibility(View.GONE);
                                for (PlaceAPI p : response.body()) {
                                    placeAPIList.add(p);
                                }
                                mRecyclerView.setAdapter(new PlacesAdapter(getContext(), response.body(), onClickPlace()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceAPI>> call, Throwable t) {
                        log("ERRO getPlacesAPI(): " + t.getMessage());
                    }
                });
            }
        }).start();

    }
}