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
import com.revenue_express.ziamfoods.dao.ReviewsDataDao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ChetPC on 12/8/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
    Bitmap mBitmap;
    private LayoutInflater mInflater;
    List<ReviewsDataDao> mData;
    private ViewHolder mViewHolder;

    public ReviewsAdapter(Activity activity, List<ReviewsDataDao> data) {
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
            convertView = mInflater.inflate(R.layout.item_reviews1, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            mViewHolder.tvDetail = (TextView) convertView.findViewById(R.id.tvDetail);
            mViewHolder.tvRating = (TextView) convertView.findViewById(R.id.tvRating);
            mViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            mViewHolder.tvUser = (TextView) convertView.findViewById(R.id.tvUser);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final ReviewsDataDao mDatas = mData.get(position);

        if (mDatas.getMemh_pictureUrl() != null) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL(mDatas.getMemh_pictureUrl());
                        mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    } catch (MalformedURLException e) {

                    } catch (IOException e) {

                    }
                    return null;
                }
            }.execute();



            mViewHolder.imgUser.setImageBitmap(mBitmap);
        }


        mViewHolder.tvTitle.setText(mDatas.getBsrh_title());
        mViewHolder.tvDetail.setText(mDatas.getBsrh_desc());
        mViewHolder.tvDate.setText(mDatas.getBsrh_cdate());
        mViewHolder.tvUser.setText(mDatas.getMemh_display());
        mViewHolder.tvRating.setText(mDatas.getBsrh_score());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgUser;
        TextView tvUser,tvDate,tvRating,tvTitle,tvDetail;
    }

    }


