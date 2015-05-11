package com.example.eshan.photos;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 4/25/15.
 */
public class NetworkActivity extends AsyncTask<String, Void, Void> {

    private int workType;
    private Activity activity;
    private BuiltUser builtUserObject;

    public NetworkActivity(int workType, Activity activity,BuiltUser userObject){
        this.workType = workType;
        this.activity = activity;
        this.builtUserObject = userObject;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(workType == CustomDataClass.LOGIN){
            login();
        }else if(workType == CustomDataClass.FETCH_ALBUM){
            fetchData();
        }else if(workType == CustomDataClass.FETCH_PICTURE){
            String album_name = params[0];
            fetchPictures(album_name);
        }else if(workType == CustomDataClass.SEARCH_PICTURE) {
            String search_text = params[0];
            fetchSearchResults(search_text);
        }
        return null;
    }

//    @Override
//    protected Object doInBackground(Object[] params) {
//
//
//    }

    private void login(){
        builtUserObject.login("deven@gmail.com", "asdqweasd", new BuiltResultCallBack() {
            boolean rtn;
            // here "john@malkovich.com" is a valid email id of your user
            // and "password", the corresponding password
            @Override
            public void onSuccess() {
                // user has logged in successfully
                String email = builtUserObject.getEmailId();
                String lastName = builtUserObject.getLastName();

                Log.d("Data", "Email id " + email);
                Log.d("Data", "Last Name " + lastName);
                rtn = true;
            }

            @Override
            public void onError(BuiltError builtErrorObject) {
                // login failed
                // the message, code and details of the error
                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
                Log.i("error: ", "" + builtErrorObject.getErrorCode());
                Log.i("error: ", "" + builtErrorObject.getErrors());
                rtn = false;
            }

            @Override
            public void onAlways() {
                //((LoginActivity)activity).updateLogin(rtn);
                // write code here that you want to execute
                // regardless of success or failure of the operation
            }

        });
    }

    private void fetchPictures(String album_name){


//        BuiltQuery query = new BuiltQuery("picture");
//
//        query.where("album",album_name);
//        query.exec(new QueryResultsCallBack() {
//            List<BuiltObject> pictures;
//            @Override
//            public void onSuccess(QueryResult queryResultObject) {
//                // the queryResultObject will contain the objects of the class
//                // here's the object we just created
//                pictures = queryResultObject.getResultObjects();
//                for(BuiltObject object : pictures){
//                    Log.i("Data","Name "+object.get("name"));
//                    Log.i("Data","Title "+object.get("title"));
//                }
//            }
//
//            @Override
//            public void onError(BuiltError builtErrorObject) {
//                // query failed
//                // the message, code and details of the error
//                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
//                Log.i("error: ", "" + builtErrorObject.getErrorCode());
//                Log.i("error: ", "" + builtErrorObject.getErrors());
//            }
//
//            @Override
//            public void onAlways() {
//                // write code here that you want to execute
//                // regardless of success or failure of the operation
//                if(pictures.size() > 0){
//                    ((AlbumActivity)activity).updatePictures(albums);
//                }
//            }
//        });
    }

    private void fetchData(){
        BuiltQuery query = new BuiltQuery("album");

        query.exec(new QueryResultsCallBack() {
            List<BuiltObject> albums;
            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                albums = queryResultObject.getResultObjects();
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
                    //((AlbumActivity)activity).updateData(albums);
                }
            }
        });
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
                   // ((SearchActivity)activity).updateData(pictures);
                }
            }
        });
    }



}
