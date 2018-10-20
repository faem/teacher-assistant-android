package net.net23.fahimabrar.teacherassistant;

/**
 * Created by fahim on 3/1/2017.
 */

public class Subject {
    private String subjectName;
    private String deptName;
    private String type;
    private String semester;
    private String subjectId;
    private String credit;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private String section;

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }


    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }


    public String getSubjectName() {

        return subjectName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getType() {
        return type;
    }

    public String getSemester() {
        return semester;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
