package bitp3453.b032110463.spms_mobile.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Vector;

public class Question implements Serializable {
    private int id;
    private String text;
    private int type;
    private Vector<Answer> answers;
    private Vector<Attachment> attachments;
    private String answerText;
    private int choosenAnserNo;

    public Question(){

    }
    public  Question(JSONObject jsonObj) throws JSONException {
        answers = new Vector<>();
        attachments = new Vector<>();
        answerText = "";
        choosenAnserNo = -1;
        if(jsonObj.has("questionId")){
            setId(jsonObj.getInt("questionId"));
        }
        if(jsonObj.has("questionText")){
            setText(jsonObj.getString("questionText"));
        }
        if(jsonObj.has("questionType")){
            setType(jsonObj.getInt("questionType"));
        }
        if(jsonObj.has("answer")){
            JSONArray tmpans = jsonObj.getJSONArray("answer");
            for(int i=0; i < tmpans.length();i++){
                int attachmentId = tmpans.getJSONObject(i).isNull("attachmentId")?0: tmpans.getJSONObject(i).getInt("attachmentId") ;
                Log.d("att","s" + attachmentId);
                Log.d("te",tmpans.getJSONObject(i).toString());
                //Answer tt = new Answer(tmpans.getJSONObject(i).getInt("answerNo"),tmpans.getJSONObject(i).getString("answerText"), attachmentId);
                answers.add(new Answer(tmpans.getJSONObject(i).getInt("answerNo"),tmpans.getJSONObject(i).getString("answerText"), attachmentId));
            }
        }
        if(jsonObj.has("attachmentIds")){
            if(!jsonObj.isNull("attachmentIds")){
                Log.d("te","sini ke");
                String [] ids = jsonObj.getString("attachmentIds").split(",");
                for(String id : ids){
                    attachments.add(new Attachment(Integer.parseInt(id),""));
                }
            }
        }
        if(jsonObj.has("answerText")){
            if(!jsonObj.isNull("answerText")){
                setAnswerText(jsonObj.getString("answerText"));
            }
        }
        if(jsonObj.has("choosenAnswerNo")){
            if(!jsonObj.isNull("choosenAnswerNo")){
                setChoosenAnserNo(jsonObj.getInt("choosenAnswerNo"));
            }
        }
    }
    public int getId() {
        return id;
    }

    public Vector<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Vector<Answer> answers) {
        this.answers = answers;
    }

    public Vector<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Vector<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getChoosenAnserNo() {
        return choosenAnserNo;
    }

    public void setChoosenAnserNo(int choosenAnserNo) {
        this.choosenAnserNo = choosenAnserNo;
    }

}
