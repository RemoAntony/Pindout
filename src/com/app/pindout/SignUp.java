package com.app.pindout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends ActionBarActivity {
    private static final String url_signup = "http://pindout.com/remuser/pindout/insert_signup.php";
    String semail,spass,sname;
    private CheckBox agreeCheckBox;

    JSONParser jsonParser=new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        agreeCheckBox = (CheckBox) findViewById(R.id.agree_check);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.signuppage);
        Picasso.with(SignUp.this).load(R.drawable.background).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                relativeLayout.setBackground(new BitmapDrawable(SignUp.this.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
        TextView term=(TextView) findViewById(R.id.term);
       term.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
          // Toast.makeText(getApplicationContext(),"Terms",Toast.LENGTH_LONG).show();
               Intent i=new Intent(SignUp.this,Terms.class);
               startActivity(i);
           }
           });
        Button btn=(Button)findViewById(R.id.btnSingIn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText en=(EditText) findViewById(R.id.etUserName);
                EditText ee=(EditText) findViewById(R.id.etEmail);
                EditText ep=(EditText) findViewById(R.id.etPass);
                semail=ee.getText().toString();
                sname=en.getText().toString();
                spass=ep.getText().toString();
                EditText cp=(EditText) findViewById(R.id.conPass);
                String conp=cp.getText().toString();
                if(!(conp.equals(spass))) {
                    Toast.makeText(SignUp.this,"Password do not Match",Toast.LENGTH_LONG).show();
                    return;
                }
                if(semail==null||sname==null||spass==null||semail.equals("")||conp==null||sname.equals("")||spass.equals("")||conp.equals(""))
                {
                    Toast.makeText(SignUp.this,"Please fill in all Details",Toast.LENGTH_LONG).show();
                    return;
                }
                if(spass.length()<8)
                {
                    Toast.makeText(SignUp.this,"Password must be atleast 8 characters",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!agreeCheckBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "You must agree to terms",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new SignUpUser().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    class SignUpUser extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage("Signing You Up ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", semail));
            params.add(new BasicNameValuePair("password", spass));
            params.add(new BasicNameValuePair("email", sname));
            JSONObject json = jsonParser.makeHttpRequest(url_signup, "POST", params);

            // check json success tag
            try {
                success = json.getInt("success");
                if(success==3)
                {
                   // Toast.makeText(SignUp.this,"This Email/Mobile Already Exists",Toast.LENGTH_LONG).show();
                }
                else if (success == 1) {
                  //  Toast.makeText(SignUp.this,"Successfully Signed Up",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    //Toast.makeText(SignUp.this,"Sign Up Unsuccessfull ",Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
            if(success==3) Toast.makeText(SignUp.this,"This Email/Mobile Already Exists",Toast.LENGTH_LONG).show();
        }
    }

}
