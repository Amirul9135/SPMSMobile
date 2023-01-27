package bitp3453.b032110463.spms_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.databinding.ActivityLoginBinding;
import bitp3453.b032110463.spms_mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private JWT jwt;
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
    }
}