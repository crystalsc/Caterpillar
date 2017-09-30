package cisc181.caterpillarcrawl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static cisc181.caterpillarcrawl.MainActivity.startI;

public class MyAnimatedSurfaceView  extends SurfaceView {
    private Context mContext;
    int score = 1;
    MyThread myThread;
    Paint mPaint;
    Bitmap circle;
    Bitmap apple;
    boolean yMoveDown = true;
    boolean yMoveUp = false;
    boolean xMoveLeft = false;
    boolean xMoveRight = false;
    int xPos = 0;
    int yPos = 0;
    int xPosApple;
    int yPosApple;
    double xDelta = 0;
    double yDelta = 20;
    ArrayList<Integer> xHead = new ArrayList<>();
    ArrayList<Integer> yHead = new ArrayList<>();
    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();
    int xSeg;
    int ySeg;
    Bitmap head;
    MainActivity m;

    public MyAnimatedSurfaceView(Context context,AttributeSet attrs) {
        super(context, attrs);
        m = new MainActivity();
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        myThread = new MyThread(this);
        apple =  BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        head = BitmapFactory.decodeResource(getResources(),R.drawable.black);
        //Picks caterpillar color
        if(MainActivity.color.equals("Blue")) {
            circle = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
        } else if(MainActivity.color.equals("Magenta")) {
            circle = BitmapFactory.decodeResource(getResources(), R.drawable.magenta);
        } else if (MainActivity.color.equals("Gray")) {
            circle = BitmapFactory.decodeResource(getResources(), R.drawable.gray);
        }else{
            circle = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        }

        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                myThread.setRunning(true);
                myThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                myThread.setRunning(false);
                while (retry) {
                    try {
                        myThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }
    public void myDraw(Canvas canvas){
        xHead.add(xPos);
        yHead.add(yPos);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(head,xPos,yPos,mPaint);
        xPos += xDelta;
        yPos += yDelta;
        if(score == 1){ //Starting position of apple
            xPosApple = getWidth()/2;
            yPosApple = getHeight()/2;
        }
        if(eat()){ //New apple if caterpillar eats apple
            Random rand = new Random();
            xPosApple = rand.nextInt(getWidth()-100);
            yPosApple = rand.nextInt(getHeight()-100);
        }
        canvas.drawBitmap(apple,xPosApple,yPosApple,mPaint);
        //Adds segments after the head depending on the score
        x.clear();
        y.clear();
        int i = 1;
        while(i<score){
            xSeg = xHead.get(xHead.size()-(i*4));
            ySeg = yHead.get(yHead.size()-(i*4));
            x.add(xSeg);
            y.add(ySeg);
            canvas.drawBitmap(circle,xSeg,ySeg,mPaint);
            i++;
        }
        gameOver();
    }
    //Determines caterpillar movement depending on touchscreen event
    public boolean onTouchEvent(MotionEvent e){
        float x = e.getX();
        float y = e.getY();
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (yMoveDown && x <= getWidth()/2) {
                    yMoveDown = false;
                    xMoveRight = true;
                    xDelta = 20;
                    yDelta = 0;
                } else if (yMoveUp && x <= getWidth()/2) {
                    yMoveUp = false;
                    xMoveLeft = true;
                    xDelta = -20;
                    yDelta = 0;
                } else if (xMoveRight && x <= getWidth()/2) {
                    xMoveRight = false;
                    yMoveUp = true;
                    xDelta = 0;
                    yDelta = -20;
                } else if(xMoveLeft && x <= getWidth()/2 ){
                    yMoveDown = true;
                    xMoveLeft = false;
                    yDelta = 20;
                    xDelta = 0;
                } else if (yMoveDown && x > getWidth()/2) {
                    yMoveDown = false;
                    xMoveLeft = true;
                    xDelta = -20;
                    yDelta = 0;
                } else if (yMoveUp && x > getWidth()/2) {
                    yMoveUp = false;
                    xMoveRight = true;
                    xDelta = 20;
                    yDelta = 0;
                } else if (xMoveRight && x > getWidth()/2) {
                    xMoveRight = false;
                    yMoveDown = true;
                    xDelta = 0;
                    yDelta = 20;
                } else {
                    yMoveUp = true;
                    xMoveLeft = false;
                    yDelta = -20;
                    xDelta = 0;
                }
        }
        return true;
    }
    //Checks to see if caterpillar is close enough to apple to eat it
    public boolean eat(){
        if((xPos - 50 <= xPosApple && xPos+50 >= xPosApple) && (yPos - 50 <= yPosApple && yPos + 50 >= yPosApple)){
            score ++;
            return true;
        } else{
            return false;
        }
    }
    //Goes back to title screen when game ends.
    public void gameOver(){
        if(xPos > getWidth() || xPos < -70 || yPos > getHeight() || yPos < -70){
            SharedPreferences sharedPref = mContext.getSharedPreferences("HighScore",Context.MODE_PRIVATE);
            int defaultValue = 0;
            int highscore = sharedPref.getInt("saved_high_score",defaultValue);
            if(score>highscore){
                highscore=score;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("saved_high_score",highscore);
                editor.commit();
            }
            startI.putExtra("score",score);
            startI.putExtra("hScore",highscore);
            m.setResult1(RESULT_OK,startI);
            ((MainActivity)mContext).finish();
        } else{
            int i = 1;
            while(i < x.size()){
                if((xPos-50 <= x.get(i) && xPos+50 >= x.get(i)) && (yPos-50 <= y.get(i) && yPos + 50 >= y.get(i))){
                    SharedPreferences sharedPref = mContext.getSharedPreferences("HighScore",Context.MODE_PRIVATE);
                    int defaultValue = 0;
                    int highscore = sharedPref.getInt("saved_high_score",defaultValue);
                    if(score>highscore){
                        highscore=score;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("saved_high_score",highscore);
                        editor.commit();
                    }
                    startI.putExtra("score",score);
                    startI.putExtra("hScore",highscore);
                    m.setResult1(RESULT_OK,startI);
                    ((MainActivity)mContext).finish();
                }
                i++;
            }
        }
    }
}
