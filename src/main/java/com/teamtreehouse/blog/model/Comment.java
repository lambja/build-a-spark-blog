package com.teamtreehouse.blog.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {

    private String name;
    private String comment;
    private Date date;

    public Comment(String name, String comment) {
        if(name.length() == 0){
            this.name = "Anonymous";
        }else{
            this.name = name;
        }
        this.comment = comment;
        this.date = new Date();
    }


    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        String pattern = "MMMMM dd, yyyy 'at' hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment1 = (Comment) o;

        if (!comment.equals(comment1.comment)) return false;
        return date.equals(comment1.date);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + comment.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
