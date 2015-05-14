package com.example.eshan.photos;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    BuiltUser builtUserObject = new BuiltUser();
    private List<PictureClass> dataItems;
    private GridPictureViewAdapter adapter;
    private EditText searchText;
    private Button btn;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        dataItems = new ArrayList<PictureClass>();

        try {
            Built.initializeWithApiKey(getActivity(), "blt643f5d49ff2042cb", "0000011");
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchText = (EditText)rootView.findViewById(R.id.editText);
        btn = (Button)rootView.findViewById(R.id.button);
        btn.setOnClickListener(this);

        return rootView;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void updateData(List<BuiltObject> pictures) {

        Log.i("Size", " " + pictures.size());
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

        adapter = new GridPictureViewAdapter(getActivity(), dataItems);
        GridView gridView = (GridView) getView().findViewById(R.id.gridView2);
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
