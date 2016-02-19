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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class categoryInstore extends ActionBarActivity {
    String url_tag_deal="http://pindout.com/remuser/pindout/instoredeals.php";
    ArrayList<View> allviews1=new ArrayList<View>();
    JSONParser jParser1=new JSONParser();
    String deal_title,deal_description,deal_price,main_price,end_date,url_link,deal_image,business_name;
    LinearLayout instoremain;
    int count=1, cityid=16;
    Button prev,next;
    int nbottom=0;
    int ntop=0;
    String tot,curtot;
    ScrollView scroll;
    int totalpage=0;
    int currentpage=0;
    Button b1,b2,b3,b4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_instore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.catinstoretool);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        instoremain=(LinearLayout)findViewById(R.id.instoremain);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        scroll=(ScrollView)findViewById(R.id.insScroll);
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        b3=(Button)findViewById(R.id.b3);
        b4=(Button)findViewById(R.id.b4);

    /*    scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int max= scroll.getMaxScrollAmount();
                int xy=scroll.getScrollY();
                RelativeLayout rl=(RelativeLayout)findViewById(R.id.scrollrelcheck1);
                int diff = (rl.getBottom() - (scroll.getHeight() + scroll.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    // do stuff
                    //Toast.makeText(MoreDealsSub3.this,"Hello belowyyyy",Toast.LENGTH_LONG).show();
                    if(nbottom>0) return false;
                    nbottom++;
                    dysetMoreDeals();

                }
                if(scroll.getScrollY()==0)
                {
                    if(count==1) return false;
                    else dysetPredeals();
                }
                return false;
            }
        });*/

        SharedPreferences pref111 = getSharedPreferences("whatcity", MODE_PRIVATE);
        String item = pref111.getString("cityname", null);
        if (item.equals("Bengaluru")) {
            cityid = 29;


        } else if (item.equals("Hyderabad")) {
            cityid = 20;


        } else if (item.equals("Chennai")) {
            cityid = 16;

        } else if (item.equals("Mumbai")) {
            cityid = 25;

        } else if (item.equals("Web Based")) {
            cityid = 26;

        }
        new GetTaggedDeal().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //     getMenuInflater().inflate(R.menu.menu_category_instore, menu);
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

    public void predealssearch(View view)
    {
        if(curtot.equals(tot))
        {
            Toast.makeText(categoryInstore.this,"No more Deals To display",Toast.LENGTH_LONG).show();
            return;
        }
        count--;
        new GetTaggedDeal().execute();
        if(count<2) prev.setVisibility(View.INVISIBLE);

    }

    public void nextdealssearch(View view)
    {
        if(curtot.equals(tot))
        {
            Toast.makeText(categoryInstore.this,"No more Deals To display",Toast.LENGTH_LONG).show();
            return;
        }
        count++;
        new GetTaggedDeal().execute();

    }
    public void dysetPredeals()
    {
        if(count-1<0) return;

        count--;
        new GetTaggedDeal().execute();
        if(count<2) prev.setVisibility(View.INVISIBLE);
    }


    public void dysetMoreDeals()
    {
        if(curtot.equals(tot))
        {
            Toast.makeText(categoryInstore.this,"No more Deals To display",Toast.LENGTH_LONG).show();
            return;
        }
        count++;
        new GetTaggedDeal().execute();
    }
    ImageView dealimg;

    class GetTaggedDeal extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        String[] area;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(categoryInstore.this);
            pDialog.setMessage("Making things Ready for You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        LayoutInflater lin;
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("count", String.valueOf(count)));
            params.add(new BasicNameValuePair("city_id",String.valueOf(cityid)));
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
instoremain.removeAllViews();
                //      for (int i = 0; i < allviews.size(); i++)
                //        tagbuslay.addView(allviews.get(i));

                // Creating The DashViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                if(success==1) {
                    JSONArray products = json.getJSONArray("products");
                    area = new String[products.length()];
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        deal_title = c.getString("deal_title");
                        deal_description = c.getString("deal_description");
                        url_link = c.getString("url_link");
                        main_price = c.getString("main_price");
                        deal_price = c.getString("deal_price");
                        deal_image = c.getString("deal_image");
                        business_name = c.getString("business_name");
                        area[i] = c.getString("Area");
                        String catname=c.getString("deal_category");

                        tot = c.getString("total");
                        curtot = c.getString("curtotal");
                        String enddate = c.getString("end_date");

                        View v = lin.inflate(R.layout.listdeals1, null);
                        TextView a1 = (TextView) v.findViewById(R.id.areaname);
                        TextView cat_name=(TextView) v.findViewById(R.id.dealdescript);
                        cat_name.setVisibility(View.VISIBLE);
                        cat_name.setText("Category :"+catname);
                        if (i == 0) {
                            a1.setVisibility(View.VISIBLE);
                            a1.setText("Area :" + area[i]);
                        } else {
                            if (area[i].equals(area[i - 1])) {
                                a1.setVisibility(View.GONE);
                            } else {
                                a1.setVisibility(View.VISIBLE);
                                a1.setText("Area :" + area[i]);
                            }
                        }


                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        Date end = dateFormat.parse(enddate);

                        Date d1 = dateFormat.parse(dateFormat.format(date));

                        //   Log.e("End Date"," "+dateFormat.format(end));
                        //     Log.e("Today"," "+dateFormat.format(d1));

                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.top_lay);
                        //  Log.e("Date result",""+d1.compareTo(end));
                        String admin=c.getString("admin");
                        String status = c.getString("status");
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

                       // TextView tvdescript = (TextView) v.findViewById(R.id.dealdescript);
                        dealimg = (ImageView) v.findViewById(R.id.deal_img);
                        TextView tvdurl = (TextView) v.findViewById(R.id.correcturl);
                        tvmprice.setText(main_price);
                        tvdprice.setText(deal_price);
                        tvdexpiry.setText("Offered By: " + business_name + " till " + enddate.substring(0, 10));
                        tvdname.setText(deal_title);
                        //tvdescript.setText("Details:  " + deal_description);
                        //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
                        double mainprice = Double.parseDouble(tvmprice.getText().toString());
                        double dealprice = Double.parseDouble(tvdprice.getText().toString());
                        ;
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

                        allviews1.add(v);
                        instoremain.addView(v);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View par = (View) view.getParent();
                                TextView stat = (TextView) view.findViewById(R.id.hiddenstatus);
                                if (stat.getText().toString().equals("0")) {
                                    Toast.makeText(categoryInstore.this, "Local Business Deal", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                TextView correcturl = (TextView) view.findViewById(R.id.correcturl);
                                Intent intent = new Intent(categoryInstore.this, DisplayDealUrl.class);
                                intent.putExtra("urlofdeal", correcturl.getText().toString());
                                startActivity(intent);
                            }
                        });
                    }
                    if(count==1)
                    {
                        prev.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }
                    else if(count>=2)
                    {
                        prev.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        prev.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.INVISIBLE);
                    }
                    scroll.smoothScrollTo(10,10);
                    nbottom=0;
                    ntop=0;
                    try {
                        totalpage = Integer.valueOf(tot) / 30;

                        if ((Integer.valueOf(tot) % 30) != 0)
                            totalpage++;
                    }catch (Exception e){}
                    tv1=(TextView)findViewById(R.id.mdpgination1);
                    tv2=(TextView)findViewById(R.id.mdpgination2);
                    tv3=(TextView)findViewById(R.id.mdpgination3);
                    if(totalpage==0)
                    {
                        tv1.setText("");
                        tv2.setText("");
                        tv3.setText("");
                    }
                    else if(totalpage==1)
                    {
                        double temp=Math.ceil(Double.valueOf(curtot)/30);
                        int t=(int)temp;
                        tv1.setText(""+t);
                        tv2.setText("");
                        tv3.setText("");
                    }
                    else if (totalpage==2)
                    {
                        double temp=Math.ceil(Double.valueOf(curtot)/30);
                        int t=(int)temp;
                        tv1.setText(""+t);
                        tv2.setText(""+(t+1));

                        tv3.setText("");
                    }
                    else
                    {
                        double temp=Math.ceil(Double.valueOf(curtot)/30);
                        int t=(int)temp;
                        tv1.setText(""+t);
                        tv2.setText(""+(t+1));
                        tv3.setText(""+(t+2));
                    }

                    try {
                        if (Integer.valueOf(tv1.getText().toString()) == totalpage) {
                            b1.setEnabled(false);
                            b2.setEnabled(false);
                            b3.setEnabled(false);
                            b4.setEnabled(false);
                            tv2.setText("");
                            tv3.setText("");
                        }
                    }catch(Exception e){
                        tv2.setText("");
                        tv3.setText("");}
                    try {
                        if (Integer.valueOf(tv2.getText().toString()) == totalpage) {
                            tv3.setText("");
                        }
                    }catch (Exception e){tv3.setText("");}
                    currentpage=Integer.valueOf(tv1.getText().toString());
                    tv1.setTextColor(Color.parseColor("#6C2DC7"));

                }
                else
                {
                  //  Toast.makeText(categoryInstore.this,"No deals",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Log.d("Mewtwo", "" + e);
            }


            pDialog.dismiss();

            if((curtot==null))
                Toast.makeText(categoryInstore.this,"No deals to display",Toast.LENGTH_LONG).show();
                else

            Toast.makeText(categoryInstore.this,"Showing "+curtot+" of "+tot+" deals",Toast.LENGTH_LONG).show();

        }
    }
    TextView tv1,tv2,tv3;
    public void m3ff(View view)
    {
        try {
            count=totalpage;
            new GetTaggedDeal().execute();
        }catch (Exception e){}
    }
    public void m3pre(View view)
    {
        try {
            int next = Integer.valueOf(tv1.getText().toString());
            if(next==1) return;
            next-=1;
            if(next<=0) count=1;
            else  count=next;
            new GetTaggedDeal().execute();
        }catch (Exception e){}
    }
    public void m3rewind(View view)
    {

        try {
            count=1;
            new GetTaggedDeal().execute();
        }catch (Exception e){}
    }
    public void m3more(View view)
    {
        try {
            int next = Integer.valueOf(tv3.getText().toString());
            if(next==totalpage) ;
            else
                next++;
            count=next;
            new GetTaggedDeal().execute();
        }catch (Exception e){}
    }

    public void tv11n(View view)
    {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count=Integer.valueOf(tv1.getText().toString());
        new GetTaggedDeal().execute();
    }
    public void tv22n(View view)
    {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count=Integer.valueOf(tv2.getText().toString());
        new GetTaggedDeal().execute();
    }
    public void tv33n(View view)
    {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count=Integer.valueOf(tv3.getText().toString());
        new GetTaggedDeal().execute();
    }

}
