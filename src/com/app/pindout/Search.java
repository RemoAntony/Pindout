package com.app.pindout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Search extends ActionBarActivity {
    int count=0;
    int cityid;
    Button predeals,nexdeals;
    Spinner spinner22;
    Spinner locality;
    String url_fetch_city = "http://pindout.com/remuser/pindout/areas.php";
    String url_business_search="http://pindout.com/remuser/pindout/businesssearch.php";
    String url_deals_search="http://pindout.com/remuser/pindout/dealssearch.php";
    String url_gettag="http://pindout.com/remuser/pindout/tagbusiness.php";
    String url_getuntag="http://pindout.com/remuser/pindout/UnTag.php";
    ArrayList<String> listofareas;
    String finallocality;
    String finaltype;
    String iddd;
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
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.searchtool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        b3=(Button)findViewById(R.id.b3);
        b4=(Button)findViewById(R.id.b4);
        locality = (Spinner) findViewById(R.id.localitysearch);
        ArrayList<String> sublist = new ArrayList<String>();
        sublist.add("Business");

        LinearLayout lv=(LinearLayout)findViewById(R.id.fetchpagination);
        lv.setVisibility(View.GONE);

        SharedPreferences preff = getSharedPreferences("loginpref", MODE_PRIVATE);
        String username = preff.getString("name", null);
        String email = preff.getString("email", null);
        iddd=preff.getString("id", null);

        sublist.add("Deal");
         spinner22 = (Spinner) findViewById(R.id.spinnerbord);


        Spinner spinnercity = (Spinner) findViewById(R.id.spinner_city);
        ArrayList<String> subcity = new ArrayList<String>();
        subcity.add("Bengaluru");
        subcity.add("Chennai");
        subcity.add("Hyderabad");
        subcity.add("Mumbai");
        subcity.add("Web Based");

        spinnercity.setVisibility(View.INVISIBLE);

        predeals=(Button)findViewById(R.id.searchprev);
        nexdeals=(Button)findViewById(R.id.searchnext);
        predeals.setVisibility(View.INVISIBLE);
        nexdeals.setVisibility(View.INVISIBLE);
        scroll=(ScrollView)findViewById(R.id.insScroll);
        /*
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int max = scroll.getMaxScrollAmount();
                int xy = scroll.getScrollY();
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.scrollrelcheck1);
                int diff = (rl.getBottom() - (scroll.getHeight() + scroll.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    // do stuff
                    //Toast.makeText(MoreDealsSub3.this,"Hello belowyyyy",Toast.LENGTH_LONG).show();
                    if (nbottom > 0) return false;
                    nbottom++;
                    dysetMoreDeals();

                }
                if (scroll.getScrollY() == 0) {
                    if (count == 1) return false;
                    else dysetPredeals();
                }
                return false;
            }
        });*/


        SharedPreferences pref = getSharedPreferences("whatcity", MODE_PRIVATE);
        String whatcity = pref.getString("cityname", null);

        ArrayAdapter<String> ads = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sublist);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subcity);
        spinnercity.setAdapter(ad);



        spinner22.setAdapter(ads);
        listofareas = new ArrayList<String>();
        locality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long l) {
                String item = adapter.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "Selected  : " + item,
  //                      Toast.LENGTH_LONG).show();
                finallocality=item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String item = adapter.getItemAtPosition(position).toString();
                if (item.equals("Bengaluru")) {
                    cityid = 29;
//                    Toast.makeText(getApplicationContext(), "Selected  : " + item,
  //                          Toast.LENGTH_LONG).show();

                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Bengaluru")
                            .commit();


                    new GetArea().execute();
                } else if (item.equals("Hyderabad")) {
                    cityid = 20;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                      //      Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Hyderabad")
                            .commit();



                    new GetArea().execute();
                } else if (item.equals("Chennai")) {
                    cityid = 16;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                        //    Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Chennai")
                            .commit();
                    new GetArea().execute();
                } else if (item.equals("Mumbai")) {
                    cityid = 25;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                      //      Toast.LENGTH_LONG).show();

                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Mumbai")
                            .commit();
                    new GetArea().execute();
                } else if (item.equals("Web Based")) {
                    cityid = 26;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                      //      Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Web Based")
                            .commit();
                    new GetArea().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Selected  : Nothing",
                            Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity",MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Chennai")
                            .commit();
                    new GetArea().execute();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(whatcity==null)
        {
            getSharedPreferences("whatcity",MODE_PRIVATE)
                    .edit()
                    .putString("cityname", "Chennai")
                    .commit();
            spinnercity.setSelection(ad.getPosition("Chennai"));
        }
        else
        {
            spinnercity.setSelection(ad.getPosition(whatcity));
        }


            spinner22.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long l) {
                    String item = adapter.getItemAtPosition(position).toString();
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //      Toast.LENGTH_LONG).show();
                    finaltype = item;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });





        TextView searchtext=(TextView)findViewById(R.id.searchresultstext);
        searchtext.setVisibility(View.GONE);
        ScrollView scrolltext=(ScrollView)findViewById(R.id.insScroll);
        scrolltext.setVisibility(View.GONE);



        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search, menu);
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
        count--;
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }

    public void nextdealssearch(View view)
    {
        count++;
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }
    public void dysetPredeals()
    {
        if(count-1<0) return;

        count--;
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
        if(count<2) predeals.setVisibility(View.INVISIBLE);
    }


    public void dysetMoreDeals()
    {
        if(curtot.equals(tot))
        {
            Toast.makeText(Search.this,"No more Deals To display",Toast.LENGTH_LONG).show();
            return;
        }
        count++;
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }
    String keytosearch="";
    public void searchall(View view)
    {
        EditText keyword=(EditText)findViewById(R.id.keywordsearch);
        keytosearch=keyword.getText().toString();
        //Toast.makeText(Search.this,""+keytosearch+finallocality+finaltype,Toast.LENGTH_LONG).show();
        if(finallocality.equals("All")&&finaltype.equals("Business"))
        {
            count=0;
            count++;
            TextView searchtext=(TextView)findViewById(R.id.searchresultstext);
            searchtext.setVisibility(View.VISIBLE);
            ScrollView scrolltext=(ScrollView)findViewById(R.id.insScroll);
            scrolltext.setVisibility(View.VISIBLE);

            new GetBusiness().execute();

        }
        else if(keytosearch==null ||keytosearch.equals("")||keyword.length()<3) {
            Toast.makeText(Search.this,"Keyword must Be atleast 3 Characters",Toast.LENGTH_LONG).show();
            return;
        }
       else if(finaltype.equals("Business")){
            count=0;
            count++;
            TextView searchtext=(TextView)findViewById(R.id.searchresultstext);
            searchtext.setVisibility(View.VISIBLE);
            ScrollView scrolltext=(ScrollView)findViewById(R.id.insScroll);
            scrolltext.setVisibility(View.VISIBLE);

            new GetBusiness().execute();

        }
        else
        {
            count=0;
            count++;
            TextView searchtext=(TextView)findViewById(R.id.searchresultstext);
            searchtext.setVisibility(View.VISIBLE);
            ScrollView scrolltext=(ScrollView)findViewById(R.id.insScroll);
            scrolltext.setVisibility(View.VISIBLE);

            new GetDeals().execute();

        }
    }

    class GetArea extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
