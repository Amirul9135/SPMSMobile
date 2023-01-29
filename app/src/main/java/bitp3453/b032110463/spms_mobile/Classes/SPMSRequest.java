package bitp3453.b032110463.spms_mobile.Classes;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bitp3453.b032110463.spms_mobile.Model.JWT;

public class SPMSRequest extends StringRequest {
    public static String serverUrl = "http://192.168.8.102:5000/api/";
    private JWT jwt;
    private JSONObject body;

    public SPMSRequest(JWT jwt,int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, serverUrl + url, listener, errorListener);
        this.jwt = jwt;
    }

    public SPMSRequest(JWT jwt, int method, String url, JSONObject body, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, serverUrl + url, listener, errorListener);
        this.body = body;
        this.jwt = jwt;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("cookie","token=" + jwt.getToken());
        headers.put("jwtT",jwt.getJwtToken());
        headers.put("jwtP",jwt.getJwtPayload());
        headers.put("uid",jwt.getUserId());
        if(body != null){
            headers.put("Content-Type", "application/json; charset=utf-8");
        }
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        //elaborate here with condition, if body null same if not null parse
        super.getBody();
        try {
            Log.d("bodyb",body.toString());
            return body.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            Log.d("ERRPartsbody", uee.toString());
            return null;
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }
}
