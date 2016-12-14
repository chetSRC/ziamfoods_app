package com.revenue_express.ziamfoods.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewsDataDao {
    int bsrh_id;
    String bsrh_title,bsrh_desc,bsrh_score,bsrh_cdate,bsrh_cuser,memh_display,memh_pictureUrl;

    public int getBsrh_id() {
        return bsrh_id;
    }

    public void setBsrh_id(int bsrh_id) {
        this.bsrh_id = bsrh_id;
    }

    public String getBsrh_title() {
        return bsrh_title;
    }

    public void setBsrh_title(String bsrh_title) {
        this.bsrh_title = bsrh_title;
    }

    public String getBsrh_desc() {
        return bsrh_desc;
    }

    public void setBsrh_desc(String bsrh_desc) {
        this.bsrh_desc = bsrh_desc;
    }

    public String getBsrh_score() {
        return bsrh_score;
    }

    public void setBsrh_score(String bsrh_score) {
        this.bsrh_score = bsrh_score;
    }

    public String getBsrh_cdate() {
        return bsrh_cdate;
    }

    public void setBsrh_cdate(String bsrh_cdate) {
        this.bsrh_cdate = bsrh_cdate;
    }

    public String getBsrh_cuser() {
        return bsrh_cuser;
    }

    public void setBsrh_cuser(String bsrh_cuser) {
        this.bsrh_cuser = bsrh_cuser;
    }

    public String getMemh_display() {
        return memh_display;
    }

    public void setMemh_display(String memh_display) {
        this.memh_display = memh_display;
    }

    public String getMemh_pictureUrl() {
        return memh_pictureUrl;
    }

    public void setMemh_pictureUrl(String memh_pictureUrl) {
        this.memh_pictureUrl = memh_pictureUrl;
    }
}
