# JobsFinder Website

In the final project, a web spider is designed and programed which can crawl job information from target websites like GlassDoor. In order to manage the data crawled by the spider and to realize real time job posting and searching, a job management system named Job Hunter is built to operate the spider, upload job data from xml file to database, search job items by keywords, and post/edit/delete job items.

The core components of this project are the web spider, Hibernate and Spring MVC structure, of which the principle of design is that based on information provided by commercial job hunting websites, like GlassDoor, the web spider crawls job information and aggregate it to build a jobs.xml that stores sufficient information of every job items published by commercial websites.
##FrameWork and Relationship
![alt text](https://lh3.googleusercontent.com/-bLy2tIXQWug/VV1qs0P75JI/AAAAAAAAAIU/xkj5SfsHsHw/w958-h489-no/workflow.jpg)

## Functions and Data Flow
![alt text](https://lh6.googleusercontent.com/-00kdal7_Ipk/VV1vWS_1vKI/AAAAAAAAAIs/nlKfXNY3lNY/w823-h430-no/functions.jpg)

## Hibernate Framework Implementation 
###1. Creating Hibernate Configuration File
Here are two patterns of the configuration file: The first one is based on Java, called *.properties. This config file is mainly focused on defining the parameters of database connection. The Second one is based on XML. This config file not only set parameters for connecting different database, but also define the mapping file used by procedure. 

Here is the sample configuration profile for hibernate, and you can find the configure profile in the directory of "JobsFinder/src/java".
```xml 
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
  <session-factory>
    <property name="hibernate.dialect">org.hibernate.dialect.DerbyDialect</property>
    <property name="hibernate.connection.driver_class">org.apache.derby.jdbc.ClientDriver</property>
    <property name="hibernate.connection.url">jdbc:derby://localhost:1527/EBizTermProjectDB</property>
    <property name="hibernate.connection.username">test</property>
    <property name="hibernate.connection.password">test</property>
    <property name="hibernate.show_sql">true</property>
    <mapping resource="hibernate/mapping/UserJob.hbm.xml"/>
    <mapping resource="hibernate/mapping/Users.hbm.xml"/>
    <mapping resource="hibernate/mapping/UserInfo.hbm.xml"/>
    <mapping resource="hibernate/mapping/Job.hbm.xml"/>
  </session-factory>
</hibernate-configuration>
```
Here is the hibernate-reverse-engineering file. A hibernate.reveng.xml file can provide a finer degree of control of the reverse engineering process. In this file you can specify type mappings and table filtering.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-reverse-engineering PUBLIC "-//Hibernate/Hibernate Reverse Engineering DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-reverse-engineering-3.0.dtd">
<hibernate-reverse-engineering>
  <schema-selection/>
  <table-filter match-name="USER_JOB"/>
  <table-filter match-name="USER_INFO"/>
  <table-filter match-name="USERS"/>
  <table-filter match-name="JOB"/>
</hibernate-reverse-engineering>
```

###2. Mapping and Build Models
In our project, we built the basic mapping by createing concrete classes (in our project, we called them as model) according to the structure of database. You can find all models in our project under the directory of "JobsFinder/src/java/model". 

Here is the sample code of user model
```java
public class User implements java.io.Serializable {

    private String id;
    private String name;
    private String password;
    private String email;
    private String role;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //...
    //you can visit all code in my project
}
```
For the hibernate implementation, the model building mentioned above is not enough. We need one more XML file to ensure the mapping relationship. And you can find all mapping xml file from the directory of "JobsFinder/src/java/hibernate/mapping". 

Here is the sample code of User model
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<!-- Generated May 19, 2015 10:36:16 PM by Hibernate Tools 4.3.1 -->
<hibernate-mapping>
    <class name="model.User" optimistic-lock="version" schema="TEST" table="USERS">
        <id name="id" type="string">
            <column name="ID"/>
            <generator class="assigned"/>
        </id>
        <property name="name" type="string">
            <column name="NAME"/>
        </property>
        <property name="password" type="string">
            <column name="PASSWORD"/>
        </property>
        <property name="email" type="string">
            <column name="EMAIL"/>
        </property>
        <property name="role" type="string">
            <column name="ROLE"/>
        </property>
    </class>
</hibernate-mapping>
```
###3. Access Database through DAO (Data Access Objects)
In fact, hibernate encapsulates the JDBC implementation, and Data Access Objects (or DAOs for short) are used as a direct line of connection and communication with our database. DAOs are used when the actual CRUD (CRUD = Create, Read, Update, Delete) operations are needed and invoked in our Java code. Also Spring framework provides a good support for the implementation of hibernate and DAO. How to support? I will explained in the following file. In our project, we built DAO class based on the hibernate models, and we utilized hiberate to operate database by using DAO layer. You can find all DAO class in the "JobsFinder/src/java/dao" directory.

Here is the sample code of UserDAO.
```java
public class UserDAO {

    Session session = null;

    public UserDAO() {
        this.session = HibernateUtils.getSessionFactory().openSession();
    }

    public User login(String name, String password) {
        User userinfo;
        try {
            Query q = session.createQuery("from User as userinfo where userinfo.name = '" + name + "' and userinfo.password = '" + password + "'");
            userinfo = new User();
            userinfo = (User) q.uniqueResult();
            return userinfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * check duplicate user
     *
     * @param name
     * @return
     */
    public boolean checkUsernameDuplicate(String name) {
        try {
            Query q = session.createQuery("from User as userinfo where userinfo.name = '" + name + "'");
            if (q.list().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * update user
     *
     * @param method
     * @param userinfo
     * @return
     */
    public int insertOrUpdateUser(String method, User userinfo) {
        org.hibernate.Transaction tx = null;
        userinfo.setId(String.valueOf(userinfo.hashCode()));
        try {
            tx = session.beginTransaction();
            if (checkUsernameDuplicate(userinfo.getName()) && method.equals("register")) {
                return Constant.USERNAME_DUPLICATE;
            }
            if (method.equals("update")) {
                String hql = "update User set name=:name, email=:email, password=:password where id=:id ";
                Query q = session.createQuery(hql);
                q.setParameter("name", userinfo.getName());
                q.setParameter("id", userinfo.getId());
                q.setParameter("email", userinfo.getEmail());
                q.setParameter("password", userinfo.getPassword());
                int result = q.executeUpdate();
                tx.commit();
                session.close();
                return result;
            }
            session.saveOrUpdate(userinfo);
            tx.commit();
            session.close();
            return Constant.REG_SUCCESS;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            tx.rollback();
            return Constant.USER_INSERT_ERROR;
        } catch (Exception e) {
            tx.rollback();
            return Constant.UNKNOWN_ERROR;
        }
    }
    //....
    //you can visit all code in my project.
}
```
## Spring MVC
###1. Configure Environment for Spring MVC
Copied important libraries of Spring MVC under "JobsFinder/web/WEB-INF/lib" directory. And we modified the web.xml and configure DispatcherServlet. In fact, dispatcher servlet is a front controller, which is configured in the file of web.xml. It is going to control where all our requests are routed based on information we will enter at a later point. This servlet definition also has an attendant <servlet-mapping/> entry that maps to the URL patterns that we will be using. We have decided to let any URL with '.html', '.css', '.js' and '.do' extensions be routed to the 'dispatcher-servlet' servlet. 

Next, we created a file called 'dispatcher-servlet.xml' in the 'JobsFinder/web/WEB-INF' directory. This file contains the bean definitions (plain old Java objects) used by the DispatcherServlet.

Here is the sample code of the configuration of web.xml.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>2</load-on-startup>
    </servlet>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/dispatcher-servlet.xml,
            /WEB-INF/applicationContext.xml
        </param-value>
    </context-param>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>*.html</url-pattern>
        <url-pattern>*.css</url-pattern>
        <url-pattern>*.js</url-pattern>
        <url-pattern>*.do</url-pattern>  
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
    <welcome-file-list>
        <welcome-file>redirect.jsp</welcome-file>
    </welcome-file-list>
</web-app>

```
Here is the sample code of the configuration of dispatcher-servlet.xml.
```xml
<?xml version='1.0' encoding='UTF-8' ?>
<!-- was: <?xml version="1.0" encoding="UTF-8"?> -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">
    <bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping"/>
    <!-- bean class="controller.UserController" id="userKey"/-->
    <!--Most controllers will use the ControllerClassNameHandlerMapping above, but
    for the index controller we are using ParameterizableViewController, so we must
    define an explicit mapping for it.-->
    <bean id="urlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="index.html">indexController</prop>
            </props>
        </property>
    </bean>
    
    <bean id="viewResolver"
          class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          p:prefix="/WEB-INF/jsp/"
          p:suffix=".jsp" />
    
    <!--The index controller.-->
    <bean name="indexController"
          class="org.springframework.web.servlet.mvc.ParameterizableViewController"
          p:viewName="index" />
    
    <!--multipartResolver-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="UTF-8" />
        <property name="maxUploadSize" value="999999" />
    </bean>
    
    <bean id="exceptionResolver"
          class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <property name="exceptionMappings">
            <props>
                <!-- meet the exception of MaxUploadSizeExceededException, jump to the page-->
                <prop
                    key="org.springframework.web.multipart.MaxUploadSizeExceededException">jump pages</prop>
            </props>
        </property>
    </bean>
</beans>

```
###2. Create Controller, Create View and their transaction.
In our project, we created a controller. The controller is mainly responsible for all requests sent by dispacher servlet. Controller processes the user requests as a model through service processing layer, and it returns this model to the related view(.jsp page) for showing up. We used @Controller annotate a class as controller, and used @RequestMapping  or @RequestParam annotations to define the URL requests and to enture the mapping to controller. Thus, the controller can be visited by service. And you can visit the controller from the path of "JobsFinder/src/java/controller". 

Here is the sample code of controller
```java
@Controller
public class UserController {

    /**
     * Get index
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.put("user", new User());
        return "login";
    }

    /**
     * Get search
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String toSearchPage(ModelMap model) {
        model.put("user", new User());
        return "search";
    }

    /**
     * Get post
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "post", method = RequestMethod.GET)
    public String toPostPage(ModelMap model) {
        model.put("user", new User());
        return "post";
    }
    //...
    //you can visit all code of controller in my project.
}
```
## Spider
The degree of sufficiency of the crawled information depends on the number of available information provided by commercial websites. Since every kind of website, like GlassDoor, Indeed etc., has its own pattern, it needs to implement different analyzing/parsing procedure for every kind. Due to the time reason, in this project, only GlassDoor is analyzed as crawling source.

###1. Create HttpLevel1Handler to control the parsing process
HttpLevel1Handler is a core controller for spider process. This handler reads URLs from ref_source.txt, and in this project we only stored one URL (www.glassdoor.com) in ref_source.txt. By doing the deapth first search, HttpLevel1Handler parses level_1 URL on the webpage, and continously find next level_1 URL in the same page if it has.  And HttpLevel1Handler initiates the spider which schedules web crawlers to parse Level_2 URLs.

Here is the sample code of HttpLevel1Parser.
```java
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
```

###2.The parsing process (HttpLevel1Parser and HttpLevel2Parser) 
The parsing process will be called by HttpLevel1Handler. All parsed information will be saved in temp.txt during the process of HttpLevel1Parser and HttpLevel2Parser class.

Here is the sample code of HttpLevel1Parser
```java
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
```

###3. Create job.xml file storing parsed information
We read crawled information from temp.txt, and then write them to job.xml. This process is actually to build a new XML file by calling XmlFileMaker class. XmlFileMaker class is responsible for decoding tag names from temp.txt and encode a new tag name into jobs.xml.

Here is the sample code of XmlFileMaker.
```java
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

```
###4. Input information of jobs.xml to database
To complete the database import, we used the external API called SAXParserFactory. This API is used to configure and obtain a SAX based parser to parse XML documents.And by calling DBHandler, we wrote the content of the file into database. In this process, we also sent DAO objects to manage hibernate to operate database. In our project, we sent a job DAO and updated the job table in our database through hibernate operation.

Here is the sample code for DBFileHandler.
```java
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

```
## UI Presentation
###1. Index page
![alt text](https://lh4.googleusercontent.com/-uA0Jku-C4qo/VV1qsZzyE2I/AAAAAAAAAH8/rr68vMmgVB8/w958-h460-no/Screen%2BShot%2B2015-05-20%2Bat%2B8.16.43%2BPM.png)

###2. SignUp page
![alt text](https://lh5.googleusercontent.com/-ly95ggKqUAw/VV1qsLVAW0I/AAAAAAAAAIA/zV94twi4wfc/w957-h457-no/Screen%2BShot%2B2015-05-20%2Bat%2B8.17.25%2BPM.png)

###3. Profile page
![alt text](https://lh3.googleusercontent.com/-V4Xp-1I9le4/VV1qsW01YNI/AAAAAAAAAIM/0HDVCa_HUcM/w957-h451-no/Screen%2BShot%2B2015-05-20%2Bat%2B8.18.19%2BPM.png)

###4. Post page
![alt text](https://lh6.googleusercontent.com/-EM5n0ZDLlAU/VV1qsjvEGsI/AAAAAAAAAII/P5cvPciV9PQ/w958-h462-no/Screen%2BShot%2B2015-05-20%2Bat%2B8.21.32%2BPM.png)

###5. Search page
![alt text](https://lh3.googleusercontent.com/-2KII_RpEc-k/VV1qsnHbG1I/AAAAAAAAAIY/kpVooTCgTBc/w957-h484-no/Screen%2BShot%2B2015-05-20%2Bat%2B9.03.57%2BPM.png)

###Thank you for view my project. If you have any questions or advice, please feel free to contact me via email (yueningli4949@gmail.com)
