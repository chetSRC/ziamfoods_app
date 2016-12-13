package com.revenue_express.ziamfoods.manager.http;

import com.revenue_express.ziamfoods.dao.ReviewsListItemDao;
import retrofit2.Call;
import retrofit2.http.POST;


/**
 * Created by ChetPC on 10/31/2016.
 */
public interface ApiService {
    //    Call<ReviewListItemDao> loadReviewList();
    @POST("index.php/Review/getReviewApp")
    Call<ReviewsListItemDao> loadReviewsList();


}
