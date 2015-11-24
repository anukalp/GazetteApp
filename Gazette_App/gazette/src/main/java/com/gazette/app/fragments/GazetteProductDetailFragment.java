package com.gazette.app.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gazette.app.GazetteProductDetailActivity;
import com.gazette.app.R;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.provider.GazetteDatabaseHelper;


/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a
 * in two-pane mode (on tablets) or a {@link GazetteProductDetailActivity}
 * on handsets.
 */
public class GazetteProductDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private String mTitle;
    private LinearLayout product_details_layout;
    private LinearLayout chat_layout;
    private static final int LOADER_ID_TABLE = 1;
    private Product mProduct;
    private ImageView invoice_image;
    private TextView purchase_date;
    private TextView model_number;
    private TextView place_of_purchase;
    private TextView serial_number;
    private TextView price;
    private TextView warranty_end_date;
    private TextView product_name;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GazetteProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mTitle = getArguments().getString(ARG_ITEM_ID);
        }
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_TABLE, null,
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        // Show the dummy content as text in a TextView.
        chat_layout = (LinearLayout) rootView.findViewById(R.id.chat_layout);
        product_details_layout = (LinearLayout) rootView.findViewById(R.id.product_details_layout);
        product_name = (TextView) rootView.findViewById(R.id.product_name);
        invoice_image = (ImageView) rootView.findViewById(R.id.invoice_image);
        purchase_date = (TextView) rootView.findViewById(R.id.purchase_date);
        model_number = (TextView) rootView.findViewById(R.id.model_number);
        place_of_purchase = (TextView) rootView.findViewById(R.id.place_of_purchase);
        serial_number = (TextView) rootView.findViewById(R.id.serial_number);
        price = (TextView) rootView.findViewById(R.id.price);
        warranty_end_date = (TextView) rootView.findViewById(R.id.warranty_end_date);

        return rootView;
    }

    public void lunchChatview() {
        product_details_layout.setVisibility(View.GONE);
        chat_layout.setVisibility(View.VISIBLE);
    }

    public void hideChatview() {
        product_details_layout.setVisibility(View.VISIBLE);
        chat_layout.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int Loaderid, Bundle args) {
        switch (Loaderid) {
            case LOADER_ID_TABLE:
                Log.i("Anil", "Product Id :" + mTitle);
                String selection = "product_id = ?";
                String[] selectionArgs = {mTitle};
                return new CursorLoader(getActivity(),
                        GazetteDatabaseHelper.Views.PRODUCT_DATA_CONTENT_URI, null, selection, selectionArgs,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        buildProductObject(data);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && null != mProduct) {
            appBarLayout.setTitle(mProduct.getProductName());
            if (null != mProduct.getProductInvoice() && null != mProduct.getProductInvoice().getBitmap())
                invoice_image.setImageBitmap(mProduct.getProductInvoice().getBitmap());

            if(null != mProduct.getProductName())
            product_name.setText(mProduct.getProductName().isEmpty() ? getString(R.string.missing) : mProduct.getProductName());
            if(null != mProduct.getProductPurchaseDate())
            purchase_date.setText(mProduct.getProductPurchaseDate().isEmpty() ? getString(R.string.missing) : mProduct.getProductPurchaseDate());
            if(null != mProduct.getProductBrand())
            model_number.setText(mProduct.getProductBrand().isEmpty() ? getString(R.string.missing) : mProduct.getProductBrand());
            if(null != mProduct.getProductVendor())
                place_of_purchase.setText(mProduct.getProductVendor().isEmpty() ? getString(R.string.missing) : mProduct.getProductVendor());
            if(null != mProduct.getProductSerialNumber())
            serial_number.setText(mProduct.getProductSerialNumber().isEmpty() ? getString(R.string.missing) : mProduct.getProductSerialNumber());
            if(null != mProduct.getProductPrice())
            price.setText(mProduct.getProductPrice().isEmpty() ? getString(R.string.missing) : mProduct.getProductPrice());
            if(null != mProduct.getProductWarrantyDuration())
                warranty_end_date.setText(mProduct.getProductWarrantyDuration().isEmpty() ? getString(R.string.missing) : mProduct.getProductWarrantyDuration());
        }
    }

    private void buildProductObject(Cursor dataCursor) {
        dataCursor.moveToFirst();
        if (null != dataCursor && dataCursor.getCount() > 0) {
            mProduct = new Product();

            mProduct.setProductId(dataCursor.getInt(dataCursor
                    .getColumnIndex("product_id")));

            mProduct.setProductName(dataCursor.getString(dataCursor
                    .getColumnIndex("product_name")));

            byte[] byteArray = dataCursor.getBlob(dataCursor
                    .getColumnIndex("photo"));

            mProduct.setProductCategory(dataCursor.getString(dataCursor
                    .getColumnIndex("category")));

            mProduct.setProductPrice(dataCursor.getString(dataCursor
                    .getColumnIndex("amount")));

            mProduct.setProductVendor(dataCursor.getString(dataCursor
                    .getColumnIndex("purchase_place")));

            mProduct.setProductVendor(dataCursor.getString(dataCursor
                    .getColumnIndex("product_code")));

            mProduct.setProductBarCode(dataCursor.getString(dataCursor
                    .getColumnIndex("barcode")));

            mProduct.setProductBrand(dataCursor.getString(dataCursor
                    .getColumnIndex("brand")));

            mProduct.setProductPurchaseDate(dataCursor.getString(dataCursor
                    .getColumnIndex("purchase_date")));

            if (null != byteArray) {
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Image productInvoice = new Image();
                productInvoice.setBitmap(bm);
                mProduct.setProductInvoice(productInvoice);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
