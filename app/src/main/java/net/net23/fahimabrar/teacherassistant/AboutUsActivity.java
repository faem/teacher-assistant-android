package net.net23.fahimabrar.teacherassistant;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutUsActivity extends AppCompatActivity {
    ImageView imageViewFahim, imageViewSabir, imageViewCuet, imageViewFbSabir, imageViewLinkedinSabir, imageViewMailSabir, imageViewPhoneSabir, imageViewFbFahim, imageViewLinkedinFahim, imageViewMailFahim, imageViewPhoneFahim;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsClient mClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsIntent.Builder builder;
    boolean warmedUp = false;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.hold, R.anim.pull_in_from_left);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewFahim = (ImageView) findViewById(R.id.imageViewFahim);
        imageViewSabir = (ImageView) findViewById(R.id.imageViewSabir);
        imageViewCuet = (ImageView) findViewById(R.id.imageViewCuet);
        imageViewFbFahim = (ImageView) findViewById(R.id.imageViewFbFahim);
        imageViewFbSabir = (ImageView) findViewById(R.id.imageViewFbSabir);
        imageViewMailFahim = (ImageView) findViewById(R.id.imageViewMailFahim);
        imageViewMailSabir = (ImageView) findViewById(R.id.imageViewMailSabir);
        imageViewLinkedinFahim = (ImageView) findViewById(R.id.imageViewLinkdinFahim);
        imageViewLinkedinSabir = (ImageView) findViewById(R.id.imageViewLinkdinSabir);
        imageViewPhoneFahim = (ImageView) findViewById(R.id.imageViewPhoneFahim);
        imageViewPhoneSabir = (ImageView) findViewById(R.id.imageViewPhoneSabir);

        imageViewCuet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.cuet.ac.bd"));
                startActivity(intent);*/

                url = "http://www.cuet.ac.bd";
                startChrome(url);
            }
        });

        imageViewFahim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "http://www.facebook.com/fahim.abrar3";
                startChrome(url);
            }
        });

        imageViewSabir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "http://www.facebook.com/msabir.cuet";
                startChrome(url);
            }
        });

        imageViewFbFahim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "http://www.facebook.com/fahim.abrar3";
                startChrome(url);
            }
        });

        imageViewFbSabir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "http://www.facebook.com/msabir.cuet";
                startChrome(url);
            }
        });

        imageViewLinkedinFahim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "https://www.linkedin.com/in/fahim-abrar-855784103/";
                startChrome(url);
            }
        });

        imageViewLinkedinSabir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = "https://www.linkedin.com/in/mdsabirhossain/";
                startChrome(url);
            }
        });

        imageViewMailFahim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "fahimabrar02@gmail.com", null));
                startActivity(intent);
            }
        });

        imageViewMailSabir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "sabirndc08cuet10@gmail.com", null));
                startActivity(intent);
            }
        });

        imageViewPhoneFahim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+8801521485508"));
                startActivity(callIntent);
            }
        });

        imageViewPhoneSabir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+8801882826575"));
                startActivity(callIntent);
            }
        });
    }


    void startChrome(final String url){
        builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //Pre-warming
                mClient = customTabsClient;
                mClient.warmup(0L);
                mCustomTabsSession = mClient.newSession(null);
                mCustomTabsSession.mayLaunchUrl(Uri.parse(url), null, null);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };
        warmedUp = CustomTabsClient.bindCustomTabsService(AboutUsActivity.this, "com.android.chrome", mCustomTabsServiceConnection);

        builder.build().launchUrl(AboutUsActivity.this, Uri.parse(url));
    }


}
