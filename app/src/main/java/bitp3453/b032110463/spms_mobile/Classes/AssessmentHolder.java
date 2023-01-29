package bitp3453.b032110463.spms_mobile.Classes;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        binding.vhasTitle.setText(assessment.getTitle());
        binding.vhasSubj.setText( "[" + assessment.getSubject() + "] " + assessment.getSubjectTitle()  );
        binding.vhasOpenD.setText(assessment.getOpenDT().toString("dd/MM/YYYY"));
        binding.vhasCloseD.setText(assessment.getCloseDT().toString("dd/MM/YYYY"));
        binding.vhasOpenT.setText(assessment.getOpenDT().toString("hh:mm aa"));
        binding.vhasCloseT.setText(assessment.getCloseDT().toString("hh:mm aa"));
    }
}
