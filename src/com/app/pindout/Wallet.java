package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Wallet extends ActionBarActivity {
String iddd="";
    int totalpage=0;
    int currentpage=0;
    TextView tv1,tv2,tv3;
    int nbottom=0;
    int ntop=0;
    ScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        SharedPreferences pref = getSharedPreferences("loginpref", MODE_PRIVATE);
        String username = pref.getString("name", null);
        String email1 = pref.getString("email", null);
        iddd = pref.getString("id", null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dealstoolsub);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scroll=(ScrollView)findViewById(R.id.testscroll);
        new GetTaggedDeal().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_wallet, menu);
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

    String url_tag_deal="http://pindout.com/remuser/pindout/wallet.php";
    ArrayList<View> allviews1=new ArrayList<View>();
    JSONParser jParser1=new JSONParser();
    String deal_title,deal_description,deal_price,main_price,end_date,url_link,deal_image,business_name;
    String tot,curtot;
    int count=1;
    String maindealid,mainuserid;
    class GetTaggedDeal extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Wallet.this);
            pDialog.setMessage("Fetching Your Favorite Deals...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        LayoutInflater lin;
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            params.add(new BasicNameValuePair("count", String.valueOf(count)));
            json = jParser1.makeHttpRequest(url_tag_deal, "GET", params);
            lin=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                success = json.getInt("success");

                if (success == 1) {


                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {
                if (success == 99) {

                    Toast.makeText(Wallet.this, "No More Deals to Display", Toast.LENGTH_LONG).show();
                }
                LinearLayout lv = (LinearLayout) findViewById(R.id.finalmoredeals);
                lv.removeAllViews();


                //        tagbuslay.addView(allviews.get(i));
                JSONArray products = json.getJSONArray("products");
                if (products.length() > 0) {
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        deal_title = c.getString("deal_title");
                        deal_description = c.getString("deal_description");
                        url_link = c.getString("url_link");
                        main_price = c.getString("main_price");
                        deal_price = c.getString("deal_price");
                        deal_image = c.getString("deal_image");
                        business_name = c.getString("business_name");
                        tot = c.getString("total");
                        curtot = c.getString("curtotal");

                        View v = lin.inflate(R.layout.listdealsno, null);
                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.top_lay);
                        //  Log.e("Date result",""+d1.compareTo(end));
                        Button interest=(Button)v.findViewById(R.id.interestedbutton);
                        //interest.setVisibility(View.GONE);
                        interest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                RelativeLayout midlayout = (RelativeLayout) view.getParent();
                                RelativeLayout parmidlayout = (RelativeLayout) midlayout.getParent();
                                TextView dealname = (TextView) parmidlayout.findViewById(R.id.deal_name);
                                String dtitle = "", dmrp, doff, dbusname;
                                dtitle = dealname.getText().toString();
                                TextView tmrp = (TextView) midlayout.findViewById(R.id.showmain_price);
                                dmrp = tmrp.getText().toString();

                                TextView tddidd = (TextView) midlayout.findViewById(R.id.tesladealid);
                                String curdealid = tddidd.getText().toString();

                                maindealid=curdealid;
                                mainuserid=iddd;

                                TextView toff = (TextView) midlayout.findViewById(R.id.showdeal_price);
                                doff = toff.getText().toString();

                                TextView tbus = (TextView) midlayout.findViewById(R.id.deal_expiry);
                                dbusname = tbus.getText().toString();

                                Toast.makeText(Wallet.this, "Removing Deal from your wallet", Toast.LENGTH_LONG).show();
                                view.setVisibility(View.GONE);
                                new InsertInterested().execute();
                            }
                        });

                        String admin=c.getString("admin");
                        String status = c.getString("status1");
                        if (admin.equals("1")&&status.equals("1")) {
                            rl.setBackgroundColor(Color.GREEN);
                            // Log.e("Date result", " hhg");
                        } else {
                            rl.setBackgroundColor(Color.RED);
                        }
                        TextView tvmprice = (TextView) v.findViewById(R.id.showmain_price);
                        TextView tvdprice = (TextView) v.findViewById(R.id.showdeal_price);
                        TextView tvdexpiry = (TextView) v.findViewById(R.id.deal_expiry);
                        TextView tvdname = (TextView) v.findViewById(R.id.deal_name);
                        TextView disc = (TextView) v.findViewById(R.id.discount);

                        TextView stat = (TextView) v.findViewById(R.id.hiddenstatus);
                        stat.setText(status);
                        String currrdealid=c.getString("teslaid");
                        TextView thiddealid = (TextView) v.findViewById(R.id.tesladealid);
                        thiddealid.setText(currrdealid);

                        TextView statimg = (TextView) v.findViewById(R.id.hiddenimgurl);
                        statimg.setText(deal_image);
                        TextView tvdescript = (TextView) v.findViewById(R.id.dealdescript);
                        ImageView dealimg = (ImageView) v.findViewById(R.id.deal_img);
                        TextView tvdurl = (TextView) v.findViewById(R.id.correcturl);
                        tvmprice.setText(main_price);
                        tvdprice.setText(deal_price);
                        end_date = c.getString("end_date");
                        tvdexpiry.setText("Offered By: " + business_name + " till " + end_date.substring(0, 10));
                        tvdname.setText(deal_title);
                        tvdescript.setText("Details:  " + deal_description);
                        //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
                        tvdurl.setText(url_link);
                        Picasso.with(v.getContext())
                                .load("http://pindout.com/files/businessdeal_images/main_images/" + deal_image)
                                .into(dealimg);


                        double mainprice = Double.parseDouble(tvmprice.getText().toString());
                        double dealprice = Double.parseDouble(tvdprice.getText().toString());
                        ;
                        double diff = mainprice - dealprice;
                        if(mainprice==0||diff<=0);
                        else {
                            double percen = diff * 100 / mainprice;
                            disc.setText((Math.ceil(percen) + " %  Off"));
                        }


                        allviews1.add(v);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View par = (View) view.getParent();
                                TextView correcturl = (TextView) view.findViewById(R.id.correcturl);
                                Intent intent = new Intent(Wallet.this, DisplayDealUrl.class);
                                intent.putExtra("urlofdeal", correcturl.getText().toString());
                                startActivity(intent);
                            }
                        });
                        lv.addView(v);
                    }

                    scroll.smoothScrollTo(10, 10);
                    nbottom = 0;
                    ntop = 0;
                    try {
                        totalpage = Integer.valueOf(tot) / 30;

                        if ((Integer.valueOf(tot) % 30) != 0)
                            totalpage++;
                    } catch (Exception e) {
                    }
                    tv1 = (TextView) findViewById(R.id.mdpgination1);
                    tv2 = (TextView) findViewById(R.id.mdpgination2);
                    tv3 = (TextView) findViewById(R.id.mdpgination3);
                    if (totalpage == 0) {
                        tv1.setText("");
                        tv2.setText("");
                        tv3.setText("");
                    } else if (totalpage == 1) {
                        double temp = Math.ceil(Double.valueOf(curtot) / 30);
                        int t = (int) temp;
                        tv1.setText("" + t);
                        tv2.setText("");
                        tv3.setText("");
                    } else if (totalpage == 2) {
                        double temp = Math.ceil(Double.valueOf(curtot) / 30);
                        int t = (int) temp;
                        tv1.setText("" + t);
                        tv2.setText("" + (t + 1));

                        tv3.setText("");
                    } else {
                        double temp = Math.ceil(Double.valueOf(curtot) / 30);
                        int t = (int) temp;
                        tv1.setText("" + t);
                        tv2.setText("" + (t + 1));
                        tv3.setText("" + (t + 2));
                    }

                    //      for (int i = 0; i < allviews.size(); i++)
                    if(totalpage==1||totalpage==0)
                    {
                        LinearLayout lv1=(LinearLayout)findViewById(R.id.fetchpagination);
                        lv1.setVisibility(View.GONE);
                    }
                    else
                    {
                        LinearLayout lv1=(LinearLayout)findViewById(R.id.fetchpagination);
                        lv1.setVisibility(View.VISIBLE);
                    }

                    try {
                        if (Integer.valueOf(tv1.getText().toString()) == totalpage) {
                            tv2.setText("");
                            tv3.setText("");
                        }
                    } catch (Exception e) {
                        tv2.setText("");
                        tv3.setText("");
                    }
                    try {
                        if (Integer.valueOf(tv2.getText().toString()) == totalpage) {
                            tv3.setText("");
                        }
                    } catch (Exception e) {
                        tv3.setText("");
                    }
                    currentpage = Integer.valueOf(tv1.getText().toString());
                    tv1.setTextColor(Color.parseColor("#6C2DC7"));
                }
            }catch(Exception e){
                Log.d("Mewtwo", "" + e);
            }


            pDialog.dismiss();
            if(curtot==null)
                Toast.makeText(Wallet.this,"No deals",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(Wallet.this,"Showing "+curtot+" of "+tot+" deals",Toast.LENGTH_LONG).show();
        }
    }

    String url_interest="http://pindout.com/remuser/pindout/walletremove.php";;

    class InsertInterested extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Wallet.this);
            pDialog.setMessage("Removing Deal from your Wallet");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONArray productObj;
        JSONParser jp = new JSONParser();

        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {


                    try {


                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_id", mainuserid));
                        params.add(new BasicNameValuePair("deal_id", maindealid));
                        json = jp.makeHttpRequest(url_interest, "GET", params);


                    } catch (Exception e) {
                        Log.d("Review1", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

        }
    }

}
