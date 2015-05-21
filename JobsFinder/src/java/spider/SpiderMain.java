package spider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Hao Bai This is the main class for the whole spider functionality. It
 * can run independently as a main class (invoking its main() function) or run
 * as part of the web service (invoking its startSpider() function).
 */
public class SpiderMain {

    private String tempFileName = "temp.txt";
    private String tempFilePath = "./";
    private String errorFileName = "error.txt";
    private String errorFilePath = "./";
    private String jobsFilePath = "./";
    private String jobsFileName = "jobs.xml";

    public SpiderMain() {
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getErrorFileName() {
        return errorFileName;
    }

    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public String getJobsFileName() {
        return jobsFileName;
    }

    public String getJobsFilePath() {
        return jobsFilePath;
    }

    public void setJobsFilePath(String JobsFilePath) {
        this.jobsFilePath = JobsFilePath;
    }

    //delete the existing temp file
    public boolean deleteTempFile() {
        try {
            File file = new File(this.tempFilePath + this.tempFileName);
            if ((file.exists() && !file.isDirectory())) {
                return file.delete();
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    //delete the existing error file
    public boolean deleteErrorFile() {
        try {
            File file = new File(this.errorFilePath + this.errorFileName);
            if ((file.exists() && !file.isDirectory())) {
                return file.delete();
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    //rename the existing jobs.xml file
    public boolean renameJobsFile() {
        try {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");

            File file = new File(this.jobsFilePath + this.jobsFileName);
            if ((file.exists() && !file.isDirectory())) {
                file.renameTo(new File(this.jobsFilePath + dateFormat.format(date) + "-" + this.jobsFileName));
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //start the spider as a part of web service or other service by invoking this function
    public void startSpider() {
        this.deleteTempFile();
        this.deleteErrorFile();
        //rename previous jobs.xml file
        this.renameJobsFile();

        HttpLevel1Handler httpLevel1Handler;
        try {
            httpLevel1Handler = new HttpLevel1Handler();
            httpLevel1Handler.parseLevel1URL();
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", SpiderMain.class.getName(), ex.toString());
        }
        //set job.xml file path and name
        XmlFileMaker.setJobsFilePathName(this.jobsFilePath + this.jobsFileName);
        //read crawled information from temp.txt, and then write them to job.xml
        TempFileParser tempFileParser = new TempFileParser(this.tempFilePath + this.tempFileName);
        tempFileParser.parse();

        //input information of jobs.xml to database
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DBFileHandler handler = new DBFileHandler();
            saxParser.parse(this.jobsFilePath + this.jobsFileName, handler);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.printf("Class: %s. Exception: %s.\n", SpiderMain.class.getName(), ex.toString());
        }
    }

    //start the spider independently by invoking this function
    public static void main(String[] args) {
        SpiderMain spiderMain = new SpiderMain();

        spiderMain.deleteTempFile();
        spiderMain.deleteErrorFile();
        //rename previous jobs.xml file
        spiderMain.renameJobsFile();

        HttpLevel1Handler httpLevel1Handler;
        try {
            httpLevel1Handler = new HttpLevel1Handler();
            httpLevel1Handler.parseLevel1URL();
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", SpiderMain.class.getName(), ex.toString());
        }
        //set job.xml file path and name
        XmlFileMaker.setJobsFilePathName(spiderMain.jobsFilePath + spiderMain.jobsFileName);
        //read crawled information from temp.txt, and then write them to job.xml
        TempFileParser tempFileParser = new TempFileParser(spiderMain.tempFilePath + spiderMain.tempFileName);
        tempFileParser.parse();

        //input information of jobs.xml to database
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DBFileHandler handler = new DBFileHandler();
            saxParser.parse(spiderMain.jobsFilePath + spiderMain.jobsFileName, handler);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.printf("Class: %s. Exception: %s.\n", SpiderMain.class.getName(), ex.toString());
        }
    }
}
