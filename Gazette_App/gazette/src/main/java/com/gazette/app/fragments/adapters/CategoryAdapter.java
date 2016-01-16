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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gazette.app.GazetteApplication;
import com.gazette.app.R;
import com.gazette.app.model.Category;

import java.util.List;

public class CategoryAdapter extends
        RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Activity mActivity;
    private TextView mName;
    private ListOnclickListener mListener;
    private List<Category> mCategoriesLists;
    private ImageView mBaselineJpegView;
    private RelativeLayout mCounter_layout;
    private TextView mCounterText;

    public CategoryAdapter(Activity activity, List<Category> categoriesList) {
        mActivity = activity;
        mCategoriesLists = categoriesList;
        mListener = new ListOnclickListener();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mCategoryCard;

        public ViewHolder(View v) {
            super(v);
            mCategoryCard = (LinearLayout) v;
        }

    }

    @Override
    public int getItemCount() {
        if (null != mCategoriesLists) {
            return mCategoriesLists.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mBaselineJpegView = (ImageView) holder.mCategoryCard.findViewById(R.id.baseline_jpeg);
        mName = (TextView) holder.mCategoryCard.findViewById(R.id.name);
        mCounter_layout = (RelativeLayout) holder.mCategoryCard.findViewById(R.id.count_layout);
        mCounterText = (TextView) holder.mCategoryCard.findViewById(R.id.count);
        Typeface font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Muli-Regular.ttf");
        mName.setTypeface(font);
        Category category = mCategoriesLists.get(position);
        if (null != category) {
            if (category.getCount() > 0) {
                mCounterText.setText(String.valueOf(category.getCount()));
                mCounter_layout.setVisibility(View.VISIBLE);
            } else {
                mCounter_layout.setVisibility(View.INVISIBLE);
            }
            mName.setText(category.getName());
            if (null != category.getImage()) {
                mBaselineJpegView.setImageResource(category.getImage().getSrc());
            }
            holder.mCategoryCard.setTag(category);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.category_layout, parent, false);
        v.setOnClickListener(mListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ListOnclickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (null != v.getTag()) {
                launchAddProductActivity(((Category) v.getTag()));
            }

        }

    }

    private void launchAddProductActivity(Category category) {
        GazetteApplication.getInstance().launchAddProductDetailsActivity(mActivity, category);
    }
}
