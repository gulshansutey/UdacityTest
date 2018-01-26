package com.technophile.sarah;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Articles {
    private String title;
    private String date;
    private String section;
    private List<String> contributors=new ArrayList<>();

    public Articles(String title, String date, String section, List<String> contributors) {
        this.title = title;
        this.date = date;
        this.section = section;
        this.contributors = contributors;

    }

    public String getcontributorsName() {

        System.out.println("contributors = " + contributors);
        if (contributors != null && !contributors.isEmpty()) {
            return contributors.toString().replaceAll("[\\[\\]]", "");
        }

        return "";
    }

    public String getTitle() {
        return TextUtils.isEmpty(title) ? "N/A" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {

        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(date);
            return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    public String getSection() {
        return section;
    }


    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", section='" + section + '\'' +
                '}';
    }
}
