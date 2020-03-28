

package com.dopaminequest.mathalgorithmsdatastructures.views;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;


        import com.dopaminequest.mathalgorithmsdatastructures.R;
        import com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras.Grid;

        import androidx.annotation.Nullable;

        import static java.sql.DriverManager.println;


public class MainView extends View implements Runnable{

    int x = 10;

    Thread ourThread = null;
    long lastFrameTime;
    int fps;
    Button button;






    private Rect mRectSquare;
    private Paint mPaintSquare;


    public MainView(Context context) {
        super(context);

        init(null);
    }

    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void  init(@Nullable AttributeSet set)
    {

        mRectSquare = new Rect();
        mPaintSquare = new Paint();
        ourThread = new Thread(this);
        ourThread.start();
        //Button button = (Button) findViewById(R.id.test);







    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawColor(Color.RED);

        //Rect rect = new Rect();

        mRectSquare.left = x+ 100;
        mRectSquare.right = mRectSquare.left + 100;
        mRectSquare.top = x+100;
        mRectSquare.bottom = mRectSquare.top + 100;
        mPaintSquare.setColor(Color.GREEN);

        canvas.drawRect(mRectSquare,mPaintSquare);


    }

    @Override
    public void run() {


        x = 100;
        System.out.println("--------------=--========-=-=-=-=-========-=-=--=-=-=test");



        while(true)
        {


            x += 1;
            controlFPS();

            invalidate();


        }




    }

    public void controlFPS() {
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        long timeToSleep = 15 - timeThisFrame;
        if (timeThisFrame > 0) {
            fps = (int) (1000 / timeThisFrame);
        }
        if (timeToSleep > 0) {

            try {
                ourThread.sleep(timeToSleep);
            } catch (InterruptedException e) {
            }
        }
        lastFrameTime = System.currentTimeMillis();
    }

    public void resetX()
    {


        x = 0;
    }





}
