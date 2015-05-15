package com.example.eshan.photos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import java.net.URL;

import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.io.InputStream;


import com.loopj.android.image.SmartImageView;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.AdapterView;
import android.view.Menu;
import android.content.res.Configuration;
import android.view.MenuItem;
import java.io.File;
import java.util.List;

import android.widget.Toast;
import com.facebook.Session;
import com.google.android.gms.plus.Plus;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import android.widget.ImageButton;
/**
 * Created by eshan on 4/29/15.
 */
public class HomeActivity extends ActionBarActivity implements View.OnClickListener{

    private static String TAG = HomeActivity.class.getSimpleName();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private String mActivityTitle;
    private TextView txtViewUserName;
    private String loggedInNetwork;

    //Floating action button
    private ListView myList;

    //Devendra Integration
    BuiltUser builtUserObject = new BuiltUser();
    private List<AlbumClass> dataItems;
    private GridAlbumViewAdapter adapter;
    private int page_number;
    private Button left,right;
    private TextView textView;
    private int totalObjects;
    private final static int LIMIT = 12;
    private int totalPage ;
    public static String useremail;
    public String userprofilepicNew;
    FragmentManager manager;

    public final static String TAG_ALBUM_BUTTON = "album_button";
    public final static String TAG_PHOTO_BUTTON = "photo_button";

    public static FloatingActionMenu actionMenu;
    public static FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_home);

        try {
            Built.initializeWithApiKey(HomeActivity.this, "blt643f5d49ff2042cb", "0000011");
        } catch (Exception e) {
            e.printStackTrace();
        }



        loggedInNetwork = extras.getString("loggedInNetwork");

        if(loggedInNetwork.equalsIgnoreCase("facebook"))
        {
            String url = "https://graph.facebook.com/"+extras.getString("userID")+"/picture";
            userprofilepicNew = url;
            SmartImageView myImage = (SmartImageView) this.findViewById(R.id.avatar);
            myImage.setImageUrl(url);
        }
        else
        {
            SmartImageView myImage = (SmartImageView) this.findViewById(R.id.avatar);
            Log.d("eshanutsav",extras.getString("profilephoto"));
            userprofilepicNew=extras.getString("profilephoto");
            myImage.setImageUrl(extras.getString("profilephoto"));
        }


        txtViewUserName = (TextView)findViewById(R.id.userName);
        txtViewUserName.setText(extras.getString("useremail"));
        useremail=extras.getString("useremail");
        mActivityTitle = getTitle().toString();

        mNavItems.add(new NavItem("Albums", "View My Albums", R.drawable.ic_action_home));
        mNavItems.add(new NavItem("Albums shared with me", "View Albums shared with me", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Search", "Search for a photo", R.drawable.ic_action_about));
        mNavItems.add(new NavItem("Share", "Send download link to friends", R.drawable.shareimg));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    HomeActivity.actionMenu.close(true);
                    AlbumFragment albumFragment = new AlbumFragment();
                    manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.mainContent, albumFragment);

                    transaction.commit();
                } else {

                    if (position == 1) {
                        HomeActivity.actionMenu.close(true);
                        SharedAlbumFragment sharedAlbumFragment = new SharedAlbumFragment();
                        manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mainContent, sharedAlbumFragment);

                        transaction.commit();
                    }

                    if (position == 2) {
                        HomeActivity.actionMenu.close(true);
                        SearchFragment searchFragment = new SearchFragment();
                        manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mainContent, searchFragment);

                        transaction.commit();
                    }
                    if(position==3)
                    {
                        HomeActivity.actionMenu.close(true);
                        try
                        { Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "Photos");
                            String sAux = "\nLet me recommend you this application\n\n";
                            sAux = sAux + "Link to download the apk for the app \n\n";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "choose one"));
                        }
                        catch(Exception e)
                        {

                        }
                    }
                }
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2962FF")));

        //Fragments
        AlbumFragment albumFragment = new AlbumFragment();
        manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContent, albumFragment);

        transaction.commit();


            ImageView icon = new ImageView(this);
            icon.setImageResource(R.drawable.fab_ic_add);
             actionButton = new FloatingActionButton.Builder(this)
                    .setContentView(icon)
                    .setBackgroundDrawable(R.drawable.selectore_button_pink)
                    .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                    .build();

        /*
            ImageView addAlbum = new ImageView(this);
            addAlbum.setImageResource(R.drawable.album_64);

            ImageView addPhoto = new ImageView(this);
            addPhoto.setImageResource(R.drawable.photos);

            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
            SubActionButton addAlbumButton = itemBuilder.setContentView(addAlbum).build();
            addAlbumButton.setOnClickListener(this);
            addAlbumButton.setTag(TAG_ALBUM_BUTTON);
            addAlbumButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
            SubActionButton addPhotoButton = itemBuilder.setContentView(addPhoto).build();
            addPhotoButton.setOnClickListener(this);
            addPhotoButton.setTag(TAG_PHOTO_BUTTON);
            addPhotoButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(addAlbumButton)
                   .addSubActionView(addPhotoButton)
                    .attachTo(actionButton)
                    .build();

            */




        //Dev
        /*
        page_number = 1;

        left = (Button)findViewById(R.id.button2);
        right = (Button)findViewById(R.id.button3);
        textView = (TextView)findViewById(R.id.textView);

        Log.d("check button",left.toString());

        left.setOnClickListener(this);
        right.setOnClickListener(this);



        dataItems = new ArrayList<AlbumClass>();
        textView.setText("Page 0/0");


        fetchData(0);
        */
    }

