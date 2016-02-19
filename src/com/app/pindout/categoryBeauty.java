package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class categoryBeauty extends ActionBarActivity {
    LinearLayout instoremain;
    int count=1, cityid=16;
    Button prev,next;
    int nbottom=0;
    int ntop=0;
    String tot,curtot;
    ScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_beauty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.catbtytool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        instoremain=(LinearLayout)findViewById(R.id.beautymain);
        new GetTaggedDeal().execute();

    }


    String url_tag_deal="http://pindout.com/remuser/pindout/othercategorydeals.php";
    ArrayList<View> allviews1=new ArrayList<View>();
    JSONParser jParser1=new JSONParser();
    String deal_title,deal_description,deal_price,main_price,end_date,url_link,deal_image,business_name;
    ImageView dealimg;
    class GetTaggedDeal extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(categoryBeauty.this);
            pDialog.setMessage("Making things Ready for You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        LayoutInflater lin;
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category_id", "39"));
            params.add(new BasicNameValuePair("count", "1"));
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

                //      for (int i = 0; i < allviews.size(); i++)
                //        tagbuslay.addView(allviews.get(i));
                // Creating The DashViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                JSONArray products = json.getJSONArray("products");
                for(int  i=0;i<products.length();i++){
                    JSONObject c = products.getJSONObject(i);
                    deal_title=c.getString("deal_title");
                    deal_description=c.getString("deal_description");
                    url_link=c.getString("url_link");
                    main_price=c.getString("main_price");
                    deal_price=c.getString("deal_price");
                    deal_image=c.getString("deal_image");
                    business_name=c.getString("business_name");
                    String status=c.getString("status");
                    String enddate=c.getString("end_date");

                    View v=lin.inflate(R.layout.listdeals, null);
                    TextView tvmprice=(TextView) v.findViewById(R.id.showmain_price);
                    TextView tvdprice=(TextView) v.findViewById(R.id.showdeal_price);
                    TextView tvdexpiry=(TextView) v.findViewById(R.id.deal_expiry);
                    TextView tvdname=(TextView) v.findViewById(R.id.deal_name);
                    TextView disc=(TextView) v.findViewById(R.id.discount);

                    TextView stat=(TextView) v.findViewById(R.id.hiddenstatus);
                    stat.setText(status);

                    TextView tvdescript=(TextView) v.findViewById(R.id.dealdescript);
                    dealimg=(ImageView) v.findViewById(R.id.deal_img);
                    TextView tvdurl=(TextView) v.findViewById(R.id.correcturl);
                    tvmprice.setText(main_price);
                    tvdprice.setText(deal_price);
                    tvdexpiry.setText("Offered By: "+business_name);
                    tvdname.setText(deal_title);
                    tvdescript.setText("Details:  " + deal_description);
                    //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
                    double mainprice=Double.parseDouble(tvmprice.getText().toString());
                    double dealprice=Double.parseDouble(tvdprice.getText().toString());;
                    double diff = mainprice - dealprice;
                    if(mainprice==0||diff<=0);
                    else {
                        double percen = diff * 100 / mainprice;
                        disc.setText((Math.ceil(percen) + " %  Off"));
                    }


                    tvdurl.setText(url_link);
                    Picasso.with(v.getContext())
                            .load("http://pindout.com/files/businessdeal_images/main_images/" + deal_image)
                            .into(dealimg);
                    instoremain.addView(v);

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View par=(View)view.getParent();
                            TextView stat=(TextView) view.findViewById(R.id.hiddenstatus);
                            if(stat.getText().toString().equals("0"))
                            {
                                Toast.makeText(categoryBeauty.this, "Local Business Deal", Toast.LENGTH_LONG).show();
                                return;
                            }
                            TextView correcturl=(TextView) view.findViewById(R.id.correcturl);
                            Intent intent=new Intent(categoryBeauty.this, DisplayDealUrl.class);
                            intent.putExtra("urlofdeal",correcturl.getText().toString());
                            startActivity(intent);
                        }
                    });
                }
                if(products.length()==0) Toast.makeText(categoryBeauty.this,"No Deals to display",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(categoryBeauty.this,"No Deals to display",Toast.LENGTH_LONG).show();
            }


            pDialog.dismiss();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_category_beauty, menu);
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
