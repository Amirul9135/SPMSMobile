package bitp3453.b032110463.spms_mobile.Fragment;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import bitp3453.b032110463.spms_mobile.Classes.AssessmentAdapter;
import bitp3453.b032110463.spms_mobile.Classes.SPMSActivity;
import bitp3453.b032110463.spms_mobile.Classes.SPMSRequest;
import bitp3453.b032110463.spms_mobile.MainActivity;
import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.Model.JWT;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.FragmentHomeBinding;

public class Home extends Fragment {
    private FragmentHomeBinding binding;
    private JWT jwt;
    private AssessmentAdapter asAdapter;
    private Vector<Assessment> assessments;
    public Home() {
        // Required empty public constructor
    }
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jwt = ((SPMSActivity)getActivity()).getToken();
        Log.d("token frag",">>"+jwt.getToken());
        SPMSRequest req = new SPMSRequest(jwt, Request.Method.GET, "assessment/?status=ongoing", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for(int i=0; i < arr.length();i++){
                        assessments.add(new Assessment( arr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                asAdapter.notifyDataSetChanged();
                Log.d("here", assessments.size()+ "|"+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err",error.getMessage());
                ((SPMSActivity)getActivity()).getSwal().showError("Error","unable to load assessment list");
            }
        });
        ((SPMSActivity)getActivity()).getReqQueue().add(req);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        assessments = new Vector<>();
        asAdapter = new AssessmentAdapter(assessments);
        binding.rcvHomeAssessment.setAdapter(asAdapter);
        binding.rcvHomeAssessment.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return binding.getRoot();
    }
}