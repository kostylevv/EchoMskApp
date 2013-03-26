package com.unionfind.android.echomskapp;

/**
 * Custom adapter to inflate ListView with flight entries
 */

import java.util.ArrayList;

import com.example.echomskapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TripsAdapter extends ArrayAdapter<Trip> {
	
	
	private LayoutInflater mInflater;
	private Context mContext;
	
	public TripsAdapter(Context context, int resource,
			ArrayList<Trip> progs) {
		super(context, resource, progs);
		mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext=context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Trip trip = getItem(position);
		
		TextView duration = null;
		TextView takeoff = null;
		TextView landing = null;
		TextView flight = null;
		
		
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {		
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			
			duration = holder.getDuartion();
			duration.setText(trip.getDuration());
			
			takeoff = holder.getTakeoff();
			takeoff.setText(trip.getTakeoff());
			
			landing = holder.getLanding();
			landing.setText(trip.getLanding());
			
			flight = holder.getFlight();
			flight.setText(trip.getFlight());
			
			
			

		} catch (Exception e) {
			Log.v("adapter", "getView(): exception" + e + "pos:" + position);
		}
		return convertView;
	}

	private class ViewHolder {
		private View mRow;
		private TextView duration = null;
		private TextView takeoff = null;
		private TextView landing = null;
		private TextView flight = null;
		
	
		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getDuartion() {
			if (null == duration) {
				duration = (TextView) mRow.findViewById(R.id.programTitle);
			}
			return duration;
		}
		
		public TextView getTakeoff() {
			if (null == takeoff) {
				takeoff = (TextView) mRow.findViewById(R.id.programSubtitle);
			}
			return takeoff;
		}
		
		public TextView getLanding() {
			if (null == landing) {
				landing = (TextView) mRow.findViewById(R.id.programGuest);
			}
			return landing;
		}
		
		public TextView getFlight() {
			if (null == flight) {
				flight = (TextView) mRow.findViewById(R.id.programAuthor);
			}
			return flight;
		}
		
		
	}
	
}
