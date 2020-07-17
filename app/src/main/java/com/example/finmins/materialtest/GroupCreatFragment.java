package com.example.finmins.materialtest;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finmins.materialtest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupCreatFragment extends Fragment {


    public GroupCreatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_creat, container, false);
    }

}
