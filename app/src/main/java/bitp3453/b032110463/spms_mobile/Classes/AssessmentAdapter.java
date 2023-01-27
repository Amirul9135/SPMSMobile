package bitp3453.b032110463.spms_mobile.Classes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.VhAssessmentBinding;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentHolder> {
   // private LayoutInflater layoutInflater;
    private Vector<Assessment> assessments;
    public AssessmentAdapter(Vector<Assessment> assessments){
       // this.layoutInflater = layoutInflater;
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssessmentHolder(VhAssessmentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        holder.setAssessment(assessments.get(position));
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }
}
