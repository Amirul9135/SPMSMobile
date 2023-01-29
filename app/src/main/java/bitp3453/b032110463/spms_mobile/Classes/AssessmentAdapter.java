package bitp3453.b032110463.spms_mobile.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.VhAssessmentBinding;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentHolder> {
   // private LayoutInflater layoutInflater;
    private Vector<Assessment> assessments;
    private AssessmentClick clickAction = null;
    private Context context;
    public AssessmentAdapter(Vector<Assessment> assessments){
       // this.layoutInflater = layoutInflater;
        this.assessments = assessments;
        this.context = context;
    }
    public AssessmentAdapter(Vector<Assessment> assessments,Context context, AssessmentClick clickAction){
        this.assessments = assessments;
        this.clickAction = clickAction;
        this.context = context;
    }


    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssessmentHolder(VhAssessmentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        holder.setAssessment(assessments.get(position));
        if(clickAction != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickAction.clickAssessment(assessments.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }
}
