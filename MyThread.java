package cisc181.caterpillarcrawl;

import android.graphics.Canvas;

public class MyThread extends Thread{
    MyAnimatedSurfaceView myView;
    private boolean running = false;
    public MyThread(MyAnimatedSurfaceView view){
        myView = view;
    }
    public void setRunning(boolean running){
        this.running = running;
    }
    @Override
    public void run(){
        while(running){
            Canvas canvas = myView.getHolder().lockCanvas();
            if(canvas != null){
                synchronized (myView.getHolder()){
                    myView.myDraw(canvas);
                }
                myView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
