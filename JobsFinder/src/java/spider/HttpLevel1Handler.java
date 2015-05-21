package spider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hao Bai This handler parses the Level_1 URL read from ref_source.txt,
 * and initiates the spider which schedules web crawlers to parse Level_2 URLs.
 */
public class HttpLevel1Handler {

    private JobItemFlag jobItemFlag;
    private List<String> level1URL;
    private String nextLevel1URL;

    //The constructor read URLs from ref_source.txt
    //In this project, only one URL is stored in ref_source.txt
    public HttpLevel1Handler() {

        //create the temp file (only name)
        FilesUtility.createTempFile();

        if (JobItemFlag.loadRefSourceURLs() == true) {
            //Read URLs from ref_source.txt
            level1URL = new ArrayList<>();
            level1URL = JobItemFlag.getRefSourceURLs();
        }
    }

    //parse level_1 URL, and find next level_1 URL in the page
    public void parseLevel1URL() {
        jobItemFlag = new JobItemFlag();
        int countURL = 0;
        String singleURL = level1URL.get(countURL);
        countURL++;
        while (singleURL != null) {
            nextLevel1URL = HttpLevel1Parser.parse(singleURL);

            singleURL = nextLevel1URL;
            if (singleURL == null && countURL < level1URL.size()) {
                singleURL = level1URL.get(countURL);
                countURL++;
            }
        }
    }
}
