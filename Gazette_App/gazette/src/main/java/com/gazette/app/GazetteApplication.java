package com.gazette.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.gazette.app.callbacks.OTPVerifySuccessListener;
import com.gazette.app.callbacks.OnProductAddedListener;
import com.gazette.app.callbacks.OnXMPPPacketReceivedListener;
import com.gazette.app.callbacks.ProductScannerListener;
import com.gazette.app.model.Category;
import com.gazette.app.model.opt.OTPVerificationResponseModel;
import com.gazette.app.utils.GazetteConstants;
import com.gazette.app.utils.SharedPreferenceManager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import de.duenndns.ssl.MemorizingTrustManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Anil Gudigar on 11/10/2015.
 */
public class GazetteApplication extends Application implements ConnectionListener, StanzaListener, PingFailedListener {


    private static GazetteApplication _instance;
    private ArrayList<OTPVerifySuccessListener> otpVerifySuccessListenerList = null;
    private ArrayList<ProductScannerListener> productScannerListenersList = null;
    private ArrayList<OnProductAddedListener> onProductAddedListenersList = null;
    private ArrayList<OnXMPPPacketReceivedListener> OnXMPPPacketRecivedList = null;
    private ConnectTOJabberTask mConnectTOJabberTask = null;
    private final ArrayList<Object> registeredManagers;
    private final AcceptAll ACCEPT_ALL = new AcceptAll();
    private XMPPTCPConnection mXmpptcpConnection;
    private XMPPConnection mXmppConnection;
    private boolean isJabberSetupDone = false;
    /**
     * Thread to execute tasks in background..
     */
    private final ExecutorService backgroundExecutor;
    /**
     * Handler to execute runnable in UI thread.
     */
    private final Handler handler;
    private SharedPreferenceManager pref;

    public GazetteApplication() {
        registeredManagers = new ArrayList<>();
        handler = new Handler();

        backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Background executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Anil", "GazetteApplication onCreate");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Muli-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        otpVerifySuccessListenerList = new ArrayList<>();
        productScannerListenersList = new ArrayList<>();
        onProductAddedListenersList = new ArrayList<>();
        OnXMPPPacketRecivedList = new ArrayList<>();
        pref = new SharedPreferenceManager(getApplicationContext());
        mConnectTOJabberTask = new ConnectTOJabberTask();
        _instance = this;
    }

    public static GazetteApplication getInstance() {
        return _instance;
    }

    public void setupJabber() {
        if(!isJabberSetupDone)
        mConnectTOJabberTask.execute();
    }

