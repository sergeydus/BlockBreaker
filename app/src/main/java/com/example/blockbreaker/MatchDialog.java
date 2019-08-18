package com.example.blockbreaker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;

public class MatchDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button multiplayer, singleplayer;
    private Socket mSocket;
    public static boolean check = false;

    public MatchDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.matchdialog);
        mSocket= MainActivity.mSocket;
        multiplayer = (Button) findViewById(R.id.MultiplayerButton);
        singleplayer= (Button) findViewById(R.id.SinglePlayerButton);
        Log.e("IS it null?",(multiplayer==null)?("is null"):("not null"));
        multiplayer.setOnClickListener(this);
        singleplayer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SinglePlayerButton:
//                c.finish();
                Log.e("MatchModal","SinglePlayer");
                mSocket.emit("matchvsai","test");
                break;
            case R.id.MultiplayerButton:
//                dismiss();
                if(check==false)
                {
                    check=true;
                    Log.e("Check","check is now true");
                    Toast.makeText(MatchDialog.super.getContext(), "Searching for match",
                            Toast.LENGTH_LONG).show();
                    mSocket.emit("quickmatch","test");

                }else {
                    check=false;
                    Toast.makeText(MatchDialog.super.getContext(), "Canceling search for match",
                            Toast.LENGTH_LONG).show();
                    mSocket.emit("stopqueue","test");
                    Log.e("Check","check is now false");
                }
                Log.e("MatchModal","Multiplayer");
              //  Intent intent = new Intent(c, GameActivity.class); sergay noob
           //     c.startActivity(intent);

                break;
            default:
                break;
        }
        dismiss();
    }
}