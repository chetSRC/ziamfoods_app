package com.revenue_express.ziamfoods.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewsDao {
    Boolean success;
    List<ReviewsDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ReviewsDataDao> getData() {
        return data;
    }

    public void setData(List<ReviewsDataDao> data) {
        this.data = data;
    }
}
