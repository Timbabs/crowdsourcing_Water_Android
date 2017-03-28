package com.gatech.edu.soloTechno.m4_login.model;

/**
 * Created by Joon.Y.K on 2017-03-28.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatech.edu.soloTechno.m4_login.R;

import java.util.List;

public class WaterPurityListAdapter extends RecyclerView.Adapter<WaterPurityListAdapter.ViewHolder> {

    private List<WaterPurityReportData> purityList;

    /**
     * Constructor to form adapter for Water Purity List
     * @param context this class context
     * @param waterPurityList list of water purity information
     */
    public WaterPurityListAdapter(Context context, List<WaterPurityReportData> waterPurityList) {
        purityList = waterPurityList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(LinearLayout v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.purity_text);
        }
    }

    /**
     * View Holder for the adapter
     * @param parent parent ViewGroup
     * @param viewType viewType not used
     * @return View Holder
     */
    @Override
    public WaterPurityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.water_purity_item, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;

    }

    /**
     * Sets the text for the display on recycler view
     * @param holder View Holder
     * @param position postion of the text
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(purityList.get(position).getReport());

    }

    /**
     * Gets the size of the recyclerview
     * @return size in integer
     */
    @Override
    public int getItemCount() {
        return purityList.size();
    }

}