package com.example.blockbreaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View {
    private int deviceX;    //my device X size
    private int deviceY;    //my device Y size
    final float boardYsize=600;//server canvas y size
    final float boardXsize=600;//server canvas x size
    public Board myboard=null;
    Bitmap win=BitmapFactory.decodeResource(getResources(), R.drawable.win);
    Bitmap lose=BitmapFactory.decodeResource(getResources(), R.drawable.lose);

    public GameView(Context context) {
        super(context);
        setdevicesize(context);
    }

    public GameView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setdevicesize(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setdevicesize(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        setdevicesize(context);
    }
    private void init(AttributeSet set){
    }
    private void setdevicesize(Context context){
        deviceY=context.getResources().getDisplayMetrics().heightPixels;
        deviceX = context.getResources().getDisplayMetrics().widthPixels;
    }
    public void setMyboard(Board board){
        myboard=board;
        this.invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Log.e("onDraw","Drawing Canvas");
        canvas.drawColor(Color.CYAN);
//        Paint paint = new Paint();
//        canvas.drawRect(this.myrect,mypaint);
        if(myboard!=null){
            DrawPlayers(canvas);
            DrawBall(canvas);
            DrawRectangles(canvas);
            DrawScores(canvas);
//            Log.e("myboard",myboard.HasWon+"BABE");
            if(myboard.isover){
//                Log.e("GAME OVER!","DRAWING LOSE/WIN!!!@!");
                if(myboard.HasWon){
                    //image size is:(x,y): (217,233),
//                    Drawable d = getResources().getDrawable(R.drawable.win, null);
////                    d.setBounds((deviceX/2)-(217/2), (deviceY/2)-(233/2), (deviceX/2)-(217/2)+217, (deviceY/2)-(233/2)+233);
//                    d.setBounds(0, 0, deviceX, deviceY);
//                    d.draw(canvas);
                    Paint p=new Paint();
                    p.setColor(Color.RED);
                    canvas.drawBitmap(win, Math.round(deviceX/2)-win.getHeight()/2,Math.round(deviceY/2)-lose.getHeight()/2, p);
                }
                else{
//                    Drawable d = getResources().getDrawable(R.drawable.lose, null);
////                    d.setBounds((deviceX/2)-(217/2), (deviceY/2)-(233/2), (deviceX/2)-(217/2)+217, (deviceY/2)-(233/2)+233);
//                    d.setBounds(0,0,deviceX,deviceY);
//                    d.draw(canvas);
                    Paint p=new Paint();
                    Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.win);
                    p.setColor(Color.RED);
                    canvas.drawBitmap(lose, Math.round(deviceX/2)-lose.getWidth()/2,Math.round(deviceY/2)-lose.getHeight()/2, p);

                }
                Log.e("Drew the canvas!","Drew it!");

            }
        }


    }
    private void DrawPlayers(Canvas canvas){
        Paint paint= new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRoundRect(
                ConvertToDeviceSize(this.myboard.board.player1.height,
                this.myboard.board.player1.width,
                this.myboard.board.player1.posx,
                this.myboard.board.player1.posy),
                12,
                12,
                paint);
        canvas.drawRoundRect(
                ConvertToDeviceSize(this.myboard.board.player2.height,
                        this.myboard.board.player2.width,
                        this.myboard.board.player2.posx,
                        this.myboard.board.player2.posy),
                12,
                12,
                paint);


    }
    private void DrawBall(Canvas canvas){
//        if(myboard.isplayer1){
//
//        }
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for(int i=0;i<myboard.board.balls.size();i++){
            canvas.drawRoundRect(
                    ConvertToDeviceSize(this.myboard.board.balls.get(i).height,
                            this.myboard.board.balls.get(i).width,
                            this.myboard.board.balls.get(i).posx,
                            this.myboard.board.balls.get(i).posy),
                    12,
                    12,
                    paint);
        }
    }
    private void DrawRectangles(Canvas canvas){
        Paint paint = new Paint();
//        Log.e("Rectangles",myboard.board.blocks.toString());
        for(int i=0;i<myboard.board.blocks.size();i++){
            paint.setShader(null);
            if(myboard.board.blocks.get(i).score==10){
                paint.setColor(Color.GRAY);
            }
            else if(myboard.board.blocks.get(i).score==20){
                paint.setColor(Color.GREEN);
            }
            else if(myboard.board.blocks.get(i).score==30){
                paint.setColor(Color.MAGENTA);
            }
            else if(myboard.board.blocks.get(i).score==40){
                int[] colors = {Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FFFFAC"),
                        Color.parseColor("#D1B464"),
                        Color.parseColor("#C49700"),
                        Color.parseColor("#9f7928"),
                        Color.parseColor("#FEDB37")};
                float[] positions={0,(float)0.08,(float)0.25,(float)0.625,(float)0.75,1};
                paint.setShader(new LinearGradient(myboard.board.blocks.get(i).posx,
                        myboard.board.blocks.get(i).posy,
                        myboard.board.blocks.get(i).posx+myboard.board.blocks.get(i).width,
                        myboard.board.blocks.get(i).posy+myboard.board.blocks.get(i).height,colors,positions,  Shader.TileMode.MIRROR));
            }
            canvas.drawRoundRect(ConvertToDeviceSize(this.myboard.board.blocks.get(i).height,
                    this.myboard.board.blocks.get(i).width,
                    this.myboard.board.blocks.get(i).posx,
                    this.myboard.board.blocks.get(i).posy),
                    12,
                    12,
                    paint
            );
        }
    }
    public void DrawScores(Canvas canvas){
//        Log.e("Scores","Drawing scores");
        String player1scoretext="Score:"+myboard.board.player1score.toString();
        String player2scoretext="Score:"+myboard.board.player2score.toString();
        Paint paint= new Paint();
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.BLACK);
        paint.setTextSize(deviceX/10);
        Rect bounds = new Rect();
        if(myboard.isplayer1){
            //get player1text bounds
            paint.getTextBounds(player1scoretext,0,player1scoretext.length(),bounds);
            canvas.drawText(player1scoretext,0,deviceY-bounds.height(),paint);
            //get player2text bounds
            paint.getTextBounds(player2scoretext,0,player2scoretext.length(),bounds);
            canvas.drawText(player2scoretext,deviceX-bounds.width(),bounds.height(),paint);
        }
        else{
            //get player1text bounds
            paint.getTextBounds(player2scoretext,0,player2scoretext.length(),bounds);
            canvas.drawText(player2scoretext,0,deviceY-bounds.height(),paint);
            //get player2text bounds
            paint.getTextBounds(player1scoretext,0,player1scoretext.length(),bounds);
            canvas.drawText(player1scoretext,deviceX-bounds.width(),bounds.height(),paint);
        }
    }
    //converts number to fit properrly on our screen
    private float convertXToMySize(float num){
//        Result := ((Input - InputLow) / (InputHigh - InputLow))
//                * (OutputHigh - OutputLow) + OutputLow;
        float res = (((Math.round(num)-0)/(this.boardXsize-0))*(deviceX-0) + 0);
//
        return res;
    }
    /*
    converts Servers Y to fit in our screen
     */
    private float convertYToMySize(float num){
//        Result := ((Input - InputLow) / (InputHigh - InputLow))
//                * (OutputHigh - OutputLow) + OutputLow;
        float res = (((Math.round(num)-0)/(this.boardYsize-0))*(deviceY-0) + 0);
//        Log.e("ConvertY","Received:"+num+"Outputted:"+res);
        return res;
    }
    /*
    receives a rectangle from the server and manipulates it to fit
    properly on our screen
     */
    private RectF ConvertToDeviceSize(Float Height,Float Width, Float posx, Float posy){
         RectF Rectangle;
        if(myboard.isplayer1){
            Rectangle = new RectF(convertXToMySize(posx),
                    convertYToMySize(posy),
                    convertXToMySize(posx)+convertXToMySize(Width),
                    convertYToMySize(posy)+convertYToMySize(Height));
        }
        else{
            Rectangle = new RectF(convertXToMySize(posx),
                    convertYToMySize(boardYsize)-convertYToMySize(posy)-Height,
                    convertXToMySize(posx)+convertXToMySize(Width),
                    convertYToMySize(boardYsize)-convertYToMySize(posy)-Height+convertYToMySize(Height));
        }
        return Rectangle;
    }
}
