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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowCommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowCommentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;
    private Button commentButton;
    private EditText comment;

    private List<PhotoComments> dataItems;
    private PhotoCommentViewAdapter adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowCommentFragment newInstance(String param1, String param2) {
        ShowCommentFragment fragment = new ShowCommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowCommentFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_show_comment, container, false);

        commentButton = (Button)rootView.findViewById(R.id.commentButton);
        commentButton.setOnClickListener(this);

        comment = (EditText)rootView.findViewById(R.id.comment);
        dataItems = new ArrayList<PhotoComments>();
        updateData();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.commentButton)
        {
            if(!comment.getText().equals(null))
            {
                if( comment.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getActivity(), "Empty comment!", Toast.LENGTH_LONG).show();
                } else {
                    BuiltObject uploadComm = new BuiltObject("comment");
                    uploadComm.set("email",getArguments().getString("albumEmail"));
                    uploadComm.set("user_name",HomeActivity.useremail);
                    uploadComm.set("picture",getArguments().getString("picture_name"));
                    uploadComm.set("album",getArguments().getString("album_name"));
                    uploadComm.set("comment",comment.getText().toString());

                    //Save the comment
                    uploadComm.save(new BuiltResultCallBack() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(getActivity(), "comment added", Toast.LENGTH_SHORT).show();

                            ShowCommentFragment showCommentFragment = new ShowCommentFragment();
                            Bundle bundle = new Bundle();


                            bundle.putString("albumEmail",getArguments().getString("albumEmail"));
                            bundle.putString("album_name",getArguments().getString("album_name"));
                            bundle.putString("picture_name",getArguments().getString("picture_name"));
                            showCommentFragment.setArguments(bundle);

                            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                            transaction.replace(R.id.mainContent, showCommentFragment);

                            transaction.commit();
                        }

                        @Override
                        public void onError(BuiltError builtError) {
                            Toast.makeText(getActivity(), builtError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("file error message", builtError.getErrorMessage());

                        }

                        @Override
                        public void onAlways() {

                        }
                    });


                }
            } else {
                Toast.makeText(getActivity(),"No comment entered!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void updateData(){

        BuiltQuery q1 = new BuiltQuery("comment");
        BuiltQuery q2 = new BuiltQuery("comment");
        BuiltQuery q3 = new BuiltQuery("comment");
        BuiltQuery q4 = new BuiltQuery("comment");

        q1.where("email", getArguments().getString("albumEmail"));
        q2.where("album", getArguments().getString("album_name"));
        q3.where("picture", getArguments().getString("picture_name"));

        ArrayList<BuiltQuery> andQuery =new ArrayList<BuiltQuery>();

        andQuery.add(q1);
        andQuery.add(q2);
        andQuery.add(q3);
        q4.and(andQuery);
        q4.includeCount();

        q4.exec(new QueryResultsCallBack() {
            List<BuiltObject> comments;

            @Override
            public void onSuccess(QueryResult queryResultObject) {
                // the queryResultObject will contain the objects of the class
                // here's the object we just created
                Log.d("count size", String.valueOf(queryResultObject.getCount()));
                if (queryResultObject.getCount() > 0) {
                    comments = queryResultObject.getResultObjects();
                    //Log.i("Image Data", queryResultObject.getResultObjects().get(0).getJSONObject("image").toString());


                    if (comments.size() > 0) {
                        Log.d("comment size",String.valueOf(comments.size()));
                        for (BuiltObject object : comments) {
                            PhotoComments objComments = new PhotoComments(object.getString("user_name"), object.getString("comment"));
                            dataItems.add(objComments);

                        }
                        adapter = new PhotoCommentViewAdapter(getActivity(), dataItems);
                        ListView listView = (ListView) getView().findViewById(R.id.commentListView);
                        listView.setAdapter(adapter);


                    }

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
                /*
                if (pictures != null) {
                    if (pictures.size() > 0) {
                        updatePictures(pictures);
                    }
                } else {
                    Toast.makeText(getActivity(), "Nothing to show", Toast.LENGTH_SHORT).show();
                }
                */
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
