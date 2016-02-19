package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MoreDealsSub2 extends ActionBarActivity {

    String title="";
    String url_deal_sub="http://pindout.com/remuser/pindout/moredealscategory.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_deals_sub2);
        Bundle bundle=getIntent().getExtras();
        title=bundle.getString("title");
//        Toast.makeText(MoreDealsSub2.this,""+title,Toast.LENGTH_LONG).show();
        new GetBusiness().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more_deals_sub2, menu);
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

    class GetBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MoreDealsSub2.this);
            pDialog.setMessage("Loading Deals...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONParser jParser = new JSONParser();

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", title));
            json = jParser.makeHttpRequest(url_deal_sub, "GET", params);

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
        String deal_category_name;
        TextView subdealid;
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            try {
                LinearLayout lv=(LinearLayout)findViewById(R.id.moresubdeals);
                lv.removeAllViews();
                JSONArray products = json.getJSONArray("products");
                //Toast.makeText(MoreDealsSub2.this,""+products,Toast.LENGTH_LONG).show();
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    String id=c.getString("id");
                    deal_category_name=c.getString("deal_category_name");
                    //Toast.makeText(MoreDealsSub2.this,""+deal_category_name,Toast.LENGTH_LONG).show();

                    LayoutInflater lin=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View v=lin.inflate(R.layout.buttonsubdeals,null);

                    subdealid=(TextView)v.findViewById(R.id.idforsubdeal);
                    subdealid.setText(id);

                    final Button taddr=(Button)v.findViewById(R.id.buttonsubdeals);
                    taddr.setText(deal_category_name);
                    taddr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View parent=(View)view.getParent();
                            TextView tv=(TextView)parent.findViewById(R.id.idforsubdeal);
                            //Toast.makeText(MoreDealsSub2.this, tv.getText().toString()+" : " + taddr.getText(), Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MoreDealsSub2.this,MoreDealsSub3.class);
                            intent.putExtra("idd",tv.getText().toString());
                            intent.putExtra("titlee",taddr.getText().toString());
                            startActivity(intent);
                            
                            overridePendingTransition(R.anim.right_to_left_enter,
                                    R.anim.right_to_left_exit);
                        }
                    });


                    lv.addView(v);
                }
                if(products.length()==0) Toast.makeText(MoreDealsSub2.this,"No Search Results...Try a Different Keyword or Location",Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(MoreDealsSub2.this,"Exception:"+e,Toast.LENGTH_LONG).show();
            }

        }
    }


}
