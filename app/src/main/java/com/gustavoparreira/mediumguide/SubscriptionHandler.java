package com.gustavoparreira.mediumguide;

import android.content.Context;
import android.util.Log;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OnConnected;
import ibt.ortc.extensibility.OnDisconnected;
import ibt.ortc.extensibility.OnException;
import ibt.ortc.extensibility.OnMessage;
import ibt.ortc.extensibility.OnReconnected;
import ibt.ortc.extensibility.OnReconnecting;
import ibt.ortc.extensibility.OnSubscribed;
import ibt.ortc.extensibility.OnUnsubscribed;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

public class SubscriptionHandler {

    private Context context;
    private OrtcClient ortcClient;

    public SubscriptionHandler(Context _context) {
        context = _context;
        prepareOrtcClient();
    }

    private void prepareOrtcClient(){
        Ortc api = new Ortc();
        OrtcFactory factory;
        try {
            factory = api.loadOrtcFactory("IbtRealtimeSJ");
            ortcClient = factory.createClient();
            ortcClient.setApplicationContext(context);
            ortcClient.setGoogleProjectId(Config.PROJECTID);
            ortcClient.setClusterUrl(Config.CLUSTERURL);

            ortcClient.onConnected = new OnConnected(){
                @Override
                public void run(OrtcClient sender) {
                    Log.d("CONNECT", "Connected");
                }
            };

            ortcClient.onDisconnected = new OnDisconnected() {
                @Override
                public void run(OrtcClient sender) {
                    Log.i("CONNECT", "Disconnected");
                }
            };
            ortcClient.onSubscribed = new OnSubscribed(){
                @Override
                public void run(OrtcClient sender, String channel) {
                    Log.i("SUBSCRIBE", "Subscribed to " + channel);
                }
            };
            ortcClient.onUnsubscribed = new OnUnsubscribed(){
                @Override
                public void run(OrtcClient sender, String channel) {
                    Log.i("SUBSCRIBE", "Unsubscribed from " + channel);
                }
            };
            ortcClient.onException = new OnException(){
                @Override
                public void run(OrtcClient sender, Exception exc) {
                    Log.e("ERROR", "Exception " + exc.toString());
                }
            };
            ortcClient.onReconnected = new OnReconnected(){
                @Override
                public void run(OrtcClient sender) {
                    Log.i("CONNECT", "Reconnected");
                }
            };
            ortcClient.onReconnecting = new OnReconnecting(){
                @Override
                public void run(OrtcClient sender) {
                    Log.i("CONNECT", "Reconnecting");
                }
            };

            ortcClient.connect(Config.APPKEY, Config.TOKEN);

        } catch (Exception e) {
            Log.e("Exception ",e.toString());
        }
    }

    public void subscribeChannel(String channel) {
        if (ortcClient.getIsConnected()) {
            ortcClient.subscribeWithNotifications(channel, true, new OnMessage() {
                @Override
                public void run(OrtcClient ortcClient, String channel, String message) {
                    Log.d("MESSAGE", "New message received: " + message);
                }
            });
        } else {
            Log.i("NOT CONNECTED", "Can't subscribe channel because ortcClient isn't connected yet.");
        }
    }

    public void sendMessage(String channel, String message) {
        if (ortcClient.getIsConnected()) {
            ortcClient.send(channel, message);
        } else {
            Log.i("NOT CONNECTED", "Can't send message because ortcClient isn't connected yet.");
        }
    }

    public boolean isOrtcConnected() {
        return  ortcClient.getIsConnected();
    }

    public boolean isOrtcSubscribed(String channel) {
        return ortcClient.isSubscribed(channel);
    }
}
