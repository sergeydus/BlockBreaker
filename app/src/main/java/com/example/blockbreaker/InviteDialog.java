package com.example.blockbreaker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class InviteDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button AcceptButton, DeclineButton;
    public String inviterName;
    public InviteDialog(Activity a,String inviterName) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.inviterName=inviterName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invitedialog);
        AcceptButton = (Button) findViewById(R.id.AcceptInviteButton);
        DeclineButton= (Button) findViewById(R.id.DeclineInvitationButton);
        Log.e("IS it null?",(AcceptButton==null)?("is null"):("not null"));
        AcceptButton.setOnClickListener(this);
        DeclineButton.setOnClickListener(this);
        TextView text =(TextView) findViewById(R.id.InviterNameTextView);
        text.setText(inviterName+" has challenged you!");

    }

    @Override
    public void onClick(View v) {
        JSONObject json = new JSONObject();
        switch (v.getId()) {
            case R.id.AcceptInviteButton:
//                c.finish();
                Log.e("InviteModal","Accept");
                try {
                    json.put("HasAccepted",true);
                    json.put("opponent",inviterName);
                    MainActivity.mSocket.emit("acceptgame",json);
                } catch (JSONException e) {
                    Log.e("AcceptInviteJson",e.getMessage());
                }
                break;
            case R.id.DeclineInvitationButton:
//                dismiss();
                Log.e("InviteModal","Decline");
                try {
                    json.put("opponent",inviterName);
                    json.put("HasAccepted",false);
                    MainActivity.mSocket.emit("acceptgame",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
        dismiss();
    }
}