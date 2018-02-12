package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.pocket.poktsales.R;


/**
 * Created by MAV1GA on 12/02/2018.
 */

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title("Bienvenido a OhKey")
                .description("Estamos (casi) seguros que te encantará.")
                .image(R.drawable.logo_size_invert)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("OhKey es para tu negocio")
                .description("Usarlo es muy fácil y no necesitas nada más que tu smartphone. (Y un negocio de compra y venta, claro).")
                .image(R.drawable.ic_001_store)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Empieza con tus productos")
                .description("Crea productos que vendas. Hacerlo es muy fácil y puedes ordenarlos por categorías.")
                .image(R.drawable.ic_bag)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Registra las ventas")
                .description("Cuando vendas un producto, registra la venta en la app. OhKey se encarga de ordenar el papeleo aburrido.")
                .image(R.drawable.ic_003_smartphone)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Observa la magia")
                .description("Cada vez que registras una venta, OhKey actualizará todas las gráficas. Puedes consultar información muy " +
                        "importante para tu negocio desde tu primera venta. (Ya sabes a qué hora vendes más?).")
                .image(R.drawable.ic_007_business)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Disfruta tus ganancias")
                .description("Después de todo, creamos OhKey para que tu negocio crezca. Esperamos que te agrade!")
                .image(R.drawable.ic_005_coins)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());

    }
}
