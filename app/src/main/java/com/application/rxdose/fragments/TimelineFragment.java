package com.application.rxdose.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.R;
import com.application.rxdose.adapters.TimelineItemAdapter;
import com.application.rxdose.model.DailyMedicineModel;
import com.application.rxdose.model.TabletModel;
import com.application.rxdose.utils.Utils;
import com.application.rxdose.utils.ViewCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    private List<List<DailyMedicineModel>> allDailyReportList = new ArrayList<>();
    private List<ViewCategory> viewCategories = new ArrayList<>();
    private TimelineItemAdapter dAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareMultipleReports();
        dAdapter = new TimelineItemAdapter(allDailyReportList);
        recyclerView.setAdapter(dAdapter);


        return view;
    }

    private void prepareMultipleReports() {
        for (int i = 0; i < 3; i++) {
            allDailyReportList.add(prepareDailyReport());
        }
    }

    private List<DailyMedicineModel> prepareDailyReport() {
        List<DailyMedicineModel> dailyReportList = new ArrayList<>();
        viewCategories.add(ViewCategory.DOCTOR_INFO);
        viewCategories.add(ViewCategory.REPORT);
        TabletModel tabletModel1 = new TabletModel("xyz", true);
        TabletModel tabletModel2 = new TabletModel("abc", true);
        TabletModel tabletModel3 = new TabletModel("abc", false);
        List<String> timeList = new ArrayList<>();
        timeList.add("7:00");
        timeList.add("7:10");
        timeList.add("7:20");

        DailyMedicineModel mondayReport = new DailyMedicineModel(Utils.getPreviousDateString(0));
        mondayReport.addTabletInfo(tabletModel1);
        mondayReport.addTabletInfo(tabletModel2);
        mondayReport.addTabletInfo(tabletModel3);
        mondayReport.setTabletTimeList(timeList);

        DailyMedicineModel tuesdayReport = new DailyMedicineModel(Utils.getPreviousDateString(1));
        tabletModel1 = new TabletModel("xyz", true);
        tabletModel2 = new TabletModel("abc", true);
        tabletModel3 = new TabletModel("abc", true);
        tuesdayReport.addTabletInfo(tabletModel1);
        tuesdayReport.addTabletInfo(tabletModel2);
        tuesdayReport.addTabletInfo(tabletModel3);
        tuesdayReport.setTabletTimeList(timeList);

        DailyMedicineModel wedReport = new DailyMedicineModel(Utils.getPreviousDateString(2));
        tabletModel2.setCosumed(false);
        wedReport.addTabletInfo(tabletModel1);
        wedReport.addTabletInfo(tabletModel2);
        wedReport.addTabletInfo(tabletModel3);
        wedReport.setTabletTimeList(timeList);

        DailyMedicineModel thursdayReport = new DailyMedicineModel(Utils.getPreviousDateString(3));
        thursdayReport.addTabletInfo(tabletModel1);
        thursdayReport.addTabletInfo(tabletModel2);
        tabletModel3.setCosumed(false);
        thursdayReport.addTabletInfo(tabletModel3);
        thursdayReport.setTabletTimeList(timeList);

        DailyMedicineModel fridayReport = new DailyMedicineModel(Utils.getPreviousDateString(4));
        fridayReport.addTabletInfo(tabletModel1);
        fridayReport.addTabletInfo(tabletModel2);
        fridayReport.addTabletInfo(tabletModel3);
        fridayReport.setTabletTimeList(timeList);

        DailyMedicineModel saturdayReport = new DailyMedicineModel(Utils.getPreviousDateString(5));
        tabletModel1.setCosumed(false);
        saturdayReport.addTabletInfo(tabletModel1);
        saturdayReport.addTabletInfo(tabletModel2);
        saturdayReport.addTabletInfo(tabletModel3);
        saturdayReport.setTabletTimeList(timeList);

        DailyMedicineModel sundayReport = new DailyMedicineModel(Utils.getPreviousDateString(6));
        sundayReport.addTabletInfo(tabletModel1);
        sundayReport.addTabletInfo(tabletModel2);
        tabletModel3.setCosumed(false);
        sundayReport.addTabletInfo(tabletModel3);
        sundayReport.setTabletTimeList(timeList);

        dailyReportList.add(mondayReport);
        dailyReportList.add(tuesdayReport);
        dailyReportList.add(wedReport);
        dailyReportList.add(thursdayReport);
        dailyReportList.add(fridayReport);
        dailyReportList.add(saturdayReport);
        dailyReportList.add(sundayReport);
        return dailyReportList;
    }
}