package student.inti.assignment.Course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.Task.Model;
import student.inti.assignment.R;

public class Adapter extends RecyclerView.Adapter<Adapter.MyAdapterViewHolder> {
    public Context c;
    public ArrayList<Model>list;
    public  Adapter(Context c, ArrayList<Model>list){
        this.c=c;
        this.list=list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row,parent,false);
        return new MyAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        Model model= list.get(position);

        holder.t1.setText(model.getSubject());
        holder.t2.setText(model.getTask());
        holder.t3.setText(model.getDate());
        holder.t4.setText(model.getTitle());
        holder.t5.setText(model.getDetails());

    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView t1,t2,t3,t4,t5;

        public MyAdapterViewHolder(View itemView){
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.subject);
            t2=(TextView)itemView.findViewById(R.id.task);
            t3=(TextView)itemView.findViewById(R.id.calendar);
            t4=(TextView)itemView.findViewById(R.id.editText5);
            t5=(TextView)itemView.findViewById(R.id.editText7);

        }
    }

}
