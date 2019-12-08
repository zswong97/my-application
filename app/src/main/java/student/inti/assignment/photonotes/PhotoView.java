package student.inti.assignment.photonotes;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;
import student.inti.assignment.Task.TaskView;

public class PhotoView extends RecyclerView.ViewHolder {

    View mView;

    public PhotoView(@NonNull View photoView) {
        super(photoView);

        mView= photoView;

        photoView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view ,getAdapterPosition());
                return true;
            }
        });
    }


    public void setInfo(Context context,String title,String image,String description,String user,String date){
        TextView tle=mView.findViewById(R.id.title);
        ImageView img=mView.findViewById(R.id.image);
        TextView des=mView.findViewById(R.id.description);
        TextView us=mView.findViewById(R.id.User);
        TextView dv=mView.findViewById(R.id.date);

        tle.setText(title);
        Picasso.get().load(image).into(img);
        des.setText(description);
        us.setText(user);
        dv.setText(date);
    }

    private PhotoView.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view,int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(PhotoView.ClickListener clickListener){
        mClickListener=clickListener;
    }

}
