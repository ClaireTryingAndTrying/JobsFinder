package spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hao Bai
 * This parser parses the secondary page according to level2URL
 */
public class HttpLevel2Parser {
    
    public static void parse(String urlAddress) {
        Pattern pattern;
        Matcher matcher;
        try {
            String description = "Not Found";
            String cType = "Not Found";
            String cDescription = "Not Found";
            String cIndustry = "Not Found";
        
            //parse url address
            URL url = new URL(urlAddress);
            String line;
            //get all content of the web page
            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection(); 
            httpURLConn.addRequestProperty("User-Agent", "Mozilla/4.76"); 
            BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));
            //read content line by line
            while ((line = input.readLine()) != null) { 
                pattern = Pattern.compile("<h2.*>.*Company Details.*</h2>");
                matcher = pattern.matcher(line);
                //job description is in one line
                if(line.contains("<h2>Job Description</h2>") || line.contains("<H2>Job Description</H2>")) {      
                    description = line.replaceAll("<p></p>", "").replaceAll("</(li|LI)>", "\r\n").
                                       replaceAll("<(li|LI)>", "    ").replaceAll("</(p|P)>", "\r\n").
                                       replaceAll("<[^>]*>", "");
                    description = description.replaceFirst(".*Job Description", "").trim();
                    
                    //write found items to temp.txt
                    FilesUtility.writeToTempFile("##O***description***O##");
                    FilesUtility.writeToTempFile(description);
                }
                //all other items are in one line
                else if (matcher.find()) {
                    pattern = Pattern.compile("<[^>]*>");
                    
                    cType = line.replaceFirst(".*<strong>Type</strong> <span class=\"empData\">([^<]*[^>]*)</span>.*", "$1");
                    matcher = pattern.matcher(cType);
                    if (!matcher.find()) {
                        FilesUtility.writeToTempFile("##O***cType***O##");
                        FilesUtility.writeToTempFile(cType);
                    }
                    cIndustry = line.replaceFirst(".*<strong>Industry</strong> <span class=\"empData\">([^<]*[^>]*)</span>.*", "$1");
                    matcher = pattern.matcher(cIndustry);
                    if (!matcher.find()) {
                        FilesUtility.writeToTempFile("##O***cIndustry***O##");
                        FilesUtility.writeToTempFile(cIndustry);
                    }
                    cDescription = line.replaceFirst(".*<strong>Description</strong> <span class=\"empData\">([^<]*[^>]*)</span>.*", "$1");
                    matcher = pattern.matcher(cDescription);
                    if (!matcher.find()) {
                        FilesUtility.writeToTempFile("##O***cDescription***O##");
                        FilesUtility.writeToTempFile(cDescription);
                    }
                }    
            }//end of while loop  
            input.close(); 
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
            JobItemFlag.setIsFailed();
        }
    }
}


