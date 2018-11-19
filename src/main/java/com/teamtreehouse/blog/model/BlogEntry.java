package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BlogEntry implements Comparable<BlogEntry> {
    private String title;
    private Date date;
    private String entry;
    private List<Comment> comments;
    private String slug;
    private Set<String> tags;

    public BlogEntry(String title, String body){
        this.title = title;
        this.date = new Date();
        this.entry = body;
        this.comments = new ArrayList<>();
        try{
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.tags = new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        String pattern = "MMMMM dd, yyyy 'at' hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public String getEntry() {
        return entry;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getSlug() {
        return slug;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean addTag(String tag){ return tags.add(tag);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry blogEntry = (BlogEntry) o;

        if (!title.equals(blogEntry.title)) return false;
        if (!entry.equals(blogEntry.entry)) return false;
        return date.equals(blogEntry.date);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + entry.hashCode();
        return result;
    }

    @Override
    public int compareTo(BlogEntry o) {
        if(equals(o)){
            return 0;
        }
        int dateCmp = date.compareTo(o.date);
        if(dateCmp == 0){
            return title.compareTo(o.title);
        }
        return dateCmp;
    }
}
