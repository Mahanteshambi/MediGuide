package com.application.rxdose.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.R;
import com.application.rxdose.activities.MainActivity;
import com.application.rxdose.viewmodel.PrescriptionViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.application.rxdose.model.PrescriptionContent.loadSavedImages;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrescriptionFragment newInstance(String param1, String param2) {
        PrescriptionFragment fragment = new PrescriptionFragment();
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
        prescriptionViewModel = ViewModelProviders.of(requireActivity())
                .get(PrescriptionViewModel.class);


        prescriptionViewModel.getUrl().observe(this, nameObserver);
    }

    Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onChanged(String name) {
//            receivedText.setText(name);
            updateViews();
        }
    };

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FragmentActivity myContext;
    private PrescriptionViewModel prescriptionViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prescription, container, false);
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Prescriptions");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent, 7);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        if (recyclerViewAdapter == null) {
//            Fragment currentFragment = view.findViewById(R.id.main_fragment);
            recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment);
            recyclerViewAdapter = ((RecyclerView) recyclerView).getAdapter();
        }
        loadSavedImages(myContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        return view;
    }

    public void updateViews() {
        loadSavedImages(myContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        recyclerViewAdapter.notifyDataSetChanged();
    }


}