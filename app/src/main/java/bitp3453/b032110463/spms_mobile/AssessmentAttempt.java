package bitp3453.b032110463.spms_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.Fragment.AssessmentCover;
import bitp3453.b032110463.spms_mobile.Fragment.AssessmentList;
import bitp3453.b032110463.spms_mobile.Fragment.QuestionAttempt;
import bitp3453.b032110463.spms_mobile.Fragment.SweetAlert;
import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.Model.Question;
import bitp3453.b032110463.spms_mobile.databinding.ActivityAssessmentAttemptBinding;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AssessmentAttempt extends AppCompatActivity implements SPMSActivity {
    ActivityAssessmentAttemptBinding binding;
    private Assessment assessment;
    private RequestQueue requestQueue;
    private JWT jwt;
    private SweetAlert swal;
    private FragmentManager frm;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Vector<Question> questions;
    private NavigationView nav;
    private String startTIme;
    private String endTime;
    private DateTime realEnd;
    private AssessmentCover coverFrag;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssessmentAttemptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startTIme = "";
        endTime = "";
        jwt = (JWT) getIntent().getSerializableExtra("token");
        assessment = (Assessment) getIntent().getSerializableExtra("assessment");
        if(assessment ==null){
            Intent back = new Intent(this,MainActivity.class);
            startActivity(back);
        }

        requestQueue = Volley.newRequestQueue(this);
        questions = new Vector<>();

        nav = binding.navInAssessment;
        final Menu menu = nav.getMenu();
        menu.add(0,-1,0,"Assessment Details");
        SubMenu submnu = menu.addSubMenu("Questions");
        menu.add(0,-2,0,"Finish Attempt");

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("id","itemid" + item.getItemId());

                int id = item.getItemId();
                if(id == -1){
                    frm.beginTransaction().replace(binding.mainContent.getId(),coverFrag).commit();
                }
                if(id == -2){
                    swal.confirm("Submit and End attempt?", "You will no longer be able to change your answer", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Log.d("Fini","ish");
                            sweetAlertDialog.dismiss();
                            try {
                                finishAttempt();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if(id >=0 && id < questions.size()){
                    Log.d("selected",questions.get(id).getText());
                    openQuestion(id);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        //load assessment detail
        SPMSRequest reqPaper = new SPMSRequest(jwt, Request.Method.GET, "api/assessment/paper?asId=" + assessment.getAssessmentId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("loaded",response);
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray questionList = responseObj.getJSONArray("questions");
                            JSONObject timing = responseObj.getJSONObject("atTime");
                            startTIme = timing.getString("startAttempt");
                            endTime = timing.getString("endAttempt");
                            realEnd = new DateTime(timing.getString("realEnd"));
                            Log.d("rend","" + realEnd.getMillis());
                            Log.d("now",""+ DateTime.now().getMillis());
                            if(realEnd.getMillis() <= DateTime.now().getMillis()){
                                swal.showError("Error","Cannot access assessment, attempt ended");
                                Intent back = new Intent(getApplicationContext(),MainActivity.class);
                                back.putExtra("token",jwt);
                                Timer t= new Timer();
                                t.schedule(new TimerTask() {

                                    @Override
                                    public void run() {
                                        startActivity(back);
                                    }
                                }, 1000);
                            }
                            long milistoend = realEnd.getMillis() - DateTime.now().getMillis();
                            Log.d("time to end",""+milistoend);
                            timer = new CountDownTimer(milistoend,1000) {
                                @Override
                                public void onTick(long l) {
                                    binding.txtClock.setText(String.format("%d min, %d sec",
                                            TimeUnit.MILLISECONDS.toMinutes(l),
                                            TimeUnit.MILLISECONDS.toSeconds(l) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                                    ));
                                }

                                @Override
                                public void onFinish() {
                                    try {
                                        finishAttempt();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            timer.start();
                            coverFrag = new AssessmentCover(assessment, startTIme, endTime, questionList.length(), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openQuestion(0);
                                }
                            });
                            frm.beginTransaction().replace(binding.mainContent.getId(),coverFrag).commit();

                            for(int i=0;i < questionList.length();i++){
                                questions.add(new Question(questionList.getJSONObject(i)));
                            }
                            Log.d("loaded","leng" + questions.size());
                            for (int i=0; i < questions.size();i++){
                                submnu.add(0,i,0,"Question " + (i+1));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swal.showError("Failed to load assessment detail","");
                        Intent back = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(back);
                    }
        });
        requestQueue.add(reqPaper);
        //load assessment end

        frm = getSupportFragmentManager();
        swal = new SweetAlert();
        frm.beginTransaction().replace(binding.frgSwal.getId(),swal).commit();



        //drawer setup--------------------------------------

        setSupportActionBar(binding.mytb.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = binding.myDrawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        binding.mytb.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //drawer setup END--------------------------------------
    }

    private void openQuestion(int index){
        View.OnClickListener prevQ = null;
        View.OnClickListener nextQ = null;
        if(index > 0){
            prevQ = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("prev","prevQ");
                    openQuestion(index - 1);
                    //  QuestionAttempt prevFrag = new QuestionAttempt(item.getTitle().toString(),questions.get(id -1),assessment);
                    //frm.beginTransaction().replace(binding.mainContent.getId(),prevFrag).commit();

                }
            };
        }
        if(index < (questions.size() -1)){
            nextQ = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("next","nQ");
                    openQuestion(index + 1);
                }
            };
        }
        QuestionAttempt newFrg = new QuestionAttempt("Question " + (index +1),questions.get(index),assessment, nextQ,prevQ);
        frm.beginTransaction().replace(binding.mainContent.getId(),newFrg).commit();
    }

    @Override
    public JWT getToken() {
        return jwt;
    }

    @Override
    public SweetAlert getSwal() {
        return swal;
    }

    private void finishAttempt() throws JSONException {
        JSONObject reqbody = new JSONObject();
        reqbody.put("assessmentId",assessment.getAssessmentId());
        SPMSRequest finish = new SPMSRequest(jwt, Request.Method.POST, "api/assessment/finishAttempt", reqbody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("token",jwt);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swal.showError("Error","Failed to finish attempt");
            }
        });
        requestQueue.add(finish);
    }

    @Override
    public RequestQueue getReqQueue() {
        return requestQueue;
    }
}