package com.example.eshan.photos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eshan.photos.AlbumClass;
import com.example.eshan.photos.CustomDataClass;
import com.example.eshan.photos.GridAlbumViewAdapter;
import com.example.eshan.photos.GridPictureViewAdapter;
import com.example.eshan.photos.NetworkActivity;
import com.example.eshan.photos.PictureClass;
import com.facebook.Session;
import com.google.android.gms.plus.Plus;
import com.loopj.android.image.SmartImageView;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltFile;
import com.raweng.built.BuiltImageDownloadCallback;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.view.BuiltImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class PictureViewActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    private String album_name;
    private List<PictureClass> dataItems;
    private GridPictureViewAdapter adapter;
    private GridView view;
    private int pageNumber;
    private BuiltUser builtUserObject;
    private Button left,right;
    private TextView textView;
    private int page_number;
    private int totalObjects;
    private final static int LIMIT = 12;
    private int totalPage ;
    ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    RelativeLayout mDrawerPane;

    private TextView txtViewUserName;
    private String mActivityTitle;
    private String loggedInNetwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);

        Bundle extras1 = getIntent().getExtras();

        page_number = 1;
        view = (GridView)findViewById(R.id.gridView3);

        builtUserObject = new BuiltUser();

        //view.setOnScrollListener(this);

        left = (Button)findViewById(R.id.button2);
        right = (Button)findViewById(R.id.button3);
        textView = (TextView)findViewById(R.id.textView);

        dataItems = new ArrayList<PictureClass>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            album_name = extras.getString("album_name");
            fetchPictures(0);
        }

        if(extras1.getString("loggedInNetwork").equalsIgnoreCase("facebook"))
        {
            String url =extras1.getString("profilephoto");

            SmartImageView myImage = (SmartImageView) this.findViewById(R.id.avatar);
            myImage.setImageUrl(url);
        }
        else
        {
            SmartImageView myImage = (SmartImageView) this.findViewById(R.id.avatar);
            //Log.d("eshan",extras1.getString("profilephoto"));

            myImage.setImageUrl(extras1.getString("profilephoto"));
        }

        textView.setText("Page 0/0");
        mActivityTitle = "Photos";

        txtViewUserName = (TextView)findViewById(R.id.userName);
        txtViewUserName.setText(extras.getString("useremail"));

        mNavItems.add(new NavItem("Albums", "View My Albums", R.drawable.ic_action_home));
        mNavItems.add(new NavItem("Albums shared with me", "View Albums shared with me", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Search", "Search for a photo", R.drawable.ic_action_about));
        mNavItems.add(new NavItem("Friends", "My friend list", R.drawable.ic_action_friends));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectItemFromDrawer(position);
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
                Log.d("Drawer Closed", "onDrawerClosed: " + getTitle());
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fetchPictures(int skipSize) {
        BuiltQuery query = new BuiltQuery("picture");
        BuiltQuery query2 = new BuiltQuery("picture");
        BuiltQuery query3 = new BuiltQuery("picture");

        ArrayList<BuiltQuery> andQuery = new ArrayList<BuiltQuery>();

        query.where("email",HomeActivity.useremail);
        //query3.where("album",album_name);

        andQuery.add(query2);
        andQuery.add(query3);
        //query.and(andQuery);
//        query.limit(4);
//        query.skip(skipSize);

        query.limit(LIMIT);
        query.skip(skipSize);

        query.exec(new QueryResultsCallBack() {
            List<BuiltObject> pictures;
            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                pictures = queryResultObject.getResultObjects();
                Log.i("Image Data",queryResultObject.getResultObjects().get(0).getJSONObject("image").toString());

                for(BuiltObject object : pictures){
                    Log.i("Data", "Name " + object.get("name"));
                    Log.i("Data","Title "+object.get("caption"));
                }
                totalObjects = pictures.size();
                totalPage = (int)Math.ceil(totalObjects/(double)LIMIT);

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
                if(pictures.size() > 0){
                    updatePictures(pictures);
                }
            }
        });
    }


    private void updatePictures(List<BuiltObject> pictures)
    {
        Log.i("Size"," "+pictures.size());
//        JSONObject jsonField = pictures.get(0).get("image").toJSON();
////        Log.i("JSON OBJECT",jsonField.toString());
//        try {
//            String imageUrl = jsonField.getString("url");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        for (BuiltObject obj : pictures) {
            JSONObject jsonField = obj.getJSONObject("image");
            String url = null;
            try {
                url = jsonField.get("url").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("URL",url);
            PictureClass objPic = new PictureClass(obj.get("name").toString(), obj.get("caption").toString(),url,obj.getString("location") );
            dataItems.add(objPic);

        }
        Log.i("Size"," "+dataItems.size());

        adapter = new GridPictureViewAdapter(this, dataItems);
        GridView gridView = (GridView) findViewById(R.id.gridView3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        textView.setText("Page "+page_number+" of "+totalPage);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.i("Position"," "+position);
//        AlbumClass objAlbum = dataItems.get(position);
////        if(objAlbum!=null){
////            NetworkActivity pictureNetworkActivity = new NetworkActivity(CustomDataClass.FETCH_PICTURE,AlbumActivity.this,builtUserObject);
////            pictureNetworkActivity.execute(objAlbum.title);
////        }
//        Intent pictureIntent = new Intent(this,PictureViewActivity.class);
//        pictureIntent.putExtra("album_name",objAlbum.title);
//        startActivity(pictureIntent);

        Log.i("Position"," "+position);
        PictureClass objPicture = dataItems.get(position);
//        if(objAlbum!=null){
//            NetworkActivity pictureNetworkActivity = new NetworkActivity(CustomDataClass.FETCH_PICTURE,AlbumActivity.this,builtUserObject);
//            pictureNetworkActivity.execute(objAlbum.title);
//        }
        Intent pictureIntent = new Intent(this,PhotoActivity.class);
        pictureIntent.putExtra("image",objPicture.imageUrl);
        pictureIntent.putExtra("caption",objPicture.caption);
        startActivity(pictureIntent);


    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button3 && page_number<totalPage){
            dataItems.clear();
            page_number++;
            int skipSize = (page_number-1) * LIMIT;
            fetchPictures(skipSize);
        }
        else if(v.getId() == R.id.button2 && page_number>1){
            dataItems.clear();
            page_number--;
            int skipSize = (page_number-1)*LIMIT;
            fetchPictures(skipSize);
        }
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