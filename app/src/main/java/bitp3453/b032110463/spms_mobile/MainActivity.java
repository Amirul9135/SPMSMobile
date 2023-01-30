package bitp3453.b032110463.spms_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.Fragment.AssessmentList;
import bitp3453.b032110463.spms_mobile.Fragment.SweetAlert;
import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SPMSActivity {
    private ActivityMainBinding binding;
    private JWT jwt;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SweetAlert swal;
    private RequestQueue requestQueue;
    private NavigationView navigationView;
    private FragmentManager frm;
    private Fragment mainContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jwt = (JWT) getIntent().getSerializableExtra("token");
//        Log.d("token","TT>"+jwt.getToken());
//        Log.d("token","TTT>"+jwt.getJwtToken());
//        Log.d("token","TP>"+jwt.getJwtPayload());
//        Log.d("token","uid>"+jwt.getUserId());

        requestQueue = Volley.newRequestQueue(this);

        frm = getSupportFragmentManager();
        swal = new SweetAlert();
        frm.beginTransaction().replace(binding.frgSwal.getId(),swal).commit();

        AssessmentList frgAsList = new AssessmentList(1);
        frm.beginTransaction().replace(binding.mainContent.getId(),frgAsList).commit();

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
        navigationView = binding.navMenu;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_OngoingAssessment:
                        AssessmentList ongList = new AssessmentList(1);
                        frm.beginTransaction().replace(binding.mainContent.getId(),ongList).commit();
                        break;
                    case R.id.nav_UpcomingAssessment:
                        AssessmentList upcominglist = new AssessmentList(0);
                        frm.beginTransaction().replace(binding.mainContent.getId(),upcominglist).commit();
                        break;
                    case R.id.nav_PastAssessment:
                        AssessmentList pastList = new AssessmentList(2);
                        frm.beginTransaction().replace(binding.mainContent.getId(),pastList).commit();
                        break;
                    case R.id.nav_logout:
                        SPMSRequest logoutReq = new SPMSRequest(jwt, Request.Method.GET, "api/account/logout", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Logout();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Logout();
                            }
                        });
                        requestQueue.add(logoutReq);
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });

        //drawer setup END--------------------------------------
    }

    private void Logout(){
        try {
            String masterKey = null;
            masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            EncryptedSharedPreferences encpref = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    "token",
                    masterKey,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            encpref.edit().remove("token").apply();
            encpref.edit().remove("payload").apply();
            encpref.edit().remove("jwtToken").apply();
            encpref.edit().remove("uid").apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    @Override
    public JWT getToken(){
        return jwt;
    }

    @Override
    public SweetAlert getSwal() {
        return swal;
    }

    @Override
    public RequestQueue getReqQueue() {
        return requestQueue;
    }
}