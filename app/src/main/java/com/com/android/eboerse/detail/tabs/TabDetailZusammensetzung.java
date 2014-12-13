package com.com.android.eboerse.detail.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.android.eboerse.R;
/**
 * Detail Tab Zusammensetzung
 * @author Tok
 *
 */
public class TabDetailZusammensetzung extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.detail_zusammen_list_view, container, false);

	}
}
