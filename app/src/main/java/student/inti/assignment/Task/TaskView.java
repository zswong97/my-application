package student.inti.assignment.Task;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;

public class TaskView extends RecyclerView.ViewHolder {
    View mView;
    public TaskView(@NonNull View itemView) {
        super(itemView);
        mView= itemView;
        itemView.setOnClickListener(new View.OnClickListener(){

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

    public void setInfo(Context context,String subject,String task,String date, String title, String details){
        TextView sub=mView.findViewById(R.id.subject);
        TextView tsk=mView.findViewById(R.id.task);
       TextView calendar=mView.findViewById(R.id.calendar);
       TextView textView1=mView.findViewById(R.id.editText5);
       TextView textView2=mView.findViewById(R.id.editText7);
        sub.setText(subject);
        tsk.setText(task);
        calendar.setText(date);
        textView1.setText(title);
    }
    private TaskView.ClickListener mClickListener;
    public interface ClickListener {
        void onItemClick(View view,int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(TaskView.ClickListener clickListener){
        mClickListener=clickListener;
    }

}
