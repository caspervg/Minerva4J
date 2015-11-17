package be.pielambr.minerva4j.beans.json;

import be.pielambr.minerva4j.beans.Course;

public class CourseJson {
    private String announcements;
    private String description;
    private String documents;
    private String id;
    private String name;

    public String getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course adapt() {
        return new Course(id, name);
    }
}
