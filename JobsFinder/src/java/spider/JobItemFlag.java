package spider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Hao Bai This is a "Flag" class to tell parser/crawler classes that
 * whether some job attribute has been acquired from crawling
 */
public class JobItemFlag {

    private static String refSourceFilePath = "./";
    private static String refSourceFileName = "ref_source.txt";
    private static List<String> refSourceURLs = new ArrayList<>();
    private static boolean isFailed;
    //hash table to check if a specific attribute is saved
    private static Hashtable allAttributes;
    //a counter to record the total pages need to crawl
    private static int pageCount;

    public JobItemFlag() {
        isFailed = false;
        allAttributes = new Hashtable();
        allAttributes.put("title", false);
        allAttributes.put("description", false);
        allAttributes.put("requirement", false);
        allAttributes.put("website", false);
        allAttributes.put("location", false);
        allAttributes.put("cName", false);
        allAttributes.put("cType", false);
        allAttributes.put("cDescription", false);
        allAttributes.put("cIndustry", false);
    }

    public static Hashtable getAllAttributes() {
        return allAttributes;
    }

    public static List<String> getRefSourceURLs() {
        return refSourceURLs;
    }

    public static boolean getIsFailed() {
        return isFailed;
    }

    //when there are exceptions parsing htmls, set this to true
    public static void setIsFailed() {
        isFailed = true;
    }

    public static int getPageCount() {
        return pageCount;
    }

    public static void setPageCount(int pageCount) {
        JobItemFlag.pageCount = pageCount;
    }

    public static void setRefSourceFilePath(String refSourceFilePath) {
        JobItemFlag.refSourceFilePath = refSourceFilePath;
    }

    //load the reference websites' URLs from ref_source.txt
    public static boolean loadRefSourceURLs() {
        BufferedReader bufferedReaderFile;
        try {
            bufferedReaderFile = new BufferedReader(new FileReader(refSourceFilePath + refSourceFileName));
            String fileLine;
            while ((fileLine = bufferedReaderFile.readLine()) != null) {
                refSourceURLs.add(fileLine.trim());
            }
            bufferedReaderFile.close();
            return !refSourceURLs.isEmpty();
        } catch (Exception ex) {
            System.out.printf("Class: %s. Exception: %s.\n", JobItemFlag.class.getName(), ex.toString());
            return false;
        }
    }
}
