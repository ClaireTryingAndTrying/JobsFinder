package spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hao Bai This parser parses the level_1 URL and crawls all needed
 * links.
 */
public class HttpLevel1Parser {

    public static String parse(String urlAddress) {
        Pattern pattern;
        Matcher matcher;
        String nextLink = null;
        try {
            String level2URL;
            String title;
            String cName;
            String location;

            String[] tempArray;

            //parse url address
            URL url = new URL(urlAddress);
            System.out.println(urlAddress);
            String line;

            //get all content of the web page
            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.addRequestProperty("User-Agent", "Mozilla/4.76");

            //Document doc = Jsoup.parse(url, 30);
            //System.out.print(doc.body().toString());
            BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));
            //read content line by line (for Glassdoor, all needed information is in one line)
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                while ((line.contains("<a href='/partner/jobListing.htm?") || line.contains("<A HREF='/partner/jobListing.htm?"))) {
                    //split out the first appearing level2 link
                    tempArray = line.split("rel='nofollow' target='_job' class='jobLink'", 2);
                    level2URL = tempArray[0].replaceFirst(".*href='/partner/jobListing.htm?(\\S+).*", "$1").replaceAll("'", "");
                    level2URL = "http://www.glassdoor.com/partner/jobListing.htm" + level2URL;

                    //split out the second appearing level2 link
                    tempArray = tempArray[1].split("rel='nofollow' target='_job' class='jobLink'", 2);

                    //find job title
                    tempArray = tempArray[1].split("</tt>", 2);
                    title = tempArray[0].replaceAll(".*<[^>]*>", "");

                    //find location and cName
                    tempArray = tempArray[1].split("location=", 2);
                    tempArray = tempArray[1].split("employerId=", 2);
                    location = tempArray[0].replaceAll("\"", "").trim();
                    tempArray = tempArray[1].split("employerShortName=", 2);
                    cName = tempArray[0].replaceFirst(".*employerName=\"(.+)\"", "$1").replaceAll("\"", "").trim();

                    //go to next line
                    line = tempArray[1];

                    //write found items to temp.txt
                    FilesUtility.writeToTempFile("##O***title***O##");
                    FilesUtility.writeToTempFile(title);

                    FilesUtility.writeToTempFile("##O***website***O##");
                    FilesUtility.writeToTempFile(level2URL);

                    FilesUtility.writeToTempFile("##O***location***O##");
                    FilesUtility.writeToTempFile(location);

                    FilesUtility.writeToTempFile("##O***cName***O##");
                    FilesUtility.writeToTempFile(cName);

                    //go to level2 page to get more information
                    HttpLevel2Parser.parse(level2URL);

                    //write the item end tag in temp.txt
                    FilesUtility.writeToTempFile("##O***itemEnd***O##");
                }
                if (line.contains("<li class='next'>")) {
                    tempArray = line.split("<li class='next'>", 2);
                    nextLink = tempArray[1].replaceFirst(".*href=\"(\\S+)\".*", "$1");
                    nextLink = "http://www.glassdoor.com" + nextLink;
                    input.close();
                    return nextLink;
                }
            }//end of while loop  
            input.close();
            return nextLink;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", HttpLevel1Parser.class.getName(), ex.toString());
            //Logger.getLogger(HttpLevel1Parser.class.getName()).log(Level.SEVERE, null, ex);
            JobItemFlag.setIsFailed();
            return null;
        }
    }
}
