package com.app.pindout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RemIndUserMain extends ActionBarActivity {
    ViewPager pager;
    MainViewPagerAdapter adapter;
    LinearLayout tagbuslay;
    String url_tag_bus = "http://pindout.com/remuser/pindout/taggedmessages.php";
    String url_bus = "http://pindout.com/remuser/pindout/Busdetails.php";
    String url_reply_add = "http://pindout.com/remuser/pindout/reviewcomment.php";
    String iddd, bus_name, group_id, email, bus_id, tot, curtot;
    int cityid;
    String[] review_id;
    String reply_text;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"My Shops", "Deals", "Messages", "Reviews"};
    int Numboftabs = 4;
    int cv1state = 0, cv2state = 0;
    ScrollView cv1, cv2;
    private static final String url_taggedbusiness = "http://pindout.com/remuser/pindout/taggedbusiness.php";
    private static final String url_Untagbusiness = "http://pindout.com/remuser/pindout/UnTag.php";
    private static final String url_review_add = "http://pindout.com/remuser/pindout/Review_add.php";
    //JSONParser jParser = new JSONParser();
    //  String[] bus_id,group_id,email;
    String id;
    String text;
    Toolbar toolbar;
    int glo = 0, ntop = 0, nbottom = 0;
    Context context = this;

    TagDealsDb db1;
    TagBusDb tagbus;
    TagMsgDb tagmsg;
    ReviewDb rdb;
    Businessdeatilsdb busdb;
    private ArrayList<String> dealname = new ArrayList<String>();
    private ArrayList<String> row_id = new ArrayList<String>();
    private ArrayList<String> dealidofinterest = new ArrayList<String>();
    private ArrayList<String> dealintereststatus = new ArrayList<String>();
    private ArrayList<String> dealdesc = new ArrayList<String>();
    private ArrayList<String> busname = new ArrayList<String>();
    private ArrayList<String> admin_approval = new ArrayList<String>();
    private ArrayList<String> status2 = new ArrayList<String>();
    private ArrayList<String> status1 = new ArrayList<String>();
    private ArrayList<String> enddate = new ArrayList<String>();
    private ArrayList<String> main_price_list = new ArrayList<String>();
    private ArrayList<String> deal_price_list = new ArrayList<String>();
    private ArrayList<String> deal_image_list = new ArrayList<String>();
    private ArrayList<String> deal_url_link_list = new ArrayList<String>();
    private ArrayList<String> total = new ArrayList<String>();
    private ArrayList<String> current_total = new ArrayList<String>();
    private ArrayList<String> businessname = new ArrayList<String>();
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<String> addresslist = new ArrayList<String>();
    private ArrayList<String> phonelist = new ArrayList<String>();
    private ArrayList<String> message = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> review_status_list = new ArrayList<String>();
    private ArrayList<String> review_datetime_list = new ArrayList<String>();
    private ArrayList<String> user_name_list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_ind_user_main);
        tagbuslay = (LinearLayout) findViewById(R.id.tagbuslayout);
        SharedPreferences pref = getSharedPreferences("loginpref", MODE_PRIVATE);
        String username = pref.getString("name", null);
        String email1 = pref.getString("email", null);
        iddd = pref.getString("id", null);
        try {
            Cursor mc = null, mc1 = null, mc2 = null, mc3 = null, mc4 = null;
            db1 = new TagDealsDb(this);
            db1.open();
            mc = db1.getAll();
            if (mc != null) {
                db1.deleteAll();
            }
            db1.close();
            tagbus = new TagBusDb(this);
            tagbus.open();
            mc1 = tagbus.getAll();
            if (mc1 != null) {
                tagbus.deleteAll();
            }
            tagbus.close();
            tagmsg = new TagMsgDb(this);
            tagmsg.open();
            mc2 = tagmsg.getAll();
            if (mc2 != null) {
                tagmsg.deleteAll();
            }
            tagmsg.close();
            rdb = new ReviewDb(this);
            rdb.open();
            mc3 = rdb.getAll();
            if (mc3 != null)
                rdb.deleteAll();
            rdb.close();
            busdb = new Businessdeatilsdb(this);
            busdb.open();
            mc4 = busdb.getAll();
            if (mc4 != null)
                busdb.deleteAll();
            busdb.close();
        } catch (Exception e) {
            Log.e("DbException", e.getMessage());
        }
        Toast.makeText(RemIndUserMain.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.openDrawer(GravityCompat.START);
        //   Button tag=(Button) findViewById(R.id.yourtaggedbus);
        Button dash = (Button) findViewById(R.id.dash);
        Button baby = (Button) findViewById(R.id.babyandkids);
        Button bag = (Button) findViewById(R.id.bags);
        Button book = (Button) findViewById(R.id.books);
        Button comp = (Button) findViewById(R.id.computers);
        Button elec = (Button) findViewById(R.id.electronics);
        Button home = (Button) findViewById(R.id.homeandkitchen);
        Button men = (Button) findViewById(R.id.men);
        Button mob = (Button) findViewById(R.id.mobiles);
        Button fash = (Button) findViewById(R.id.fashion);
        Button sport = (Button) findViewById(R.id.sports);
        Button wom = (Button) findViewById(R.id.women);
        Button lo = (Button) findViewById(R.id.logoutt);
        //  buttonEffect(tag);
        buttonEffect(dash);
        buttonEffect(baby);
        buttonEffect(bag);
        buttonEffect(book);
        buttonEffect(comp);
        buttonEffect(elec);
        buttonEffect(home);
        buttonEffect(men);
        buttonEffect(mob);
        buttonEffect(fash);
        buttonEffect(sport);
        buttonEffect(wom);
        buttonEffect(lo);


        Button mdeal = (Button) findViewById(R.id.catbut);
        mdeal.setVisibility(View.GONE);
        Button mdeal1 = (Button) findViewById(R.id.moredealsbut);
        mdeal1.setVisibility(View.GONE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        new ActionBarDrawerToggle(this, dl, toolbar, R.string.drawer_open, R.string.drawer_close);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.drawer_open, R.string.drawer_close) {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dl.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();


        Spinner spinnercity = (Spinner) findViewById(R.id.spinner_city111);
        ArrayList<String> subcity = new ArrayList<String>();
        subcity.add("Bengaluru");
        subcity.add("Chennai");
        subcity.add("Hyderabad");
        subcity.add("Mumbai");
        subcity.add("Web Based");


        SharedPreferences pref111 = getSharedPreferences("whatcity", MODE_PRIVATE);
        String whatcity = pref111.getString("cityname", null);

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subcity);
        spinnercity.setAdapter(ad);


        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String item = adapter.getItemAtPosition(position).toString();
                if (item.equals("Bengaluru")) {
                    cityid = 29;
//                    Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //                          Toast.LENGTH_LONG).show();

                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Bengaluru")
                            .commit();


                } else if (item.equals("Hyderabad")) {
                    cityid = 20;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //      Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Hyderabad")
                            .commit();


                } else if (item.equals("Chennai")) {
                    cityid = 16;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //    Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Chennai")
                            .commit();
                } else if (item.equals("Mumbai")) {
                    cityid = 25;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //      Toast.LENGTH_LONG).show();

                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Mumbai")
                            .commit();
                } else if (item.equals("Web Based")) {
                    cityid = 26;
                    //Toast.makeText(getApplicationContext(), "Selected  : " + item,
                    //      Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Web Based")
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Selected  : Nothing",
                            Toast.LENGTH_LONG).show();
                    getSharedPreferences("whatcity", MODE_PRIVATE)
                            .edit()
                            .putString("cityname", "Chennai")
                            .commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (whatcity == null) {
            getSharedPreferences("whatcity", MODE_PRIVATE)
                    .edit()
                    .putString("cityname", "Chennai")
                    .commit();
            spinnercity.setSelection(ad.getPosition("Chennai"));
        } else {
            spinnercity.setSelection(ad.getPosition(whatcity));
        }


        ScrollView sv = (ScrollView) findViewById(R.id.drawer_list);
        sv.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                //     findViewById(R.id.dealscroll).getParent().requestDisallowInterceptTouchEvent(false);
                //   findViewById(R.id.categoryscroll).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });


        cv1 = (ScrollView) findViewById(R.id.dealscroll);
        cv2 = (ScrollView) findViewById(R.id.categoryscroll);
        cv1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        cv2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        childScrollviewRecursiveLoopChildren(sv, cv1);
        childScrollviewRecursiveLoopChildren(sv, cv2);


        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        new GetTaggedBus().execute();
        new GetTaggedDeal().execute();
        new GetTaggedBusiness().execute();
        new GetReviewDetails().execute();


    }

    public void childScrollviewRecursiveLoopChildren(final ScrollView parentScrollView, View parent) {
        for (int i = ((ViewGroup) parent).getChildCount() - 1; i >= 0; i--) {
            final View child = ((ViewGroup) parent).getChildAt(i);
            if (child instanceof ViewGroup) {
                childScrollviewRecursiveLoopChildren(parentScrollView, (ViewGroup) child);
            } else {
                child.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of
                        // child view
                        parentScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Destroy", "");
        RemIndUserMain.this.deleteDatabase("Tagdeals.db");
        RemIndUserMain.this.deleteDatabase("TagMsg.db");
        RemIndUserMain.this.deleteDatabase("TagBus.db");
        RemIndUserMain.this.deleteDatabase("Review.db");
        RemIndUserMain.this.deleteDatabase("Bus.db");


    }

    @Override
    public void onResume() {
        super.onResume();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        //    dl.openDrawer(GravityCompat.START);
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        cv1state = 0;
        cv2state = 0;

    }

    public void catbut(View view) {
        if (cv2state == 1) {
            cv2.setVisibility(View.GONE);
            cv2state = 0;
            return;
        }
        //cv2.setVisibility(View.VISIBLE);
        Intent intent = new Intent(RemIndUserMain.this, categorylisting.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
        cv2state = 1;
    }

    public void moredeals(View view) {
        if (cv1state == 1) {
            cv1.setVisibility(View.GONE);
            cv1state = 0;
            return;
        }
        //cv1.setVisibility(View.VISIBLE);
        Intent intent = new Intent(RemIndUserMain.this, MoreDealsSub1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
        cv1state = 1;
    }

    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rem_ind_user_main, menu);
        return true;
    }

    public void taggedbus(View view) {
        // Toast.makeText(this, "INside View function", Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, TaggedBusinesses.class);
        startActivity(intent);
    }

    public void dashboard(View view) {
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, DashBoard.class);
        startActivity(intent);
    }

    public void baby(View view) {
        //   Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void bag(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void book(View view) {
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void computer(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void electronics(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void homkitch(View view) {
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void men(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void mobile(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void fashion(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void sport(View view) {
        //Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void women(View view) {
        // Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        Intent intent = new Intent(RemIndUserMain.this, Deals.class);
        startActivity(intent);
    }

    public void signout(View view) {
        //    Toast.makeText(this,"INside View function",Toast.LENGTH_LONG).show();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawers();
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you wanna LogOut ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                getSharedPreferences("loginpref", MODE_PRIVATE).edit().clear().commit();
                //System.exit(0);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog ad = alert.create();
        ad.show();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent ke) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure you wanna Exit ?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(RemIndUserMain.this, MainActivityRemo.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    //System.exit(0);
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
            AlertDialog ad = alert.create();
            ad.show();
        }
        return super.onKeyDown(keycode, ke);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.wallet) {
            Intent intent = new Intent(RemIndUserMain.this, Wallet.class);
            startActivity(intent);
        }
        if (id == R.id.search) {
            Intent intent = new Intent(RemIndUserMain.this, Search.class);
            startActivity(intent);
        }
        if (id == R.id.action_refresh) {
            Intent intent = new Intent(RemIndUserMain.this, RemIndUserMain.class);
            startActivity(intent);
            finish();
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.all_settings) {
            Intent intent=new Intent(RemIndUserMain.this, categoryAll.class);
            startActivity(intent);
        }
        else if(id == R.id.online_settings)
        {
            Intent intent=new Intent(RemIndUserMain.this, categoryOnline.class);
            startActivity(intent);
        }
        else if(id == R.id.restaurant_settings)
        {
            Intent intent=new Intent(RemIndUserMain.this, categoryRestaurant.class);
            startActivity(intent);
        }
        else if(id == R.id.grocery_settings)
        {
            Intent intent=new Intent(RemIndUserMain.this, categoryGrocery.class);
            startActivity(intent);
        }
        else if (id == R.id.beauty_settings)
        {
            Intent intent=new Intent(RemIndUserMain.this, categoryBeauty.class);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }


    public void topcategory(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryAll.class);
        startActivity(intent);
    }

    public void toponline(View view) {
        Intent intent = new Intent(RemIndUserMain.this, MoreDealsSub1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left_enter,
                R.anim.right_to_left_exit);
        /*
        Intent intent=new Intent(RemIndUserMain.this,categoryOnline.class);
        startActivity(intent);*/
    }

    public void toprestaurant(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryRestaurant.class);
        startActivity(intent);
    }

    public void topgrocery(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryGrocery.class);
        startActivity(intent);
    }

    public void topinstore(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryInstore.class);
        startActivity(intent);
    }

    public void topbeauty(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryBeauty.class);
        startActivity(intent);
    }


    public void allcat(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryAll.class);
        startActivity(intent);
    }

    public void onlinecat(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryOnline.class);
        startActivity(intent);
    }

    public void restaurantcat(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryRestaurant.class);
        startActivity(intent);
    }

    public void grocerycat(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryGrocery.class);
        startActivity(intent);
    }

    public void beautycat(View view) {
        Intent intent = new Intent(RemIndUserMain.this, categoryBeauty.class);
        startActivity(intent);
    }

    public ArrayList<View> disBname() {
        return allviews;
    }

    int bcount;
    String bname, bmessage, bemail, bid, bphone;
    JSONParser jParser = new JSONParser();
    ArrayList<View> allviews = new ArrayList<View>();

    class GetTaggedBus extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;
        JSONObject json;
        LayoutInflater lin;
        int lo = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));

            json = jParser.makeHttpRequest(url_tag_bus, "GET", params);
            lin = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            // Log.e("Entered","");
