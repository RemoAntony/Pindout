package com.app.pindout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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


public class BusinessPage extends ActionBarActivity {
    Toolbar toolbar;
    ViewPager pager;
    BusinessPageAdapter adapter;
    private static final String url_Untagbusiness = "http://pindout.com/remuser/pindout/UnTag.php";
    String url_tag_bus="http://pindout.com/remuser/pindout/businessmessage.php";
    String url_bus="http://pindout.com/remuser/pindout/Busdetails.php";
    String url_gettag="http://pindout.com/remuser/pindout/tagbusiness.php";
    String url_checktag="http://pindout.com/remuser/pindout/checktag.php";
    private static final String url_review_add = "http://pindout.com/remuser/pindout/Review_add.php";
    private static String url_review = "http://pindout.com/remuser/pindout/businessreview.php";
    SlidingTabLayout tabs;
    String[] review_id;
    String reply_text,la,lp,lw,lh,tot,curtot;;
    TextView ladd,lpho,lwork,lweb;
    String url_reply_add="http://pindout.com/remuser/pindout/reviewcomment.php";
    int glo=0,ntop=0,nbottom=0;
    JSONParser jParser = new JSONParser();
    CharSequence Titles[]={"Deals","Message","Review"};
    int Numboftabs =3;
    Context con=this;
String bus_name,bus_id,user_id,group_id,email,text="";
    Button untag;
    EditText t;
    ImageView i;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);
        try {

     /*   Picasso.with(this.getApplicationContext())
                .load("http://pindout.com/remuser/pindout/no_user_image.jpg")
                .into(i);*/

        toolbar = (Toolbar) findViewById(R.id.toolbarbus1);
        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent in=getIntent();

      bus_name=in.getStringExtra("com.app.pindout.Business_Name");
            bus_id=in.getStringExtra("com.app.pindout.Business_Id");
            user_id=in.getStringExtra("com.app.pindout.UserId");
           group_id=in.getStringExtra("com.app.pindout.Group");
           email=in.getStringExtra("com.app.pindout.Email");
             i=(ImageView) findViewById(R.id.Bus_Logo);
          ladd =(TextView) findViewById(R.id.ladd);

            lpho =(TextView) findViewById(R.id.lpho);

             lweb =(TextView) findViewById(R.id.lweb);

             lwork =(TextView) findViewById(R.id.lhrs);

            Log.e(" " + bus_name + " " + bus_id, " " + user_id + " " + group_id + " " + email);
            new GetBusinessDetails().execute();

            toolbar.setTitle(bus_name);
            new checkTag().execute();
            untag=(Button) findViewById(R.id.btntag);
            untag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Text",untag.getText()+"");
                    if(untag.getText()=="UnTag") {
                        new UntagBusiness().execute();
                        Toast.makeText(getApplicationContext(), "SuccessfulltyUntagged", Toast.LENGTH_SHORT).show();
                        untag.setBackground(getResources().getDrawable(R.drawable.addtag));
                        untag.setText("Tag");
                    }
                    else
                    {
                        new GetTag().execute();
                       // Toast.makeText(getApplicationContext(), "Successfullty Tagged", Toast.LENGTH_SHORT).show();
                        untag.setBackground(getResources().getDrawable(R.drawable.removetag));
                        untag.setText("UnTag");
                    }

                }
            });

            new GetTaggedDeal().execute();
          new  GetBusMsg().execute();
            new  GetReviewDetails().execute();


        // Setting the ViewPager For the SlidingTabsLayout

        }
        catch (Exception e)
        {
            Log.d("Exception " ,""+ e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_business_page, menu);
        return true;
    }
