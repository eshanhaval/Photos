package com.example.eshan.photos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;


import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    BuiltUser builtUserObject = new BuiltUser();
    private List<PictureClass> dataItems;
    private GridPictureViewAdapter adapter;
    private EditText searchText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dataItems = new ArrayList<PictureClass>();

        try {
            Built.initializeWithApiKey(SearchActivity.this, "blt643f5d49ff2042cb", "0000011");
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchText = (EditText)findViewById(R.id.editText);
        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public void updateData(List<BuiltObject> pictures) {

        Log.i("Size"," "+pictures.size());
        for (BuiltObject obj : pictures) {
            JSONObject jsonField = obj.getJSONObject("image");
            String url = null;
            try {
                url = jsonField.get("url").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("URL",url);
            PictureClass objPic = new PictureClass(obj.get("name").toString(), obj.get("caption").toString(),url );
            dataItems.add(objPic);

        }

        Log.i("Size"," "+dataItems.size());

        adapter = new GridPictureViewAdapter(this, dataItems);
        GridView gridView = (GridView) findViewById(R.id.gridView2);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Position", " " + position);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button){
            Log.i("Data","Button clicked");
            String searchString = searchText.getText().toString();
            Log.i("Text"," "+searchString);

            //NetworkActivity searchNetworkActivity = new NetworkActivity(CustomDataClass.SEARCH_PICTURE,SearchActivity.this,builtUserObject);
            //searchNetworkActivity.execute(searchString);
            fetchSearchResults(searchString);
        }
    }

    private void fetchSearchResults(String search_text){
        BuiltQuery q1 = new BuiltQuery("picture");
        BuiltQuery q2 = new BuiltQuery("picture");
        BuiltQuery q3 = new BuiltQuery("picture");
        BuiltQuery q4 = new BuiltQuery("picture");
        BuiltQuery q5 = new BuiltQuery("picture");
        BuiltQuery q6 = new BuiltQuery("picture");
        BuiltQuery q7 = new BuiltQuery("picture");

        ArrayList orQuery1 = new ArrayList();
        ArrayList orQuery2 = new ArrayList();
        ArrayList andQuery = new ArrayList();


        q1.where("email", CustomDataClass.email);
        q2.containedIn("shared_with", new String[]{CustomDataClass.email});

        q3.where("name",search_text);
        q4.where("caption",search_text);

        orQuery1.add(q1);
        orQuery1.add(q2);
        q5.or(orQuery1);

        orQuery2.add(q3);
        orQuery2.add(q4);
        q6.or(orQuery2);

        andQuery.add(q5);
        andQuery.add(q6);

        q7.and(andQuery);


        q7.exec(new QueryResultsCallBack() {
            List<BuiltObject> pictures;
            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                pictures = queryResultObject.getResultObjects();
                for(BuiltObject object : pictures){
                    Log.i("Data","Name "+object.get("name"));
                    Log.i("Data","Caption "+object.get("caption"));
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
                if(pictures.size() > 0){
                     updateData(pictures);
                }
            }
        });
    }
}