//            Log.e("Message ", json.toString());
            try {
                success = json.getInt("success");

                if (success == 1) {

                    JSONArray products = json.getJSONArray("products");
                    lo = products.length();

                    tagmsg.open();

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        tagmsg.insert(c.getString("business_name"), c.getString("message"), c.getString("date"));
                    }
                    tagmsg.close();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {

                if (success == 1) {

                    String[][] b = new String[lo + 1][3];

                    tagmsg.open();
                    Cursor c = tagmsg.getAll();
                    if (c.moveToFirst()) {
                        int i = 0;
                        do {

                            b[i][0] = c.getString(0);
                            b[i][1] = "->" + c.getString(1);
                            bemail = c.getString(2);
                            i++;
                        } while (c.moveToNext());
                        c.close();
                    }
                    tagmsg.close();

                    //bid=c.getString("id");
                    //  Log.e("Date", "" + bemail);

                    int[] c1 = new int[lo];
                    c1[0] = -1;
                    int temp = 0, y = 0;
                    for (int i = 0; i < lo; i++) {
                        c1[y] = i;
                        temp = i;
                        for (int k = i + 1; k < lo; k++) {
                            //   Log.e("T" + b[i][0], "" + b[k][1]);
                            if (b[temp][0].equals(b[k][0])) {
                                b[temp][1] += "\n" + b[k][1];

                                i++;
                            }
                        }
                        y++;
                    }
                    if (c1[0] == -1) {
                        c1[0] = 0;
                        y = 1;
                    }
                    for (int i = 0; i < y; i++) {
                        Log.e("" + b[c1[i]][0], "" + b[c1[i]][1]);
               /* }
                for(int i=0;i<products.length();i++) {
                    JSONObject c = products.getJSONObject(i);
                    bname = c.getString("business_name");
                    bmessage = c.getString("message");
                    bemail = c.getString("date");*/
                        View v = lin.inflate(R.layout.listtagbusiness, null);
                        TextView tvbname = (TextView) v.findViewById(R.id.tagbname);

                        TextView tvbaddress = (TextView) v.findViewById(R.id.tagbaddr);

                        tvbname.setText(b[c1[i]][0]);
                        tvbaddress.setText(b[c1[i]][1]);

                        allviews.add(v);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView tvbname = (TextView) view.findViewById(R.id.tagbname);
                                bus_name = tvbname.getText().toString();
                                Log.e("Bus_name", "" + bus_name);
                                new GetBusinessDetails().execute();

                            }
                        });
                    }

                }

            } catch (Exception e) {
                Log.d("Mewtwo", "" + e);
            }
            pDialog.dismiss();

        }
    }

    int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        new GetTaggedDeal().execute();
    }

    String url_tag_deal = "http://pindout.com/remuser/pindout/taggeddeals.php";
    ArrayList<View> allviews1 = new ArrayList<View>();
    JSONParser jParser1 = new JSONParser();
    String deal_title, deal_description, deal_price, main_price, end_date, url_link, deal_image, business_name;


    public ArrayList<View> disBdeal() {
        return allviews1;
    }

    ImageView dealimg;
