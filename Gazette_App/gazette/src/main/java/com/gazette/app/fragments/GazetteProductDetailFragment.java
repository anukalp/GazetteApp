package com.gazette.app.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gazette.app.GazetteApplication;
import com.gazette.app.GazetteProductDetailActivity;
import com.gazette.app.R;
import com.gazette.app.callbacks.OnXMPPPacketReceivedListener;
import com.gazette.app.fragments.adapters.ConversationAdapter;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.model.chat.Message;
import com.gazette.app.provider.GazetteDatabaseHelper;
import com.gazette.app.utils.ChatUtils;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a
 * in two-pane mode (on tablets) or a {@link GazetteProductDetailActivity}
 * on handsets.
 */
public class GazetteProductDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnXMPPPacketReceivedListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String TAG = "Anil";
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
    private EditText inputMsg;
    // Client name
    private String name = "Me";
    private ChatUtils utils;
    // Chat messages list adapter

    private List<Message> listMessages;
    private RecyclerView listViewMessages;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConversationAdapter adapter;
    private String delegate = "hh:mm aaa";


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
        GazetteApplication.getInstance().addXMPPPacketReceivedListener(this);
        utils = new ChatUtils(getActivity().getApplicationContext());
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
        listViewMessages = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        listViewMessages.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        listViewMessages.setLayoutManager(mLayoutManager);

        listMessages = new ArrayList<Message>();
        String time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        final Message m = new Message("Service Person", "Hello Sir, \nHow could i assist you today.\n", time, false);
        listMessages.add(m);
        adapter = new ConversationAdapter(getActivity(), listMessages);
        listViewMessages.setAdapter(adapter);

        inputMsg = (EditText) rootView.findViewById(R.id.inputMsg);
        inputMsg.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
                    Message message = new Message();
                    message.setSelf(true);
                    message.setFromName("Me");
                    message.setMessage(inputMsg.getText().toString());
                    message.setTime(time);
                    String msgId = GazetteApplication.getInstance().sendMessage(inputMsg.getText().toString(), "9968713449@ec2-52-11-139-107.us-west-2.compute.amazonaws.com");
                    message.setMsgID(msgId);
                    appendMessage(message);
                    inputMsg.setText("");
                    return true;
                }
                return false;
            }
        });
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

            if (null != mProduct.getProductName())
                product_name.setText(mProduct.getProductName().isEmpty() ? getString(R.string.missing) : mProduct.getProductName());
            if (null != mProduct.getProductPurchaseDate())
                purchase_date.setText(mProduct.getProductPurchaseDate().isEmpty() ? getString(R.string.missing) : mProduct.getProductPurchaseDate());
            if (null != mProduct.getProductBrand())
                model_number.setText(mProduct.getProductBrand().isEmpty() ? getString(R.string.missing) : mProduct.getProductBrand());
            if (null != mProduct.getProductVendor())
                place_of_purchase.setText(mProduct.getProductVendor().isEmpty() ? getString(R.string.missing) : mProduct.getProductVendor());
            if (null != mProduct.getProductSerialNumber())
                serial_number.setText(mProduct.getProductSerialNumber().isEmpty() ? getString(R.string.missing) : mProduct.getProductSerialNumber());
            if (null != mProduct.getProductPrice())
                price.setText(mProduct.getProductPrice().isEmpty() ? getString(R.string.missing) : mProduct.getProductPrice());
            if (null != mProduct.getProductWarrantyDuration())
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


    private void showToast(final String message) {
        if (null != getActivity()) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Snackbar.make(inputMsg, message,
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Appending message to list view
     */
    private void appendMessage(final Message m) {
        if (null != getActivity()) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    listMessages.add(m);
                    adapter.notifyDataSetChanged();
                    // Playing device's notification
                    playBeep();
                }
            });
        }
    }

    /**
     * Plays device's default notification sound
     */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GazetteApplication.getInstance().removeXMPPPacketReceivedListener(this);
    }

    @Override
    public void onMessageReceived(org.jivesoftware.smack.packet.Message message) {
        String time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        Message currentmessage = new Message();
        currentmessage.setSelf(false);
        String[] From = message.getFrom().split("@");
        if (null != From && From.length > 1) {
            currentmessage.setFromName(From[0]);
        } else {
            currentmessage.setFromName("Service Person");
        }

        currentmessage.setMessage(message.getBody());
        currentmessage.setTime(time);
        appendMessage(currentmessage);
    }

    @Override
    public void onPresenceReceived(Presence presence) {

    }

    @Override
    public void onIQReceived(IQ iq) {

    }

    @Override
    public void onMessageDelivered(org.jivesoftware.smack.packet.Message msg) {
        DeliveryReceipt receiptdata = msg.getExtension(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE);
        Log.i("Anil", "ID:" + receiptdata.getId());
        Log.i("Anil", "Send Message :" + listMessages.get(listMessages.size() - 1).getMsgID());

        for (Message message : listMessages) {
            if (receiptdata.getId().equalsIgnoreCase(message.getMsgID())) {
                Log.i("Anil", "ID:"+message.getMsgID()+"  equal "+receiptdata.getId());
                message.setIsDelivered(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