/*
    private void fetchData(int skipSize){
        BuiltQuery query = new BuiltQuery("album");
        query.skip(skipSize);
        query.limit(LIMIT);
        query.includeCount();
        Log.i("Method","Fetch Data Called");
        query.exec(new QueryResultsCallBack() {
            List<BuiltObject> albums;
            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                albums = queryResultObject.getResultObjects();
                Log.i("Count", " " + queryResultObject.getCount());
                totalObjects = queryResultObject.getCount();
                totalPage = (int)Math.ceil(totalObjects/(double)LIMIT);
                for(BuiltObject object : albums){
                    Log.i("Data","Name "+object.get("name"));
                    Log.i("Data","Title "+object.get("description"));

                }
            }

            @Override
            public void onError(BuiltError builtErrorObject) {
                // query failed
                // the message, code and details of the error
                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
                Log.i("error: ", "" + builtErrorObject.getErrorCode());
                Log.i("error: ", "" + builtErrorObject.getErrors());
            }

            @Override
            public void onAlways() {
                // write code here that you want to execute
                // regardless of success or failure of the operation
                if(albums.size() > 0){
                    updateData(albums);
                }
            }
        });
//
    }

*/
    /*
    //Dev
    public void updateData(List<BuiltObject> albums){


        for(BuiltObject obj : albums){
            AlbumClass objAlbum = new AlbumClass(obj.get("name").toString(),obj.get("description").toString(),getResources().getDrawable(R.drawable.album));
            dataItems.add(objAlbum);
        }

        adapter = new GridAlbumViewAdapter(this,dataItems);
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        textView.setText("Page "+page_number+" of "+totalPage);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Position"," "+position);
        AlbumClass objAlbum = dataItems.get(position);
//        if(objAlbum!=null){
//            NetworkActivity pictureNetworkActivity = new NetworkActivity(CustomDataClass.FETCH_PICTURE,AlbumActivity.this,builtUserObject);
//            pictureNetworkActivity.execute(objAlbum.title);
//        }
        Intent pictureIntent = new Intent(this,PictureViewActivity.class);
        pictureIntent.putExtra("loggedInNetwork",loggedInNetwork);
        pictureIntent.putExtra("album_name",objAlbum.title);

        pictureIntent.putExtra("useremail",useremail);
        pictureIntent.putExtra("profilephoto",userprofilepicNew);


        startActivity(pictureIntent);


    }
**/

    @Override
    public void onClick(View v) {

        /*
        if(v.getTag().equals(TAG_ALBUM_BUTTON))
        {
            Toast.makeText(getApplicationContext(), "Album Button", Toast.LENGTH_SHORT).show();
        }
        else {
            if (v.getTag().equals(TAG_PHOTO_BUTTON)) {
                Toast.makeText(getApplicationContext(), "Photo Button", Toast.LENGTH_SHORT).show();
            }
        }
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Are you sure you want to exit?");
        alertDlg.setCancelable(false);
        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int id) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();

        }});

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
            public void onClick(DialogInterface dialog, int which) {}});

        alertDlg.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }



        switch(item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "logging out", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                //Toast.makeText(getApplicationContext(), "logging out", Toast.LENGTH_SHORT).show();

                Session session = Session.getActiveSession();
                if (session != null) {

                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                        //clear your preferences if saved
                    }
                } else  {

                    session = new Session(getApplicationContext());
                    Session.setActiveSession(session);

                    session.closeAndClearTokenInformation();
                    //clear your preferences if saved

                }
                Log.d("saumil","saumil");
                if (MainActivity.mGoogleApiClient!=null) {
                    if(MainActivity.mGoogleApiClient.isConnected()) {
                        //Log.d("saumil","saumil");
                        Toast.makeText(getApplicationContext(), "google out", Toast.LENGTH_SHORT).show();
                        Plus.AccountApi.clearDefaultAccount(MainActivity.mGoogleApiClient);
                        MainActivity.mGoogleApiClient.disconnect();
                        MainActivity.mGoogleApiClient.connect();
                    }

                }
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(myIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        // Handle your other action bar items...

    }

   /*
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

}
