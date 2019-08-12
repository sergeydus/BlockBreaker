package com.example.blockbreaker;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
public class SocketService {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.20:3004");
        } catch (URISyntaxException e) {
            Log.e("SocketIo",e.getMessage());
        }
    }
    void on(String EventName,Emitter.Listener listener){
        mSocket.on(EventName, listener);
    }

}
