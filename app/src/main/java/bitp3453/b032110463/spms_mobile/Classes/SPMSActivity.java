package bitp3453.b032110463.spms_mobile.Classes;

import androidx.fragment.app.FragmentContainer;

import com.android.volley.RequestQueue;

import bitp3453.b032110463.spms_mobile.Fragment.SweetAlert;
import bitp3453.b032110463.spms_mobile.Model.JWT;


//for scalability
//in future maybe akan develop more activity yang bertoken
//in these activites maybe akan ade fragment yang nk retrieve token
//so instead of casting specific activity cast interface better reusability
public interface SPMSActivity {
    public JWT getToken();
    public SweetAlert getSwal();
    public RequestQueue getReqQueue();
}
