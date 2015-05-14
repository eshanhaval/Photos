package com.example.eshan.photos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raweng.built.BuiltError;
import com.raweng.built.BuiltFile;
import com.raweng.built.BuiltFileUploadCallback;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltResultCallBack;

import java.io.File;


public class AddPictureFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText picName;
    private EditText picCaption;
    private EditText picLocation;
    private Button picButton;
    private Button addButton;
    private String albumName;
    Uri selectedImagePath;
    private final static String TAG_SELECT_BUTTON  = "select_pic";
    private final static String TAG_ADD_BUTTON  = "add_pic";
    BuiltUIPickerController pickerObject = null;
//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPictureFragment newInstance(String param1, String param2) {
        AddPictureFragment fragment = new AddPictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddPictureFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_add_picture, container, false);

        albumName = getArguments().getString("album");
        Log.i("Album Name",albumName);

        picName = (EditText)rootView.findViewById(R.id.picname);
        picCaption = (EditText)rootView.findViewById(R.id.piccaption);
        picLocation = (EditText)rootView.findViewById(R.id.location);

        picButton = (Button)rootView.findViewById(R.id.picpath);
        addButton = (Button)rootView.findViewById(R.id.addpicbutton);
        picButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        picButton.setTag(TAG_SELECT_BUTTON);
        addButton.setTag(TAG_ADD_BUTTON);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().equals(TAG_SELECT_BUTTON)){
            Log.i("Status","Inside pic path");
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }else if(v.getTag().equals(TAG_ADD_BUTTON)){
                Log.i("Status","Inside add pic button called");
                final BuiltFile file = new BuiltFile();
                File file1 = new File(getRealPathFromURI(selectedImagePath));
                Log.i("file name",file1.toString());
                file.setFile(file1.getPath());
                file.save(new BuiltFileUploadCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("uid",file.getUploadUid().toString());

                        BuiltObject pictureObject = new BuiltObject("picture");
                        //albumObject.setApplication("blt643f5d49ff2042cb", "0000011");
                        Object objPicName = picName.getText();
                        pictureObject.set("name",objPicName);
                        Object objPicCaption = picCaption.getText();
                        pictureObject.set("caption",objPicCaption);
                        Object objPicLocation = picLocation.getText();
                        pictureObject.set("location",objPicLocation);
                        Object email = HomeActivity.useremail;
                        pictureObject.set("email",email);
                        Object objAlbumName = albumName;
                        pictureObject.set("album",objAlbumName);
                        Object objFileUid = file.getUploadUid();
                        pictureObject.set("image",objFileUid);

                        pictureObject.save(new BuiltResultCallBack() {
                            @Override
                            public void onSuccess() {
                                AlbumFragment albumFragment = new AlbumFragment();
                                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainContent,albumFragment);
                                transaction.commit();
                                Toast.makeText(getActivity(), "Picture added successfully.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(BuiltError builtError) {
                                Toast.makeText(getActivity(), "Operation failed.Please try again!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAlways() {

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i) {
                        Log.i("progress"," "+i);
                    }

                    @Override
                    public void onError(BuiltError builtError) {
                        Log.i("error",builtError.getErrorMessage());
                    }

                    @Override
                    public void onAlways() {

                    }
                });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("resultcode", String.valueOf(resultCode));


        if (requestCode == 1 && resultCode == -1 && null != data) {
            selectedImagePath = data.getData();
            Log.d("picturepath",selectedImagePath.toString());// getPath().toString());

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}
