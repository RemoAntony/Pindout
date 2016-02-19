package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaggedBusinesses extends ActionBarActivity {
    private static final String url_taggedbusiness = "http://pindout.com/remuser/pindout/taggedbusiness.php";
    private static final String url_Untagbusiness = "http://pindout.com/remuser/pindout/UnTag.php";
    private static final String url_review_add = "http://pindout.com/remuser/pindout/Review_add.php";
 /*   public final static String USER = "com.app.pindout.UserId";
    public final static String USER1 = "com.app.pindout.Business_Name";
    public final static String USER2 = "com.app.pindout.Business_Id";
    public final static String USER3 = "com.app.pindout.Group";
    public final static String USER4 = "com.app.pindout.Email";*/
    JSONParser jParser = new JSONParser();
    String[] bus_id,group_id,email;
    String id;
    String business_id,text;
    Toolbar toolbar;
    int glo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taggedbusiness);
        SharedPreferences pref = getSharedPreferences("loginpref", MODE_PRIVATE);
        id=pref.getString("id", null);
     toolbar = (Toolbar) findViewById(R.id.toolbartag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new GetTaggedBusiness().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tagged_businesses, menu);
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
    class GetTaggedBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        JSONObject json;
        LayoutInflater lin;
        int k=0;
        Button untag;

        LinearLayout lv=(LinearLayout) findViewById(R.id.taggedbusiness);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaggedBusinesses.this);
            pDialog.setMessage("Loading Tagged Business");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", id));
            json = jParser.makeHttpRequest(url_taggedbusiness, "GET", params);
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

