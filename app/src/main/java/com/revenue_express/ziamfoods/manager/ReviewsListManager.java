package com.revenue_express.ziamfoods.manager;

import android.content.Context;

import com.revenue_express.ziamfoods.dao.ReviewsListItemDao;

/**
 * Created by ChetPC on 12/8/2016.
 */
public class ReviewsListManager {
    private static ReviewsListManager instance;

    public static ReviewsListManager getInstance() {
        if (instance == null)
            instance = new ReviewsListManager();
        return instance;
    }

    private Context mContext;
    private ReviewsListItemDao dao;

    private ReviewsListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public ReviewsListItemDao getDao() {
        return dao;
    }

    public void setDao(ReviewsListItemDao dao) {
        this.dao = dao;
    }
}
