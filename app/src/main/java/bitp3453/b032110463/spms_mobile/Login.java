package bitp3453.b032110463.spms_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

        StringRequest jsonReq = new StringRequest(Request.Method.POST, SPMSRequest.serverUrl + "account/login" ,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.putExtra("token",jwt);
                startActivity(intent);
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(jsonReq);
    }

}