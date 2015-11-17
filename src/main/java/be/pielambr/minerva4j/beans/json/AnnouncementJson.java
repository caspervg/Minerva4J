package be.pielambr.minerva4j.beans.json;

import be.pielambr.minerva4j.beans.Announcement;

import java.util.Date;

public class AnnouncementJson {
    private Date date;
    private String text;
    private String title;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AnnouncementJson{" +
                "date=" + date +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Announcement adapt() {
        return new Announcement(text, title, date);
    }
}
