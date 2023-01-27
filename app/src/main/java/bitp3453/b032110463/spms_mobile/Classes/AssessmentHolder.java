package bitp3453.b032110463.spms_mobile.Classes;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.VhAssessmentBinding;

public class AssessmentHolder extends RecyclerView.ViewHolder {
    private VhAssessmentBinding binding;
   // private final TextView tv1,tv2;
    public AssessmentHolder(VhAssessmentBinding itemView) {
        super(itemView.getRoot());
//        this.tv1 = itemView.findViewById(R.id.vh_as_txt1);
//        this.tv2 = itemView.findViewById(R.id.vh_as_txt2);
        this.binding = itemView;
    }


    public void setAssessment(Assessment assessment){
        Log.d("SINNI","S" + assessment.getTitle());
        binding.vhAsTxt1.setText(assessment.getTitle());
    }
}
