package com.pocket.poktsales.activities;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.pocket.poktsales.R;

/**
 * Created by Vicente on 25/02/2018.
 */

public class NewVersionActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        String version = getIntent().getExtras().getString("version", SettingsActivity.KEY_V_1_1_2);

        if (version.equals(SettingsActivity.KEY_V_1_1_2)){
            addSlide(new SimpleSlide.Builder()
                    .title("Explora la nueva versión")
                    .description("OhKey se ha actualizado a la version 1.1.2. Explora sus nuevas funcionalidades!")
                    .image(R.drawable.logo_size_invert)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .scrollable(false)
                    .build());
            addSlide(new SimpleSlide.Builder()
                    .title("Tickets")
                    .description("Ahora puedes ver información adicional de tus ventas realizadas")
                    .image(R.drawable.ic_receipt_candy)
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .scrollable(false)
                    .build());
        }
    }
}
