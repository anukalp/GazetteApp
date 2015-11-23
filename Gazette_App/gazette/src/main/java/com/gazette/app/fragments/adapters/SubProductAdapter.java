package com.gazette.app.fragments.adapters;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gazette.app.R;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.provider.GazetteDatabaseHelper;

import java.util.List;

/**
 * Created by Anil Gudigar on 11/12/2015.
 */
public class SubProductAdapter extends RecyclerView.Adapter<SubProductAdapter.ViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {

    private AppCompatActivity mActivity;
    private TextView mName;
    private ListOnclickListener mListener;
    private ImageView mBaselineJpegView;
    private static final int LOADER_ID_TABLE = 1;
    private Cursor dataCursor = null;

    public SubProductAdapter(AppCompatActivity activity, List<Product> productsList) {
        mActivity = activity;
        mListener = new ListOnclickListener();
        mActivity.getSupportLoaderManager().initLoader(LOADER_ID_TABLE, null,
                this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int Loaderid, Bundle args) {
        switch (Loaderid) {
            case LOADER_ID_TABLE:
                return new CursorLoader(mActivity,
                        GazetteDatabaseHelper.Views.PRODUCT_DATA_CONTENT_URI, null, null, null,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_TABLE:
                if(null != data){
                    Log.i("Anil", " onLoadFinished Table Count:" + data.getCount());
                    swapCursor(data);
                }else{
                    Log.i("Anil", " onLoadFinished data null");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Log.i("Anil", " swapCursor :" + cursor.getCount());
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            Log.i("Anil", " notifyDataSetChanged :" + cursor.getCount());
            this.notifyDataSetChanged();
        }
        return oldCursor;
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
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);
        Product product = buildProductObject(dataCursor);
        mBaselineJpegView = (ImageView) holder.mProductCard.findViewById(R.id.baseline_jpeg);
        mName = (TextView) holder.mProductCard.findViewById(R.id.name);
        Typeface font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ProximaNova-Semibold.otf");
        mName.setTypeface(font);
        if (null != product) {
            mName.setText(product.getProductName());
            if (null != product.getProductInvoice() && null != product.getProductInvoice().getBitmap()) {
                mBaselineJpegView.setImageBitmap(product.getProductInvoice().getBitmap());
            }
            holder.mProductCard.setTag(product);
        }

    }

    private Product buildProductObject(Cursor dataCursor) {
        Product product = new Product();
        String mTitle = dataCursor.getString(dataCursor
                .getColumnIndex("product_name"));
        product.setProductName(mTitle);
        byte[] byteArray = dataCursor.getBlob(dataCursor
                .getColumnIndex("photo"));
        if (null != byteArray) {
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Image productInvoice = new Image();
            productInvoice.setBitmap(bm);
            product.setProductInvoice(productInvoice);
        }
        return product;
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