try {
    pDialog = new ProgressDialog(Search.this);
    pDialog.setMessage("Loading...");
    pDialog.setIndeterminate(false);
    pDialog.setCancelable(false);
    pDialog.show();
}catch (Exception e){}
        }

        JSONObject json;
        JSONParser jParser = new JSONParser();

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("city_id", String.valueOf(cityid)));

            json = jParser.makeHttpRequest(url_fetch_city, "GET", params);

            try {
                success = json.getInt("success");

                if (success == 1) {


                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
                try {
                    listofareas.clear();
                    if(cityid==29) listofareas.add("All in Bengaluru");
                    else if(cityid==25)  listofareas.add("All in Mumbai");
                    else if(cityid==16)listofareas.add("All in Chennai");
                    else if(cityid==20) listofareas.add("All in Hyderabad");
                   // listofareas.add("All");
                    JSONArray products = json.getJSONArray("products");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        listofareas.add(c.getString("area_name"));
                    }
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_dropdown_item, listofareas);
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ad.notifyDataSetChanged();
                    locality.setAdapter(ad);

                } catch (Exception e) {
                }

        }
    }


    class GetBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Searching...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONParser jParser = new JSONParser();

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("keyword", keytosearch));
            params.add(new BasicNameValuePair("areaname", finallocality));
            params.add(new BasicNameValuePair("count", String.valueOf(count)));
            params.add(new BasicNameValuePair("user_id", String.valueOf(iddd)));

            json = jParser.makeHttpRequest(url_business_search, "GET", params);

            try {
                success = json.getInt("success");

                if (success == 1) {


                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(count==1)
            {
                predeals.setVisibility(View.INVISIBLE);
                nexdeals.setVisibility(View.VISIBLE);
            }
            else if(count>=2)
            {
                predeals.setVisibility(View.VISIBLE);
                nexdeals.setVisibility(View.VISIBLE);
            }
            else
            {
                predeals.setVisibility(View.INVISIBLE);
                nexdeals.setVisibility(View.INVISIBLE);
            }


            try {
                LinearLayout lv=(LinearLayout)findViewById(R.id.searchresults);
                lv.removeAllViews();
                JSONArray products = json.getJSONArray("products");
                if(products.length()==0) {
                    Toast.makeText(Search.this,"No Search Results...Try a Different Keyword or Location",Toast.LENGTH_LONG).show();
                    Button mbut=(Button)findViewById(R.id.searchnext);
                    mbut.setVisibility(View.INVISIBLE);
                    return;
                }
                for (int i = 0; i < products.length(); i++) {

                    JSONObject c = products.getJSONObject(i);
                    String bname=c.getString("business_name");
                    String baddr=c.getString("address");
                    String bphone=c.getString("phone");
                    String tag=c.getString("tag");
                    String bid=c.getString("id");
                    String burl="";
                    try{
                        burl=c.getString("url");
                    }catch (Exception e){}


                    businessid=bid;

                    tot=c.getString("total");
                    curtot=c.getString("curtotal");
                    Log.e("CurTot",curtot+"");
                    LayoutInflater lin=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View v=lin.inflate(R.layout.businesssearchresults,null);


                    TextView tuurrll=(TextView)v.findViewById(R.id.teslaburl);
                    tuurrll.setText(burl);

                    TextView tname=(TextView)v.findViewById(R.id.businesssearchname);
                    tname.setText(bname);
                    TextView taddr=(TextView)v.findViewById(R.id.businesssearchaddr);
                    taddr.setText(baddr);
                    TextView tphone=(TextView)v.findViewById(R.id.businesssearchphone);
                    tphone.setText(bphone);
                    TextView tbid=(TextView)v.findViewById(R.id.hiddenbid);
                    tbid.setText(bid);


                     Button tagbutt=(Button) v.findViewById(R.id.tagbutton);
                     Button untagbutt=(Button) v.findViewById(R.id.untagbutton);

                    if(finallocality.equals("All")) {
                        untagbutt.setVisibility(View.INVISIBLE);
                        tagbutt.setVisibility(View.INVISIBLE);
                    }
                    if(tag.equals("0")) untagbutt.setVisibility(View.INVISIBLE);
                    else tagbutt.setVisibility(View.INVISIBLE);


                    untagbutt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View parent=(View)view.getParent();
                           Button uuntagbutt=(Button) parent.findViewById(R.id.untagbutton);
                            Button ttagbutt=(Button) parent.findViewById(R.id.tagbutton);
                            uuntagbutt.setVisibility(View.INVISIBLE);
                            ttagbutt.setVisibility(View.VISIBLE);

                            TextView tbid=(TextView) parent.findViewById(R.id.hiddenbid);
                            businessid=tbid.getText().toString();

                            new GetUntag().execute();
                        }
                    });


                    tagbutt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View parent=(View)view.getParent();
                            Button uuntagbutt=(Button) parent.findViewById(R.id.untagbutton);
                            Button ttagbutt=(Button) parent.findViewById(R.id.tagbutton);
                            ttagbutt.setVisibility(View.INVISIBLE);
                            uuntagbutt.setVisibility(View.VISIBLE);

                            TextView tbid=(TextView) parent.findViewById(R.id.hiddenbid);
                            businessid=tbid.getText().toString();

                            new GetTag().execute();
                        }
                    });

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView tname=(TextView)view.findViewById(R.id.businesssearchname);
                            TextView tuurrll=(TextView)view.findViewById(R.id.teslaburl);
                            TextView taddr=(TextView)view.findViewById(R.id.businesssearchaddr);
                            TextView tphone=(TextView)view.findViewById(R.id.businesssearchphone);
                            TextView tbid=(TextView)view.findViewById(R.id.hiddenbid);
                            SharedPreferences pref = getSharedPreferences("loginpref", MODE_PRIVATE);
                            String username = pref.getString("name", null);
                            String email1 = pref.getString("email", null);
                            String id111=pref.getString("id", null);

                            if(!tuurrll.getText().toString().equals(""))
                            {
                                Intent intent=new Intent(Search.this,DisplayDealUrl.class);
                                intent.putExtra("urlofdeal", tuurrll.getText().toString());
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(Search.this, BusinessPage.class);
                                intent.putExtra("com.app.pindout.Business_Name", tname.getText().toString());
                                intent.putExtra("com.app.pindout.Business_Id", tbid.getText().toString());
                                intent.putExtra("com.app.pindout.UserId", id111);
                                intent.putExtra("com.app.pindout.Group", "");
                                intent.putExtra("com.app.pindout.Email", email1);
                                startActivity(intent);
                            }
                        }
                    });

                    lv.addView(v);
                }


            } catch (Exception e) {
                Button mbut=(Button)findViewById(R.id.searchnext);
                mbut.setVisibility(View.INVISIBLE);
                Toast.makeText(Search.this,"No Search Results...Try a Different Keyword or Location",Toast.LENGTH_LONG).show();
                return;
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

            if(totalpage==1||totalpage==0)
            {
                LinearLayout lv=(LinearLayout)findViewById(R.id.fetchpagination);
                lv.setVisibility(View.GONE);
            }
            else
            {
                LinearLayout lv=(LinearLayout)findViewById(R.id.fetchpagination);
                lv.setVisibility(View.VISIBLE);
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
            if(!TextUtils.isEmpty(tv1.getText().toString()))
            currentpage=Integer.valueOf(tv1.getText().toString());
            tv1.setTextColor(Color.parseColor("#6C2DC7"));
            Toast.makeText(Search.this,"Showing "+curtot+" of "+tot+" deals",Toast.LENGTH_LONG).show();

        }
    }


    class GetDeals extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Searching...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONArray products;
        JSONObject json;
        JSONParser jParser;

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("keyword", keytosearch));
            params.add(new BasicNameValuePair("areaname", finallocality));
            params.add(new BasicNameValuePair("count", String.valueOf(count)));
                jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_deals_search, "GET", params);

            try {
                success = json.getInt("success");

                if (success == 1) {


                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        ArrayList<View> allviews1=new ArrayList<View>();

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(count==1)
            {
                predeals.setVisibility(View.INVISIBLE);
                nexdeals.setVisibility(View.VISIBLE);
            }
            else if(count>=2)
            {
                predeals.setVisibility(View.VISIBLE);
                nexdeals.setVisibility(View.VISIBLE);
            }
            else
            {
                predeals.setVisibility(View.INVISIBLE);
                nexdeals.setVisibility(View.INVISIBLE);
            }

            try {
                LinearLayout lv = (LinearLayout) findViewById(R.id.searchresults);
                lv.removeAllViews();
                products = json.getJSONArray("products");
                if(products.length()==0){
                    Button mbut=(Button)findViewById(R.id.searchnext);
                    mbut.setVisibility(View.INVISIBLE);
                    Toast.makeText(Search.this,"No Search Results...Try a Different Keyword or Location",Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 0; i < products.length(); i++) {

                    JSONObject c = products.getJSONObject(i);
                    String deal_title = c.getString("deal_title");
                    //     String deal_description= c.getString("deal_description");
                    String url_link = c.getString("url_link");
                    String main_price = c.getString("main_price");
                    String deal_price = c.getString("deal_price");
                    String end_date = c.getString("end_date");
                    String deal_image = c.getString("deal_image");
                    String business_name = c.getString("business_name");
                    tot=c.getString("total");
                    curtot=c.getString("curtotal");
                    LayoutInflater lin = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View v = lin.inflate(R.layout.listdeals, null);
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
                    TextView tvdescript = (TextView) v.findViewById(R.id.dealdescript);
                    ImageView dealimg = (ImageView) v.findViewById(R.id.deal_img);
                    TextView tvdurl = (TextView) v.findViewById(R.id.correcturl);
                    tvmprice.setText(main_price);
                    tvdprice.setText(deal_price);
                    tvdexpiry.setText("Offered By: " + business_name+" till "+end_date.substring(0,10));
                    tvdname.setText(deal_title);

                    TextView stat=(TextView) v.findViewById(R.id.hiddenstatus);
                    stat.setText(status);

                    //   tvdescript.setText("Details:  "+deal_description);
                    //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
                    TextView disc=(TextView) v.findViewById(R.id.discount);
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


                    TextView popup=(TextView)v.findViewById(R.id.tesladealimagepopup);
                    popup.setText("http://pindout.com/files/businessdeal_images/main_images/"+deal_image);

                    dealimg.setClickable(true);
                    dealimg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View parent = (View) view.getParent();
                            TextView popup = (TextView) parent.findViewById(R.id.tesladealimagepopup);
                            String temp = popup.getText().toString();
                            LayoutInflater lin = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);;
                            View v = lin.inflate(R.layout.popupimage, null);
                            ImageView iv = (ImageView) v.findViewById(R.id.showpopupimage);


                            Picasso.with(v.getContext())
                                    .load(temp)
                                    .into(iv);


                            Dialog settingsDialog = new Dialog(Search.this);
                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            settingsDialog.setContentView(v);


                            settingsDialog.show();
                        }
                    });





                    allviews1.add(v);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View par=(View)view.getParent();
                            TextView stat=(TextView) view.findViewById(R.id.hiddenstatus);
                            if(stat.getText().toString().equals("0"))
                            {
                                Toast.makeText(Search.this,"Local Business Deal",Toast.LENGTH_LONG).show();
                                TextView tvmprice=(TextView) view.findViewById(R.id.showmain_price);
                                TextView tvdprice=(TextView) view.findViewById(R.id.showdeal_price);
                                TextView tvdexpiry=(TextView) view.findViewById(R.id.deal_expiry);
                                TextView tvdname=(TextView) view.findViewById(R.id.deal_name);
                                TextView disc=(TextView) view.findViewById(R.id.dealdescript);
                                TextView dealimg1=(TextView) view.findViewById(R.id.hiddenimgurl);
                                Intent intent=new Intent(Search.this,DealPage.class);
                                intent.putExtra("mrp",tvmprice.getText().toString());
                                intent.putExtra("dprice",tvdprice.getText().toString());
                                intent.putExtra("bname",tvdexpiry.getText().toString());
                                intent.putExtra("dname",tvdname.getText().toString());
                                intent.putExtra("disc",disc.getText().toString());
                                intent.putExtra("dimg",dealimg1.getText());

                                startActivity(intent);
                            }
                            else {
                                TextView correcturl = (TextView) view.findViewById(R.id.correcturl);
                                Intent intent = new Intent(Search.this, DisplayDealUrl.class);
                                intent.putExtra("urlofdeal", correcturl.getText().toString());
                                startActivity(intent);
                            }
                        }
                    });
                    lv.addView(v);
                }
            }

            catch (Exception e) {
                Button mbut=(Button)findViewById(R.id.searchnext);
                mbut.setVisibility(View.INVISIBLE);
                Toast.makeText(Search.this,"No Search Results...Try a Different Keyword or Location",Toast.LENGTH_LONG).show();
                return;
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
            Toast.makeText(Search.this,"Showing "+curtot+" of "+tot+" deals",Toast.LENGTH_LONG).show();

        }
    }


