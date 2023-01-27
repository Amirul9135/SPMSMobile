package bitp3453.b032110463.spms_mobile.Model;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SPMSRequest extends StringRequest {
    public static String serverUrl = "http://192.168.8.102:5000/api/";
    private JWT jwt;

    public SPMSRequest(JWT jwt,int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
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
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        //elaborate here with condition, if body null same if not null parse
        return super.getBody();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }
}
