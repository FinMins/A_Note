package com.example.finmins.materialtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;

import androidx.core.content.FileProvider;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhao on 2020/2/20.
 */
public class DrawingBoard extends View {

    private File file;    //图片

    public Uri imageUri;    //图片路径

    //画图的模式（默认是画笔）
    private DrawMode mDrawMode = DrawMode.PaintMode;
    //画笔
    private Paint mPaint;
    //画笔颜色
    private int mPaintColor = Color.RED;
    //画笔宽度
    private int mPaintSize = dip2px(5);
    //橡皮擦宽度
    private int mEraserSize = dip2px(36);
    //缓冲的位图
    private Bitmap mBufferBitmap;
    //缓冲的画布
    private Canvas mBufferCanvas;
    //当前控件的宽度
    private int mWidth;
    //当前控件的高度
    private int mHeight;
    //画布的颜色
    private int mCanvasColor = Color.WHITE;
    //上次的位置
    private float mLastX;
    private float mLastY;
    //路径
    private Path mPath;
    //设置图形混合模式为清除
    private PorterDuffXfermode mEraserMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    //保存的路径
    private List<DrawPathInfo> savePaths;
    //当前的路径
    private List<DrawPathInfo> currPaths;
    //最多保存20条路径
    private int MAX_PATH = 20;
    public DrawingBoard(Context context) {
        this(context,null);
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawingBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initPath();
    }
    //获取屏幕像素，实现外部赋值控制控件大小
    public int dip2px(float dipValue){
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    //重新测量宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
        initCanvas();
    }
    private void initPath(){
        mPath = new Path();
        savePaths = new ArrayList<>();
        currPaths = new ArrayList<>();
    }
    private void initCanvas(){
        //创建一个BITMAP
        mBufferBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
        mBufferCanvas.drawColor(mCanvasColor);
    }
    private void initPaint(){
        //设置画笔抗锯齿和抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        //设置画笔填充方式为只描边
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔颜色
        mPaint.setColor(mPaintColor);
        //设置画笔宽度
        mPaint.setStrokeWidth(mPaintSize);
        //设置圆形线帽
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置线段连接处圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBufferBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                //画出路径
                mPath.quadTo(mLastX,mLastY,(mLastX+x)/2,(mLastY+y)/2);
                mBufferCanvas.drawPath(mPath,mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                //保存路径
                saveDrawPaths();
                mPath.reset();
                break;
        }
        return true;
    }
    private void saveDrawPaths(){
        if (savePaths.size() == MAX_PATH){
            savePaths.remove(0);
        }
        savePaths.clear();
        savePaths.addAll(currPaths);
        Path cachePath = new Path(mPath);
        Paint cachePaint = new Paint(mPaint);
        savePaths.add(new DrawPathInfo(cachePaint,cachePath));
        currPaths.add(new DrawPathInfo(cachePaint,cachePath));
    }
    /**
     * 设置画笔模式
     * */
    public void setMode(DrawMode mode){
        if (mode != mDrawMode){
            if (mode == DrawMode.EraserMode){
                mPaint.setStrokeWidth(mEraserSize);
                mPaint.setXfermode(mEraserMode);
                mPaint.setColor(mCanvasColor);
            }
            else{
                mPaint.setXfermode(null);
                mPaint.setColor(mPaintColor);
                mPaint.setStrokeWidth(mPaintSize);
            }
            mDrawMode = mode;
        }
    }
    public DrawMode getMode(){
        return mDrawMode;
    }

    /**
     * 下一步 反撤销
     * */
    public void nextStep(){
        if (currPaths != savePaths){
            if (savePaths.size()>currPaths.size()){
                currPaths.add(savePaths.get(currPaths.size()));
                redrawBitmap();
            }
        }
    }
    /**
     * 上一步 撤销
     * */
    public void lastStep(){
        if (currPaths.size()>0){
            currPaths.remove(currPaths.size()-1);
            redrawBitmap();
        }
    }
    /**
     * 重绘位图
     * */
    private void redrawBitmap(){
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        for (int i = 0;i<currPaths.size();i++){
            DrawPathInfo path = currPaths.get(i);
            mBufferCanvas.drawPath(path.getPath(),path.getPaint());
        }
        invalidate();
    }
    /**
     * 保存图片
     * */
    public void savecanvas(String str , Context context){
        //保存绘图为本地图片
        mBufferCanvas.save();// mBufferCanvas.save(Canvas.ALL_SAVE_FLAG);
        mBufferCanvas.restore();
        // 保存到sdcard根目录下
         file = new File(context.getExternalCacheDir() ,str+".png");
         imageUri =   FileProvider.getUriForFile(context,"com.example.finmins.MaterialTest.fileprovider",file);
       // File file = new File(Environment.getExternalStorageDirectory().getPath() + "/"+str+".png");
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            mBufferBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e){
            // TODO Auto-generated catch block
            e.printStackTrace();		}

        try {
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();		}
    }
    /**
     * 擦除画布
     * */
    public void clean(){
        savePaths.clear();
        currPaths.clear();
        //将位图擦成透明的
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }



}
