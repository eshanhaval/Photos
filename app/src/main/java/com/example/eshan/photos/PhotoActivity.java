package com.example.eshan.photos;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eshan.photos.PhotoCommentViewAdapter;
import com.example.eshan.photos.PhotoComments;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.raweng.built.BuiltUser;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Utsav on 4/28/2015.
 */
public class PhotoActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<PhotoComments> dataItems;
    private PhotoCommentViewAdapter adapter;
    private String caption;
    private String imageUrl;
    private SmartImageView image;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            caption = extras.getString("caption");
            imageUrl = extras.getString("image");
        }

        image = (SmartImageView)findViewById(R.id.avatar1);
        image.setImageUrl(imageUrl);

        textView = (TextView)findViewById(R.id.caption);
        textView.setText(caption);
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Position", " " + position);
    }

    public void updateData(){

        dataItems.add(new PhotoComments("user1","comment1"));
        dataItems.add(new PhotoComments("user2","comment2"));
        dataItems.add(new PhotoComments("user3","comment3"));
        dataItems.add(new PhotoComments("user4","comment4"));

        adapter = new PhotoCommentViewAdapter(this,dataItems);
        ListView listView = (ListView)findViewById(R.id.commentListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
}