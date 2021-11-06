package br.fernando.controlekm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.fernando.controlekm.R;

/**
 * Created by Flavia on 02/12/2017.
 */

public class SplashScreenKM extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_km);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarMenuPrincipal();
            }
        }, 2000);

    }

    private void mostrarMenuPrincipal() {
        Intent intent = new Intent(SplashScreenKM.this, Main.class);
        startActivity(intent);
        finish();
    }
}
