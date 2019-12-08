package student.inti.assignment.Exam;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;
public class ExamView extends RecyclerView.ViewHolder {
    View mView;
    public ExamView(@NonNull View examView) {
        super(examView);
        mView= examView;
        examView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view ,getAdapterPosition());
                return true;
            }
        });
    }

    public void setInfo(Context context,String subject,String date,String time, String duration, String room,String notes){
        TextView sub=mView.findViewById(R.id.subject);
        TextView dat=mView.findViewById(R.id.date);
        TextView tim=mView.findViewById(R.id.time);
        TextView dura=mView.findViewById(R.id.duration);

        sub.setText(subject);
        dat.setText(date);
        tim.setText(time);
        dura.setText(duration);
    }
    private ExamView.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view,int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ExamView.ClickListener clickListener){
        mClickListener=clickListener;
    }

}
