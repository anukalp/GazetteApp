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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;
import com.gazette.app.GazetteProductDetailActivity;
import com.gazette.app.R;
import com.gazette.app.fragments.adapters.CategoryAdapter;
import com.gazette.app.fragments.adapters.ConversationAdapter;
import com.gazette.app.model.Image;
import com.gazette.app.model.Product;
import com.gazette.app.model.chat.Message;
import com.gazette.app.provider.GazetteDatabaseHelper;
import com.gazette.app.utils.ChatUtils;
import com.gazette.app.utils.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
    private static final String TAG = "Anil";
    public static final String URL_WEBSOCKET = "ws://192.168.1.47:8080/WebMobileGroupChatServer/chat?name=";
    public static final String ARG_ITEM_ID = "item_id";
    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";
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
    private WebSocketClient client;
    private EditText inputMsg;
    // Client name
    private String name = "Me";
    private ChatUtils utils;
    // Chat messages list adapter

    private List<Message> listMessages;
    private RecyclerView listViewMessages;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConversationAdapter adapter;

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
        Message m = new Message("Service Person", "Hello Sir, \nHow could i assist you today.\n", false);
        listMessages.add(m);
        adapter = new ConversationAdapter(getActivity(), listMessages);
        listViewMessages.setAdapter(adapter);

        inputMsg = (EditText) rootView.findViewById(R.id.inputMsg);
        inputMsg.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText()
                            .toString()));
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


        /**
         * Creating web socket client. This will have callback methods
         * */
        client = new WebSocketClient(URI.create(URL_WEBSOCKET
                + URLEncoder.encode(name)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {

            }

            /**
             * On receiving the message from web socket server
             * */
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);

            }

            @Override
            public void onMessage(byte[] data) {
                Log.d(TAG, String.format("Got binary message! %s",
                        bytesToHex(data)));

                // Message will be in JSON format
                parseMessage(bytesToHex(data));
            }

            /**
             * Called when the connection is terminated
             * */
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);

                showToast(message);

                // clear the session id from shared preferences
                utils.storeSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

                showToast("Error! : " + error);
            }

        }, null);

        client.connect();
    }

    public void hideChatview() {
        product_details_layout.setVisibility(View.VISIBLE);
        chat_layout.setVisibility(View.GONE);
        if (client != null & client.isConnected()) {
            client.disconnect();
        }
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


    /**
     * Method to send message to web socket server
     */
    private void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            Log.i("Anil", "message:" + message);
            client.send(message);
        }
    }

    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                Message m = new Message(fromName, message, isSelf);

                // Appending the message to chat list
                appendMessage(m);

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
