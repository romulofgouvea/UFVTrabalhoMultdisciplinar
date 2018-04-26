package br.com.stevanini.espplay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Locale;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.helpers.Constantes;


public class PlaceInfoActivity extends BaseActivity implements View.OnClickListener {

    ImageView ivImageAcPlace;
    TextView tvName, tvDescription, tvLatLng;
    Button btGoLocation;

    PlaceAPI placeAPI = new PlaceAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        initViews();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            placeAPI = (PlaceAPI) getIntent().getExtras().getSerializable("place_api");
            if (placeAPI != null) {
                // set title action bar
                getSupportActionBar().setTitle(placeAPI.getPlaceNome());

                //Loading Image
                String url = Constantes.BASE_URL + "uploads/" + placeAPI.getPlaceThumb();
                if (placeAPI.getPlaceThumb() == null) {
                    ivImageAcPlace.setImageResource(R.drawable.ic_camera_off);
                } else {
                    Glide.with(getContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .crossFade()
                            .into(ivImageAcPlace);
                }

                tvName.setText(placeAPI.getPlaceNome());
                tvDescription.setText(placeAPI.getPlaceDescription());
                tvLatLng.setText("LATITUDE: " + placeAPI.getPlaceLatitude() + "\nLONGITUDE: " + placeAPI.getPlaceLongitude());
            }
        } catch (NullPointerException e) {
            log("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initViews() {
        ivImageAcPlace = (ImageView) findViewById(R.id.ivImageAcPlace);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvLatLng = (TextView) findViewById(R.id.tvLatLng);

        btGoLocation = (Button) findViewById(R.id.btGoLocation);
        btGoLocation.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btGoLocation:
                //chama o google maps fazendo a rota
                startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse(String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%s,%s", placeAPI.getPlaceLongitude(), placeAPI.getPlaceLatitude() ) ) ) );
                break;
            default:
                toast("Default");
        }
    }
}
