package br.com.stevanini.espplay;

import android.app.Application;
import android.content.Context;


public class ESPplayApplication extends Application{
    private static final String TAG = "ESPplayApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);//        MultiDex.install(this);
    }

    // Singleton
    private static ESPplayApplication instance = null;
    public static ESPplayApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//salva a instancia para termos acesso como singleton
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
