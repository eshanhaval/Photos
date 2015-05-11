package com.example.eshan.photos;

/**
 * Created by eshan on 4/30/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.List;

/**
 * Created by Utsav on 4/28/2015.
 */
public class PhotoCommentViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoComments> mItems;

    public PhotoCommentViewAdapter(Context mContext,List<PhotoComments> mItems){
        this.mContext = mContext;
        this.mItems = mItems;
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
            convertView = inflater.inflate(R.layout.comment_listview, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        PhotoComments item = mItems.get(position);
        viewHolder.tvUserName.setText(item.username);
        viewHolder.tvComment.setText(item.comment);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvUserName;
        TextView tvComment;
    }
}