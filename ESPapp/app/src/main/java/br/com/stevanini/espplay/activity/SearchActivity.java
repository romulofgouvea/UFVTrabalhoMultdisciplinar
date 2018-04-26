package br.com.stevanini.espplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.adapter.PlacesAdapter;
import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.helpers.Constantes;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {

    private FloatingSearchView mSearchView;
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private TextView tvInfoHome;
    private List<PlaceAPI> placeAPIListSearch = new ArrayList<PlaceAPI>();
    private RecyclerView mRecyclerView;

    private String mLastQuery = "";

    public SearchActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupSearchBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rvPlacesSearch);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private void initViews() {
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        tvInfoHome = (TextView) findViewById(R.id.tvInfoHome);
    }

    private void setupSearchBar() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    //mSearchView.showProgress();
                }
                log("oldQuery: " + oldQuery);
                log("newQuery: " + newQuery);
                goSearchAPI(newQuery);

                //pass them on to the search view
//                mSearchView.swapSuggestions(newSuggestions);
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                log("onSuggestionClicked()");
                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                log("onSearchAction()");
                mLastQuery = query;
            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                log("onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());
                log("onFocusCleared()");
            }
        });
    }

    private void goSearchAPI(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<PlaceAPI>> listCall = mRetrofitHelper.getItemApi().getSearchPlaces(
                        Constantes.API_KEY_PLACES,
                        Constantes.API_TOKEN_PLACES,
                        query,
                        "30",
                        "0",
                        "1",
                        "place_name"
                );
                listCall.enqueue(new Callback<List<PlaceAPI>>() {
                    @Override
                    public void onResponse(Call<List<PlaceAPI>> call, Response<List<PlaceAPI>> response) {
                        if (response.isSuccessful()) {
                            log("response: " + response.body().toString());
                            if (response.body() != null) {
                                tvInfoHome.setVisibility(View.GONE);
                                for (PlaceAPI p : response.body()) {
                                    placeAPIListSearch.add(p);
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

    public PlacesAdapter.PlaceOnClickListener onClickPlace() {
        return new PlacesAdapter.PlaceOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                PlaceAPI p = placeAPIListSearch.get(idx);
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

//    private void setupDrawer() {
//        attachSearchViewActivityDrawer(mSearchView);
//    }
}