int j=0;
String bname,badd,bphone,bid,bgroupid,bemail,brate;
                JSONArray products = json.getJSONArray("products");
                bus_id=new String[products.length()];
                group_id=new String[products.length()];
                email=new String[products.length()];
                for(int i=0;i<products.length();i++){
                    JSONObject c = products.getJSONObject(i);
                    bname=c.getString("business_name");
                    bphone =c.getString("phone");
                    badd=c.getString("address");
                   bemail=c.getString("email");
                    brate=c.getString("avg_rate");
                    if(bphone.equals("0"))
                        bphone="No Number";
                    if(badd.equals("all"))
                        badd="webbased";
                    bid=c.getString("id");
                    bgroupid=c.getString("group_id");
                   bus_id[j]=bid;
                    group_id[j]=bgroupid;
email[j]=bemail;

                    View v=lin.inflate(R.layout.activity_tagged_businesses, null);
                    TextView bus_name=(TextView) v.findViewById(R.id.bus_name);
                final  TextView count=(TextView) v.findViewById(R.id.count);
                    count.setText(String.valueOf(j));

                    j++;
                    bus_name.setText(bname);
                     TextView phone=(TextView) v.findViewById(R.id.phone);
                  //  TextView rate=(TextView) v.findViewById(R.id.rate);
                    TextView address=(TextView) v.findViewById(R.id.address);
/*
                     untag=(Button) v.findViewById(R.id.Untag);

                    final Button review=(Button) v.findViewById(R.id.Review);*/

                  address.setText("Address :"+badd);
                    phone.setText("Phone :"+bphone);
                /*      rate.setText("Rate :"+brate+"/5");
                   final EditText reviewtext=(EditText) v.findViewById(R.id.Review_text);


                    View.OnClickListener myhandler = new View.OnClickListener() {
                        public void onClick(View v) {
                            switch(v.getId()) {
                                case R.id.Review:
                                    // it was the first button
                                    glo=Integer.parseInt(count.getText().toString());
                                  text= reviewtext.getText().toString();
                                    new ReviewBusiness().execute();
                                    Intent in=new Intent(TaggedBusinesses.this,TaggedBusinesses.class);
                                    startActivity(in);
                                  //  Toast.makeText(getApplicationContext(),"Review_added",Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.Untag:
                                    // it was the second button
                                    glo=Integer.parseInt(count.getText().toString());
                                    new UntagBusiness().execute();
                                    Log.e("Successfully", "" + "Untagged");
                                     in=new Intent(TaggedBusinesses.this,TaggedBusinesses.class);
                                    startActivity(in);

                                    break;
                            }
                        }
                    };
                    review.setOnClickListener(myhandler);
                    untag.setOnClickListener(myhandler);
*/
                   lv.addView(v);


                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                  TextView temp = (TextView) view.findViewById(R.id.count);
                            glo = Integer.parseInt(temp.getText().toString());
                            Log.e("glo", ""+glo);

                            TextView bus_name=(TextView) view.findViewById(R.id.bus_name);
                            Intent in=new Intent(TaggedBusinesses.this,BusinessPage.class);
                            // Log.e("bus", "" + bus_name.getText().toString());
                            in.putExtra("com.app.pindout.Business_Name",bus_name.getText().toString());
                            in.putExtra("com.app.pindout.Business_Id", bus_id[glo]);
                            in.putExtra("com.app.pindout.Group",group_id[glo]);
                            in.putExtra("com.app.pindout.Email",email[glo]);
                            in.putExtra("com.app.pindout.UserId",id);
                            startActivity(in);



                        }
                    });

                }



            }catch (Exception e){
                Log.d("Tag Business", "" + e);
            }
            pDialog.dismiss();


        }
    }
    class UntagBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        int j=0;
        JSONObject json;
        LayoutInflater lin;
        LinearLayout lv=(LinearLayout) findViewById(R.id.taggedbusiness);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaggedBusinesses.this);
            pDialog.setMessage("UnTagging Busines...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            Log.e("value", " " + bus_id[glo]);
            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", id));
            params.add(new BasicNameValuePair("business_id",bus_id[glo]));
            json = jParser.makeHttpRequest(url_Untagbusiness, "GET", params);

                success = json.getInt("success");

                if (success == 1) {
                    Toast.makeText(getApplicationContext(),"Successfully Untagged",Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("UnTag", "Unsuccesful");
                }
            } catch (Exception e) {
                Log.d("Tag Business", "" + e);
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            pDialog.dismiss();


        }
    }
    class ReviewBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        Boolean update_success;
        JSONParser jP=new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaggedBusinesses.this);
            pDialog.setMessage("adding Review...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            InetAddress ip=null;
            try {
               ip =  InetAddress.getLocalHost();

            }

            catch(Exception e)
            {
                Log.d("host", "" + e);
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", id));
            params.add(new BasicNameValuePair("business_id",bus_id[glo]));
            params.add(new BasicNameValuePair("group_id", group_id[glo]));
            params.add(new BasicNameValuePair("email",email[glo]));
            params.add(new BasicNameValuePair("review_text", text));
            params.add(new BasicNameValuePair("review_datetime",dateFormat.format(date)));
            params.add(new BasicNameValuePair("ip_address",   ip.getHostAddress()));

Log.e("date+ip",ip.getHostAddress()+""+dateFormat.format(date));

            JSONObject json = jP.makeHttpRequest(url_review_add, "GET", params);

            Log.d("Create Response", json.toString());

            try {

                int success = json.getInt("success");

                if (success == 1) {
                    update_success = true;
                } else {
                    update_success = false;
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "UpdatError", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                Log.d("Update1", "" + e);
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                pDialog.dismiss();
                if (update_success) {
                    Toast.makeText(getApplicationContext(), "Review Added",
                            Toast.LENGTH_SHORT).show();
                    update_success = false;


                    overridePendingTransition(R.anim.abc_fade_in,
                            R.anim.abc_fade_out);
                } else {
                    Toast.makeText(getApplicationContext(), "Not Updated",
                            Toast.LENGTH_SHORT).show();
                    update_success = false;
                    finish();
                }
            }
            catch(Exception e)
            {
                Log.d("Update",""+e);
            }
        }
    }
}
