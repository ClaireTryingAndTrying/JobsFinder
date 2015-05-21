package spider;

import dao.JobDAO;
import model.Job;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Hao Bai This class parses jobs.xml files and write the content of the
 * file into database.
 */
public class DBFileHandler extends DefaultHandler {

    private Job job;
    private JobDAO jobDAO;
    private String columnName;
    private boolean isDone;
    private String result;

    public DBFileHandler() {
        job = null;
        jobDAO = new JobDAO();
        columnName = "";
        result = "";
        isDone = false;
    }

    @Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
        if (isDone && !result.equals("")) {
            writeAttribute(columnName, result);
        }
        if (tagName.equalsIgnoreCase("job")) {
            //create a new Job object
            job = new Job();
        } else if (!tagName.equalsIgnoreCase("job") && !tagName.equalsIgnoreCase("jobs")) {
            columnName = tagName;
            isDone = false;
            result = "";
        }
    }

    @Override
    public void endElement(String uri, String localName, String tagName) throws SAXException {
        if (tagName.equalsIgnoreCase("job")) {
            //write the job object to the database
            jobDAO.saveJob(job);
        } else if (tagName.equalsIgnoreCase("jobs")) {
            //close connection of database
            jobDAO.close();
        } else if (!tagName.equalsIgnoreCase("job") && !tagName.equalsIgnoreCase("jobs")) {
            columnName = tagName;
            isDone = true;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String text = new String(ch, start, length);

        if (!columnName.equals("")) {
            //remove the spaces before the concrete content
            result += text.replaceAll("\\s*(.+)", "$1") + "\r\n";
        }
    }

    private void writeAttribute(String columnName, String result) {
        switch (columnName) {
            case "title":
                job.setTitle(result);
                break;
            case "description":
                job.setDescription(result);
                break;
            case "website":
                job.setWebsite(result);
                break;
            case "location":
                job.setLocation(result);
                break;
            case "cName":
                job.setCname(result);
                break;
            case "cType":
                job.setCtype(result);
                break;
            case "cDescription":
                job.setCdescription(result);
                break;
            case "cIndustry":
                job.setCindustry(result);
                break;
        }
    }
}