    private void setConnection() {
        Log.i("Anil", "SetXMPP Connection");
        try {
            XMPPTCPConnectionConfiguration.Builder connectionConfiguration = XMPPTCPConnectionConfiguration.builder();
            connectionConfiguration.setServiceName(GazetteConstants.Jabber.DOMAIN);
            connectionConfiguration.setPort(GazetteConstants.Jabber.PORT);
            connectionConfiguration.setHost(GazetteConstants.Jabber.HOST);
            Log.i("Anil", "Username :" + pref.getName() + " Mobile:" + pref.getMobileNumber());
            connectionConfiguration.setUsernameAndPassword(pref.getName() + "-" + pref.getMobileNumber(), pref.getMobileNumber());
            connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
            connectionConfiguration.setDebuggerEnabled(true);
            connectionConfiguration.setCompressionEnabled(true);
            TLSUtils.acceptAllCertificates(connectionConfiguration);

            connectionConfiguration.setResource("Gazette");

            SSLContext sc = SSLContext.getInstance("TLS");
            MemorizingTrustManager mtm = new MemorizingTrustManager(this);
            sc.init(null, new X509TrustManager[]{mtm}, new java.security.SecureRandom());
            connectionConfiguration.setCustomSSLContext(sc);
            connectionConfiguration.setHostnameVerifier(
                    mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));

            mXmpptcpConnection = new XMPPTCPConnection(connectionConfiguration.build());
            mXmpptcpConnection.addAsyncStanzaListener(this, ACCEPT_ALL);
            mXmpptcpConnection.addConnectionListener(this);
            mXmpptcpConnection.setPacketReplyTimeout(30000);
            ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
            ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, new DeliveryReceiptRequest().getNamespace(), new DeliveryReceiptRequest.Provider());
            mXmpptcpConnection.connect();
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | SmackException | XMPPException ex) {
            Log.i("Anil", "XMPP Connection exception :" + ex.getMessage());
        }

    }

    public String sendMessage(String message, String to) {
        Log.i("XMPPClient", "Sending text [" + message + "] to [" + to + "]");
        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(message);
        msg.addExtension(new DeliveryReceipt(msg.getPacketID()));
        try {
            mXmppConnection.sendStanza(msg);
            DeliveryReceiptManager.addDeliveryReceiptRequest(msg);
        } catch (SmackException.NotConnectedException ex) {
            Log.i("Anil", "Send message exception:" + ex.getMessage());
        }
        return msg.getStanzaId();
    }

    public void launchAddProductDetailsActivity(Activity activity, Category category) {
        Intent intent = new Intent(this, GazetteAddProductActivity.class);
        Log.i("Anil", "category id :" + category.getName());
        intent.putExtra(GazetteConstants.PRODUCT_ID, category.getName());
        activity.startActivity(intent);
    }

    public void launchLoginActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteLoginActivity.class);
        activity.startActivity(intent);
    }

    public void launchSpalshActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteSplashActivity.class);
        activity.startActivity(intent);
    }

    public void launchMainActivity(Activity activity) {
        Intent intent = new Intent(this, GazetteMainActivity.class);
        activity.startActivity(intent);
    }


    public void addotpVerifySuccessListener(OTPVerifySuccessListener onOtpVerifySuccessListener) {
        otpVerifySuccessListenerList.add(onOtpVerifySuccessListener);
    }

    public void removeonotpVerifySuccessListener(OTPVerifySuccessListener onOtpVerifySuccessListener) {
        otpVerifySuccessListenerList.remove(onOtpVerifySuccessListener);
    }

    public void notifyAllonotpVerifySuccessListener(OTPVerificationResponseModel otpVerificationResponseModel) {
        for (OTPVerifySuccessListener callback : otpVerifySuccessListenerList) {
            callback.OnOTPSuccess(otpVerificationResponseModel);
        }
    }

    public void addProductScannerListener(ProductScannerListener onProductScannerListener) {
        productScannerListenersList.add(onProductScannerListener);
    }

    public void removeProductScannerListener(ProductScannerListener onProductScannerListener) {
        productScannerListenersList.remove(onProductScannerListener);
    }

    public void notifyAllProductScannerListener() {
        for (ProductScannerListener callback : productScannerListenersList) {
            callback.OnProductInfoUpdate();
        }
    }

    public void addXMPPPacketReceivedListener(OnXMPPPacketReceivedListener onXMPPPacketReceivedListener) {
        OnXMPPPacketRecivedList.add(onXMPPPacketReceivedListener);
    }

    public void removeXMPPPacketReceivedListener(OnXMPPPacketReceivedListener onXMPPPacketReceivedListener) {
        OnXMPPPacketRecivedList.remove(onXMPPPacketReceivedListener);
    }

    public void notifyAllXMPPPacketMessageReceivedListener(Message message) {
        for (OnXMPPPacketReceivedListener callback : OnXMPPPacketRecivedList) {
            callback.onMessageReceived(message);
        }
    }

    public void notifyAllonReceiptRecived(Message message) {
        for (OnXMPPPacketReceivedListener callback : OnXMPPPacketRecivedList) {
            callback.onMessageDelivered(message);
        }
    }

    public void addOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        onProductAddedListenersList.add(onProductAddedListener);
    }

    public void removeOnProductAddedListener(OnProductAddedListener onProductAddedListener) {
        onProductAddedListenersList.remove(onProductAddedListener);
    }

    public void notifyAllonProductAddedListenerr() {
        for (OnProductAddedListener callback : onProductAddedListenersList) {
            callback.OnProductAdded();
        }
    }


    @Override
    public void pingFailed() {
        Log.i("Anil", "pingFailed");
    }

    @Override
    public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
        Log.i("Anil", "process Packet Received: " + stanza.getFrom());
        Log.i("Anil", "process Packet Received: " + stanza.getTo());
        if (stanza instanceof Presence) {
            Log.i("Anil", "process Packet Presence");
        }
        if (stanza instanceof Message) {
            Message message = (Message) stanza;
            Log.i("Anil", "process Packet Message : " + message.getTo() + ":" + message.getFrom() + ":" + message.getBody());
            if (null != message.getBody())
                notifyAllXMPPPacketMessageReceivedListener(message);
        }

        if (stanza instanceof IQ) {
            Log.i("Anil", "process Packet IQ : ");
            IQ iq = (IQ) stanza;
            String packetId = iq.getStanzaId();
            if (packetId != null && (iq.getType() == IQ.Type.result || iq.getType() == IQ.Type.error)) {
                if (iq.getType() == IQ.Type.result) {
                    Log.i("Anil", "process Packet IQ result :" + packetId);
                } else {
                    Log.i("Anil", "process Packet IQ Error :" + packetId);
                }

            }
        }
    }

    class ConnectTOJabberTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            setConnection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isJabberSetupDone = true;
        }
    }

    /**
     * Register new manager.
     */
    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }


    /**
     * Submits request to be executed in background.
     */
    public void runInBackground(final Runnable runnable) {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * Submits request to be executed in UI thread.
     */
    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * Submits request to be executed in UI thread.
     */
    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }


    @Override
    public void connected(XMPPConnection connection) {
        Log.i("Anil", "connected");
        mXmppConnection = connection;
        try {
            mXmpptcpConnection.login();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DeliveryReceiptManager.setDefaultAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
        DeliveryReceiptManager.getInstanceFor(connection).autoAddDeliveryReceiptRequests();
        DeliveryReceiptManager.getInstanceFor(connection).addReceiptReceivedListener(new ReceiptReceivedListener() {
            @Override
            public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {
                Log.i("Anil", "onReceiptReceived :" + fromJid + "-> " + toJid + " :" + receiptId);
                DeliveryReceipt receiptdata = receipt.getExtension(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE);

                if (receiptdata == null) {
                    return;
                }
                notifyAllonReceiptRecived((Message) receipt);
            }
        });

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.i("Anil", "authenticated");
        mXmppConnection = connection;
    }

    @Override
    public void connectionClosed() {
        Log.i("Anil", "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("Anil", "connectionClosedOnError");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.i("Anil", "reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.i("Anil", "reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.i("Anil", "reconnectionFailed");
    }

    /**
     * Filter to accept all packets.
     *
     * @author alexander.ivanov
     */
    static class AcceptAll implements StanzaFilter {
        @Override
        public boolean accept(Stanza packet) {
            return true;
        }
    }
}
