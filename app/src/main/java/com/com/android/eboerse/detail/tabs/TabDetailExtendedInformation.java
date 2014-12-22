package com.com.android.eboerse.detail.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.android.eboerse.R;

/**
 * Created by tok on 20.12.2014.
 */
public class TabDetailExtendedInformation extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.extended_infos_layout, container, false);

        }
}
