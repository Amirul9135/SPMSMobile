package bitp3453.b032110463.spms_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import bitp3453.b032110463.spms_mobile.Classes.LoadingOverlay;
import bitp3453.b032110463.spms_mobile.Fragment.SweetAlert;
import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SweetAlert swal;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //fragment setuo
        FragmentManager frm = getSupportFragmentManager();
        swal = new SweetAlert();
        frm.beginTransaction().replace(binding.frgSwal.getId(),swal).commit();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getToken(binding.edtAccId.getText().toString(),binding.edtPass.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            JWT ext = new JWT();//existing token
            ext.setToken( encpref.getString("token",""));
            ext.setJwtPayload( encpref.getString("payload",""));
            ext.setJwtToken( encpref.getString("jwtToken",""));
            ext.setUserId( encpref.getString("uid",""));
            if(!ext.getToken().isEmpty() && !ext.getJwtPayload().isEmpty() && !ext.getJwtToken().isEmpty() && !ext.getUserId().isEmpty()){
                verify(ext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verify(JWT token){
        Log.d("ver","verify s");
        LoadingOverlay loader = new LoadingOverlay(this);
        loader.show();
        SPMSRequest verification = new SPMSRequest(token, Request.Method.GET, "account/verify", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Verification","succ");
                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.putExtra("token",token);
                loader.dismiss();
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Verification","failed");
                try {
                    //fail clear token
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
                loader.dismiss();
            }
        });
        requestQueue.add(verification);
    }

    private void getToken(String accId, String password){
        JWT jwt = new JWT();
        jwt.setUserId(accId);
        JSONObject body = new JSONObject();
        try {
            body.put("accountId",accId);
            body.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LoadingOverlay loader = new LoadingOverlay(this);
        loader.show();
        StringRequest jsonReq = new StringRequest(Request.Method.POST, SPMSRequest.serverUrl + "account/login" ,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loader.dismiss();
                        Log.d("Error respon",error.toString());
                        String content = "";
                        try {
                            content = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("Er",content);
                        swal.showError("Invalid Login","Please Check your credentials and try again");
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String,String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d("ERRPartsbody", uee.toString());
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                String jwtt = responseHeaders.get("jwtT");
                String jwtp = responseHeaders.get("jwtP");
                String token = rawCookies.split("oken=")[1].split(";")[0];
                jwt.setToken(token);
                jwt.setJwtPayload(jwtp);
                jwt.setJwtToken(jwtt);

                //save token to shared pref for persistent login
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
                    encpref.edit().putString("token",jwt.getToken()).apply();
                    encpref.edit().putString("payload",jwt.getJwtPayload()).apply();
                    encpref.edit().putString("jwtToken",jwt.getJwtToken()).apply();
                    encpref.edit().putString("uid",jwt.getUserId()).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.putExtra("token",jwt);
                loader.dismiss();
                startActivity(intent);
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(jsonReq);
    }

}