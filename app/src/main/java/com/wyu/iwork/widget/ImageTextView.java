package com.wyu.iwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * Created by sxliu on 2017/1/3.
 * 自定义组合控件
 * 左边图，中间文字，右边箭头
 */

public class ImageTextView extends RelativeLayout {

    private ImageView imageView;  //左侧的图片
    private TextView textView; //文字
    private ImageView arrow; //右侧的箭头
    private View line;//底部横线
    private String leftText;//文字
    private int leftTextColor;//字体颜色
    private boolean showRight  ;//是否显示右边箭头，默认显示
    private int leftImageSrc ;//图片路径
    private double leftImageWith;//左边图片宽度
    private double leftImageHeight;//左边图片高度
    private boolean showline;//是否显示底部横线，默认显示
    private double lineheight;//底部横线高度
    private double linecolor;//底部横线颜色

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        if (a!=null){
            leftText = (String) a.getText(R.styleable.ImageTextView_text);
            leftTextColor = a.getColor(R.styleable.ImageTextView_textcolor, Color.parseColor("#000000"));
            leftImageSrc = a.getResourceId(R.styleable.ImageTextView_src,0);
            showRight = a.getBoolean(R.styleable.ImageTextView_showarrow,true);
            leftImageWith = a.getDimension(R.styleable.ImageTextView_imagewith,50);
            leftImageHeight = a.getDimension(R.styleable.ImageTextView_imageheight,50);
            showline = a.getBoolean(R.styleable.ImageTextView_showline,true);
            lineheight = a.getDimension(R.styleable.ImageTextView_lineheight,1);
            linecolor = a.getColor(R.styleable.ImageTextView_linecolor,Color.parseColor("#7e7676"));
            SetLeftImagesXY(leftImageWith,leftImageHeight);
            setLeftTextView(leftText);
            setLeftTextColor(leftTextColor);
            SetLeftImage(leftImageSrc);
            ShowArrow(showRight);
            showLine(showline);
            setLineHeight(lineheight);
            a.recycle();
        }
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化布局文件
     */
    private void init(Context context){
        View view =  LayoutInflater.from(context).inflate(R.layout.customivew_item,this);
        imageView = (ImageView) view.findViewById(R.id.left_imagview);
        arrow = (ImageView) view.findViewById(R.id.right_imagview);
        textView = (TextView) view.findViewById(R.id.left_text);
        line = view.findViewById(R.id.under_line);
    }

    /**
     * 左边文字控件设置值
     */
    public void setLeftTextView(String text){
        textView.setText(text);

    }

    /**
     * 设置左边图片
     */
    public void SetLeftImage(int res){
        imageView.setImageResource(res);
    }

    /**
     * 左边字体设置颜色
     */
    public void setLeftTextColor(int color){
        textView.setTextColor(color);
    }

    /**
     * 是否显示右边箭头
     */
    private void ShowArrow(boolean showRight){
        if (showRight){
            arrow.setVisibility(VISIBLE);
        }else {
            arrow.setVisibility(GONE);
        }
    }

    /**
     * 给左侧图片设置宽高
     */

    private void SetLeftImagesXY(double x,double y){
        LayoutParams para1;
        para1 = (LayoutParams) imageView.getLayoutParams();
        para1.width = (int)x;
        para1.height = (int)y;
        imageView.setLayoutParams(para1);
    }

    /**
     * 设置底部横线的高度
     */
    public void setLineHeight(double height){
        LayoutParams para1;
        para1 = (LayoutParams) line.getLayoutParams();
        para1.height = (int)height;
        line.setLayoutParams(para1);
    }

    /**
     *  设置是否显示底部横线
     */
    public void showLine(boolean showline){
        if (showline){
            line.setVisibility(VISIBLE);
        }else {
            line.setVisibility(GONE);
        }
    }

    //点击事件
    public void setOnClickListener(OnClickListener listener){
        super.setOnClickListener(listener);
    }
}
