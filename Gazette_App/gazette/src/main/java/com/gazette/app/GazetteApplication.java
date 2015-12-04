package com.gazette.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.gazette.app.callbacks.OTPVerifySuccessListener;
import com.gazette.app.callbacks.OnProductAddedListener;
import com.gazette.app.callbacks.ProductScannerListener;
import com.gazette.app.model.Category;
import com.gazette.app.model.opt.OTPVerificationResponseModel;
import com.gazette.app.utils.GazetteConstants;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.ping.PingFailedListener;

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
    private ConnectTOJabberTask mConnectTOJabberTask = null;
    private final ArrayList<Object> registeredManagers;
    private final AcceptAll ACCEPT_ALL = new AcceptAll();
    private  XMPPTCPConnection mXmpptcpConnection;
    /**
     * Thread to execute tasks in background..
     */
    private final ExecutorService backgroundExecutor;
    /**
     * Handler to execute runnable in UI thread.
     */
    private final Handler handler;

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
                        .setDefaultFontPath("fonts/ProximaNovaregular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        otpVerifySuccessListenerList = new ArrayList<>();
        productScannerListenersList = new ArrayList<>();
        onProductAddedListenersList = new ArrayList<>();
        mConnectTOJabberTask = new ConnectTOJabberTask();

        _instance = this;
    }

    public static GazetteApplication getInstance() {
        return _instance;
    }

    public void setupJabber() {
        mConnectTOJabberTask.execute();
    }

    private void setConnection() {
        Log.i("Anil", "SetXMPP Connection");
        try {
            XMPPTCPConnectionConfiguration.Builder connectionConfiguration = XMPPTCPConnectionConfiguration.builder();
            connectionConfiguration.setServiceName(GazetteConstants.Jabber.DOMAIN);
            connectionConfiguration.setPort(GazetteConstants.Jabber.PORT);
            connectionConfiguration.setHost(GazetteConstants.Jabber.HOST);
            connectionConfiguration.setUsernameAndPassword("7090606857", "anil@123");
            connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
            connectionConfiguration.setDebuggerEnabled(true);
            connectionConfiguration.setCompressionEnabled(true);
            TLSUtils.acceptAllCertificates(connectionConfiguration);

            connectionConfiguration.setResource("sender");

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
            mXmpptcpConnection.connect();
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | SmackException | XMPPException ex) {
            Log.i("Anil", "XMPP Connection exception :" + ex.getMessage());
        }

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
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

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
        try {
            mXmpptcpConnection.login();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.i("Anil", "authenticated");
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
