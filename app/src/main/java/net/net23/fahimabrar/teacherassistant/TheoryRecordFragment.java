package net.net23.fahimabrar.teacherassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TheoryRecordFragment extends Fragment {
   
    String tableName,credit;
    public TheoryRecordFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tableName = getArguments().getString("table");
            credit = getArguments().getString("credit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_attendance_record, container, false);
        HorizontalScrollView hsv;
        String[] columnName;
        double[][] d;
        int rowLength;
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

        columnName = databaseHandler.columnName(tableName);
        rowLength = databaseHandler.rowNumber(tableName);
        d = databaseHandler.getCtMarks(tableName);
        hsv = (HorizontalScrollView) v.findViewById(R.id.hsv2);
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(4, -1));
        //linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        root.addView(linearLayout);


        LinearLayout localLinearLayout;
        View view;
        TextView textView;

        for(int i=0;i<4;i++){
            localLinearLayout = new LinearLayout(getActivity());
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            if(columnName[i+1].equals("ct1"))
                textView.setText("CT 1");
            else if(columnName[i+1].equals("ct2"))
                textView.setText("CT 2");
            else if(columnName[i+1].equals("ct3"))
                textView.setText("CT 3");
            else
                textView.setText("ID");

            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(getActivity());
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                System.out.println(i+" "+j+" "+d[i][j]);
                double x = d[i][j]-(int)d[i][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[i][j]));
                else
                    textView.setText(Double.toString(d[i][j]));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);
        }

        hsv.addView(root);
//        view = (View) hsv.getParent();
//        view.setVisibility(View.VISIBLE);


        if(credit.equals("3")||credit.equals("4")){

            localLinearLayout = new LinearLayout(getActivity());
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());

            textView.setText("CT 4");
            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(getActivity());
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                double x = d[5][j]-(int)d[5][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[4][j]));
                else
                    textView.setText(Double.toString(d[4][j]));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);

        }

        if(credit.equals("4")){

            localLinearLayout = new LinearLayout(getActivity());
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());

            textView.setText("CT 5");
            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(getActivity());
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                double x = d[5][j]-(int)d[5][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[5][j]));
                else
                    textView.setText(Double.toString(d[5][j]));
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);


        }

        int presentCount,absentCount;

        localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(getActivity());
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Total");
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);
        localLinearLayout.addView(textView);
        for(int i=0;i<rowLength;i++)
        {
            view = new View(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(getActivity());
            double min,total;

            total = d[1][i]+d[2][i]+d[3][i]+d[4][i]+d[5][i];
            min = Math.min(d[1][i],d[2][i]);
            min = Math.min(min,d[3][i]);
            if(credit.equals("3")||credit.equals("4"))
                min = Math.min(min,d[4][i]);
            if(credit.equals("4"))
                min = Math.min(min,d[5][i]);

            total = total - min;
            textView.setText(Double.toString(Math.ceil(total)));
            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);
        
        
        /// / Inflate the layout for this fragment
        return v;
    }
}
