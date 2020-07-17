package com.example.finmins.materialtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DrawboardActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawingBoard mDrawingBoard;
    private ImageView msave;
    private ImageView mback;
    private ImageView mPaint;
    private ImageView mEraser;
    private ImageView mClean;
    private ImageView mLast;
    private ImageView mNext;
    private String name;
    private String imageUriPath ; //图片路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawbroad);
        final EditText et = new EditText(DrawboardActivity.this);
        name =String.valueOf( Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) +
                String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) )+
                String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        initView();
        initEvent();

    }
    //初始化控件
    private void initView(){
        mDrawingBoard = (DrawingBoard) findViewById(R.id.draw_board);
        msave = (ImageView) findViewById(R.id.iv_save);
        mback = (ImageView) findViewById(R.id.iv_back);
        mPaint = (ImageView) findViewById(R.id.iv_paint);
        mEraser = (ImageView) findViewById(R.id.iv_eraser);
        mClean = (ImageView) findViewById(R.id.iv_clean);
        mLast = (ImageView) findViewById(R.id.iv_last);
        mNext = (ImageView) findViewById(R.id.iv_next);
    }
    //设置监听事件
    private void initEvent(){
        //设置默认选择背景,level值为1
        mPaint.getDrawable().setLevel(1);
        mPaint.getBackground().setLevel(1);
        msave.setOnClickListener(this);
        mback.setOnClickListener(this);
        mPaint.setOnClickListener(this);
        mEraser.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mLast.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    //实现监听事件效果
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();

                break;
            case R.id.iv_save:
                if (name!=null)
                {
                    mDrawingBoard.savecanvas(name,DrawboardActivity.this);
                    imageUriPath = mDrawingBoard.imageUri.toString();    //将保存的图片路径赋值。
                    Log.d("DrawboardActivity.this","路径是"+mDrawingBoard.imageUri.toString());
                    Toast.makeText(DrawboardActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    //以String的形式传回Uri;
                    Intent intent = new Intent();
                    intent.putExtra("ImageUriPath",imageUriPath);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
            case R.id.iv_paint:
                if (mDrawingBoard.getMode() != DrawMode.PaintMode) {
                    mDrawingBoard.setMode(DrawMode.PaintMode);
                }
                mPaint.getDrawable().setLevel(1);
                mPaint.getBackground().setLevel(1);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);

                break;
            case R.id.iv_eraser:
                if (mDrawingBoard.getMode() != DrawMode.EraserMode) {
                    mDrawingBoard.setMode(DrawMode.EraserMode);
                }
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(1);
                mEraser.getBackground().setLevel(1);
                break;
            case R.id.iv_clean:
                alertDialogClean();
                break;
            case R.id.iv_last:
                mDrawingBoard.lastStep();
                break;
            case R.id.iv_next:
                mDrawingBoard.nextStep();
                break;
        }
    }

    //设置画板清空对话框
    private void alertDialogClean(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要请空画板吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDrawingBoard.clean();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final  AlertDialog dialog = builder.show();
        dialog.show();
    }
}