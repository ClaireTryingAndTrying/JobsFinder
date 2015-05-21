package model;
// Generated May 19, 2015 10:36:16 PM by Hibernate Tools 4.3.1

/**
 * UserInfo generated by hbm2java
 */
public class UserInfo implements java.io.Serializable {

    private String id;
    private String firstname;
    private String lastname;
    private Integer age;
    private String skills;
    private String resume;

    public UserInfo() {
    }

    public UserInfo(String id) {
        this.id = id;
    }

    public UserInfo(String id, String firstname, String lastname, Integer age, String skills, String resume) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.skills = skills;
        this.resume = resume;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSkills() {
        return this.skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getResume() {
        return this.resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

}