package spider;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author Hao Bai This FilesUtility is used for creating&deleting files
 */
public class FilesUtility {

    private static String tempFilePath = "./";
    private static String tempFileName;
    private static String errorFilePath = "./";
    private static final String errorFileName = "error.txt";

    public FilesUtility() {
    }

    public static void setErrorFilePath(String errorFilePath) {
        FilesUtility.errorFilePath = errorFilePath;
    }

    public static void setTempFilePath(String tempFilePath) {
        FilesUtility.tempFilePath = tempFilePath;
    }

    //create file to store crawled information
    public static void createTempFile() {
        tempFileName = "temp.txt";
    }

    //write text to the temp file
    public static boolean writeToTempFile(String text) {
        BufferedWriter bufferedWriter;
        try {
            if (tempFileName == null || tempFileName.equals("")) {
                return false;
            }
            bufferedWriter = new BufferedWriter(new FileWriter(tempFilePath + tempFileName, true));
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", FilesUtility.class.getName(), ex.toString());
            return false;
        }
    }

    //write the job title that causes exception to the error file
    public static boolean writeToErrorFile(String text) {
        BufferedWriter bufferedWriter;
        try {
            if (errorFileName == null || errorFileName.equals("")) {
                return false;
            }
            bufferedWriter = new BufferedWriter(new FileWriter(errorFilePath + errorFileName, true));
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", FilesUtility.class.getName(), ex.toString());
            return false;
        }
    }
}
