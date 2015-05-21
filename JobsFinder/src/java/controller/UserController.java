/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package controller;

import dao.JobDAO;
import dao.JobSearchDAO;
import dao.UserDAO;
import email.EmailNotifier;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Constant;
import model.Job;
import model.User;
import model.UserInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import spider.FilesUtility;
import spider.JobItemFlag;
import spider.SpiderMain;

/**
 * @author RuiBi Yuening Li
 */
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

    /**
     * Get Login
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLoginPage(ModelMap model) {
        //model.put("user", new User());
        return "login";
    }

    /**
     * Get profile
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String toProfilePage(@ModelAttribute(value = "user") User user, ModelMap model, HttpSession session) {
        model.put("user", new User());
        String id = (String) session.getAttribute("userid");
        UserDAO userDAO = new UserDAO();
        UserInfo userInfo = userDAO.getUserProfile(id);
        if (userInfo != null) {
            model.addAttribute("lname", userInfo.getLastname());
            model.addAttribute("fname", userInfo.getFirstname());
            model.addAttribute("age", userInfo.getAge());
            model.addAttribute("skill", userInfo.getSkills());
        }

        return "profile";
    }

    /**
     * Get crawler
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "crawler", method = RequestMethod.GET)
    public String toCrawlerPage(ModelMap model, HttpSession session) {
        model.put("user", new User());
//        //get the root path of the project, where all those files are
        ServletContext context = session.getServletContext();
        String projectRootPath = context.getRealPath("/");
        projectRootPath = projectRootPath.replaceAll("(.+EBizTermProject).+", "$1");
        projectRootPath += "/";
        SpiderMain spiderMain = new SpiderMain();
        spiderMain.setErrorFilePath(projectRootPath);
        spiderMain.setTempFilePath(projectRootPath);
        spiderMain.setErrorFilePath(projectRootPath);
        spiderMain.setJobsFilePath(projectRootPath);
        JobItemFlag.setRefSourceFilePath(projectRootPath);
        FilesUtility.setErrorFilePath(projectRootPath);
        FilesUtility.setTempFilePath(projectRootPath);
        spiderMain.startSpider();
        return "post";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute(value = "user") User user, ModelMap model, HttpSession session) {
        UserDAO userdao = new UserDAO();
        user = userdao.login(user.getName(), user.getPassword());
        if (user != null) {
            session.setAttribute("name", user.getName());
            session.setAttribute("userid", user.getId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("password", user.getPassword());
            UserInfo userInfo = userdao.getUserProfile(user.getId());
            if (userInfo != null) {
                model.addAttribute("lname", userInfo.getLastname());
                model.addAttribute("fname", userInfo.getFirstname());
                model.addAttribute("age", userInfo.getAge());
                model.addAttribute("skill", userInfo.getSkills());
            }
            return "profile";
        } else {
            model.put("message", "Invalid");
            return "login";
        }
    }

    /**
     * Register
     *
     * @param model
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@ModelAttribute(value = "user") User user, ModelMap model, HttpSession session) {
        UserDAO userdao = new UserDAO();
        int op = userdao.insertOrUpdateUser("register", user);
        if (op == Constant.REG_SUCCESS) {
            session.setAttribute("name", user.getName());
            session.setAttribute("userid", user.getId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("password", user.getPassword());
            return "profile";
        } else {
            if (op == Constant.USERNAME_DUPLICATE) {
                userdao.close();
                return "login";
            } else {
                model.put("message", "Register Fail");
                userdao.close();
                return "login";
            }
        }
    }

    /**
     * Update
     *
     * @param model
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@ModelAttribute(value = "user") User user, ModelMap model, HttpSession session) {
        UserDAO userdao = new UserDAO();
        user.setId((String) session.getAttribute("userid"));
        userdao.insertOrUpdateUser("update", user);
        session.setAttribute("name", user.getName());
        session.setAttribute("userid", user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("password", user.getPassword());
        return "profile";
    }

    /**
     * update profile
     *
     * @param model
     * @param userinfo
     * @param session
     * @return
     */
    @RequestMapping(value = "updateProfile", method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute(value = "userinfo") UserInfo userinfo, ModelMap model, HttpSession session) {
        userinfo.setId((String) session.getAttribute("userid"));
        UserDAO userdao = new UserDAO();
        userdao.setUserProfile(userinfo);
        if (userinfo != null) {
            model.addAttribute("lname", userinfo.getLastname());
            model.addAttribute("fname", userinfo.getFirstname());
            model.addAttribute("age", userinfo.getAge());
            model.addAttribute("skill", userinfo.getSkills());
        }
        return "profile";
    }
//    @RequestMapping(value = "postJob", method = RequestMethod.POST)
//    public String post(@ModelAttribute(value = "user") User user, ModelMap model, HttpSession session){
//        UserDAO userdao = new UserDAO();
//        user = userdao.login(user.getName(), user.getPassword());
//        if (user != null) {
//            session.setAttribute("name", user.getName());
//            session.setAttribute("userid", user.getId());
//            session.setAttribute("email", user.getEmail());
//            session.setAttribute("password", user.getPassword());
//            return "post";
//        } else {
//            model.put("message", "Invalid");
//            return "login";
//        }
//    }

    /**
     * insert job
     *
     * @param model
     * @param job
     * @param session
     * @return
     */
    @RequestMapping(value = "insertJob", method = RequestMethod.POST)
    public String insertJob(@ModelAttribute(value = "job") Job job, ModelMap model, HttpSession session) {
        JobDAO jobdao = new JobDAO();
        UserDAO userdao = new UserDAO();
        jobdao.saveJob(job);
        jobdao.close();
        //EmailNotifier emailNotifier = new EmailNotifier();
        // String mailSubject = "Job ALert:" + job.getTitle();
        //String mailText = "Title:" + job.getTitle() + "\nCompany:" + job.getCname() + "\nDescription:" + job.getDescription();
        List<User> users = userdao.getAllUsers();
        //for (int i = 0; i < users.size(); i++) {
//            emailNotifier.sendMail(users.get(i).getEmail(), mailSubject, mailText);
//        }
        userdao.close();
        return "post";
    }

    /**
     * search job
     *
     * @param model
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String searchJob(@RequestParam("keyword") String keyword, @RequestParam("location") String location, @RequestParam("cname") String cName, ModelMap model, HttpSession session) {
        JobSearchDAO jobSearchDAO = new JobSearchDAO();
        List<Job> jobList = jobSearchDAO.searchByCondition(keyword, location, cName);
        model.put("jobList", jobList);
        return "search";
    }

    /**
     * query file data
     *
     * @param file
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/upload.do")
    public String queryFileData(@RequestParam("uploadfile") CommonsMultipartFile file, HttpServletRequest request, HttpSession session) {
        // MultipartFile是对当前上传的文件的封装，当要同时上传多个文件时，可以给定多个MultipartFile参数(数组)
        if (!file.isEmpty()) {
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));// 取文件格式后缀名
            String filename = System.currentTimeMillis() + type;// 取当前时间戳作为文件名
            String path = request.getSession().getServletContext().getRealPath("/upload/" + filename);// 存放位置
            UserDAO userdao = new UserDAO();
            userdao.setPath((String) session.getAttribute("userid"), path);
            File destFile = new File(path);
            try {
                // FileUtils.copyInputStreamToFile()这个方法里对IO进行了自动操作，不需要额外的再去关闭IO流
                FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);// 复制临时文件到指定目录下
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "profile";
        } else {
            return "profile";
        }
    }

}
