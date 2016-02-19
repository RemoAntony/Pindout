package com.app.pindout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MoreDealsSub1 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_deals_sub1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more_deals_sub1, menu);
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


    public void baby(View view){
        //   Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Baby and Kids");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

    public void bag(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Bags and Luggages");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

    public void book(View view){
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Books and Media");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

    public void computer(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Computers");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

    public void electronics(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Electronics");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

    public void homkitch(View view){
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Home and Kitchen");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void men(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Men");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void mobile(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Mobiles and Tabs");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void fashion(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Personal and Fashion");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void sport(View view){
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);
        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title","Sports");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void women(View view){
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        //DrawerLayout dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        //dl.closeDrawers();
        //cv1.setVisibility(View.GONE);
        //cv2.setVisibility(View.GONE);

        Intent intent=new Intent(MoreDealsSub1.this, MoreDealsSub2.class);
        intent.putExtra("title", "Women");
        startActivity(intent);
        
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

}
