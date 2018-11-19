# build-a-spark-blog
Techdegree Project 4


# Techdegree Project 4

Build a Blog with Spark | Written in Java & Spark

Code files: /src/main/java

## Minimum Viable Product:
- Create model classes for blog entries and blog comments.
- Create a DAO interface for data storage and access and implement it.
- Add necessary routes
- Create index view as the homepage. This view contains a list of blog entries, which displays Title, Date/Time Created. Title should be hyperlinked to the detail page for each blog entry. Include a link to add an entry.
- Create detail page displaying blog entry and submitted comments. Detail page should also display a comment form with Name and Comment. Include a link to edit the entry.
- Create a password page that requires a user to provide 'admin' as the username. This page should display before adding or editing a blog entry if there is no cookie present that has the value 'admin'.
- Create add/edit page that is blocked by a password page, using before filter.
- Use CSS to style headings, font colors, blog entry container colors, body colors.


## Extra Credit:
- Add tags to blog entries, which enables the ability to categorize. A blog entry can exist with no tags, or have multiple tags.
- Add the ability to delete a blog entry.
- Issue a cookie upon entering the password, and check for it upon adding or editing a post.
