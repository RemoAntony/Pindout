package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DashBoard extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;
    DashViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Profile"};
    int Numboftabs =1;


    JSONParser jParser=new JSONParser();
    EditText inputFirstName,inputEmail,inputLastName,inputPhone,inputDOB,inputAddress, inputanniversary;
    TextInputLayout inputLayoutFirstName,inputLayoutEmail,inputLayoutLastName,inputLayoutPhone,inputLayoutDOB,inputLayoutAddress, inputLayoutanniversary;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_DOB = "dob";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ANNIVEERSARY = "anniversary";
    protected String first_name = "hi", email = "", lastname = "",phone = "", dob = "", address = "", anniversary = "",password="";
    private static String url_get_profile = "http://pindout.com/remuser/pindout/profile_detail.php";
    private static String url_edit_profile = "http://pindout.com/remuser/pindout/Profile_edit.php";
    private static String url_following = "http://pindout.com/remuser/pindout/Following.php";
    private static String url_followers = "http://pindout.com/remuser/pindout/Followers.php";
    private static String url_review = "http://pindout.com/remuser/pindout/Review.php";
    String username;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         pref = getSharedPreferences("loginpref", Context.MODE_PRIVATE);
        username  =pref.getString("id", null);

        new GetUserDetails().execute();

        // Creating The DashViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_profile, menu);
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
    ArrayList<View> allviews1=new ArrayList<View>();
    public ArrayList<View> disProfile()
    {
        return allviews1;
    }
    class GetUserDetails extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DashBoard.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        JSONArray productObj;
        protected String doInBackground(String... params) {

           runOnUiThread(new Runnable() {
               public void run() {

                   int success;
                   try {
                       List<NameValuePair> params = new ArrayList<NameValuePair>();
                       params.add(new BasicNameValuePair("username",username));
                       json = jParser.makeHttpRequest(url_get_profile, "GET", params);
                       in=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                       success = json.getInt(TAG_SUCCESS);
                       if (success == 1) {

                            productObj = json
                                   .getJSONArray(TAG_PRODUCT);
                       }
                       else {

                       }
                   } catch (JSONException e) {
                       Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
               }
               }
           });

            return null;
        }

        protected void onPostExecute(String file_url) {
try {


    for(int  i=0;i<productObj.length();i++) {
        JSONObject product = productObj.getJSONObject(i);

        first_name = product.getString(TAG_FIRSTNAME);
        email = product.getString(TAG_EMAIL);
        lastname = product.getString(TAG_LASTNAME);
        phone = product.getString(TAG_PHONE);
        address = product.getString(TAG_ADDRESS);
        dob = product.getString(TAG_DOB);
        anniversary = product.getString(TAG_ANNIVEERSARY);
        password=product.getString("password");

        profdetails.add(first_name);
        profdetails.add(email);
        profdetails.add(lastname);
        profdetails.add(phone);
        profdetails.add(address);
        profdetails.add(dob);
        profdetails.add(anniversary);
        profdetails.add(password);
        View v3 = in.inflate(R.layout.profile, null);



    /*    btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
            }
        });
*/

        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        allviews1.add(v3);
    }

    new Networkdetails().execute();
    adapter =  new DashViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

    // Assigning ViewPager View and setting the adapter
    pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(adapter);

    // Assiging the Sliding Tab Layout View
    tabs = (SlidingTabLayout) findViewById(R.id.tabs);
    tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

    // Setting Custom Color for the Scroll bar indicator of the Tab View
    tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
        @Override
        public int getIndicatorColor(int position) {
            return getResources().getColor(R.color.tabsScrollColor);
        }
    });

    // Setting the ViewPager For the SlidingTabsLayout
    tabs.setViewPager(pager);

    pDialog.dismiss();
}
catch(JSONException e)
{
    //Toast.makeText(getApplicationContext(),"OnPostExecute",Toast.LENGTH_SHORT).show();
}
            catch (Exception e1)
            {
               // Toast.makeText(getApplicationContext(),"OnPostExecute1",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<String> getProfdetails()
    {
        return profdetails;
    }

    ArrayList<String> profdetails=new ArrayList<String>();


    private boolean validateEmail() {
        try {

            if(inputEmail.getText().toString().equals(""))
            {
             //   inputLayoutEmail.setError(getString(R.string.err_msg_email));
               // requestFocus(inputEmail);
                return false;

            }
            if (inputEmail.getText().toString().trim().isEmpty()) {
                inputLayoutEmail.setErrorEnabled(false);
                return true;
            } else {
                String EMAIL_PATTERN =
                        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(inputEmail.getText().toString());
                if(!matcher.matches()) {
                    inputLayoutEmail.setError(getString(R.string.err_msg_email));
                    requestFocus(inputEmail);
                    return  false;
                }
                else
                {
                    inputLayoutEmail.setErrorEnabled(false);
                    return true;
                }

            }

        }catch (Exception e){
            //inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        }
    }



    private boolean validatePhone() {

            if (inputPhone.length() < 10) {
                inputLayoutPhone
                        .setError(getString(R.string.err_msg_invalid_mobileno));
                requestFocus(inputPhone);
                return false;
            } else {
                inputLayoutPhone.setErrorEnabled(false);
            }

        return true;
    }






    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1,
                                  int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.input_email:
                    validateEmail();
                    break;

                case R.id.input_phone:
                    validatePhone();
                    break;


            }
        }
    }

    public void upppp(String ffname,String  eemail,String llname,String pphone,String aaddress,String ddob,String aanniversary,String ppassword)
    {
        first_name=ffname;
        email=eemail;
        lastname=llname;
        phone=pphone;
        address=aaddress;
        dob=ddob;
anniversary=aanniversary;
        password=ppassword;
        new UpdateUserDetails().execute();
    }

   public class UpdateUserDetails extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
      Boolean update_success;
        JSONParser jP=new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoard.this);
            pDialog.setMessage("Updating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            Log.d("Value", username+" "+dob+" "+first_name+" "+password+" "+lastname+" "+email+" "+phone+" "+anniversary+""+address);
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("firstname",first_name));
            params.add(new BasicNameValuePair("password",password));
            params.add(new BasicNameValuePair("lastname", lastname));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("address",address));
            params.add(new BasicNameValuePair("dob", dob));
            params.add(new BasicNameValuePair("anniversary",anniversary));

            JSONObject json = jP.makeHttpRequest(url_edit_profile, "GET", params);

            Log.d("Create Response", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    update_success = true;
                } else {
                    update_success = false;
                }
            } catch (JSONException e) {
             //   Toast.makeText(getApplicationContext(), "UpdatError", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Updated",
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
    ArrayList<String> followersname=new ArrayList<String>();
    ArrayList<String> followingname=new ArrayList<String>();

    ArrayList<View> allviews2=new ArrayList<View>();
    public ArrayList<View> disNetwork()
    {
        return allviews2;
    }

    public ArrayList<String> getFollowersname()
    {
        return  followersname;
    }

    public ArrayList<String> getFollowingname()
    {
        return followingname;
    }

    class Networkdetails extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        JSONParser jP = new JSONParser();
        JSONObject json,json1;
        JSONArray productObj,productObj1;
        LayoutInflater in;
        int success=0,s=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashBoard.this);
            pDialog.setMessage("Retrieving...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            runOnUiThread(new Runnable() {
                public void run() {


                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_id", username));
                        json = jP.makeHttpRequest(url_followers, "GET", params);
                        success = json.getInt(TAG_SUCCESS);
                        Log.d("JSONError", json.toString());
                        json1 = jP.makeHttpRequest(url_following, "GET", params);
                        s = json1.getInt(TAG_SUCCESS);
                        Log.d("JSONError", json1.toString());
                        in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        if (success == 1) {

                            productObj = json
                                    .getJSONArray("products");
                        } else {

                        }
                        if (s == 1) {
                            productObj1 = json1
                                    .getJSONArray("products");
                        } else {

                        }
                    } catch (JSONException e) {
                        Log.d("JSONError", "" + e);
                  //      Toast.makeText(getApplicationContext(), "OnPreExecute", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        String followers,following;
            try {
                JSONArray products = json.getJSONArray("products");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);
                    followers=c.getString("firstname");
                    followersname.add(followers);
                }
            }catch (Exception e){}
            try{
                JSONArray products = json1.getJSONArray("products");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);
                    followers=c.getString("firstname");
                    followingname.add(followers);
                }
            }catch (Exception e){}
        }


    }
    ArrayList<View> allviews3=new ArrayList<View>();
    public ArrayList<View> disReview()
    {
        return allviews3;
    }
    class GetReviewDetails extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DashBoard.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        JSONArray productObj;
        JSONParser jp=new JSONParser();
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {


                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username",username));
                        json = jp.makeHttpRequest(url_review, "GET", params);
                        in=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        success = json.getInt(TAG_SUCCESS);
                        Log.d("Response",json.toString());
                        if (success == 1) {

                            productObj = json
                                    .getJSONArray(TAG_PRODUCT);
                        }
                        else {

                        }
                    } catch (JSONException e) {
                        Log.d("Review1",""+e);
                        Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            try {

                if(success==1) {
                    for (int i = 0; i < productObj.length(); i++) {
                        JSONObject product = productObj.getJSONObject(i);
                        View v = in.inflate(R.layout.reviewstruct, null);
                        TextView bus_name = (TextView) v.findViewById(R.id.business_name11);
                        TextView place = (TextView) v.findViewById(R.id.place);
                        TextView desc = (TextView) v.findViewById(R.id.review);
                        ImageView dealimg = (ImageView) v.findViewById(R.id.Logoo);
                        bus_name.setText(product.getString("business_name"));
                        place.setText(product.getString("slug"));
                        desc.setText("Review : "+product.getString("review_text"));
                        Log.e("Place", place.getText() + " ");
                        Picasso.with(v.getContext())
                                .load("http://pindout.com/remuser/pindout/no_user_image.jpg")
                                .into(dealimg);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                            }
                        });
                        allviews3.add(v);
                    }
                    Log.e("Size",allviews3.size()+"");
                }
                else
                {
                 //   Toast.makeText(getApplicationContext(),"Else",Toast.LENGTH_SHORT).show();
                }


                pDialog.dismiss();
            }
            catch(JSONException e)

            {
                Log.d("Review2",""+e);
             //   Toast.makeText(getApplicationContext(),"OnPostExecute",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e1)
            {
           //     Toast.makeText(getApplicationContext(),"OnPostExecute1",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
