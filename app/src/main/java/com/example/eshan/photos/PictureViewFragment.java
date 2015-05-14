package com.example.eshan.photos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PictureViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PictureViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureViewFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{

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
    //ListView mDrawerList;
    //private ActionBarDrawerToggle mDrawerToggle;
    //private DrawerLayout mDrawerLayout;
   // ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    //RelativeLayout mDrawerPane;

    private TextView txtViewUserName;
    private String mActivityTitle;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String TAG_PHOTO_BUTTON = "photo_button";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PictureViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureViewFragment newInstance(String param1, String param2) {
        PictureViewFragment fragment = new PictureViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PictureViewFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_picture_view, container, false);

        page_number = 1;
        view = (GridView)rootView.findViewById(R.id.gridView33);

        builtUserObject = new BuiltUser();
        left = (Button)rootView.findViewById(R.id.button2);
        right = (Button)rootView.findViewById(R.id.button3);
        textView = (TextView)rootView.findViewById(R.id.textView);

        dataItems = new ArrayList<PictureClass>();
        textView.setText("Page 0/0");
        if(getArguments().containsKey("album_name")) {
            album_name = getArguments().getString("album_name");
            fetchPictures(0);
        }

        ImageView addPhoto = new ImageView(getActivity());
        addPhoto.setImageResource(R.drawable.photos);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        SubActionButton addPhotoButton = itemBuilder.setContentView(addPhoto).build();
        addPhotoButton.setOnClickListener(this);
        addPhotoButton.setTag(TAG_PHOTO_BUTTON);
        addPhotoButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));

        HomeActivity.actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(addPhotoButton)

                .attachTo(HomeActivity.actionButton)
                .build();




        return rootView;
    }

    private void fetchPictures(int skipSize) {
        BuiltQuery query = new BuiltQuery("picture");

        query.where("album",album_name);

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
                if(queryResultObject.getCount() > 0) {
                    pictures = queryResultObject.getResultObjects();
                    Log.i("Image Data", queryResultObject.getResultObjects().get(0).getJSONObject("image").toString());

                    for (BuiltObject object : pictures) {
                        Log.i("Data", "Name " + object.get("name"));
                        Log.i("Data", "Title " + object.get("caption"));
                    }
                    totalObjects = pictures.size();
                    totalPage = (int) Math.ceil(totalObjects / (double) LIMIT);
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
                if(pictures != null){
                    if (pictures.size() > 0) {
                        updatePictures(pictures);
                   }
                }else{
                    Toast.makeText(getActivity(), "Nothing to show", Toast.LENGTH_SHORT).show();
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
            PictureClass objPic = new PictureClass(obj.get("name").toString(), obj.get("caption").toString(),url );
            dataItems.add(objPic);

        }
        Log.i("Size"," "+dataItems.size());

        adapter = new GridPictureViewAdapter(getActivity(), dataItems);
        GridView gridView = (GridView) getView().findViewById(R.id.gridView33);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        textView.setText("Page " + page_number + " of " + totalPage);


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

        else if (v.getTag().equals(TAG_PHOTO_BUTTON)) {
            //Toast.makeText(getActivity(), "Photo Button", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("album",album_name);
            bundle.putString("email",HomeActivity.useremail);
            AddPictureFragment addPictureFragment = new AddPictureFragment();
            addPictureFragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContent,addPictureFragment);
            transaction.commit();


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("resultcode",String.valueOf(resultCode));


        if (requestCode == 1 && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            //String[] filePathColumn = { MediaStore.Images.Media.DATA };

            //Cursor cursor = getActivity().getContentResolver().query(selectedImage,
              //      filePathColumn, null, null, null);
            //cursor.moveToFirst();

            //int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //String picturePath = cursor.getString(columnIndex);
            //cursor.close();

            //List<NameValuePair> params = new ArrayList<NameValuePair>(1);

            Log.d("picturepath",selectedImage.getPath());


//            BuiltObject albumObject = new BuiltObject("album");
//            //albumObject.setApplication("blt643f5d49ff2042cb", "0000011");
//            Object albumName = album_name;
//            albumObject.set("name",albumName);
//            Object albumDesc = album_desc;
//            albumObject.set("description",albumDesc);
//            Object email = HomeActivity.useremail;
//            albumObject.set("email",email);
//            //albumObject.set("description",(Object)album_desc);
//            //albumObject.set("email",HomeActivity.useremail);
//            albumObject.save(new BuiltResultCallBack() {
//                @Override
//                public void onSuccess() {
//                    AlbumFragment albumFragment = new AlbumFragment();
//                    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.mainContent,albumFragment);
//                    transaction.commit();
//                    Toast.makeText(getActivity(), "Album created successfully.", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onError(BuiltError builtError) {
//                    Toast.makeText(getActivity(), "Album creation failed. Try again!", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAlways() {
//
//                }
//            });


            //ImageView imageView = (ImageView) getView().findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


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
        /*
        Intent pictureIntent = new Intent(this,PhotoActivity.class);
        pictureIntent.putExtra("image",objPicture.imageUrl);
        pictureIntent.putExtra("caption",objPicture.caption);
        startActivity(pictureIntent);
        */
        Bundle bundle = new Bundle();
        bundle.putString("image",objPicture.imageUrl);
        bundle.putString("caption",objPicture.caption);
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent,photoFragment);
        transaction.commit();

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

}