String businessid="";

    class GetTag extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Tagging You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONArray products;
        JSONObject json;
        JSONParser jParser;

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            params.add(new BasicNameValuePair("business_id", businessid));
            jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_gettag, "GET", params);

            try {
                success = json.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        ArrayList<View> allviews1=new ArrayList<View>();

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {

                Toast.makeText(Search.this,"Tagged you Successfully",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Search.this,"Could not tag you to this Business",Toast.LENGTH_LONG).show();
            }

        }
    }


    class GetUntag extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Untagging You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONArray products;
        JSONObject json;
        JSONParser jParser;

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            params.add(new BasicNameValuePair("business_id", businessid));
            jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_getuntag, "GET", params);

            try {
                success = json.getInt("success");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        ArrayList<View> allviews1=new ArrayList<View>();

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (success == 1) {

                Toast.makeText(Search.this,"Untagged you Successfully",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Search.this,"Could not untag you to from Business",Toast.LENGTH_LONG).show();
            }


        }
    }

    TextView tv1,tv2,tv3;
    public void m3ff(View view)
    {
        try {
            count=totalpage;
            if(finaltype.equals("Business")) new GetBusiness().execute();
            else new GetDeals().execute();
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
            if(finaltype.equals("Business")) new GetBusiness().execute();
            else new GetDeals().execute();
        }catch (Exception e){}
    }
    public void m3rewind(View view)
    {

        try {
            count=1;
            if(finaltype.equals("Business")) new GetBusiness().execute();
            else new GetDeals().execute();
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
            if(finaltype.equals("Business")) new GetBusiness().execute();
            else new GetDeals().execute();
        }catch (Exception e){}
    }

    public void tv11n(View view)
    {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count=Integer.valueOf(tv1.getText().toString());
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }
    public void tv22n(View view) {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count = Integer.valueOf(tv2.getText().toString());
        if (finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }
    public void tv33n(View view)
    {
        tv1.setTextColor(Color.parseColor("#6C2DC7"));
        tv2.setTextColor(Color.parseColor("#79BAEC"));
        tv3.setTextColor(Color.parseColor("#79BAEC"));
        count=Integer.valueOf(tv3.getText().toString());
        if(finaltype.equals("Business")) new GetBusiness().execute();
        else new GetDeals().execute();
    }
}

