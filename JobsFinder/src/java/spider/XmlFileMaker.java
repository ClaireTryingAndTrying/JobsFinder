package spider;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author Hao Bai
 */
public class XmlFileMaker {

    private static String jobsFilePathName;
    private static BufferedWriter bufferedWriter;

    public static String getJobsFilePathName() {
        return jobsFilePathName;
    }

    public static void setJobsFilePathName(String jobsFilePathName) {
        XmlFileMaker.jobsFilePathName = jobsFilePathName;
    }

    public static void startTag(String tagName) {
        if (tagName.equals("jobs")) {
            XmlFileMaker.writeToFile("<" + tagName + ">");
        } else if (tagName.equals("job")) {
            XmlFileMaker.writeToFile("  <" + tagName + ">"); //2 spaces ahead
        } else {
            XmlFileMaker.writeToFile("    <" + tagName + ">"); //4 spaces ahead
        }
    }

    public static void endTag(String tagName) {
        if (tagName.equals("jobs")) {
            XmlFileMaker.writeToFile("</" + tagName + ">");
        } else if (tagName.equals("job")) {
            XmlFileMaker.writeToFile("  </" + tagName + ">"); //2 spaces ahead
        } else {
            XmlFileMaker.writeToFile("    </" + tagName + ">"); //4 spaces ahead
        }
    }

    public static void setText(String text) {
        //6 spaces ahead
        XmlFileMaker.writeToFile("      " + text);
    }

    public static boolean openFile() {
        try {
            if (jobsFilePathName == null || jobsFilePathName.equals("")) {
                return false;
            }
            bufferedWriter = new BufferedWriter(new FileWriter(jobsFilePathName, true));
            return true;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", XmlFileMaker.class.getName(), ex.toString());
            return false;
        }
    }

    public static boolean closeFile() {
        try {
            bufferedWriter.close();
            return true;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", XmlFileMaker.class.getName(), ex.toString());
            return false;
        }
    }

    private static boolean writeToFile(String text) {
        try {
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            return true;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", XmlFileMaker.class.getName(), ex.toString());
            return false;
        }
    }
}
