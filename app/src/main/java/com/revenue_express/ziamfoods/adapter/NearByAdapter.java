package com.revenue_express.ziamfoods.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.revenue_express.ziamfoods.R;
import com.revenue_express.ziamfoods.dao.NearByDataDao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ChetPC on 12/8/2016.
 */
public class NearByAdapter extends BaseAdapter {
    Bitmap mBitmap;
    private LayoutInflater mInflater;
    List<NearByDataDao> mData;
    private ViewHolder mViewHolder;

    public NearByAdapter(Activity activity, List<NearByDataDao> data) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_near_by, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.imgShope = (ImageView) convertView.findViewById(R.id.imgShope);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            mViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            mViewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            mViewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final NearByDataDao mDatas = mData.get(position);

        if (mDatas.getBssh_imghead() != null) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL(mDatas.getBssh_imghead());
                        mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    } catch (MalformedURLException e) {

                    } catch (IOException e) {

                    }
                    return null;
                }
            }.execute();



            mViewHolder.imgShope.setImageBitmap(mBitmap);
        }


        mViewHolder.tvName.setText(mDatas.getBssh_title());
        mViewHolder.tvAddress.setText(mDatas.getBssh_address());
        mViewHolder.tvPhone.setText(mDatas.getBssh_phone());
        mViewHolder.tvDistance.setText(mDatas.getBssh_distance());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgShope;
        TextView tvName,tvAddress,tvPhone,tvDistance;
    }

    }


