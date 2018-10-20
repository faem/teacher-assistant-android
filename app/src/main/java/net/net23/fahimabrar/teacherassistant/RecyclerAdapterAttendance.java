package net.net23.fahimabrar.teacherassistant;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fahim on 3/3/2017.
 */



public class RecyclerAdapterAttendance extends RecyclerView.Adapter<RecyclerAdapterAttendance.ViewHolder> {
    String table;
    Context context;
    public int l;


    public List<Integer> rollList = new ArrayList<Integer>();
    public int[] attendanceArray = new int[1000];

    public RecyclerAdapterAttendance(Context context, String table,int l,int[] attendanceArray) {
        this.table = table;
        this.l = l;
        this.attendanceArray = attendanceArray;
        this.context = context;
        System.out.println("ok");
    }
    CardView cardView;
    @Override
    public RecyclerAdapterAttendance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("Hi");

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_mark, null);

        //attendanceArray = new boolean[1000];

        // create ViewHolder



        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        List<Student> studentList = databaseHandler.getStudentFromDB(table);

        for(int j=0;j<studentList.size();j++){
            Student s = studentList.get(j);
            rollList.add(Integer.parseInt(s.getStudentID()));

        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String x = Integer.toString(rollList.get(position));
        holder.rollView.setText(x);

        if(attendanceArray[position]==1){
            //attendanceArray[position]=1;
            holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.check_mark_green));
        }else if(attendanceArray[position]==2){
            //attendanceArray[position]=2;
            holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.late_sign));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
        }else{
            //attendanceArray[position]=0;
            holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.check_mark_green));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
        }

        //notifyDataSetChanged();
        //holder.toggleButton.invalidate();
    }


    @Override
    public int getItemCount() {
        return l;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView rollView;
//        ToggleButton toggleButton;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            rollView = (TextView) itemView.findViewById(R.id.rollView);
//            toggleButton = (ToggleButton) itemView.findViewById(R.id.ToggleButtonAttendance);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewMark);

            cardView = (CardView) itemView.findViewById(R.id.cardViewAttendance);
            //imageView.setOnClickListener(this);

            cardView.setOnClickListener(this);

//            toggleButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            //Toast.makeText(context,position,Toast.LENGTH_LONG).show();

            //notifyDataSetChanged();
            if(attendanceArray[position]==0){
                attendanceArray[position]=1;
                imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.check_mark_green));
            }else if(attendanceArray[position]==1){
                attendanceArray[position]=2;
                imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.late_sign));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
            }else{
                attendanceArray[position]=0;
                imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.check_mark_green));
//                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cross));
            }
            System.out.println(attendanceArray[0]);
               // view.notifyAll();

        }
    }

    static class BottomOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mBottomOffset;

        public BottomOffsetDecoration(int bottomOffset) {
            mBottomOffset = bottomOffset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int pos = parent.getChildPosition(view);
            if (dataSize > 0 && pos == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }


           /* GridLayoutManager grid = (GridLayoutManager)parent.getLayoutManager();
            if ((dataSize - position) <= grid.getSpanCount()) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }*/

        }
    }
}
