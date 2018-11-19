package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.*;

public class SimpleBlogDao implements BlogDao {
    private List<BlogEntry> entries;

    public SimpleBlogDao(){
        entries = new ArrayList<>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return entries.add(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        List<BlogEntry> temp = new ArrayList<>(entries);
        Collections.sort(temp, Collections.reverseOrder());
        return temp;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return entries.stream()
                .filter(blogEntry -> blogEntry.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundExcpetion::new);
    }

    public Set<BlogEntry> findEntriesByTag(String tag){
        Set<BlogEntry> result = new HashSet<>();
        for(BlogEntry entry : entries){
            for(String entryTag : entry.getTags()){
                if(entryTag.equals(tag)){
                    result.add(entry);
                }
            }
        }
        return result;
    }

    public boolean removeEntry(BlogEntry blogEntry){
        return entries.remove(blogEntry);
    }
}
