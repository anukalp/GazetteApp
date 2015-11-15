package com.gazette.app.fragments.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gazette.app.GazetteApplication;
import com.gazette.app.R;
import com.gazette.app.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anil Gudigar on 11/12/2015.
 */
public class SubProductAdapter extends RecyclerView.Adapter<SubProductAdapter.ViewHolder> {

    private Activity mActivity;
    private TextView mName;
    private ListOnclickListener mListener;
    private List<Product> mProductLists;
    private ImageView mBaselineJpegView;

    public SubProductAdapter(Activity activity, List<Product> productsList) {
        mActivity = activity;
        mProductLists = productsList;
        mListener = new ListOnclickListener();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mProductCard;

        public ViewHolder(View v) {
            super(v);
            mProductCard = (LinearLayout) v;
        }

    }

    @Override
    public int getItemCount() {
        if (null != mProductLists) {
            return mProductLists.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mBaselineJpegView = (ImageView) holder.mProductCard.findViewById(R.id.baseline_jpeg);
        mName = (TextView) holder.mProductCard.findViewById(R.id.name);
        Typeface font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ProximaNova-Semibold.otf");
        mName.setTypeface(font);
        if (null != mProductLists.get(position)) {
            mName.setText(mProductLists.get(position).getTitle());
            if (null != mProductLists.get(position)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            }
            if (null != mProductLists.get(position).getImages() && null != mProductLists.get(position).getImages().get(0)) {
                mBaselineJpegView.setImageResource(mProductLists.get(position).getImages().get(0).getSrc());
            }
            holder.mProductCard.setTag(mProductLists.get(position));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.subproduct_layout, parent, false);
        v.setOnClickListener(mListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ListOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (null != v.getTag()) {
            }

        }

    }

}
