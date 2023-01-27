package bitp3453.b032110463.spms_mobile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jwt = (JWT) getIntent().getSerializableExtra("token");
        Log.d("token","TT>"+jwt.getToken());
        Log.d("token","TTT>"+jwt.getJwtToken());
        Log.d("token","TP>"+jwt.getJwtPayload());
        Log.d("token","uid>"+jwt.getUserId());

        requestQueue = Volley.newRequestQueue(this);

        FragmentManager frm = getSupportFragmentManager();
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