String maindealid,mainuserid;
    class GetTaggedDeal extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("Making things Ready for You...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        LayoutInflater lin;
        String dealiiddd;
        protected String doInBackground(String... args) {
//Toast.makeText(getApplicationContext(),count+"",Toast.LENGTH_SHORT).show();
            dealname.clear();
            row_id.clear();
            dealdesc.clear();
            dealidofinterest.clear();
            dealintereststatus.clear();
            enddate.clear();
            main_price_list.clear();
            deal_price_list.clear();
            deal_image_list.clear();
            status2.clear();
            admin_approval.clear();
            deal_url_link_list.clear();
            Log.e("Count :", count + "");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            params.add(new BasicNameValuePair("count", String.valueOf(count)));
            json = jParser1.makeHttpRequest(url_tag_deal, "GET", params);
            lin = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                success = json.getInt("success");

                if (success == 1) {
                    JSONArray products = json.getJSONArray("products");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        dealiiddd=c.getString("teslaid");
                        // row_id.add(c.getString(TAG_DEAL_ID));
                        dealname.add(c.getString("deal_title"));
                        dealidofinterest.add(dealiiddd);
                        dealintereststatus.add(c.getString("button_status"));
                        dealdesc.add(c.getString("deal_description"));
                        deal_url_link_list.add(c.getString("url_link"));
                        admin_approval.add(c.getString("admin"));
                        status1.add(c.getString("status"));
                        //   status1.add(c.getString("status1"));
                        enddate.add(c.getString("end_date"));
                        main_price_list.add(c.getString(("main_price")));
                        deal_price_list.add(c.getString("deal_price"));
                        total.add(c.getString("total"));
                        current_total.add(c.getString("curtotal"));
                        busname.add(c.getString("business_name"));
                        deal_image_list
                                .add("http://pindout.com/files/businessdeal_images/main_images/"
                                        + c.getString("deal_image"));
                    }
                    db1.open();
                    for (int l = 0; l < dealname.size(); l++) {
                        db1.insert(dealname.get(l), dealdesc.get(l), deal_url_link_list.get(l),
                                main_price_list.get(l), deal_price_list.get(l), enddate.get(l), status1.get(l),
                                admin_approval.get(l),
                                busname.get(l),
                                deal_image_list.get(l), total.get(l), current_total.get(l),dealidofinterest.get(l),dealintereststatus.get(l));
                    }
                    db1.close();

                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {
                if (success == 1) {
                    db1.open();
                    Cursor c = db1.getAll();
                    if (c.moveToFirst()) {
                        do {
                            deal_title = c.getString(0);
                            deal_description = c.getString(1);
                            main_price = c.getString(2);
                            deal_price = c.getString(3);
                            String end_date = c.getString(4);
                            url_link = c.getString(5);
                            String status = c.getString(6);
                            String admin = c.getString(7);
                            business_name = c.getString(8);
                            tot = c.getString(9);
                            curtot = c.getString(10);
                            deal_image = c.getString(11);
                           String currrdealid=c.getString(12);
                            String interestedstatus=c.getString(13);
                            Log.e("Dbcheck", c.getString(13) + "");

/*
return db.query(DATABASE_TABLE, new String[] {KEY_DEALNAME,KEY_DEALDESC, KEY_MAINPRICE, KEY_DEALPRICE,KEYEND,
                KEY_DEALURL, KEY_DEALSTATUS, KEY_DEALADMINAPPROVAL,KEYBUS,KEYCURTOT,KEYTOT,
                KEY_IMAGEURL }, null, null, null, null, null);
 */
                            View v = lin.inflate(R.layout.listdeals, null);


                            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.top_lay);

                            Button interest = (Button) v.findViewById(R.id.interestedbutton);
                            if(interestedstatus.equals("no")) interest.setVisibility(View.GONE);
                            interest.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RelativeLayout midlayout = (RelativeLayout) view.getParent();
                                    RelativeLayout parmidlayout = (RelativeLayout) midlayout.getParent();
                                    TextView dealname = (TextView) parmidlayout.findViewById(R.id.deal_name);
                                    String dtitle = "", dmrp, doff, dbusname;
                                    dtitle = dealname.getText().toString();
                                    TextView tmrp = (TextView) midlayout.findViewById(R.id.showmain_price);
                                    dmrp = tmrp.getText().toString();

                                    TextView tddidd = (TextView) midlayout.findViewById(R.id.tesladealid);
                                    String curdealid = tddidd.getText().toString();

                                    maindealid=curdealid;
                                    mainuserid=iddd;

                                    TextView toff = (TextView) midlayout.findViewById(R.id.showdeal_price);
                                    doff = toff.getText().toString();

                                    TextView tbus = (TextView) midlayout.findViewById(R.id.deal_expiry);
                                    dbusname = tbus.getText().toString();

                                    Toast.makeText(RemIndUserMain.this, "You can Access Your interested deals from your wallet"+curdealid, Toast.LENGTH_LONG).show();
                                    view.setVisibility(View.GONE);
                                    new InsertInterested().execute();
                                }
                            });

                            rl.setBackgroundColor(Color.GREEN);

                            TextView tvmprice = (TextView) v.findViewById(R.id.showmain_price);
                            TextView tvdprice = (TextView) v.findViewById(R.id.showdeal_price);
                            TextView tvdexpiry = (TextView) v.findViewById(R.id.deal_expiry);
                            TextView tvdname = (TextView) v.findViewById(R.id.deal_name);
                            TextView disc = (TextView) v.findViewById(R.id.discount);

                            TextView stat = (TextView) v.findViewById(R.id.hiddenstatus);
                            stat.setText(status);

                            TextView thiddealid = (TextView) v.findViewById(R.id.tesladealid);
                            thiddealid.setText(currrdealid);

                            TextView statimg = (TextView) v.findViewById(R.id.hiddenimgurl);
                            statimg.setText(deal_image);
                            TextView tvdescript = (TextView) v.findViewById(R.id.dealdescript);
                            dealimg = (ImageView) v.findViewById(R.id.deal_img);
                            TextView tvdurl = (TextView) v.findViewById(R.id.correcturl);
                            tvmprice.setText(main_price);
                            tvdprice.setText(deal_price);
                            tvdexpiry.setText("Offered By: " + business_name + " till " + end_date.substring(0, 10));
                            tvdname.setText(deal_title);
                            tvdescript.setText("Details:  " + deal_description);
                            //dealimg.setImageURI(Uri.parse("http://pindout.com/files/businessdeal_images/main_images/"+deal_image));
                            double mainprice = Double.parseDouble(tvmprice.getText().toString());
                            double dealprice = Double.parseDouble(tvdprice.getText().toString());
                            double diff = mainprice - dealprice;
                            if (mainprice == 0 || diff <= 0) ;
                            else {
                                double percen = diff * 100 / mainprice;
                                disc.setText((Math.ceil(percen) + " %  Off"));
                            }


                            tvdurl.setText(url_link);
                            Picasso.with(v.getContext())
                                    .load(deal_image)
                                    .into(dealimg);

                            TextView popup=(TextView)v.findViewById(R.id.tesladealimagepopup);
                            popup.setText(deal_image);

                            dealimg.setClickable(true);
                            dealimg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    View parent=(View)view.getParent();
                                    TextView popup=(TextView)parent.findViewById(R.id.tesladealimagepopup);
                                    String temp=popup.getText().toString();

                                    View v = lin.inflate(R.layout.popupimage, null);
                                    ImageView iv=(ImageView)v.findViewById(R.id.showpopupimage);


                                    Picasso.with(v.getContext())
                                            .load(temp)
                                            .into(iv);


                                    Dialog settingsDialog = new Dialog(RemIndUserMain.this);
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
                                    TextView stat = (TextView) view.findViewById(R.id.hiddenstatus);
                                    if (stat.getText().toString().equals("0")) {
                                        Toast.makeText(RemIndUserMain.this, "Local Business Deal", Toast.LENGTH_LONG).show();
                                        TextView tvmprice = (TextView) view.findViewById(R.id.showmain_price);
                                        TextView tvdprice = (TextView) view.findViewById(R.id.showdeal_price);
                                        TextView tvdexpiry = (TextView) view.findViewById(R.id.deal_expiry);
                                        TextView tvdname = (TextView) view.findViewById(R.id.deal_name);
                                        TextView disc = (TextView) view.findViewById(R.id.dealdescript);
                                        TextView dealimg1 = (TextView) view.findViewById(R.id.hiddenimgurl);
                                        Intent intent = new Intent(RemIndUserMain.this, DealPage.class);
                                        intent.putExtra("mrp", tvmprice.getText().toString());
                                        intent.putExtra("dprice", tvdprice.getText().toString());
                                        intent.putExtra("bname", tvdexpiry.getText().toString());
                                        intent.putExtra("dname", tvdname.getText().toString());
                                        intent.putExtra("disc", disc.getText().toString());
                                        intent.putExtra("dimg", dealimg1.getText());

                                        startActivity(intent);
                                    } else {
                                        TextView correcturl = (TextView) view.findViewById(R.id.correcturl);
                                        Intent intent = new Intent(RemIndUserMain.this, DisplayDealUrl.class);
                                        intent.putExtra("urlofdeal", correcturl.getText().toString());
                                        startActivity(intent);
                                    }
                                }
                            });
                        } while (c.moveToNext());
                    }
                    c.close();
                    db1.close();
                }
            } catch (Exception e) {
                Log.d("Mewtwo", "" + e);
            }


            pDialog.dismiss();
            nbottom = 0;
            ntop = 0;
            // Toast.makeText(RemIndUserMain.this,"Showing "+curtot+" of "+tot + " deals",Toast.LENGTH_LONG).show();

        }
    }



    private static String url_review = "http://pindout.com/remuser/pindout/Review.php";
    ArrayList<View> allviews3 = new ArrayList<View>();

    public ArrayList<View> disReview() {
        return allviews3;
    }

    class GetReviewDetails extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success = 0, length = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONArray productObj;
        JSONParser jp = new JSONParser();

        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {


                    try {
                        SharedPreferences pref = getSharedPreferences("loginpref", Context.MODE_PRIVATE);
                        String username = pref.getString("id", null);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", username));
                        json = jp.makeHttpRequest(url_review, "GET", params);
                        in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        success = json.getInt("success");
                        // Log.d("Response",json.toString());
                        if (success == 1) {
                            try {
                                productObj = json
                                        .getJSONArray("product");
                                rdb.open();
                                for (int i = 0; i < productObj.length(); i++) {
                                    JSONObject c = productObj.getJSONObject(i);

                                    if (c.getString("review_text").equals("")) {
                                        Log.e("Empty", "");
                                    } else {
                                        length++;
                                        rdb.insert(c.getString("id"), c.getString("business_name"), c.getString("slug"),
                                                c.getString("review_text"), c.getString("review_date"), c.getString("comment_text"),
                                                c.getString("user_id"), c.getString("business_image"), c.getString("date"));
                                    }

                                }
                                rdb.close();
                            } catch (Exception x) {
                                Log.e("Exc_Rev", x.getMessage());
                            }
                        } else {

                        }

                    } catch (JSONException e) {
                        Log.d("Review1", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d("Review1", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {

            try {
                if (success == 1) {
                    String[][] b = new String[length + 1][9];
                    rdb.open();
                    Cursor c = rdb.getAll();
                    if (c.moveToFirst()) {
                        int i = 0;
                        do {

                            b[i][0] = c.getString(1);
                            b[i][1] = c.getString(0);
                            b[i][2] = c.getString(6);
                            b[i][3] = c.getString(5);
                            b[i][5] = c.getString(8);
                            b[i][6] = c.getString(4);
                            b[i][7] = c.getString(7);
                            if (b[i][5].equals("0"))
                                b[i][4] = b[i][1] + " :" + c.getString(3) + " " + c.getString(2).substring(0, 10);
                            else if (!b[i][5].equals("-1"))
                                b[i][4] = "User Reply :" + c.getString(3) + " " + c.getString(2).substring(0, 10);
                            else
                                b[i][4] = c.getString(3);
                            i++;

                        } while (c.moveToNext());
                        c.close();
                    }
                    rdb.close();
                    int[] c1 = new int[length + 1];
                    c1[0] = -1;
                    int temp = 0, y1 = 0;
                    for (int i = 0; i < length; i++) {
                        c1[y1] = i;
                        temp = i;
                        for (int k = i + 1; k < length; k++) {
                            // Log.e("T"+b[i][0],""+b[k][1]);
                            if (b[temp][0].equals(b[k][0])) {
                                b[temp][4] += "\n" + b[k][4];

                                i++;
                            }
                        }
                        y1++;
                    }
                    if (c1[0] == -1) {
                        c1[0] = 0;
                        y1 = 1;
                    }
                    review_id = new String[length + 1];
                    int y = 0, z = 0;
                    for (int i = 0; i < y1; i++) {
                        Log.e("" + b[c1[i]][1], "" + b[c1[i]][2]);
                        //}
                        //for (int i = 0; i < productObj.length(); i++) {
                        if (!TextUtils.isEmpty(b[c1[i]][2])) {
                            View v = in.inflate(R.layout.reviewstruct, null);
                            final LinearLayout ll = (LinearLayout) v.findViewById(R.id.dynText);
                            final LinearLayout.LayoutParams params;
                            params = new LinearLayout.LayoutParams(
                                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            final TextView tbus_name = (TextView) v.findViewById(R.id.business_name11);
                            TextView place = (TextView) v.findViewById(R.id.place);
                            TextView desc = (TextView) v.findViewById(R.id.review);
                            TextView rep = (TextView) v.findViewById(R.id.reply);
                            View v3 = (View) v.findViewById(R.id.v1);
                            View v4 = (View) v.findViewById(R.id.v2);
                            review_id[y] = b[c1[i]][0];

                            final TextView tbus_reply = (TextView) v.findViewById(R.id.reply1);
                            tbus_reply.setText(String.valueOf(y));
                            y++;
                            final Button re = (Button) v.findViewById(R.id.repbutton);
                            ImageView dealimg = (ImageView) v.findViewById(R.id.Logoo);
                            if (i == 0) {
                                tbus_name.setVisibility(View.VISIBLE);
                                place.setVisibility(View.VISIBLE);
                                v3.setVisibility(View.VISIBLE);
                                v4.setVisibility(View.GONE);
                                tbus_name.setText(b[c1[i]][1]);
                                place.setText(b[c1[i]][3]);
                            } else {
                                if (b[c1[i]][1].equals(b[c1[i - 1]][1])) {
                                    tbus_name.setVisibility(View.GONE);
                                    place.setVisibility(View.GONE);
                                    v3.setVisibility(View.GONE);
                                    if (!b[c1[i]][1].equals(b[c1[i + 1]][1])) {
                                        v4.setVisibility(View.VISIBLE);
                                    } else
                                        v4.setVisibility(View.GONE);
                                } else {
                                    tbus_name.setVisibility(View.VISIBLE);
                                    place.setVisibility(View.VISIBLE);
                                    v3.setVisibility(View.VISIBLE);
                                    v4.setVisibility(View.GONE);
                                    tbus_name.setText(b[c1[i]][1]);
                                    place.setText(b[c1[i]][3]);
                                }
                            }

                            desc.setText("-> Review :" + b[c1[i]][2] + " " + b[c1[i]][7].substring(0, 10));
                            if (!b[c1[i]][4].equals("-1")) {
                                rep.setVisibility(View.VISIBLE);
                                rep.setText(b[c1[i]][4]);
                                re.setVisibility(View.VISIBLE);

                            } else {
                                rep.setVisibility(View.GONE);
                                re.setVisibility(View.GONE);

                            }
                            //   Log.e("Place", place.getText() + " ");
                            if (b[c1[i]][6].equals("")) {
                                Picasso.with(v.getContext())
                                        .load("http://pindout.com/remuser/pindout/no_user_image.jpg")
                                        .into(dealimg);
                            } else
                                Picasso.with(v.getContext())
                                        .load("http://pindout.com/files/business_images/main_images/" + b[c1[i]][6])
                                        .into(dealimg);
                           re.setVisibility(View.GONE);

                            desc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {

                                        final TextView dyn = new TextView(RemIndUserMain.this);
                                        dyn.setText(" ");
                                        //    dyn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        dyn.setTextSize(20);
                                        dyn.setPadding(50, 0, 0, 0);
                                        dyn.setLayoutParams(params);
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
                                                new Replyadd().execute();
                                                Toast.makeText(getApplicationContext(), "Reply added", Toast.LENGTH_SHORT).show();

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
                                                if (!TextUtils.isEmpty(t1)) {
                                                    dyn.setText("Reply :" + t1);
                                                    reply_text = t1;
                                                    glo = Integer.parseInt(tbus_reply.getText().toString());
                                                    new Replyadd().execute();
                                                    Toast.makeText(getApplicationContext(), "Reply added", Toast.LENGTH_SHORT).show();
                                                    re.setEnabled(false);
                                                } else {
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
                            tbus_name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    bus_name = tbus_name.getText().toString();
                                    Log.e("Text Click", bus_name + "");
                                    new GetBusinessDetails().execute();

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


                }
                Log.e("Size", allviews3.size() + "");

            } catch (Exception e1) {
                Log.e("Review Exc", e1.getMessage());
                //     Toast.makeText(getApplicationContext(),"OnPostExecute1",Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    adapter = new MainViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

                    // Assigning ViewPager View and setting the adapter
                    pager = (ViewPager) findViewById(R.id.pagermain);
                    pager.setOffscreenPageLimit(3);
                    pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
                    pager.setAdapter(adapter);

                    // Assiging the Sliding Tab Layout View
                    tabs = (SlidingTabLayout) findViewById(R.id.tabsmain);
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
    }


String url_interest="http://pindout.com/remuser/pindout/insertinterested.php";;

    class InsertInterested extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("You Can Access your Interested Deals from your Wallet");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONArray productObj;
        JSONParser jp = new JSONParser();

        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {


                    try {


                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_id", mainuserid));
                        params.add(new BasicNameValuePair("deal_id", maindealid));
                        json = jp.makeHttpRequest(url_interest, "GET", params);


                    } catch (Exception e) {
                        Log.d("Review1", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

        }
    }

    class GetBusinessDetails extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        LayoutInflater in;
        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        JSONObject json;
        JSONArray productObj;
        JSONParser jp = new JSONParser();

        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {


                    try {


                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        Log.e("Bus_name", "" + bus_name);
                        params.add(new BasicNameValuePair("bus_id", bus_name));
                        json = jp.makeHttpRequest(url_bus, "GET", params);
                        in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        success = json.getInt("success");
                        Log.d("Response", json.toString());
                        if (success == 1) {
                            productObj = json
                                    .getJSONArray("products");
                            JSONObject c = productObj.getJSONObject(0);
                            busdb.open();

                            Cursor m = busdb.getAll();
                            if (m != null)
                                busdb.deleteAll();

                            busdb.insert(c.getString("business_name"), c.getString("id"), c.getString("group_id"), c.getString("email"));
                            Log.e("Testdb", c.getString("business_name") + " " + c.getString("id") + " " + c.getString("group_id") + " " + c.getString("email"));
                            busdb.close();
                        } else {

                        }


                    } catch (Exception e) {
                        Log.d("Review1", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            try {

                busdb.open();
                int k = 0;
                Cursor c = busdb.getAll();
                if (c.moveToFirst()) {

                    bus_name = c.getString(0);
                    bus_id = c.getString(1);
                    group_id = c.getString(2);
                    email = c.getString(3);

                    c.close();
                }
                busdb.close();
                Log.e("" + bus_name + "" + bus_id, "" + iddd + "" + group_id + "" + email);
                Intent in = new Intent(RemIndUserMain.this, BusinessPage.class);
                in.putExtra("com.app.pindout.Business_Name", bus_name);
                in.putExtra("com.app.pindout.Business_Id", bus_id);
                in.putExtra("com.app.pindout.Group", group_id);
                in.putExtra("com.app.pindout.Email", email);
                in.putExtra("com.app.pindout.UserId", iddd);
                startActivity(in);

            } catch (Exception e1) {
                //     Toast.makeText(getApplicationContext(),"OnPostExecute1",Toast.LENGTH_SHORT).show();
            }

        }
    }


    class Replyadd extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        Boolean update_success;
        JSONParser jP = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("adding Review...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            InetAddress ip = null;
            try {
                ip = InetAddress.getLocalHost();

            } catch (Exception e) {
                Log.d("host", "" + e);
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            params.add(new BasicNameValuePair("review_id", review_id[glo]));
            params.add(new BasicNameValuePair("group_id", " "));
            params.add(new BasicNameValuePair("email", "2"));
            params.add(new BasicNameValuePair("review_text", reply_text));
            params.add(new BasicNameValuePair("review_datetime", dateFormat.format(date)));
            params.add(new BasicNameValuePair("ip_address", ip.getHostAddress()));

            Log.e("date+ip", ip.getHostAddress() + "" + dateFormat.format(date));

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
            } catch (Exception e) {
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
            } catch (Exception e) {
                Log.d("Update", "" + e);
            }
        }
    }

    ArrayList<View> allviews4 = new ArrayList<View>();

    public ArrayList<View> disTagBus() {
        return allviews4;
    }

    class GetTaggedBusiness extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        int success = 0;
        JSONObject json;
        LayoutInflater lin;
        int k = 0;
        Button untag;

        //  LinearLayout lv=(LinearLayout) findViewById(R.id.taggedbusiness);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RemIndUserMain.this);
            pDialog.setMessage("Loading things for You");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            businessname.clear();
            phonelist.clear();
            arealist.clear();
            addresslist.clear();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", iddd));
            json = jParser.makeHttpRequest(url_taggedbusiness, "GET", params);
            lin = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                success = json.getInt("success");

                if (success == 1) {
                    JSONArray products = json.getJSONArray("products");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // row_id.add(c.getString(TAG_DEAL_ID));
                        businessname.add(c.getString("business_name"));
                        phonelist.add(c.getString("phone"));
                        arealist.add(c.getString("area_name"));
                        addresslist.add(c.getString("address"));

                    }
                    tagbus.open();
                    for (int l = 0; l < businessname.size(); l++) {
                        tagbus.insert(businessname.get(l), addresslist.get(l), arealist.get(l), phonelist.get(l));
                    }
                    tagbus.close();


                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {


            try {
                if (success == 1) {
                    int j = 0;
                    tagbus.open();
                    Cursor c = tagbus.getAll();
                    String bname, badd, bphone, bid, bgroupid, bemail, brate;
                    if (c.moveToFirst()) {
                        do {
                            bname = c.getString(0);
                            bphone = c.getString(3);
                            badd = c.getString(1);
                            String area = c.getString(2);
                            if (bphone.equals("0"))
                                bphone = "No Number";
                            if (badd.equals("all"))
                                badd = "webbased";


                            View v = lin.inflate(R.layout.activity_tagged_businesses, null);
                            TextView bus_name1 = (TextView) v.findViewById(R.id.bus_name);
                            final TextView count = (TextView) v.findViewById(R.id.count);
                            count.setText(String.valueOf(j));

                            j++;
                            SpannableString content = new SpannableString(bname);
                            content.setSpan(new UnderlineSpan(), 0,bname.length(), 0);
                            bus_name1.setText(content);
                            TextView phone = (TextView) v.findViewById(R.id.phone);
                            //  TextView rate=(TextView) v.findViewById(R.id.rate);
                            TextView address = (TextView) v.findViewById(R.id.address);

                            TextView areaa = (TextView) v.findViewById(R.id.busarea);
                            areaa.setText(area);
                            address.setText(badd);
                            phone.setText(bphone);

                            // lv.addView(v);
bus_name1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        TextView temp = (TextView) view.findViewById(R.id.bus_name);
        bus_name = temp.getText().toString();
        Log.e("Bus_name", "" + bus_name);
        new GetBusinessDetails().execute();


    }
});
                            allviews4.add(v);
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {



                                }
                            });

                        } while (c.moveToNext());
                        c.close();


                    }
                    tagbus.close();
                }
            } catch (Exception e) {
                Log.d("Tag Business", "" + e);
            }
            pDialog.dismiss();


        }
    }
}
