package br.com.stevanini.espplay.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.stevanini.espplay.R;
import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.helpers.Constantes;
import br.com.stevanini.espplay.utils.Prefs;
import br.com.stevanini.espplay.utils.RetrofitAPI.io.RetrofitHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class SendFragment extends BaseFragments implements View.OnClickListener {

    private EditText etNome, etDescription;
    private RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private PlaceAPI placeAPI = new PlaceAPI();
    private ImageView ivImage1,ivImageAP;
    private AlertDialog dialog;
    private TextView tvLatLong,tvtextoAP;

    private Uri fileUri;
    private String mediaPath = null;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 0;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private int PLACE_PICKER_REQUEST = 1;

    private PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send, container, false);
        initViews(view);// inicia as views

        if (!isDeviceSupportCamera()) {// verifica se o dispositivo tem camera
            toast("Esse dispositivo não tem suporte a camera");
            getActivity().finish();
        }

        return view;
    }

    private void initViews(View view) {
        ivImageAP = (ImageView) view.findViewById(R.id.ivImageAP);
        ivImage1 = (ImageView) view.findViewById(R.id.ivImage1);
        ivImage1.setOnClickListener(this);

        etNome = (EditText) view.findViewById(R.id.etNome);
        etDescription = (EditText) view.findViewById(R.id.etDescription);

        tvLatLong = (TextView) view.findViewById(R.id.tvLatLong);
        tvtextoAP = (TextView) view.findViewById(R.id.tvtextoAP);

        Button btGetLocation = (Button) view.findViewById(R.id.btGetLocation);
        btGetLocation.setOnClickListener(this);

        Button btSend = (Button) view.findViewById(R.id.btSend);
        btSend.setOnClickListener(this);
    }

    public boolean goFillPlace() {// preenche o local com os dados obtidos
        if (isEmpty(etNome) || isEmpty(etDescription)) {
            return true;
        } else {
            placeAPI.setPlaceUserId(Prefs.getLong(getContext(), "user_id"));
            placeAPI.setPlaceAddrId(Prefs.getLong(getContext(), "addr_id"));
            placeAPI.setPlaceNome(etNome.getText().toString());
            placeAPI.setPlaceDescription(etDescription.getText().toString());
            return false;
        }
    }

    public void goSendPlace(String patch, final PlaceAPI placeAPISend) {// envia o local
        File file = new File(patch);

        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpg");
//        MediaType MEDIA_TYPE_IMAGE = MediaType.parse("multipart/form-data");

        final RequestBody requestBody = RequestBody.create(MEDIA_TYPE_IMAGE, file);
        MultipartBody.Part imageToUpload = MultipartBody.Part.createFormData("place_thumb", file.getName(), requestBody);

        RequestBody callback = RequestBody.create(MEDIA_TYPE_TEXT, "Places");
        RequestBody callback_action = RequestBody.create(MEDIA_TYPE_TEXT, "place_create");
        RequestBody placeUserId = RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(placeAPISend.getPlaceUserId()));
        RequestBody placeAddrId = RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(placeAPISend.getPlaceAddrId()));
        RequestBody placeId = RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(placeAPISend.getPlaceId()));
        RequestBody place_name = RequestBody.create(MEDIA_TYPE_TEXT, placeAPISend.getPlaceNome());
        RequestBody place_desc = RequestBody.create(MEDIA_TYPE_TEXT, placeAPISend.getPlaceDescription());
        RequestBody place_lat = RequestBody.create(MEDIA_TYPE_TEXT, placeAPISend.getPlaceLatitude());
        RequestBody place_long = RequestBody.create(MEDIA_TYPE_TEXT, placeAPISend.getPlaceLongitude());

        Call<Long> goSend = mRetrofitHelper.getItemApi().sendPlace(
                callback,
                callback_action,
                placeUserId,
                placeAddrId,
                placeId,
                imageToUpload,
                place_name,
                place_desc,
                place_lat,
                place_long
        );
        goSend.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        placeAPI.setPlaceId(response.body());
                        log("goSend onResponse" + response.body());
                        if (response.body() == 1111111111) {
                            toast("Você atingiu o limite de envios por dia! Tente novamente amanha!");
                        } else {
                            //toast("Local enviado com sucesso!");
                        }
                        showDialogPlaceSucess();
                    }
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage1:
                showDialog();
                break;
            case R.id.btGetLocation:
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btSend:
                if (!goFillPlace()) {//preenche Local
                    if (mediaPath != null && placeAPI != null) {
                        goSendPlace(mediaPath, placeAPI);//envia da galeria
                    } else if (fileUri != null && placeAPI != null) {
                        goSendPlace(fileUri.getPath(), placeAPI);//envia da camera
                    } else {
                        toast("Selecione uma imagem e preencha todos os campos!");
                    }
                }
                break;
            default:
                toast("Default");
        }
    }

    public void captureImageCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        SendFragment.this.startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void captureImageGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(galleryIntent, 0);
    }

    private boolean isDeviceSupportCamera() {
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {//existe camera no dispositivo
            return true;
        } else {
            return false;// nao existe camera no dispositivo
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable("file_uri");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (fileUri != null) {
                try {
                    rotateImageIfRequired(BitmapFactory.decodeFile(fileUri.getPath(), options), fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivImage1.setImageBitmap(BitmapFactory.decodeFile(fileUri.getPath(), options));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//recebe o resultado da activity galeria/ camera/ maps
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place placeMAPS = PlacePicker.getPlace(data, getContext());
                    if (placeMAPS.getLatLng() != null) {
                        placeAPI.setPlaceLongitude(String.valueOf(placeMAPS.getLatLng().latitude));
                        placeAPI.setPlaceLatitude(String.valueOf(placeMAPS.getLatLng().longitude));
                        tvLatLong.setText("Latitude: " + placeMAPS.getLatLng().latitude
                                + "\nLongitude: " + placeMAPS.getLatLng().longitude
                        );
                    }
                }
            } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                ivImageAP.setVisibility(View.GONE);
                tvtextoAP.setVisibility(View.GONE);
                if (resultCode == RESULT_OK) {
                    try {

                        log("fileUri: " + fileUri.getPath());

                    } catch (NullPointerException e) {
                        log(e.getMessage());
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    toast("Cancelou a captura da imagem");
                } else {
                    toast("Falhou a captura da imagem!");
                }

            } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                ivImageAP.setVisibility(View.GONE);
                tvtextoAP.setVisibility(View.GONE);

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                ivImage1.setImageBitmap(BitmapFactory.decodeFile(mediaPath, options));
                cursor.close();
            } else {
                toast("Você nao selecionou nenhuma imagem!");
            }
        } catch (Exception e) {
            toast("Algo deu errado!");
            log("Erro onActivityResult(): " + e.getMessage());
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constantes.IMAGE_DIRECTORY_NAME
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                log("Oops! falha em criar diretório");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_image, null);

        Button btImageFoto = (Button) view.findViewById(R.id.btImageFoto);
        btImageFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                captureImageCamera();
            }
        });

        Button btImageGalery = (Button) view.findViewById(R.id.btImageGalery);
        btImageGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                captureImageGallery();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void showDialogPlaceSucess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sucesso");
        builder.setMessage("Seu local foi enviado com sucesso!");
        builder.setPositiveButton("Enviar outro?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                replaceFragment(R.id.flcontainerMain, new SendFragment());
            }
        });
        builder.setNegativeButton("Ver locais", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                replaceFragment(R.id.flcontainerMain, new HomeFragment());
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}