package com.example.blockbreaker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MatchDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button multiplayer, singleplayer;

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
                break;
            case R.id.MultiplayerButton:
//                dismiss();
                Log.e("MatchModal","Multiplayer");
                Intent intent = new Intent(c, GameActivity.class);
                c.startActivity(intent);
                break;
            default:
                break;
        }
        dismiss();
    }
}