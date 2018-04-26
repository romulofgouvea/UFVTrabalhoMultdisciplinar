package br.com.stevanini.espplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.helpers.Constantes;


/**
 * Created by ROMULO-NOTE on 03/05/2016.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<PlaceAPI> placeAPIList = new ArrayList<PlaceAPI>();
    private final Context context;
    private PlaceOnClickListener PlaceOnClickListener;
    private String url = null;

    //CONSTRUTOR
    public PlacesAdapter(Context context, List<PlaceAPI> placeAPIList, PlaceOnClickListener PlaceOnClickListener) {
        this.context = context;
        this.placeAPIList = placeAPIList;
        this.PlaceOnClickListener = PlaceOnClickListener;
    }

    //CRIAR UMA VIEW ADAPTER PARA A LISTA
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_places, parent, false);
        return new PlaceViewHolder(view);
    }

    //PEGAR O TAMANHO DA LISTA RECEBIDA
    @Override
    public int getItemCount() {
        return this.placeAPIList != null ? this.placeAPIList.size() : 0;
    }

    //PREENCHER A LISTA RECEBIDA
    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, final int position) {

        holder.pbAdapterPlaces.setVisibility(View.VISIBLE);//ATIVA O PROGRESS BAR

        final PlaceAPI c = placeAPIList.get(position);
        holder.tvNomeAdapterPlaces.setText(c.getPlaceNome());
        holder.tvDescricaoAdapterPlaces.setText(c.getPlaceDescription());

        try {
            url = Constantes.BASE_URL + "uploads/" + c.getPlaceThumb();
            if (c.getPlaceThumb() == null) {
                holder.ivAdapterPlaces.setImageResource(R.drawable.ic_camera_off);
            } else {
                Glide
                        .with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(holder.ivAdapterPlaces);
            }

        } catch (NullPointerException e) {
            Log.d("RFG onBindViewHolder", "Erro: " + e.getMessage());
        }

        Log.i("RFG onBindViewHolder", "place: " + c.getPlaceNome());

        //CLICK DOS ITENS
        if (PlaceOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaceOnClickListener.onClickItem(holder.itemView, position);
                }
            });
        }
        //LONG CLICK
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PlaceOnClickListener.onLongClickItem(holder.itemView, position);
                return true;
            }
        });

        holder.pbAdapterPlaces.setVisibility(View.GONE);//ATIVA O PROGRESS BAR
    }


    public interface PlaceOnClickListener {
        //CLICK
        void onClickItem(View view, int idx);

        //LONG ClICK
        void onLongClickItem(View itemView, int position);
    }

    //CRIAR O METODO EXTENDIDO DE VIEW HOLDER DO RECYCLEVIEW
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAdapterPlaces;
        private TextView tvNomeAdapterPlaces;
        private TextView tvDescricaoAdapterPlaces;
        ProgressBar pbAdapterPlaces;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ivAdapterPlaces = (ImageView) itemView.findViewById(R.id.ivAdapterPlaces);
            tvNomeAdapterPlaces = (TextView) itemView.findViewById(R.id.tvNomeAdapterPlaces);
            tvDescricaoAdapterPlaces = (TextView) itemView.findViewById(R.id.tvDescricaoAdapterPlaces);
            pbAdapterPlaces = (ProgressBar) itemView.findViewById(R.id.pbAdapterPlaces);
        }
    }

}
