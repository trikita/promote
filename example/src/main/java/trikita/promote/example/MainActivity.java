package trikita.promote.example;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import trikita.promote.Promote;

public class MainActivity extends AppCompatActivity {

    private static final int MY_DIALOG_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask for rating after 2 days since first launch, then every 5 launches
        // until user agrees to rate, or explicitly states he doesn't want to rate it
        Promote.after(2).times().every(5).times().rate(this);

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Promote.reset(MainActivity.this);
            }
        });
        findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Promote.after(5).times().every(2, 2f).times().show(MainActivity.this, MY_DIALOG_ID, myPromotionalDialog());
            }
        });
        findViewById(R.id.btn_twitter).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Promote.after(3).times().every(1).times().share(MainActivity.this,
                    Promote.TWITTER_FACEBOOK,
                    "http://trikita.co",
                    "This is a tweet text");
            }
        });
        findViewById(R.id.btn_facebook).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Promote.after(1).times().every(3).times().share(MainActivity.this,
                    Promote.FACEBOOK,
                    "http://trikita.co",
                    "Facebook won't let you show this text via Intent API, so whatever..");
            }
        });
    }

    private Dialog myPromotionalDialog() {
        return new AlertDialog.Builder(this)
                .setMessage("Seems like you like clicking buttons, perhaps you want to test our other apps instead?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://trikita.co/")));
                        // don't bother user with the same question in future
                        Promote.ban(MainActivity.this, MY_DIALOG_ID);
                    }
                }).create();
    }
}