public String getName()
{
    return untag.getText().toString();
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
    public void Add_Review (String text1)
    {
        text= text1;
        new ReviewBusiness().execute();
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
            pDialog = new ProgressDialog(BusinessPage.this);
            pDialog.setMessage("UnTagging Busines...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", user_id));
                params.add(new BasicNameValuePair("business_id",bus_id));
                json = jParser.makeHttpRequest(url_Untagbusiness, "GET", params);

                success = json.getInt("success");

                if (success == 1) {
                    Toast.makeText(getApplicationContext(), "Successfully Untagged", Toast.LENGTH_SHORT).show();

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
    ArrayList<View> allviews1=new ArrayList<View>();
    public ArrayList<View> disDeals()
    {
        return allviews1;
    }
    class GetBusMsg extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        JSONObject json;
        LayoutInflater lin;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
            pDialog.setMessage("Loading Business Side...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("business_id",bus_id));
            json = jParser.makeHttpRequest(url_tag_bus, "GET", params);
            lin=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            Log.e("CreteResponse",json.toString());
            try {
                success = json.getInt("success");

             /*   if (success == 1) {


                } else {
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {
                Log.e("Success",""+success);
if(success==1) {
    JSONArray products = json.getJSONArray("products");
    for (int i = 0; i < products.length(); i++) {
        JSONObject c = products.getJSONObject(i);
        String bname = c.getString("business_name");
        String bmessage = c.getString("message");
        String bemail = c.getString("date");


        View v = lin.inflate(R.layout.bus_msg, null);
        TextView tvbname = (TextView) v.findViewById(R.id.tagbname);
        TextView tvbemail = (TextView) v.findViewById(R.id.tagbemail);
        TextView tvbaddress = (TextView) v.findViewById(R.id.tagbaddr);

        tvbname.setText(bname);
        tvbaddress.setText(bmessage);
        tvbemail.setText(bemail);

        allviews.add(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
}


            }catch (Exception e){
                Log.d("Mewtwo",""+e);
            }


                    pDialog.dismiss();



        }
    }
    ArrayList<View> allviews=new ArrayList<View>();
    public ArrayList<View> disMsg()
    {
        return allviews;
    }
    JSONParser jParser1=new JSONParser();
    String url_tag_deal="http://pindout.com/remuser/pindout/businessdeals.php";
    String deal_title,deal_description,deal_price,main_price,end_date,url_link,deal_image,business_name;
    int count=1;
    public int getCount()
    {
        return  count;
    }
    public void setCount(int count)
    {
        this.count=count;
        new GetTaggedDeal().execute();
    }

    ImageView dealimg;
    class GetTaggedDeal extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
            pDialog.setMessage("Making things Ready for You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONObject json;
        LayoutInflater lin;
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("business_id",bus_id));
            params.add(new BasicNameValuePair("count",String.valueOf(count)));
            json = jParser1.makeHttpRequest(url_tag_deal, "GET", params);
            lin=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                success = json.getInt("success");
/*
                if (success == 1) {


                } else {
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {
                Log.e("Success",""+success);
if(success==1) {
    //      for (int i = 0; i < allviews.size(); i++)
    //        tagbuslay.addView(allviews.get(i));
    // Creating The DashViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
    JSONArray products = json.getJSONArray("products");
    for (int i = 0; i < products.length(); i++) {
        JSONObject c = products.getJSONObject(i);
        deal_title = c.getString("deal_title");
        deal_description = c.getString("deal_description");
        url_link = c.getString("url_link");
        main_price = c.getString("main_price");
        deal_price = c.getString("deal_price");
        deal_image = c.getString("deal_image");
        tot=c.getString("total");
        curtot=c.getString("curtotal");
        String enddate=c.getString("end_date");
        //  business_name=c.getString("business_name");

        View v = lin.inflate(R.layout.listdeals, null);
        Button interest = (Button) v.findViewById(R.id.interestedbutton);
        interest.setVisibility(View.GONE);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date end=dateFormat.parse(enddate);

        Date d1=dateFormat.parse(dateFormat.format(date));



        RelativeLayout rl =(RelativeLayout) v.findViewById(R.id.top_lay);
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
        tvdexpiry.setVisibility(View.INVISIBLE);

        TextView tvdescript = (TextView) v.findViewById(R.id.dealdescript);
        dealimg = (ImageView) v.findViewById(R.id.deal_img);
        TextView tvdurl = (TextView) v.findViewById(R.id.correcturl);
        tvmprice.setText(main_price);
        tvdprice.setText(deal_price);
        // tvdexpiry.setText("Offered By: "+business_name);
        tvdname.setText(deal_title);
        tvdescript.setText("Details:  " + deal_description);
        //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
        double mainprice = Double.parseDouble(tvmprice.getText().toString());
        double dealprice = Double.parseDouble(tvdprice.getText().toString());
        ;
        double diff = mainprice - dealprice;
        double percen = diff * 100 / mainprice;
        disc.setText((Math.ceil(percen) + " %  Off"));


        tvdurl.setText(url_link);
        Picasso.with(v.getContext())
                .load("http://pindout.com/files/businessdeal_images/main_images/"+deal_image)
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

                View v = lin.inflate(R.layout.popupimage, null);
                ImageView iv = (ImageView) v.findViewById(R.id.showpopupimage);


                Picasso.with(v.getContext())
                        .load(temp)
                        .into(iv);


                Dialog settingsDialog = new Dialog(BusinessPage.this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(v);


                settingsDialog.show();
            }
        });



        allviews1.add(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View par = (View) view.getParent();
                TextView correcturl = (TextView) view.findViewById(R.id.correcturl);
                Intent intent = new Intent(BusinessPage.this, DisplayDealUrl.class);
                intent.putExtra("urlofdeal", correcturl.getText().toString());
                startActivity(intent);
            }
        });
    }
}
            }catch (Exception e){
                Log.d("Mewtwo",""+e);
            }


            pDialog.dismiss();
            nbottom=0;
            ntop=0;
           // Toast.makeText(BusinessPage.this,"Showing "+curtot+" of "+tot + " deals",Toast.LENGTH_LONG).show();

        }
    }
    public String[] to()
    {
        String[] t=new String[2];
        t[0]=curtot;
        t[1]=tot;
        return t;
    }
    public int[] bo()
    {
        int[] b=new int[2];
        b[0]=nbottom;
        b[1]=ntop;
        return  b;
    }
    public void bot(int nb,int nt)
    {
        nbottom=nb;
        ntop=nt;
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

            pDialog = new ProgressDialog(BusinessPage.this);
           // pDialog.setMessage("Loading your Profile...");
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
                        SharedPreferences pref = getSharedPreferences("loginpref", Context.MODE_PRIVATE);
                        String username  =pref.getString("id", null);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("business_id",bus_id));
                        json = jp.makeHttpRequest(url_review, "GET", params);
                        in=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        success = json.getInt("success");
                        Log.d("Response",json.toString());
                 /*       if (success == 1) {

                        }
                        else {

                        }*/


                    } catch (Exception e) {
                        Log.d("Review1",""+e);
                        Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {

            try {
                Log.e("Success",""+success);
 if(success==1) {
     productObj = json.getJSONArray("product");
     String[][] b = new String[productObj.length() + 1][7];

     for (int i = 0; i < productObj.length(); i++) {
         JSONObject c = productObj.getJSONObject(i);
         b[i][0] = c.getString("id");
         b[i][1] = c.getString("firstname");
         b[i][2] = c.getString("review_text");
         b[i][5]=c.getString("review_date");
         // b[i][3] = c.getString("comment_text");
         b[i][4] = c.getString("user_id");
         if (b[i][4].equals("0"))
             b[i][3] = bus_name + " :" + c.getString("comment_text")+ " "+c.getString("date").substring(0, 10);
         else if (!b[i][4].equals("-1"))
             b[i][3] = b[i][1] + " :" + c.getString("comment_text")+ " "+c.getString("date").substring(0, 10);
         else
             b[i][3] = c.getString("comment_text");
         //bid=c.getString("id");
         //  Log.e("Date", "" + bemail);
     }
     int[] c1 = new int[productObj.length()];
     c1[0] = -1;
     int temp = 0, y1 = 0;
     for (int i = 0; i < productObj.length(); i++) {
         c1[y1] = i;
         temp = i;
         for (int k = i + 1; k < productObj.length(); k++) {
           //  Log.e("T" + b[i][0], "" + b[k][1]);
             if (b[temp][0].equals(b[k][0])) {
                 b[temp][3] += "\n" + b[k][3];

                 i++;
             }
         }
         y1++;
     }
if(c1[0] == -1)
{
    c1[0]=0;
    y1=1;
}

     review_id = new String[productObj.length()];
     int y = 0;
     for (int i = 0; i < y1; i++) {
         Log.e("" + b[c1[i]][0], "" + b[c1[i]][2]);
         //}
         //for (int i = 0; i < productObj.length(); i++) {
         JSONObject product = productObj.getJSONObject(i);
         View v = in.inflate(R.layout.busreview, null);
         final LinearLayout ll = (LinearLayout) v.findViewById(R.id.dynText);
         final LinearLayout.LayoutParams params;
         params = new LinearLayout.LayoutParams(
                 ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
         TextView bus_name1 = (TextView) v.findViewById(R.id.user_name);
         TextView desc = (TextView) v.findViewById(R.id.review);
         TextView rep = (TextView) v.findViewById(R.id.reply);
         review_id[y] = b[c1[i]][0];

         final TextView tbus_reply = (TextView) v.findViewById(R.id.reply1);
         tbus_reply.setText(String.valueOf(y));
         y++;

         Button re = (Button) v.findViewById(R.id.repbutton);
         ImageView dealimg = (ImageView) v.findViewById(R.id.userpic);
         bus_name1.setText(b[c1[i]][1]);
         //Log.e("Username", "" + bus_name1.getText());
         desc.setText("-> Review :"+b[c1[i]][2]+" "+b[c1[i]][5].substring(0,10));

         // Log.e("Review", "" + desc.getText());
         if (!b[c1[i]][3].equals("-1")) {
             rep.setVisibility(View.VISIBLE);
             re.setVisibility(View.VISIBLE);
             // tbus_reply.setVisibility(View.INVISIBLE);
             rep.setText(b[c1[i]][3]);

         } else {
             rep.setVisibility(View.GONE);
             re.setVisibility(View.GONE);
             //  tbus_reply.setVisibility(View.INVISIBLE);
         }
         //   Log.e("Place", place.getText() + " ");
         Picasso.with(v.getContext())
                 .load("http://pindout.com/remuser/pindout/no_user_image.jpg")
                 .into(dealimg);

         re.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 try {

                     final TextView dyn = new TextView(BusinessPage.this);
                     dyn.setText(" ");
                     dyn.setTextSize(20);
                     dyn.setLayoutParams(params);
                     dyn.setPadding(50, 0, 0, 0);
                     ll.addView(dyn);
                     //    Toast.makeText(getApplicationContext(), "Reply added"+tbus_reply.getText(), Toast.LENGTH_SHORT).show();
                     //  final TextView tbus_reply = (TextView) view.findViewById(R.id.reply1);
                     //     tbus_reply.setVisibility(View.VISIBLE);

                     final Dialog dialog = new Dialog(context);
                     dialog.setContentView(R.layout.replyreview);
                     dialog.setTitle("Review Reply");

                     // set the custom dialog components - text, image and button

                     final EditText t = (EditText) dialog.findViewById(R.id.text1);

                     Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonCan);
                     // if button is clicked, close the custom dialog
                     dialogButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             dyn.setVisibility(View.GONE);

                             dialog.dismiss();
                         }
                     });
                     Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
                     // if button is clicked, close the custom dialog
                     dialogButton1.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             String t1 = t.getText().toString();
                             //          tbus_reply.setText(t1);
                             if(!TextUtils.isEmpty(t1)) {
                             dyn.setText("Reply :" + t1);
                             reply_text = t1;
                             glo = Integer.parseInt(tbus_reply.getText().toString());
                             new Replyadd().execute();
                             Toast.makeText(getApplicationContext(), "Reply added", Toast.LENGTH_SHORT).show();
                         }
                         else
                         {
                             Toast.makeText(getApplicationContext(), "Enter something", Toast.LENGTH_SHORT).show();
                         }
                             dialog.dismiss();

                         }
                     });

                     dialog.show();
                 } catch (Exception x)

                 {
                     Log.d("Exception_button", x.getMessage());
                 }

             }
         });
         v.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


             }
         });
         allviews3.add(v);
     }
 }
     Log.e("Size", allviews3.size() + "");

                pDialog.dismiss();
     Handler handler = new Handler();
     handler.postDelayed(new Runnable() {

         @Override
         public void run() {
             adapter = new BusinessPageAdapter(getSupportFragmentManager(), Titles, Numboftabs);

             // Assigning ViewPager View and setting the adapter
             pager = (ViewPager) findViewById(R.id.pager1);
             pager.setOffscreenPageLimit(3);
             pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
             pager.setAdapter(adapter);

             // Assiging the Sliding Tab Layout View
             tabs = (SlidingTabLayout) findViewById(R.id.tabs1);
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
     }, 200);


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
    class GetTag extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
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
            params.add(new BasicNameValuePair("user_id",user_id));
            params.add(new BasicNameValuePair("business_id", bus_id));
            jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_gettag, "GET", params);

            try {
                success = json.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {

                Toast.makeText(BusinessPage.this,"Tagged you Successfully",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BusinessPage.this,"Could not tag you to this Business",Toast.LENGTH_LONG).show();
            }

        }
    }
    class checkTag extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
          //  pDialog.setMessage("Tagging You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        JSONArray products;
        JSONObject json;
        JSONParser jParser;

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id",user_id));
            params.add(new BasicNameValuePair("business_id", bus_id));
            jParser = new JSONParser();
            json = jParser.makeHttpRequest(url_checktag, "GET", params);

            try {
                success = json.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {

                untag.setText("UnTag");
                untag.setBackground(getResources().getDrawable(R.drawable.removetag));
            } else {
                untag.setText("Tag");
                untag.setBackground(getResources().getDrawable(R.drawable.addtag));
            }

        }
    }

    class ReviewBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        Boolean update_success;
        JSONParser jP=new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
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
            params.add(new BasicNameValuePair("user_id",user_id));
            params.add(new BasicNameValuePair("business_id",bus_id));
            params.add(new BasicNameValuePair("group_id", group_id));
            params.add(new BasicNameValuePair("email",email));
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
                    Intent in=new Intent(BusinessPage.this,BusinessPage.class);
                    in.putExtra("com.app.pindout.Business_Name",bus_name);
                    in.putExtra("com.app.pindout.Business_Id", bus_id);
                    in.putExtra("com.app.pindout.Group",group_id);
                    in.putExtra("com.app.pindout.Email",email);
                    in.putExtra("com.app.pindout.UserId", user_id);
                    startActivity(in);

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
    class Replyadd extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        Boolean update_success;
        JSONParser jP=new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusinessPage.this);
            pDialog.setMessage("adding Reply...");
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
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("review_id",review_id[glo]));
            params.add(new BasicNameValuePair("group_id"," " ));
            params.add(new BasicNameValuePair("email","2"));
            params.add(new BasicNameValuePair("review_text",reply_text));
            params.add(new BasicNameValuePair("review_datetime",dateFormat.format(date)));
            params.add(new BasicNameValuePair("ip_address",   ip.getHostAddress()));

            Log.e("date+ip",ip.getHostAddress()+""+dateFormat.format(date));

            JSONObject json = jP.makeHttpRequest(url_reply_add, "GET", params);

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
    class GetBusinessDetails extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(BusinessPage.this);
            //   pDialog.setMessage("Loading your Profile...");
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
                        Log.e("Bus_name",""+bus_name);
                        params.add(new BasicNameValuePair("bus_id",bus_name));
                        json = jp.makeHttpRequest(url_bus, "GET", params);
                        in=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                        success = json.getInt("success");
                        Log.d("Response",json.toString());
                        if (success == 1) {

                        }
                        else {

                        }
                        productObj = json
                                .getJSONArray("products");

                    } catch (Exception e) {
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

                JSONObject c = productObj.getJSONObject(0);
                bus_name = c.getString("business_name");
                if(!c.getString("address").equals(""))
                la= c.getString("address");
                else
                la="Web Based";
                if(!c.getString("phone").equals(""))
                    lp= c.getString("phone");
                else
                    lpho.setVisibility(View.INVISIBLE);
                if(!c.getString("website").equals(""))
                    lw= c.getString("website");
                else
                    lweb.setVisibility(View.INVISIBLE);
                if(!c.getString("working").equals(""))
                    lh= c.getString("working");
                else
                    lwork.setVisibility(View.INVISIBLE);
                if(!c.getString("image").equals("")) {
                    deal_image = c.getString("image");
                    Picasso.with(con)
                            .load("http://pindout.com/files/business_images/main_images/" + deal_image)
                            .into(i);
                }

                lwork.setText(lh);
                ladd.setText(la);
                lpho.setText(lp);
                lweb.setText(lw);
                Log.e("" + la + "" + lp, "" + lh + "" + lw);


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
