package spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hao Bai
 */
public class TempFileParser {

    private String tempFilePathName;

    public TempFileParser(String tempFilePathName) {
        this.tempFilePathName = tempFilePathName;
        this.validateTempFile();
    }

    public boolean parse() {
        //open jobs.xml file
        XmlFileMaker.openFile();
        //write the root tag<jobs> to this file
        XmlFileMaker.startTag("jobs");

        Pattern pattern;
        Matcher matcher;

        BufferedReader bufferedReaderFile;
        try {
            bufferedReaderFile = new BufferedReader(new FileReader(tempFilePathName));
            //judge if a job item in the temp.txt is found
            boolean findJobItem = false;
            //store the start-tag and the end-tag
            String elementStart = "";
            String elementEnd = "";

            String fileLine;
            while ((fileLine = bufferedReaderFile.readLine()) != null) {
                fileLine = fileLine.trim();
                //the start tag of every job item
                pattern = Pattern.compile("\\#{2}O\\*{3}title\\*{3}O\\#{2}");
                matcher = pattern.matcher(fileLine);
                if (matcher.find()) {
                    findJobItem = true;
                    XmlFileMaker.startTag("job");
                    XmlFileMaker.startTag("title");
                    elementEnd = "title";
                    continue;
                }
                //the end tag of every job item
                pattern = Pattern.compile("\\#{2}O\\*{3}itemEnd\\*{3}O\\#{2}");
                matcher = pattern.matcher(fileLine);
                if (matcher.find() && findJobItem) {
                    if (!elementEnd.equals("")) {
                        XmlFileMaker.endTag(elementEnd);
                    }
                    XmlFileMaker.endTag("job");
                    findJobItem = false;
                    continue;
                }
                //find other tag except "title" and itemEnd
                pattern = Pattern.compile("\\#{2}O\\*{3}\\w+\\*{3}O\\#{2}");
                matcher = pattern.matcher(fileLine);
                if (matcher.find() && !fileLine.contains("title") && findJobItem) {
                    elementStart = fileLine.replaceAll("\\#{2}O\\*{3}(\\w+)\\*{3}O\\#{2}", "$1");

                    if (!elementEnd.equals("")) {
                        XmlFileMaker.endTag(elementEnd);
                    }
                    XmlFileMaker.startTag(elementStart);
                    elementEnd = elementStart;
                    continue;
                }
                //add the text content of an elemnt to jobs.xml
                pattern = Pattern.compile("\\#{2}O\\*{3}\\w+\\*{3}O\\#{2}");
                matcher = pattern.matcher(fileLine);
                if (!matcher.find() && findJobItem) {
                    //convert "&", "<" and ">" to "&amp;", "&lt;" and "&gt;"
                    fileLine = fileLine.replaceAll("&(?!(amp|quot|nbsp|lt|gt|\\#\\d+);)", "&amp;");
                    fileLine = fileLine.replaceAll("<", "&lt;");
                    fileLine = fileLine.replaceAll(">", "&gt;");
                    XmlFileMaker.setText(fileLine);
                }
            }// end of while loop
            bufferedReaderFile.close();

            //write the ending <jobs> tag
            XmlFileMaker.endTag("jobs");
            //close the jobs.xml
            XmlFileMaker.closeFile();

            return true;
        } catch (IOException ex) {
            System.out.printf("Class: %s. Exception: %s.\n", TempFileParser.class.getName(), ex.toString());
            return false;
        }
    }

    //judge if the temp file exits and has content
    private void validateTempFile() {
        File tempFile = new File(tempFilePathName);
        if (!tempFile.exists() || tempFile.isDirectory()) {
            System.out.println(tempFilePathName + " doesn't exist.\r\nProgram exits!");
            //Thread.currentThread().stop();
        } else if (tempFile.length() <= 0) {
            System.out.println(tempFilePathName + " doesn't have content.\r\nProgram exits!");
            //Thread.currentThread().stop();
        }
    }
}
