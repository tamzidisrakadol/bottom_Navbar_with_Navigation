package com.example.bottomnavbarwithnavgation.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bottomnavbarwithnavgation.R;
import com.example.bottomnavbarwithnavgation.databinding.FragmentManageBinding;
import com.google.android.material.textfield.TextInputEditText;


public class Manage extends Fragment {
    FragmentManageBinding fragmentManageBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentManageBinding =FragmentManageBinding.inflate(inflater,container,false);
        return fragmentManageBinding.getRoot();
    }
}