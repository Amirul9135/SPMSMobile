package bitp3453.b032110463.spms_mobile.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Assessment implements Serializable {
    private int assessmentId;
    private String title;
    private String description;
    private String open;
    private String close;
    private int duration;
    private String subject;
    private JSONObject grading;

    public Assessment(){

    }
    public Assessment(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }
    public Assessment(JSONObject jsonObj) throws JSONException {
        if(jsonObj.has("assessmentId")){
            assessmentId = jsonObj.getInt("assessmentId");
        }
        if(jsonObj.has("title")){
            title = jsonObj.getString("title");
        }
        if(jsonObj.has("description")){
            description = jsonObj.getString("description");
        }
        if(jsonObj.has("open")){
            open = jsonObj.getString("open");
        }
        if(jsonObj.has("close")){
            close = jsonObj.getString("close");
        }
        if(jsonObj.has("duration")){
            duration = jsonObj.getInt("duration");
        }
        if(jsonObj.has("subject")){
            setSubject(jsonObj.getString("subject"));
        }
        if(jsonObj.has("grading") && !jsonObj.isNull("grading")){
            setGrading(jsonObj.getJSONObject("grading"));
        }
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public JSONObject getGrading() {
        return grading;
    }

    public void setGrading(JSONObject grading) {
        if(grading != null){
            this.grading = grading;
        }
    }
}
