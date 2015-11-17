package be.pielambr.minerva4j.client;

import be.pielambr.minerva4j.beans.Announcement;
import be.pielambr.minerva4j.beans.Course;
import be.pielambr.minerva4j.beans.Document;
import be.pielambr.minerva4j.beans.Event;
import be.pielambr.minerva4j.beans.json.AnnouncementJson;
import be.pielambr.minerva4j.beans.json.CourseJson;
import be.pielambr.minerva4j.exceptions.LoginFailedException;
import be.pielambr.minerva4j.parsers.DocumentParser;
import be.pielambr.minerva4j.utility.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import jodd.util.Base64;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MinervaClient {

    private final BasicHttpClient _client;
    private final Gson gson;

    /**
     * Default constructor for the Minerva client
     * @param username Username for the user
     * @param password Password for the user
     */
    public MinervaClient(String username, String password) {
        String _auth = Base64.encodeToString(username + ":" + password);

        _client = new BasicHttpClient(Constants.BASE_URL);
        _client.addHeader("Authorization", "Basic " + _auth);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    /**
     * Needs to be called after constructor to login
     * @throws LoginFailedException Is thrown if login fails
     */
    public void connect() throws LoginFailedException, IOException {
        login();
        verifyLogin();
    }

    private void login() throws IOException {
    }

    /**
     * Verifies login for the user after calling connect
     * @return Returns true if the login was correct
     * @throws LoginFailedException Is thrown if the login was incorrect
     */
    public boolean verifyLogin() throws LoginFailedException, IOException {
        HttpResponse resp = _client.get("", null);
        if (resp.getStatus() < 200 || resp.getStatus() > 299) {
            throw new LoginFailedException();
        }
        return true;
    }

    /**
     * Returns a list of announcements
     * @param course The course for which the announcements need to be retrieved
     * @return Returns a list of announcements
     */
    public List<Announcement> getAnnouncements(Course course) throws IOException {
        HttpResponse resp = _client.get(String.format(Constants.ANNOUNCEMENT_URL, course.getCode()), null);
        Type listType = new TypeToken<ArrayList<AnnouncementJson>>() {
        }.getType();

        List<AnnouncementJson> announcementJsons = gson.fromJson(resp.getBodyAsString(), listType);
        List<Announcement> announcements = new ArrayList<Announcement>(announcementJsons.size());
        for (AnnouncementJson ser : announcementJsons) {
            announcements.add(ser.adapt());
        }

        return announcements;
    }

    /**
     * Returns a list of courses
     * @return A list of courses for the current user
     */
    public List<Course> getCourses() throws IOException {
        HttpResponse resp = _client.get("", null);
        Type listType = new TypeToken<ArrayList<CourseJson>>() {
        }.getType();

        List<CourseJson> courseJsons = gson.fromJson(resp.getBodyAsString(), listType);
        List<Course> courses = new ArrayList<Course>(courseJsons.size());
        for (CourseJson ser : courseJsons) {
            courses.add(ser.adapt());
        }
        return courses;
    }

    /**
     * Returns a list of ducments for a given course
     * @param course The course for which the documents needs to be retrieved
     * @return A list of documents
     */
    public List<Document> getDocuments(Course course) throws IOException {
        HttpResponse resp = _client.get(String.format(Constants.DOCUMENT_URL, course.getCode()), null);
        return DocumentParser.parseDocuments(resp.getBodyAsString());
    }

    public BasicHttpClient getClient() {
        return this._client;
    }

    /**
     * Returns a valid download URL for a given document
     * @param course The course in which the document is uploaded
     * @param document The document
     * @return A valid download URL for the document
     */
    public String getDocumentDownloadURL(Course course, Document document) throws IOException {
        String response = _client.get(Constants.AJAX_URL + course.getCode() + Constants.DOCUMENTS + document.getId(), null).getBodyAsString();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get("url").getAsString();
    }

    /**
     * This method checks if the last request redirected us to login,
     * meaning we have been logged out and logs us back in
     */
    public void checkLogin(MinervaClient client) throws IOException {
    }

    /**
     * Returns a list of all events
     * @return A list of all events
     */
    public List<Event> getEvents() throws IOException {
        return null; //EventParser.getEvents(this);
    }

    /**
     * Returns a list of all events in a timespan
     * @param start Start of timespan
     * @param end End of timespan
     * @return A list of all events in a timespan
     */
    public List<Event> getEvents(Date start, Date end) throws IOException {
        return null; //EventParser.getEvents(this, start, end);
    }

}
