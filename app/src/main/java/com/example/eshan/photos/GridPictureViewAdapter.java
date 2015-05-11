package com.example.eshan.photos;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.loopj.android.image.SmartImageView;

import java.util.List;

/**
 * Created by ashish on 4/28/2015.
 */
public class GridPictureViewAdapter extends BaseAdapter{

    private Context mContext;
    private List<PictureClass> mItems;

    public GridPictureViewAdapter(Context context,List<PictureClass> picList){
        this.mContext = context;
        this.mItems = picList;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.picture_grid_file, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (SmartImageView) convertView.findViewById(R.id.avatar);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle1);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // update the item view
        PictureClass item = mItems.get(position);
        viewHolder.tvTitle.setText(item.name);
        Log.i("Received url",item.imageUrl);
        viewHolder.ivIcon.setImageUrl(item.imageUrl);

        return convertView;
    }

    private static class ViewHolder {
        SmartImageView ivIcon;
        TextView tvTitle;
    }
}