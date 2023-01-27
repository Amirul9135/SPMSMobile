package bitp3453.b032110463.spms_mobile.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bitp3453.b032110463.spms_mobile.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetAlert extends Fragment {

    public SweetAlert() {
        // Required empty public constructor
    }

    public static SweetAlert newInstance(String param1, String param2) {
        SweetAlert fragment = new SweetAlert();
        return fragment;
    }

    public void showError(String title, String message){
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

}