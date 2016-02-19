package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class DisplayDealUrl extends ActionBarActivity {
String urlofdeal;
    ProgressDialog pDialog;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deal_url);
try {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbardeals);
    toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);


    if (savedInstanceState == null) {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            urlofdeal = null;
        } else {
            urlofdeal = extras.getString("urlofdeal");
        }
    } else {

        urlofdeal = (String) savedInstanceState.getSerializable("urlofdeal");
    }

    if(urlofdeal==""||urlofdeal==null)
    {
        Toast.makeText(DisplayDealUrl.this,"No Link for the deal found",Toast.LENGTH_LONG).show();
        return;
    }
    mWebView = (WebView) findViewById(R.id.webView1);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
    //mWebView.loadUrl(urlofdeal);
    pDialog = new ProgressDialog(DisplayDealUrl.this);
    pDialog.setMessage("Loading Your Deal...");
    pDialog.setIndeterminate(false);
    pDialog.setCancelable(false);
    pDialog.show();


    mWebView.setWebViewClient(new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        public void onPageFinished(WebView view, String url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }



    });
    mWebView.loadUrl(urlofdeal);
}catch (Exception e){
    ;
    }
    }


    @Override
    public boolean onKeyDown(int keycode,KeyEvent ke)
    {
        if(keycode==KeyEvent.KEYCODE_BACK)
        {
            mWebView.loadUrl("about:blank");
            Intent intent = new Intent(DisplayDealUrl.this, MainActivityRemo.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onKeyDown(keycode, ke);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_display_deal_url, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
