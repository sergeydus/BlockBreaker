package com.example.blockbreaker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends Activity {
    private Socket mSocket;
    public Board myboard;
    private String text="asd";
    private int deviceX;
    private int deviceY;
    private float countdown=(float)4;
    private GameView myview;
    private Emitter.Listener MatchUpdateListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    myboard =gson.fromJson(((JSONObject)args[0]).toString(),Board.class);
//                    Log.e("Myboard",((JSONObject)args[0]).toString());
                    myview.setMyboard(myboard);
                    if(myboard.isover){
                        setResult(RESULT_OK);
                        myview.setMyboard(myboard);
                        float mStartTime = System.currentTimeMillis();
                        new CountDownTimer(3000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                Log.e(("seconds remaining: " + millisUntilFinished / 1000),"yep");
                                // Here you do check every second = every 1000 milliseconds
                            }

                            public void onFinish() {
                                // Here you can restart game after 30 seconds which is 30K milliseconds.
                                finish();
                            }
                        }.start();
                    }
                    if(myboard.message>0){
                        if(countdown!=myboard.message){
                            Toast.makeText(GameActivity.this, myboard.message.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("Countdown",myboard.message.toString());
                            countdown=myboard.message;
                        }
                    }

                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceX = size.x;
        deviceY = size.y;
        this.mSocket=MainActivity.mSocket;
        myview = findViewById(R.id.GameView);
        mSocket.on("gameupdate", MatchUpdateListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("Ontouchevent","("+event.getRawX()+","+event.getRawY()+")");
//        Result := ((Input - InputLow) / (InputHigh - InputLow))
//                * (OutputHigh - OutputLow) + OutputLow;
        Float x = ((event.getRawX()-0)/(deviceX-0))*(600-0) + 0;

//        Float y= ((event.getRawY()-0)/(deviceY-0))*(600-0) + 0;

//        Log.e("Ontouchevent","("+x+","+y+")");
        JSONObject json= new JSONObject();
        try {
            json.put("x",x);
            mSocket.emit("gamemousemove", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }
}
