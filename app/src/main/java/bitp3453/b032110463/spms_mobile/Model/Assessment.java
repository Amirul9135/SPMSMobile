package bitp3453.b032110463.spms_mobile.Model;

import android.util.Log;


import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Assessment implements Serializable {
    private int assessmentId;
    private String title;
    private String description;
    private String open;
    private String close;
    private int duration;
    private String subject;//code

    private String subjectTitle;
    private JSONObject grading;
    private DateTime dtOpen;
    private DateTime dtClose;

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
            setOpen(jsonObj.getString("open"));
        }
        if(jsonObj.has("close")){
            setClose(jsonObj.getString("close"));
        }
        if(jsonObj.has("duration")){
            duration = jsonObj.getInt("duration");
        }
        if(jsonObj.has("subject")){
            setSubject(jsonObj.getString("subject"));
        }
        if(jsonObj.has("subjTitle")){
            setSubjectTitle(jsonObj.getString("subjTitle"));
        }
//        if(jsonObj.has("grading")){
//            if(!jsonObj.isNull("grading")){
//                ObjectMapper objectMapper = new ObjectMapper();
//                String unwrapJson = objectMapper.readValue(jsonObj.getString("grading"),String.class);
//                Log.d("unra","json" + unwrapJson);
//            }
//        }
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
        dtOpen = new DateTime(open);
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        dtClose =  new DateTime(close);
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

    public DateTime getOpenDT(){
        return dtOpen;
    }
    public DateTime getCloseDT(){
        return dtClose;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

}
