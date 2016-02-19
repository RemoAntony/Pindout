package com.app.pindout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class categorylisting extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylisting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorylisting, menu);
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
    public void allcat(View view)
    {
        Intent intent=new Intent(categorylisting.this, categoryAll.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void onlinecat(View view)
    {
        Intent intent=new Intent(categorylisting.this, categoryOnline.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void restaurantcat(View view)
    {
        Intent intent=new Intent(categorylisting.this, categoryRestaurant.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void grocerycat(View view)
    {
        Intent intent=new Intent(categorylisting.this, categoryGrocery.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }
    public void beautycat(View view)
    {
        Intent intent=new Intent(categorylisting.this, categoryBeauty.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
    }

}
