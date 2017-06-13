package arsibi_has_no_website.captionedimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static arsibi_has_no_website.captionedimage.MainActivity.captionimg;

public class CropperHandler extends AppCompatActivity {
    View drawing;
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawing=new DrawingView(getApplicationContext());
        setContentView(drawing);
        Intent intent=getIntent();
        index=intent.getIntExtra("index",0);

    }
    class DrawingView extends View implements Runnable,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                invalidate();
            }
        };
        GestureDetectorCompat g;
        Paint bmpPaint;
        Paint linePaint;
        Bitmap bmp;
        boolean down=false;
        int xbeg=50;
        int ybeg=50;
        int xend=850;
        int yend=850;
        public DrawingView(Context context) {
            super(context);
            bmpPaint=new Paint();
            bmpPaint.setAntiAlias(true);
            linePaint=new Paint();
            linePaint.setColor(Color.GREEN);
            linePaint.setStyle(Paint.Style.STROKE);
            bmp=Bitmap.createScaledBitmap(captionimg.get(index).image,800,800,false);
            g=new GestureDetectorCompat(context,this);
            g.setOnDoubleTapListener(this);
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            this.g.onTouchEvent(event);
            if(event.getAction()==MotionEvent.ACTION_UP)
                down=false;
            else{
                if(Math.abs(event.getX()-xbeg)<30&&Math.abs(event.getY()-ybeg)<30) {
                    xbeg = (int) Math.floor(event.getX());
                    ybeg = (int) Math.floor(event.getY());
                }
                if(Math.abs(event.getX()-xend)<30&&Math.abs(event.getY()-yend)<30) {
                    xend = (int) Math.floor(event.getX());
                    yend = (int) Math.floor(event.getY());
                }
            }
            return true;
        }
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bmp,50,50,bmpPaint);
            canvas.drawRect(xbeg,ybeg,xend,yend,linePaint);
        }
        @Override
        public void run(){
            while (true){
            try{
                Thread.sleep(1000/40);
            }catch (InterruptedException e){}
            handler.sendEmptyMessage(0);
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("MOO","Single");
            return false;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("MOO","Double");
            return false;
        }
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("MOO","Doublt");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("MOO","fuk");
            return true;
        }
        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("MOO","show");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(Math.abs(e.getX()-xbeg)<40&&Math.abs(e.getY()-ybeg)<40) {
                xbeg = (int) Math.floor(e.getX());
                ybeg = (int) Math.floor(e.getY());
            }
            Toast.makeText(getApplicationContext(),"WHY",Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
