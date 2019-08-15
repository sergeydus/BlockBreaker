package com.example.blockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {
    public static String mynickname;
    private Socket mSocket;
    private ArrayList<ChatMessage> messages;
    private Emitter.Listener onlineUsersListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    ArrayList<User> players = new ArrayList<User>();
                    Log.e("OnlineUsers",((JSONObject)args[0]).toString());
                    Players onlineusers=gson.fromJson(((JSONObject)args[0]).toString(),Players.class);
                    players.addAll(onlineusers.users);
                    Log.e("Players",players.toString());
                    ListView onlineuserlist = findViewById(R.id.UsersListView);
                    onlineuserlist.setAdapter(new CustomUserAdapter(players));
                    TextView myname = findViewById(R.id.usernameTextView);
                    myname.setText(onlineusers.me.nickname.toString());
                    LobbyActivity.mynickname=onlineusers.me.nickname.toString();
                    TextView myloses = findViewById(R.id.LosesTextView);
                    myloses.setText("Losses:"+onlineusers.me.details.losses.toString());
                    TextView mywins = findViewById(R.id.WinsTextView);
                    mywins.setText("Wins:"+onlineusers.me.details.wins.toString());

                }
            });
        }
    };
    private Emitter.Listener MatchAcceptListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean res=(boolean)((JSONObject)args[0]).get("HasAccepted");
                        if(res==true){
                            Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        Log.e("MatchAcceptListener",e.getMessage());
                    }

                }
            });
        }
    };
    private Emitter.Listener MatchInviteListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String InviterName=(String)args[0];
                    Log.e("OnlineUsers",((String)args[0])+" Wants 2 play with ur body!");
                    InviteDialog cdd=new InviteDialog(LobbyActivity.this,InviterName);
                    cdd.show();
                }
            });
        }
    };
    private Emitter.Listener ChatMessageListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    Log.e("Recieved chat message","yes");
                    messages.add(gson.fromJson(((JSONObject)args[0]).toString(),ChatMessage.class));
                    ListView ChatMessageList = findViewById(R.id.ChatListView);
                    ChatMessageList.setAdapter(new CustomChatAdapter(messages));
                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        mSocket= MainActivity.mSocket;
        if(mSocket==null){
            Log.e("ONCREATE","MSOCKET == NULL!!!!!");
        }
        Log.d("Oncreate LobbyActivity","Created");
        Button SubmitButton = findViewById(R.id.SendTextButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lobbyedittext= findViewById(R.id.lobbyEditText);
                String text = lobbyedittext.getText().toString();
                if(!text.isEmpty()){
                    mSocket.emit("chatmessage",text);
//                    Log.e("LobbyChatButton",)
                    lobbyedittext.setText("");
                }
            }
        });
        Button FindMatchButton = findViewById(R.id.JoinQueueButton);
        FindMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchDialog cdd=new MatchDialog(LobbyActivity.this);
                cdd.show();
            }
        });
        messages= new ArrayList<>();
        mSocket.on("users", onlineUsersListener);
        mSocket.on("chatmessage", ChatMessageListener);
        mSocket.on("requestgame", MatchInviteListener);
        mSocket.on("acceptgame", MatchAcceptListener);
//        ListView onlineuserlist = findViewById(R.id.UsersListView);
//        while(MainActivity.players==null);
//         Log.e("onCreate",(MainActivity.players==null)?"Null":"not null");
//        onlineuserlist.setAdapter(new CustomUserAdapter(MainActivity.players));
    }
    class CustomUserAdapter extends ArrayAdapter<User>{
        ArrayList<User> players;
        public CustomUserAdapter(ArrayList<User> players){
            super(LobbyActivity.this,R.layout.customuser);
            Log.e("AdapterUser",(players==null)?"Null":"not null");
            this.players=players;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public User getItem(int position) {
            return players.get(position);
        }

        @Override
        public View getView(final int position, View recycledView, ViewGroup listView) {
            if(recycledView == null){
                recycledView = LayoutInflater.from(getContext())
                        .inflate(R.layout.customuser,null);
            }
            TextView username= recycledView.findViewById(R.id.UserListItem);
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("clickity",players.get(position).nickname);
                    mSocket.emit("requestgame",players.get(position).username);
                }
            });
            username.setText(players.get(position).nickname);
//            Log.e("pos"+position,players.get(position).toString());
            return recycledView;
        }
    }
    class CustomChatAdapter extends ArrayAdapter<ChatMessage>{
        ArrayList<ChatMessage> messages;
        public CustomChatAdapter(ArrayList<ChatMessage> messages){
            super(LobbyActivity.this,R.layout.customchatuser);
            Log.e("AdapterUserChat",(messages==null)?"Null":"not null");
            this.messages=messages;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public ChatMessage getItem(int position) {
            return messages.get(position);
        }

        @Override
        public View getView(final int position, View recycledView, ViewGroup listView) {
            if(recycledView == null){
                recycledView = LayoutInflater.from(getContext())
                        .inflate(R.layout.customchatuser,null);
            }
            TextView username= recycledView.findViewById(R.id.UserListItemChat);
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click",messages.get(position).nickname);
                }
            });
            username.setText(messages.get(position).nickname+':'+messages.get(position).message);
//            Log.e("pos"+position,players.get(position).toString());
            return recycledView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("LobbyActivity","OnDestroy()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("LobbyActivity","OnResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LobbyActivity","onStop");
    }
}
