package net.net23.fahimabrar.teacherassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class AttendanceRecordFragment extends Fragment implements ScrollViewListener {

    String tableName;
    boolean interceptScroll = true;
    private ObservableScrollView scrollview1 = null;
    private ObservableScrollView scrollview2 = null;

    public AttendanceRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tableName = getArguments().getString("table");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_attendance_record, container, false);
        HorizontalScrollView hsv1,hsv2;
        hsv1 = (HorizontalScrollView) v.findViewById(R.id.hsv1);
        hsv2 = (HorizontalScrollView) v.findViewById(R.id.hsv2);

        scrollview1 = (ObservableScrollView) v.findViewById(R.id.scrollview1);
        scrollview1.setScrollViewListener(this);
        scrollview2 = (ObservableScrollView) v.findViewById(R.id.scrollview2);
        scrollview2.setScrollViewListener(this);

        String[] attendance,columnName;

        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

        List<Student> studentList = databaseHandler.getStudentFromDB(tableName);
        databaseHandler = new DatabaseHandler(getActivity());
        attendance = databaseHandler.showAttendance(tableName+"_attendance",studentList.size());
        columnName = databaseHandler.columnName(tableName+"_attendance");

        LinearLayout root1 = new LinearLayout(getActivity());
        LinearLayout root2 = new LinearLayout(getActivity());
        root1.setOrientation(LinearLayout.HORIZONTAL);
        root2.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(4, -1));
        //linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        root1.addView(linearLayout);

        LinearLayout localLinearLayout = new LinearLayout(getActivity());

        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        View view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        TextView textView = new TextView(getActivity());
        textView.setText("ID");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);

        for (int i=0; i<studentList.size();i++) {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            textView.setText(studentList.get(i).getStudentID());
            System.out.println("s "+studentList.get(i).getStudentID());
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }


        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        root1.addView(localLinearLayout);
        //System.out.println("column = "+columnName.length+" "+attendance[0]);

        hsv1.addView(root1);

        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1,4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Name");
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            textView.setText(studentList.get(i).getStudentName());
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root2.addView(localLinearLayout);

        for(int i=0;i<columnName.length-1;i++){
            localLinearLayout = new LinearLayout(getActivity());
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());

            String[] temp = columnName[i+1].split("_");

            String c = temp[1]+"/"+temp[2]/*+"/"+temp[3]*/;

            textView.setText(c);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<studentList.size();j++){
                view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(getActivity());
                textView.setText("");
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                if(Character.toString(attendance[j].charAt(i)).equals("A")){
                    textView.setBackground(getResources().getDrawable(R.drawable.red_bg));

                }else if(Character.toString(attendance[j].charAt(i)).equals("P")){
                    textView.setBackground(getResources().getDrawable(R.drawable.green_bg));
                }else{
                    textView.setBackground(getResources().getDrawable(R.drawable.yellow_bg));
                }
                localLinearLayout.addView(textView);
            }
            root2.addView(localLinearLayout);
        }

        hsv2.addView(root2);
//        view = (View) hsv2.getParent();
//        view.setVisibility(View.VISIBLE);

        int presentCount,absentCount,lateCount;

        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Present");
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            presentCount = attendance[i].length()-attendance[i].replaceAll( "P", "" ).length();
            textView.setText(Integer.toString(presentCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root2.addView(localLinearLayout);

        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setText("Absent");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            absentCount = attendance[i].length()-attendance[i].replaceAll( "A", "" ).length();
            textView.setText(Integer.toString(absentCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root2.addView(localLinearLayout);

        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Late");
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            lateCount = attendance[i].length()-attendance[i].replaceAll( "L", "" ).length();
            textView.setText(Integer.toString(lateCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root2.addView(localLinearLayout);


        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setText("Percentage");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            int total = attendance[i].length();
            presentCount = total - attendance[i].replaceAll( "P", "" ).length();
            lateCount = total - attendance[i].replaceAll("L","").length();
            double percentage = ((presentCount+(double)lateCount/2.00)/(double)total)*100.00;
            DecimalFormat four = new DecimalFormat("#0.00");
            String p = four.format(percentage);
            textView.setText(p+"%");
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root2.addView(localLinearLayout);



        return v;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(interceptScroll){
            interceptScroll=false;
            if(scrollView == scrollview1) {
                scrollview2.onOverScrolled(x,y,true,true);
            } else if(scrollView == scrollview2) {
                scrollview1.onOverScrolled(x,y,true,true);
            }
            interceptScroll=true;
        }
    }
}
