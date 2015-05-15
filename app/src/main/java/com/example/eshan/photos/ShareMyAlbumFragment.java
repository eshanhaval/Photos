package com.example.eshan.photos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltResultCallBack;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareMyAlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareMyAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareMyAlbumFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchText;
    private Button btn;

   // private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareMyAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareMyAlbumFragment newInstance(String param1, String param2) {
        ShareMyAlbumFragment fragment = new ShareMyAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareMyAlbumFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_share_my_album, container, false);
        searchText = (EditText)rootView.findViewById(R.id.editTextName);
        btn = (Button)rootView.findViewById(R.id.buttonShare);
        btn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.buttonShare)
        {
            if(searchText.getText().toString().trim()!="") {
                String emailString = searchText.getText().toString();
                shareAlbum(emailString);

            }
            else
            {
                Toast.makeText(getActivity(), "Email id of user can't be blank", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareAlbum(String email)
    {




        BuiltObject albumObject = new BuiltObject("album");

        Log.d("unique id", getArguments().getString("album_unique_id"));
        Object objEmail = email;

        //albumObject.setClassUid("album");
        Object uid = getArguments().getString("album_unique_id");
        albumObject.setUid(uid.toString());
        albumObject.pushValue("shared_with", objEmail);

        albumObject.save(new BuiltResultCallBack() {
            @Override
            public void onSuccess() {

                //Navigate back to Albums
                AlbumFragment albumFragment = new AlbumFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.mainContent, albumFragment, "albumFragment");
                transaction.commit();

            }

            @Override
            public void onError(BuiltError builtError) {
                Toast.makeText(getActivity(), builtError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.d("file error message", builtError.getErrorMessage());
                searchText.setText("");
            }

            @Override
            public void onAlways() {

            }
        });

        Toast.makeText(getActivity(), "Shared Successfully", Toast.LENGTH_SHORT).show();



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
