package net.net23.fahimabrar.teacherassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;

    String tableName,credit,type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tableName = getArguments().getString("table");
            credit = getArguments().getString("credit");
            type = getArguments().getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            Bundle bundle = new Bundle();
            bundle.putString("table", tableName);
            bundle.putString("credit",credit);
            AttendanceRecordFragment attendanceRecordFragment = new AttendanceRecordFragment();
            attendanceRecordFragment.setArguments(bundle);

            TheoryRecordFragment theoryRecordFragment = new TheoryRecordFragment();
            theoryRecordFragment.setArguments(bundle);

            LabRecordFragment labRecordFragment = new LabRecordFragment();
            labRecordFragment.setArguments(bundle);

            switch (position){
              case 0 :
                  return attendanceRecordFragment;
              case 1 :
                  if(type.equals("Theory"))
                      return theoryRecordFragment;
                  else
                      return labRecordFragment;
            }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Attendance";
                case 1 :
                    if(type.equals("Theory"))
                        return "CT Marks";
                    else
                        return "Lab Marks";
            }
                return null;
        }
    }

}
