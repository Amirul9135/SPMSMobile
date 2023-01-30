package bitp3453.b032110463.spms_mobile.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 

import bitp3453.b032110463.spms_mobile.Model.Assessment;
import bitp3453.b032110463.spms_mobile.R;
import bitp3453.b032110463.spms_mobile.databinding.FragmentAssessmentCoverBinding;
import bitp3453.b032110463.spms_mobile.databinding.FragmentAssessmentListBinding;

public class AssessmentCover extends Fragment {
    private Assessment assessment;
    private String startAttempt;
    private String endAttempt;
    private int questionCount;
    private View.OnClickListener firstQuestion;
    FragmentAssessmentCoverBinding binding;
    public AssessmentCover() {
        // Required empty public constructor
        assessment = new Assessment();
        startAttempt = "";
        endAttempt = "";
    }
    public AssessmentCover(Assessment assessment, String startAttempt, String endAttempt, int questionCount, View.OnClickListener firstQuestion){
        this.assessment = assessment;
        this.startAttempt = startAttempt;
        this.endAttempt = endAttempt;
        this.questionCount = questionCount;
        this.firstQuestion = firstQuestion;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAssessmentCoverBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        binding.asTitle.setText(assessment.getTitle());
        binding.txtSubj.setText(assessment.getSubjectTitle() + "("+assessment.getSubject()+")");
        binding.txtOpen.setText(assessment.getOpenDT().toString("dd/MM/YYYY hh:mm aa"));
        binding.txtClose.setText(assessment.getCloseDT().toString("dd/MM/YYYY hh:mm aa"));
        binding.txtDuration.setText(String.valueOf( assessment.getDuration()));
        binding.txtStartAtempt.setText("" + startAttempt);
        binding.txtEndAttempt.setText("" +endAttempt);
        binding.txtCount.setText(String.valueOf(questionCount));
        binding.btngtq.setOnClickListener(firstQuestion);
        return binding.getRoot();
    }
}