package com.revenue_express.ziamfoods.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.revenue_express.ziamfoods.R;
import com.revenue_express.ziamfoods.dao.ReviewsItemDao;
import com.revenue_express.ziamfoods.dao.ReviewsListItemDao;
import com.revenue_express.ziamfoods.manager.ReviewsListManager;
import com.revenue_express.ziamfoods.view.ReviewsListItem;

/**
 * Created by ChetPC on 12/8/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
    ReviewsListItemDao dao;
    int lastPosition = -1;




    @Override
    public int getCount() {
        if (ReviewsListManager.getInstance().getDao() == null)
            return 0;
        if (ReviewsListManager.getInstance().getDao().getData() == null)
            return 0;
        return ReviewsListManager.getInstance().getDao().getData().size();
    }

    @Override
    public Object getItem(int position) {

        return ReviewsListManager.getInstance().getDao().getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewsListItem item;
        if (convertView != null)
            item = (ReviewsListItem) convertView;
        else
            item = new ReviewsListItem(parent.getContext());
        ReviewsItemDao dao = (ReviewsItemDao) getItem(position);

        item.setTvName(dao.getName());
        item.setTvTitle(dao.getTitle());
        item.setTvDetail(dao.getDetail());
        item.setTvRating(dao.getScore());
//        item.setRatingBar(dao.getScore());


        if (position > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPosition = position;
        }


        return item;


    }

    }


