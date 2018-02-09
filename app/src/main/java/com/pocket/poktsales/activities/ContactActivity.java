package com.pocket.poktsales.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.pocket.poktsales.R;

/**
 * Created by MAV1GA on 02/02/2018.
 */

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnFacebook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init() {
        btnFacebook = (ImageButton)findViewById(R.id.btn_facebook);
        btnFacebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_facebook:
                startActivity(newFacebookIntent(getPackageManager()));
            break;
        }
    }

    private Intent newFacebookIntent(PackageManager pm) {
        String url = "https://www.facebook.com/ohkeymexico/";
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            Log.e("CONTACT ACTIVITY", ignored.getMessage());
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
