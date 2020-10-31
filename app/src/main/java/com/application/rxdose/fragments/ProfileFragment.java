package com.application.rxdose.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.application.rxdose.R;
import com.application.rxdose.utils.DBManager;
import com.application.rxdose.utils.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{DatabaseHelper._ID,
            DatabaseHelper.tablet_name, DatabaseHelper.isConsumed};

    final int[] to = new int[]{R.id.id, R.id.tablet_name, R.id.is_consumed};


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setEmptyView(view.findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(getContext(), R.layout.tablet_list_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}