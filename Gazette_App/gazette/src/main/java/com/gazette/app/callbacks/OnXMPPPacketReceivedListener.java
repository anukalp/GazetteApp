package com.gazette.app.callbacks;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Anil Gudigar on 12/4/2015.
 */
public interface OnXMPPPacketReceivedListener {

    public void onMessageReceived(Message message);
    public void onPresenceReceived(Presence presence);
    public void onIQReceived(IQ iq);
}
