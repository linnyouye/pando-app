package com.yufeng.pando.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;


/**
 * Created by Lin Youye on 2016/4/9.
 */
public class MqttClient {

    private static final String TAG = "lyy-MqttClient";

    public static final int CONNECTING = 1;
    public static final int CONNECTED = 2;
    public static final int DISCONNECTED = 3;
    private static final int ONPUBLISH = 4;

    private String mAccessToken;
    private String mAccessAddress;
    private int mDeviceId;

    private MQTT mqtt;
    private MqttListener mListener;
    private Thread mConnectThread;

    private Handler mHandler;

    public MqttClient() {

        mqtt = new MQTT();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CONNECTING:
                    case CONNECTED:
                    case DISCONNECTED:
                        mListener.onStateChanged(msg.what);
                        break;
                    case ONPUBLISH:
                        mListener.onPublish((String) msg.obj);
                        break;
                }
            }
        };

    }

    public void setHost(String host, String token, int deviceid) {
        mAccessAddress = host;
        mAccessToken = token;
        mDeviceId = deviceid;
    }

    public void connect() {
        if (mConnectThread != null) {
            mConnectThread.interrupt();
            mConnectThread = null;
        }
        mConnectThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mqtt.setHost("tcp://" + mAccessAddress);
                    mqtt.setClientId(Integer.toHexString(mDeviceId));
                    mqtt.setUserName(Integer.toHexString(mDeviceId));
                    mqtt.setPassword(mAccessToken);
                    final CallbackConnection connection = mqtt.callbackConnection();
                    mHandler.obtainMessage(CONNECTING).sendToTarget();
                    connection.listener(new org.fusesource.mqtt.client.Listener() {


                        public void onConnected() {
                            mHandler.obtainMessage(CONNECTED).sendToTarget();
                        }

                        public void onDisconnected() {
                            mHandler.obtainMessage(DISCONNECTED).sendToTarget();
                        }

                        public void onFailure(Throwable value) {
                            value.printStackTrace();
                            mHandler.obtainMessage(DISCONNECTED).sendToTarget();
                        }

                        public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {

                            Log.i(TAG, "onPublish: " + bytesToHexString(msg.data));

                            Message message = new Message();
                            message.what = ONPUBLISH;
                            message.obj = bytesToHexString(msg.data);
                            mHandler.sendMessage(message);


                        }
                    });
                    connection.connect(new Callback<Void>() {
                        @Override
                        public void onSuccess(Void value) {

                        }

                        @Override
                        public void onFailure(Throwable value) {
                        }
                    });

                    // Wait forever..

                    synchronized (Listener.class) {
                        while (true)
                            Listener.class.wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mConnectThread.start();


    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public interface MqttListener {
        void onStateChanged(int state);

        void onPublish(String msg);
    }

    public void setListener(MqttListener l) {
        mListener = l;
    }

}
