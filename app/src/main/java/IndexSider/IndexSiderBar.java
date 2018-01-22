package IndexSider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by longyue on 2018/1/21.
 */

public class IndexSiderBar extends View {
    private ChoceInterFace choceInterFace;

    public static String[] b = {"#","A", "B", "C", "D", "E", "F", "G", "H",
            "I","J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"};
    private Paint textPaint;
    private ArrayList<String> singLin=new ArrayList<>();
    private Paint circlePaint;
    private int width;
    private int height;
    private int textHeight;
    private int mChoose;
    private TextView textInfo;

    public void setChoceInterFace(ChoceInterFace choceInterFace) {
        this.choceInterFace = choceInterFace;
    }

    public IndexSiderBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndexSiderBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        textInfo=new TextView(context);
    }

    private void init() {
        for(String line:b){
            singLin.add(line);
        }

        textPaint = new Paint();
        textPaint.setAntiAlias(true);

        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setStrokeWidth(22);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        circlePaint.setStrokeWidth(10);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        textHeight = height / (singLin.size() + 1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mChoose!=-1){
            circlePaint.setColor(Color.parseColor("#FF4081"));
        }else {
            circlePaint.setColor(Color.parseColor("#4a4949"));
        }

        canvas.drawCircle(textHeight/2.0f,textHeight/2.0f,textHeight/4.0f,circlePaint);


        for (int i = 0; i < singLin.size(); i++) {
            if(mChoose==i){
                textPaint.setColor(Color.parseColor("#FF4081"));
            }else {
                textPaint.setColor(Color.parseColor("#4a4949"));
            }

            //文字的x坐标轴
            float xPos = width / 2- textPaint.measureText(singLin.get(i)) / 2;

            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            int centerLine = textHeight * (i + 1) + textHeight/ 2;
            int baseLine = centerLine + (fm.bottom - fm.top) / 2 - fm.bottom;
            canvas.drawText(singLin.get(i),xPos,baseLine,textPaint);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float posY = y / height * (singLin.size() + 1);
        mChoose = (int) (posY - 1);

        String text = singLin.get(mChoose);
        if(null!=textInfo){
            Log.i("TAG------------>", "是否设置了");
            textInfo.setVisibility(VISIBLE);
            textInfo.setText(text);
            choceInterFace.onChose(mChoose,text);
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                mChoose=-1;
                if(null!=textInfo){
                    textInfo.setVisibility(GONE);
                }
                break;
        }

        invalidate();


        return true;
    }

    public void setTextInfo(TextView textInfo) {
        this.textInfo = textInfo;
    }

    public interface ChoceInterFace{
        void onChose(int chose,String info);
    }


}
