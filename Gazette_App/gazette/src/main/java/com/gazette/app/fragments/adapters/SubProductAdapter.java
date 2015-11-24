package com.gazette.app.fragments.adapters;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gazette.app.GazetteBarCodeScanActivity;
import com.gazette.app.GazetteProductDetailActivity;
import com.gazette.app.fragments.GazetteProductDetailFragment;
import com.gazette.app.R;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.provider.GazetteDatabaseHelper;

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
    private LinearLayout addProductLayout;
    private LinearLayout ProductLayout;
    private RelativeLayout addItem;
    private String mCategory;
    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    public SubProductAdapter(AppCompatActivity activity, String category) {
        mActivity = activity;
        mListener = new ListOnclickListener();
        mCategory = category;
        mActivity.getSupportLoaderManager().initLoader(LOADER_ID_TABLE, null,
                this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int Loaderid, Bundle args) {
        switch (Loaderid) {
            case LOADER_ID_TABLE:
                String selection = "category = ?";
                String[] selectionArgs = {mCategory};
                return new CursorLoader(mActivity,
                        GazetteDatabaseHelper.Views.PRODUCT_DATA_CONTENT_URI, null, selection, selectionArgs,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_TABLE:
                if (null != data) {
                    Log.i("Anil", " onLoadFinished Table Count:" + data.getCount());
                    data.moveToFirst();
                    swapCursor(data);
                } else {
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
        return (dataCursor == null) ? 0 : dataCursor.getCount() + 1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = null;
        addProductLayout = (LinearLayout) holder.mProductCard.findViewById(R.id.add_product_layout);
        ProductLayout = (LinearLayout) holder.mProductCard.findViewById(R.id.product_layout);
        Typeface font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ProximaNova-Semibold.otf");
        if (position < dataCursor.getCount()) {
            dataCursor.moveToPosition(position);
            product = buildProductObject(dataCursor);
            addProductLayout.setVisibility(View.GONE);
            mBaselineJpegView = (ImageView) ProductLayout.findViewById(R.id.baseline_jpeg);
            mName = (TextView) ProductLayout.findViewById(R.id.name);
            mName.setTypeface(font);
            if (null != product) {
                mName.setText(product.getProductName());
                if (null != product.getProductInvoice() && null != product.getProductInvoice().getBitmap()) {
                    mBaselineJpegView.setImageBitmap(product.getProductInvoice().getBitmap());
                }
                holder.mProductCard.setTag(product);
            }
        } else {
            ProductLayout.setVisibility(View.GONE);
            product = new Product();
            product.setProductName("Other");
            Image img = new Image();
            img.setSrc(R.drawable.ic_other_product);
            product.setProductImage(img);
            addItem = (RelativeLayout) addProductLayout.findViewById(R.id.addItem);
            addItem.setOnClickListener(additemOnlick);
            mBaselineJpegView = (ImageView) addProductLayout.findViewById(R.id.baseline_jpeg);
            mName = (TextView) addProductLayout.findViewById(R.id.name);
            mName.setTypeface(font);
            if (null != product) {
                mName.setText(product.getProductName());
                mBaselineJpegView.setImageResource(product.getProductImage().getSrc());
                holder.mProductCard.setTag(product);
            }
        }


    }

    private Product buildProductObject(Cursor dataCursor) {
        Product product = new Product();
        String mTitle = dataCursor.getString(dataCursor
                .getColumnIndex("product_name"));
        product.setProductName(mTitle);
        byte[] byteArray = dataCursor.getBlob(dataCursor
                .getColumnIndex("photo"));
        product.setProductId(dataCursor.getInt(dataCursor
                .getColumnIndex("product_id")));
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
                Product product = (Product) v.getTag();
                if (!product.getProductName().equalsIgnoreCase("Other")) {
                    Intent intent = new Intent(mActivity, GazetteProductDetailActivity.class);
                    intent.putExtra(GazetteProductDetailFragment.ARG_ITEM_ID, String.valueOf(product.getProductId()));
                    mActivity.startActivity(intent);
                }
            }
        }
    }

    View.OnClickListener additemOnlick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            launchScanner();
        }
    };

    public void launchScanner() {
        if (isCameraAvailable()) {
            Intent intent = new Intent(mActivity, GazetteBarCodeScanActivity.class);
            mActivity.startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(mActivity, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = mActivity.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

}
