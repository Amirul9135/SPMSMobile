package bitp3453.b032110463.spms_mobile.Model;

import java.io.Serializable;

public class JWT implements Serializable {

    private String jwtPayload;//in header jwtP
    private String token;//in cookie token=
    private String jwtToken;//in header jwtT
    private String userId; //in header uid

    public JWT(){

    }

    public JWT(String jwtPayload, String token, String jwtToken, String userId) {
        this.jwtPayload = jwtPayload;
        this.token = token;
        this.jwtToken = jwtToken;
        this.userId = userId;
    }

    public String getJwtPayload() {
        return jwtPayload;
    }

    public void setJwtPayload(String jwtPayload) {
        this.jwtPayload = jwtPayload;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
