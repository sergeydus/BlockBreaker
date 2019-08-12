package com.example.blockbreaker;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NicknameDialog.NicknameDialogListener {
    public String username = "admin";
    public String password = "admin";
    private FirebaseAuth mAuth;
    public static float sumTotal;

    public static Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.20:3004");
        } catch (URISyntaxException e) {
            Log.e("SocketIo",e.getMessage());
        }
    }

    private Emitter.Listener OnNewNickname = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    Log.e("socketio","received:"+data);
                    Log.d("OnNewNick",data);
                    if(data.equals("Please select a nickname")){
                        Toast.makeText(MainActivity.this, data,
                                Toast.LENGTH_SHORT).show();
                        openDialog();
                    }
                    if(data.equals("ok")){
                        Toast.makeText(MainActivity.this, "Welcome!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, data,
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Oncreate MainActivity","Created");
        mSocket.on("change username", OnNewNickname);
//        mSocket.on("users", onlineUsersListener);
        mSocket.connect();
        Log.e("Soketio",mSocket.connected()?("Connected!"):"NotConnected!");
        mAuth = FirebaseAuth.getInstance();

        Log.d("oncreate:",mAuth.toString());
        setContentView(R.layout.activity_main);
        ((EditText)findViewById(R.id.username_edittext)).setText("kek@kek.kek");
        ((EditText)findViewById(R.id.password_edittext)).setText("kekkek");
        Button SubmitButton = findViewById(R.id.SubmitButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.connect();
                Log.e("Soketio", mSocket.connected() ? ("Connected!") : "NotConnected!");
                Log.e("Login", "Attemping sign in");
                EditText usernameView = findViewById(R.id.username_edittext);
                EditText passView = findViewById(R.id.password_edittext);
                String userpass = passView.getText().toString();
                final String userid = usernameView.getText().toString();
                if (!userpass.isEmpty() && !userid.isEmpty()) {
                    Log.e("Login","both not null");
                    mAuth.signInWithEmailAndPassword(userid, userpass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.e("Login", "Attemping sign in with:[" + userid + ',' + password + ']');
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.e("Login:", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            mSocket.emit("change username", user.getUid());
                                            //openNoteActivity();
                                        }
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
        Button RegisterButton = findViewById(R.id.RegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameView= findViewById(R.id.username_edittext);
                EditText passView= findViewById(R.id.password_edittext);
                final String userpass = passView.getText().toString();
                final String userid = usernameView.getText().toString();
                if(userpass.equals("")||userid.equals("")){
                    Toast.makeText(MainActivity.this, "Email or password missing.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(userid, userpass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Register:", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user!=null){
                                        //openNoteActivity();
                                        JSONObject json = new JSONObject();
                                        try {
                                            json.put("email", userid);
                                            json.put("password", userpass);
                                            json.put("uid", mAuth.getUid());
                                            mSocket.emit("NewLogin", json);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mSocket.emit("Nickname", json);
                                        mSocket.emit("change username", user.getUid());
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Register:", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser!=null) {
            Log.d("onStart:", currentUser.toString());
            mAuth.signOut();
//            mSocket.emit("change username", mAuth.getCurrentUser().getUid());
            //openNoteActivity();
        }
    }/*


    public void openNoteActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        //Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        startActivity(intent);
    }*/
    public void openDialog(){
        NicknameDialog nicknameDialog = new NicknameDialog();
        nicknameDialog.show(getSupportFragmentManager(),"nickname Dialog");
    }

    @Override
    public void applyTexts(String nickname) {
        Log.e("In ApplyTexts","nick:"+nickname);
        JSONObject json = new JSONObject();
        try {
            json.put("nickname", nickname);
            json.put("uid", mAuth.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("Nickname", json);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy MainActivity","Destroyed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onSTOP mainactivity","Stopped");
    }
}
