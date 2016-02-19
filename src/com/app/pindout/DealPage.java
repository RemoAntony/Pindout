package com.app.pindout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DealPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_page);
        Toolbar tb=(Toolbar)findViewById(R.id.dpagetool);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle=getIntent().getExtras();
        String mrp=bundle.getString("mrp");
        String dprice=bundle.getString("dprice");
        String bname=bundle.getString("bname");
        String dname=bundle.getString("dname");
        String disc=bundle.getString("disc");
        String imgurl=bundle.getString("dimg");

        TextView tdname=(TextView)findViewById(R.id.dealpagedealname);
        TextView tbname=(TextView)findViewById(R.id.dealpagebusname);
        TextView tdmrp=(TextView)findViewById(R.id.dealpagemrpname);
        TextView tdoffer=(TextView)findViewById(R.id.dealpageoffername);
        TextView tdesc=(TextView)findViewById(R.id.dealpagedescname);
        ImageView dimg=(ImageView)findViewById(R.id.dealpageimg);
        Picasso.with(DealPage.this)
                .load("http://pindout.com/files/businessdeal_images/main_images/" + imgurl)
                .into(dimg);


        tdname.setText(dname);
        tbname.setText(bname);
        tdmrp.setText("Main Price :"+mrp);
        tdoffer.setText("Deal Price :"+dprice);
        tdesc.setText(disc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deal_page, menu);
        return true;
    }


  /*  public void onBackPressed() {


    }*/
/*
    private void onBackEvent() {
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
*/

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
        if(id== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
