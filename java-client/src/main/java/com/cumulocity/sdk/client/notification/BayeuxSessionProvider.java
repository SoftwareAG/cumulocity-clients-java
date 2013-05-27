package com.cumulocity.sdk.client.notification;

import org.cometd.bayeux.client.ClientSession;

import com.cumulocity.sdk.client.SDKException;

public interface BayeuxSessionProvider {

    ClientSession get() throws SDKException;

}
