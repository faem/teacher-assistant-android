package net.net23.fahimabrar.teacherassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterStudent extends RecyclerView.Adapter<RecyclerAdapterStudent.ViewHolder2> {

    public LinearLayout linearLayout;
    ViewGroup parent;
    int viewType;
    String credit;
    String type;


    static List<Student> dbList;
    static Context context;
    RecyclerAdapterStudent(Context context, List<Student> dbList,String credit,String  type){
        this.dbList = new ArrayList<Student>();
        this.context = context;
        this.dbList = dbList;
        this.credit = credit;
        this.type = type;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        this.viewType = viewType;

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_student, null);
        // create ViewHolder

        ViewHolder2 viewHolder = new ViewHolder2(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder2 holder, int position) {

        holder.studentName.setText(dbList.get(position).getStudentName());
        holder.ID.setText(dbList.get(position).getStudentID());

        int x[] = {R.color.matColor1,R.color.matColor2,R.color.matColor3,R.color.matColor5,R.color.matColor4,R.color.matColor6,R.color.matColor7,R.color.matColor8,R.color.matColor9,R.color.matColor10};
        int i=0;
        for(int j=0;j<getItemCount();j++){
            if(i==10)
                i=0;
            if(position==j){
                holder.relativeLayout.setBackgroundColor(context.getResources().getColor(x[i]));
                i++;
            }
            i++;
        }

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }







    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener /*extends RecyclerView.ViewHolder implements View.OnClickListener*/ {

        public Button editMarksButton;
        public TextView studentName,ID;
        public ImageButton imageButtonStudentDelete,imageButtonStudentedit;
        public RelativeLayout relativeLayout;

        public ViewHolder2(View itemLayoutView) {
            super(itemLayoutView);
            studentName = (TextView) itemLayoutView.findViewById(R.id.textViewName);
            ID = (TextView) itemLayoutView.findViewById(R.id.textViewID);
            imageButtonStudentDelete = (ImageButton) itemLayoutView.findViewById(R.id.imageButtonStudentDelete);
            imageButtonStudentedit = (ImageButton) itemLayoutView.findViewById(R.id.imageButtonStudentEdit);

            imageButtonStudentDelete.setOnClickListener(this);
            imageButtonStudentedit.setOnClickListener(this);

            relativeLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.frameLayoutStdent);
//            itemLayoutView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(view==imageButtonStudentDelete){
                final DatabaseHandler databaseHandler = new DatabaseHandler(context);
                final int position = getAdapterPosition();
                final String tableName = dbList.get(position).getTableName();
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(view.getContext());
                localBuilder.setTitle("Alert");
                localBuilder.setMessage("Do you want to delete?");
                localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        List<Subject> subList =  databaseHandler.getDataFromDB();
                        databaseHandler.deleteData(tableName,dbList.get(position).getStudentID());
                        databaseHandler.deleteData(tableName+"_attendance",dbList.get(position).getStudentID());
                        RecyclerAdapterStudent recyclerAdapterStudent = new RecyclerAdapterStudent(context,databaseHandler.getStudentFromDB(tableName),credit,type);
                        notifyItemRemoved(position);

                        ((StudentActivity)context).onResume();
                    }
                });

                localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        paramAnonymousDialogInterface.dismiss();

                    }
                });
                AlertDialog alert = localBuilder.create();
                alert.show();

                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.BLUE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLUE);

            }else{
                ArrayList<Integer> studentId = new ArrayList<Integer>();

                for(int j=0;j<dbList.size();j++)
                    studentId.add(Integer.parseInt(dbList.get(j).getStudentID()));

                if(type.equals("Theory")){
                    final  int position = getAdapterPosition();
                    Intent i = new Intent(view.getContext(),EditStudent.class);
                    i.putIntegerArrayListExtra("ID",studentId);
                    i.putExtra("pos",position);
                    /*i.putExtra("ID",dbList.get(position).getStudentID());
                    i.putExtra("name",dbList.get(position).getStudentName());*/
                    i.putExtra("credit",credit);
                    i.putExtra("table",dbList.get(position).getTableName());
                    context.startActivity(i);
                }else{
                    final  int position = getAdapterPosition();
                    Intent i = new Intent(view.getContext(),EditStudentLab.class);

                    i.putIntegerArrayListExtra("ID",studentId);
                    /*i.putExtra("ID",dbList.get(position).getStudentID());
                    i.putExtra("name",dbList.get(position).getStudentName());*/
                    System.out.println("POS 2 = "+position);
                    i.putExtra("pos",position);
                    i.putExtra("credit",credit);
                    i.putExtra("table",dbList.get(position).getTableName());
                    context.startActivity(i);
                }

            }

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
            int position = parent.getChildPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }

        }
    }
}
