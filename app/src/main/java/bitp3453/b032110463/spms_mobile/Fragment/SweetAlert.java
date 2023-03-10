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

    private void show(String title,String message, int type){
        new SweetAlertDialog(this.getActivity(), type)
                .setTitleText(title)
                .setContentText(message)
                .show();

    }
    public void showMessage(String title,String message){
        show(title,message,SweetAlertDialog.NORMAL_TYPE);
    }
    public void showError(String title, String message){
        show(title,message,SweetAlertDialog.ERROR_TYPE);
    }
    public void confirm(String title, String message , SweetAlertDialog.OnSweetClickListener action){
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setCancelText("Yes")
                .setConfirmText("No")
                .showCancelButton(true)
                .setCancelClickListener(action) // reverse sbb somehow kiri die cancel button
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

}