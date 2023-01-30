package bitp3453.b032110463.spms_mobile.Fragment;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.Model.Answer;
import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.Model.Attachment;
import bitp3453.b032110463.spms_mobile.Model.Question;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.FragmentAssessmentListBinding;
import bitp3453.b032110463.spms_mobile.databinding.FragmentQuestionBinding;

public class QuestionAttempt extends Fragment {
    FragmentQuestionBinding binding;
    private Question question;
    private String questionTitle;
    private Answer currentSelected;
    private Assessment assessment;
    private DateTime startTime;
    private View.OnClickListener nextListener;
    private View.OnClickListener prevLister;
    public QuestionAttempt(String questionTitle, Question question, Assessment assessment, View.OnClickListener next, View.OnClickListener prev) {
        // Required empty public constructor
        this.questionTitle = questionTitle;
        this.question = question;
        this.assessment = assessment;
        nextListener = next;
        prevLister = prev;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentSelected = new Answer();
        binding = FragmentQuestionBinding.inflate(inflater,container,false);
        binding.questionText.setText(question.getText());

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        for(Attachment att :  question.getAttachments()){
            Log.d("att", "o"+att.getId());
            ImageView tmpimg = new ImageView(getContext());
            Glide.with(getContext()).load(SPMSRequest.serverUrl + "res/images/attachment/"+att.getId()+".png").into(tmpimg);
            binding.qAttachmentContainer.addView(tmpimg);
        }
        GradientDrawable border= new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        for(Answer ans : question.getAnswers()){
            RadioButton newRad = new RadioButton(getContext());
            newRad.setText(ans.getText());
            newRad.setId(View.generateViewId());

            newRad.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            newRad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("selected",String.valueOf( ans.getNo()));
                    currentSelected = ans;
                }
            });
            RadioGroup.LayoutParams radparam = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            if(ans.getAttachmentId() != 0) {
                ImageView ansAtt = new ImageView(getContext());
                Glide.with(getContext()).load(SPMSRequest.serverUrl + "res/images/attachment/" + ans.getAttachmentId() + ".png").
                        into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                Log.d("lukis", "ss");
                                newRad.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resource);

                            }
                        });
            }
            radparam.bottomMargin = 10;
            binding.qRadgroup.addView(newRad,radparam);
            if(ans.getNo() == question.getChoosenAnserNo()){
                Log.d("ANS LOD MCQ","S");
                newRad.setChecked(true);
                currentSelected = ans;
            }

        }
        if(question.getType() == 1){//short text
            binding.edtAnswer.setVisibility(View.VISIBLE);
            binding.edtAnswer.setText(question.getAnswerText());
        }
        if(question.getType() == 0){
            binding.qAnswerContainer.setVisibility(View.VISIBLE);
        }
        binding.questionTitle.setText(questionTitle);
        startTime = DateTime.now();
        if(nextListener != null){
            binding.buttonNext.setVisibility(View.VISIBLE);
            binding.buttonNext.setOnClickListener(nextListener);
        }
        if(prevLister != null){
            binding.buttonPrev.setVisibility(View.VISIBLE);
            binding.buttonPrev.setOnClickListener(prevLister);
        }
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            submitAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitAnswer() throws JSONException {
        JSONObject submitbody = new JSONObject();
        Boolean changes = false;
        if(question.getType() == 0){//mcq
            if(question.getChoosenAnserNo() != currentSelected.getNo()){
                if(currentSelected.getNo() != -1){
                    submitbody.put("ansNo",currentSelected.getNo());
                    changes = true;
                    Log.d("changed mcq",""+ currentSelected.getNo());
                }
            }
        }
        if(question.getType() == 1){//short text
            if(question.getAnswerText() != binding.edtAnswer.getText().toString()){
                if(!binding.edtAnswer.getText().toString().isEmpty()){
                    submitbody.put("ansText",binding.edtAnswer.getText().toString());
                    changes =true;
                    Log.d("Changed short text","" + binding.edtAnswer.getText().toString());
                }
            }
        }
        if(changes){
            int secondElapsed = 0;
            secondElapsed = Seconds.secondsBetween(startTime,DateTime.now()).getSeconds();
            startTime = DateTime.now();//move start to current for next2 calculcation
            submitbody.put("time",secondElapsed);
            submitbody.put("assessmentId",assessment.getAssessmentId());
            submitbody.put("questionId",question.getId());
            submitbody.put("questionType",question.getType());
            SPMSRequest submit = new SPMSRequest(((SPMSActivity) getActivity()).getToken(), Request.Method.POST, "api/assessment/submit",
                    submitbody, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Submitted","ANS");
                    try {

                        if(question.getType() == 0) {
                            question.setChoosenAnserNo(submitbody.getInt("ansNo"));
                        }
                        if(question.getType() == 1){
                            question.setAnswerText(submitbody.getString("ansText"));
                        }
                    } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((SPMSActivity)getActivity()).getSwal().showError("Error","Unable to submit answer");
                }
            });
            Log.d("body",submitbody.toString());
            ((SPMSActivity)getActivity()).getReqQueue().add(submit);
        }
    }
}