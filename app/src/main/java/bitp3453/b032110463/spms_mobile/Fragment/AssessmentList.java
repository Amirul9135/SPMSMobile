package bitp3453.b032110463.spms_mobile.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

import bitp3453.b032110463.spms_mobile.AssessmentAttempt;
import bitp3453.b032110463.spms_mobile.Classes.AssessmentAdapter;
import bitp3453.b032110463.spms_mobile.Classes.AssessmentClick;
import bitp3453.b032110463.spms_mobile.Classes.LoadingOverlay;
import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.databinding.FragmentAssessmentListBinding;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AssessmentList extends Fragment {
    FragmentAssessmentListBinding binding;

    private JWT jwt;
    private AssessmentAdapter asAdapter;
    private Vector<Assessment> assessments;
    private int statusCode = 1;//default fallback satu
    private static String [] statusName = {"upcoming","ongoing","past"};
    public AssessmentList(int statusCode) {
        this.statusCode = statusCode;
        // Required empty public constructor
    }

    public static AssessmentList newInstance(String param1, String param2) {
        AssessmentList fragment = new AssessmentList(1);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jwt = ((SPMSActivity)getActivity()).getToken();
        Log.d("token frag",">>"+jwt.getToken());
        SPMSRequest req = new SPMSRequest(jwt, Request.Method.GET, "api/assessment/?status="+ statusName[statusCode] , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for(int i=0; i < arr.length();i++){
                        assessments.add(new Assessment( arr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                asAdapter.notifyDataSetChanged();
                Log.d("here", assessments.size()+ "|"+response);
                binding.txtListtitle.setText("Assessments ("+statusName[statusCode]+") : " + assessments.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err",error.getMessage());
                ((SPMSActivity)getActivity()).getSwal().showError("Error","unable to load assessment list");
            }
        });
        ((SPMSActivity)getActivity()).getReqQueue().add(req);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAssessmentListBinding.inflate(inflater,container,false);
        assessments = new Vector<>();
        if(statusCode == 1){//ongoing assessment click start attempt
            asAdapter = new AssessmentAdapter(assessments,getActivity(),new AssessmentClick(){
                @Override
                public void clickAssessment(Assessment assessment) {
                    Log.d("click","h");
                    ((SPMSActivity)getActivity()).getSwal().confirm("Start Attempt?", "",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Log.d("Start",assessment.getTitle());
                                    LoadingOverlay loader = new LoadingOverlay(getActivity());
                                    loader.show();
                                    try {
                                        JSONObject reqBody = new JSONObject();
                                        reqBody.put("assessmentId",  assessment.getAssessmentId());
                                        SPMSRequest startAssessment = new SPMSRequest(((SPMSActivity) getActivity()).getToken(), Request.Method.POST,
                                                "api/assessment/startAttempt",
                                               reqBody
                                                , new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                loader.dismiss();
                                                Log.d("Started",assessment.getTitle());
                                                Intent stAs = new Intent(getActivity(), AssessmentAttempt.class);
                                                stAs.putExtra("token",((SPMSActivity)getActivity()).getToken());
                                                stAs.putExtra("assessment",assessment);
                                                startActivity(stAs);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                loader.dismiss();
                                                ((SPMSActivity)getActivity()).getSwal().showError("Failed to Start","Unable to start assessment your allowed attempt time might have ended");

                                            }
                                        });
                                        ((SPMSActivity)getActivity()).getReqQueue().add(startAssessment);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        loader.dismiss();
                                    }
                                }
                            });
                }
            });
        }
        else if(statusCode == 2){//past click dialog result
            asAdapter = new AssessmentAdapter(assessments, getActivity(), new AssessmentClick() {
                @Override
                public void clickAssessment(Assessment assessment) {
                    SPMSRequest reqmark = new SPMSRequest(jwt, Request.Method.GET, "api/assessment/sumMark?asid=" + assessment.getAssessmentId(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Succ","r:" +response);
                            try {
                                JSONObject res = new JSONObject(response);
                                String grade = "ungraded";
                                double tmark = res.getDouble("totalMark");
                                double fmark = res.getDouble("fullMark");
                                if(res.has("grading")){
                                    if(!res.isNull("grading")){
                                        double percent = tmark / fmark * 100;
                                        JSONArray grading = res.getJSONArray("grading");
                                        for(int i=0 ; i < grading.length();i++){
                                            if(percent >= grading.getJSONObject(i).getDouble("start") && percent <= grading.getJSONObject(i).getDouble("end")){
                                                grade = grading.getJSONObject(i).getString("label");
                                            }
                                        }
                                    }
                                }
                                ((SPMSActivity)getActivity()).getSwal().showMessage(assessment.getTitle(),"Mark: "+tmark+"/"+
                                        fmark+" ["+grade+"]");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((SPMSActivity)getActivity()).getSwal().showError("Error","Unable to load assessment details");
                        }
                    });
                    ((SPMSActivity)getActivity()).getReqQueue().add(reqmark);
                }
            });
        }
        else{
            asAdapter = new AssessmentAdapter(assessments);//simple
        }
        binding.rcvHomeAssessment.setAdapter(asAdapter);
        binding.rcvHomeAssessment.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        binding.txtListtitle.setText("Assessments ("+statusName[statusCode]+") : 0");

        return binding.getRoot();
    }
}