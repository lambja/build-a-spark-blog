package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.SimpleBlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import static spark.Spark.*;

//TODO:jrl - CSS, fonts have colors and faces, header fonts are larger than body fonts

public class Main {
    public static void main(String[] args) {
        staticFileLocation("/public");

        //Create 3 Sample Blog Entries with Starter Tags
        BlogEntry blogEntry1 = new BlogEntry("Best Hikes in the US", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pharetra pretium eros non pretium. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin varius risus id dui tempor, sit amet convallis nisi venenatis. Nullam sed lectus molestie, dapibus ligula eget, volutpat magna. Integer fringilla orci a nunc hendrerit rhoncus. Aliquam erat volutpat. In scelerisque eros vitae turpis suscipit ultrices. Suspendisse mauris purus, luctus vitae ullamcorper ut, iaculis et odio. Aliquam id congue felis, id condimentum est. Cras ac lorem sed justo volutpat pretium. Duis a eros ultrices ante finibus lobortis. Quisque eget elit ultricies, tincidunt mi mattis, gravida ante.");
        BlogEntry blogEntry2 = new BlogEntry("What do I need to know about hiking the PCT?", "Proin at odio volutpat, ornare turpis nec, hendrerit nunc. Nam consectetur diam non est ornare, id dapibus leo porttitor. Proin sollicitudin neque odio, sit amet mattis lectus bibendum sed. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean pulvinar lacus mauris. Aliquam pellentesque eros eu risus dapibus condimentum. Donec tellus lacus, accumsan sodales mauris a, varius bibendum sapien. Aliquam sem lacus, porta et accumsan sed, aliquam ac dolor. Cras sollicitudin non elit porta congue.");
        BlogEntry blogEntry3 = new BlogEntry("Top 10 Thru-Hikes", "Pellentesque viverra neque ex, in vehicula neque vulputate sit amet. Quisque ut mollis sem, id faucibus massa. Proin lobortis sed mauris congue vulputate. Morbi eget tincidunt diam, at posuere nibh. In rhoncus hendrerit ex, sodales tempus erat laoreet et. Proin est elit, placerat quis tristique vel, fringilla vitae erat. Maecenas erat lorem, posuere nec vulputate id, tempus sed mauris. Cras efficitur, lacus ut pharetra molestie, ligula mi pretium orci, quis ultrices urna dolor vel dolor. Aliquam sit amet porta metus.");
        blogEntry1.addTag("hike");
        blogEntry2.addTag("hike");
        blogEntry3.addTag("hike");
        blogEntry3.addTag("backpack");
        blogEntry2.addTag("thru-hike");
        blogEntry3.addTag("thru-hike");

        BlogDao dao = new SimpleBlogDao();
        dao.addEntry(blogEntry1);
        dao.addEntry(blogEntry2);
        dao.addEntry(blogEntry3);

        before((req, res) -> {
            if(req.cookie("username") != null){
                req.attribute("username", req.cookie("username"));
            }
            if(req.cookie("password") != null){
                req.attribute("password", req.cookie("password"));
            }

        });

        before("/new", (req, res) -> {
            String username = "admin";
            if (req.attribute("username") == null
                    || !(req.attribute("username").equals(username))
                    || req.attribute("password") == null){
                res.redirect("/password");
                halt();
            }
        });

        before("/detail/:slug/edit", (req, res) -> {
            String username = "admin";
            if (req.attribute("username") == null
                    || !(req.attribute("username").equals(username))
                    || req.attribute("password") == null){
                res.redirect("/password");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entries", dao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            BlogEntry blogEntry = new BlogEntry(req.queryParams("title"), req.queryParams("entry"));
            dao.addEntry(blogEntry);
            model.put("entries", dao.findAllEntries());
            res.redirect("/");
            return null;
        });

        get("/password", (req, res) -> {
            return new ModelAndView(null, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            res.cookie("username", username);
            res.cookie("password", password);
            model.put("username", username);
            model.put("password", password);
            res.redirect("/");
            return null;
        });

        get("/detail/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("/new", (req, res) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug", (req,res) ->{
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            Comment comment = new Comment(req.queryParams("name"), req.queryParams("comment"));
            blogEntry.addComment(comment);
            res.redirect("/detail/" + blogEntry.getSlug());
            return null;
        });

        get("/detail/:slug/edit", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/detail/:slug/edit", (req, res) ->{
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            blogEntry.setDate(new Date());
            blogEntry.setTitle(req.queryParams("title"));
            blogEntry.setEntry(req.queryParams("entry"));
            res.redirect("/detail/" + blogEntry.getSlug());
            return null;
        });

        post("/delete/:slug", (req, res) ->{
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            ((SimpleBlogDao) dao).removeEntry(blogEntry);
            res.redirect("/");
            return null;
        });

        get("/tag", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("entries", ((SimpleBlogDao) dao).findEntriesByTag(req.queryParams("tag")));
            model.put("tag", req.queryParams("tag"));
            return new ModelAndView(model, "tag.hbs");
        }, new HandlebarsTemplateEngine());

        post("tag/:slug", (req, res) ->{
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            blogEntry.addTag(req.queryParams("tag"));
            res.redirect("/detail/" + blogEntry.getSlug() + "/edit");
            return null;
        });
    }
}
