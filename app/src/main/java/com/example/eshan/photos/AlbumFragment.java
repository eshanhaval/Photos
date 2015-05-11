package com.example.eshan.photos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    BuiltUser builtUserObject = new BuiltUser();
    private List<AlbumClass> dataItems;
    private GridAlbumViewAdapter adapter;
    private int page_number;
    private Button left,right;
    private TextView textView;
    private int totalObjects;
    private final static int LIMIT = 12;
    private int totalPage ;
    public String useremail;
    public String userprofilepicNew;

    public final static String TAG_ALBUM_BUTTON = "album_button";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumFragment() {
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


        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        page_number = 1;

        left = (Button)rootView.findViewById(R.id.button2);
        right = (Button)rootView.findViewById(R.id.button3);
        textView = (TextView)rootView.findViewById(R.id.textView);

        Log.d("check button", left.toString());

        left.setOnClickListener(this);
        right.setOnClickListener(this);



        dataItems = new ArrayList<AlbumClass>();
        textView.setText("Page 0/0");


        fetchData(0);

        ImageView addAlbum = new ImageView(getActivity());
        addAlbum.setImageResource(R.drawable.album_64);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        SubActionButton addAlbumButton = itemBuilder.setContentView(addAlbum).build();
        addAlbumButton.setOnClickListener(this);
        addAlbumButton.setTag(TAG_ALBUM_BUTTON);
        addAlbumButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));

        HomeActivity.actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(addAlbumButton)

                .attachTo(HomeActivity.actionButton)
                .build();



        return rootView;

    }

    private void fetchData(int skipSize){
        BuiltQuery query = new BuiltQuery("album");
        query.skip(skipSize);
        query.limit(LIMIT);
        query.includeCount();
        Log.i("Method", "Fetch Data Called");
        query.exec(new QueryResultsCallBack() {
            List<BuiltObject> albums;

            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                albums = queryResultObject.getResultObjects();
                Log.i("Count", " " + queryResultObject.getCount());
                totalObjects = queryResultObject.getCount();
                totalPage = (int) Math.ceil(totalObjects / (double) LIMIT);
                for (BuiltObject object : albums) {
                    Log.i("Data", "Name " + object.get("name"));
                    Log.i("Data", "Title " + object.get("description"));

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
                if (albums.size() > 0) {
                    updateData(albums);
                }
            }
        });
//
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Position"," "+position);
        AlbumClass objAlbum = dataItems.get(position);
//        if(objAlbum!=null){
//            NetworkActivity pictureNetworkActivity = new NetworkActivity(CustomDataClass.FETCH_PICTURE,AlbumActivity.this,builtUserObject);
//            pictureNetworkActivity.execute(objAlbum.title);
//        }
        /*
        Intent pictureIntent = new Intent(getActivity(),PictureViewActivity.class);
        pictureIntent.putExtra("loggedInNetwork", loggedInNetwork);
        pictureIntent.putExtra("album_name", objAlbum.title);

        pictureIntent.putExtra("useremail", useremail);
        pictureIntent.putExtra("profilephoto", userprofilepicNew);


        startActivity(pictureIntent);
        */
        Bundle bundle = new Bundle();
        bundle.putString("album_name",objAlbum.title);
        PictureViewFragment pictureViewFragment = new PictureViewFragment();
        pictureViewFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent,pictureViewFragment);
        transaction.commit();

    }

    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.button3 && page_number<totalPage){
            Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            dataItems.clear();
            page_number++;
            int skipSize = (page_number-1) * LIMIT;
            fetchData(skipSize);
        }
        else if(v.getId() == R.id.button2 && page_number>1){
            dataItems.clear();
            page_number--;
            int skipSize = (page_number-1)*LIMIT;
            fetchData(skipSize);
        }

        else if(v.getTag().equals(TAG_ALBUM_BUTTON))
        {
            Toast.makeText(getActivity(), "Album Button", Toast.LENGTH_SHORT).show();
        }

    }


    public void updateData(List<BuiltObject> albums){


        for(BuiltObject obj : albums){
            AlbumClass objAlbum = new AlbumClass(obj.get("name").toString(),obj.get("description").toString(),getResources().getDrawable(R.drawable.album));
            dataItems.add(objAlbum);
        }

        adapter = new GridAlbumViewAdapter(getActivity() ,dataItems);
        GridView gridView = (GridView)getView().findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        textView.setText("Page "+page_number+" of "+totalPage);

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
/*
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


}
