import be.pielambr.minerva4j.beans.Announcement;
import be.pielambr.minerva4j.beans.Course;
import be.pielambr.minerva4j.beans.Document;
import be.pielambr.minerva4j.client.MinervaClient;
import be.pielambr.minerva4j.exceptions.LoginFailedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by Pieterjan Lambrecht on 16/06/2015.
 */
public class TestClient {

    private String _username;
    private String _password;

    /**
     * Loads the username and the password from the settings.properties file
     */
    @Before
    public void loadProperties() {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("src/test/resources/settings.properties");
            properties.load(in);
            in.close();
            _username = properties.getProperty("username");
            _password = properties.getProperty("password");
        } catch (FileNotFoundException e) {
            System.out.println("Properties file not found");
        } catch (IOException e) {
            System.out.println("Error closing properties file");
        }
    }

    /**
     * Tests the login to Minerva and the detection for wrong credentials
     */
    @Test
    public void testConnect() throws IOException {
        // This should not fail
        MinervaClient client = new MinervaClient(_username, _password);
        try {
            client.connect();

            List<Course> courses = client.getCourses();
            for (Course course : courses) {
                List<Announcement> announcements = client.getAnnouncements(course);
                List<Document> documents = client.getDocuments(course);
            }
        } catch (LoginFailedException e) {
            Assert.fail("Login failed");
        }
    }
}
