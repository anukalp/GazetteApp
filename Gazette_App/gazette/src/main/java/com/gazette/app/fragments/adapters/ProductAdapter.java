package com.gazette.app.fragments.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class ProductAdapter extends
        RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Activity mActivity;
    private TextView mName;
    private ListOnclickListener mListener;
    private List<Product> mProductLists;
    private ImageView mBaselineJpegView;

    public ProductAdapter(Activity activity, List<Product> productsList) {
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
            mName.setText(mProductLists.get(position).getProductName());
            if (null != mProductLists.get(position)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            }
            if (null != mProductLists.get(position).getProductImage()) {
                mBaselineJpegView.setImageResource(mProductLists.get(position).getProductImage().getSrc());
            }
            holder.mProductCard.setTag(mProductLists.get(position));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.product_layout, parent, false);
        v.setOnClickListener(mListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ListOnclickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if(null!= v.getTag()){
                launchAddProductActivity(((Product) v.getTag()));
            }

        }

    }
    private void launchAddProductActivity(Product product) {
      GazetteApplication.getInstance().launchAddProductDetailsActivity(mActivity, product);
    }
}
