package com.npe.horse.travel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by horse on 2017. 10. 25..
 */

public class SplashActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //READ_PHONE_STATE 권한 체크
                if (ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    Log.e("SplashActivity", "권한 없음");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {
                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST);
                    }
                }else{
                    // 권한 있음
                    Log.e("SplashActivity", "권한 있음");
                    redirectToMainActivity();
                }
            }
        }, 1500);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 권한 허가
            // 해당 권한을 사용해서 작업을 진행할 수 있습니다
            Log.e("SplashActivity", "권한 허가함");
            redirectToMainActivity();
        } else {
            // 권한 거부
            // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
            Log.e("SplashActivity", "권한 거부함");
            finish();
        }
    }

    public void redirectToMainActivity(){
        Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }
}
