package com.sgk.sunshine.app.frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sgk.sunshine.app.R;
import com.sgk.sunshine.app.common.Fixed;

public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        String w = getActivity().getIntent().getStringExtra(Fixed.INTENT_DETAIL_WEATHER);
        Toast.makeText(getActivity(), w, Toast.LENGTH_LONG).show();

        return rootView;
    }
}
