package com.wyu.iwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by sxliu on 2017/5/2.
 * 自定义组合控件
 */

public class CustomeViewGroup extends AutoRelativeLayout {

    /**
     * 左边的TextView
     */
    private TextView textview;
    /**
     * 右边的TextView
     */
    private TextView righttextview;
    /**
     * 红星星
     */
    private TextView red_star;
    /**
     * 右边文字
     */
    private EditText editText;
    /**
     * 右边箭头
     */
    private ImageView arrow;
    /**
     * 下划线
     */
    private View under_line;

    private String text;
    private String rightText;
    /**
     * 是否显示星星
     */
    private boolean show_star = true;
    /**
     * 是否显示下划线
     */
    private boolean isShow_line = true;
    /**
     * 是否显示右边箭头
     */
    private boolean isShow_arrow = true;
    /**
     * 是否显示输入框
     */
    private boolean isShow_input = false;
    /**
     * 字体大小
     */
    private float textSize = 12.0f;

    /**
     * 字体颜色
     */
    private int textColor = 0x00333333;
    /**
     * righttextview字体大小
     */
    private float righttextSize = 12.0f;

    /**
     * righttextview字体颜色
     */
    private int righttextcolor = 0x00333333;

    /**
     * 星星大小
     */
    private float star_size = 12.0f;

    /**
     * 星星颜色
     */
    private int starColor = 0x00ff0000;
    /**
     *
     * @param context
     */
    /**
     * 输入框文字大小
     */
    private float inputSize = 12.0f;

    /**
     * 输入框字体颜色
     */
    private int inputcolor = 0x00666666;

    public CustomeViewGroup(Context context) {
        super(context);
    }

    public CustomeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomeViewGroup);
        if (array!=null){
            text = (String) array.getText(R.styleable.CustomeViewGroup_NameText);
            rightText = (String) array.getText(R.styleable.CustomeViewGroup_RightText);
            textSize = array.getDimension(R.styleable.CustomeViewGroup_NameSize,12.0f);
            star_size = array.getDimension(R.styleable.CustomeViewGroup_StarSize,12.0f);
            inputSize = array.getDimension(R.styleable.CustomeViewGroup_InputSize,12.0f);
            righttextSize = array.getDimension(R.styleable.CustomeViewGroup_RightTextSize,12.0f);
            textColor =  array.getColor(R.styleable.CustomeViewGroup_NameColor,Color.parseColor("#333333"));
            starColor =  array.getColor(R.styleable.CustomeViewGroup_StarColor,Color.parseColor("#333333"));
            inputcolor =  array.getColor(R.styleable.CustomeViewGroup_InputColor, Color.parseColor("#666666"));
            righttextcolor =  array.getColor(R.styleable.CustomeViewGroup_RightTextColor, Color.parseColor("#666666"));
            show_star = array.getBoolean(R.styleable.CustomeViewGroup_ShowStar,true);
            isShow_line = array.getBoolean(R.styleable.CustomeViewGroup_ShowLine,true);
            isShow_arrow = array.getBoolean(R.styleable.CustomeViewGroup_showArrow,true);
            isShow_input = array.getBoolean(R.styleable.CustomeViewGroup_showInput,false);
            setinit();
            array.recycle();//回收对象 避免内存泄漏
        }
    }

    public CustomeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化布局文件
     */
    private void init(Context context){
        View view =  LayoutInflater.from(context).inflate(R.layout.layout_customeview,this);
        textview = (TextView) view.findViewById(R.id.name);
        righttextview = (TextView) view.findViewById(R.id.right_textview);
        red_star = (TextView) view.findViewById(R.id.red_star);
        editText = (EditText) view.findViewById(R.id.input);
        arrow = (ImageView) view.findViewById(R.id.my_right_arrow);
        under_line = view.findViewById(R.id.under_line);
        if (red_star!=null){
            red_star.setText("*");
        }
    }

    private void setinit(){
        setNameContent(text);
        setNameSize(textSize);
        setNameColor(textColor);
        setStarColor(starColor);
        setStarSize(star_size);
        setInputColor(inputcolor);
        setInputSize(inputSize);
        isShowStar(show_star);
        is_ShowLine(isShow_line);
        is_Showarrow(isShow_arrow);
        setRightText(rightText);
        setRightTextColor(righttextcolor);
        setRightTextSize(righttextSize);
        is_ShowInput(isShow_input);
    }

    /**
     * TextView设置字体大小
     */
    public void setNameSize(float size){
        if (textview!=null){
            textview.setTextSize(size);
        }
    }
    /**
     * 设置字体颜色
     */
    public void setNameColor(int color){
        if (textview!=null){
            textview.setTextColor(color);
        }
    }
    /**
     * TextView设置内容
     */
    public void setNameContent(String content){
        if (textview!=null){
            textview.setText(content);
        }
    }


    /**
     * 设置righttextview字体大小
     */
    public void setRightTextSize(float size){
        if (righttextview!=null){
            righttextview.setTextSize(size);
        }
    }
    /**
     * 设置righttextview字体颜色
     */
    public void setRightTextColor(int color){
        if (righttextview!=null){
            righttextview.setTextColor(color);
        }
    }
    /**
     * 设置righttextview内容
     */
    public void setRightText(String content){
        if (righttextview!=null){
            righttextview.setText(content);
        }
    }

    /**
     * T星星设置字体大小
     */
    public void setStarSize(float size){
        if (red_star!=null){
            red_star.setTextSize(size);
        }
    }
    /**
     * 设置星星颜色
     */
    public void setStarColor(int color){
        if (red_star!=null){
            red_star.setTextColor(color);
        }
    }

    /**
     * Edtext设置字体大小
     */
    public void setInputSize(float size){
        if (editText!=null){
            editText.setTextSize(size);
        }
    }
    /**
     * 设置Edittext颜色
     */
    public void setInputColor(int color){
        if (editText!=null){
            editText.setTextColor(color);
        }
    }

    /**
     * 设置Edittext内容
     */
    public void setInputContent(String content){
        if (editText!=null){
            editText.setText(content);
        }
    }
    /**
     * 是否显示星星
     */
    public void isShowStar(boolean isshow){
        if (red_star!=null){
            if (isshow){
                red_star.setVisibility(VISIBLE);
            }else {
                red_star.setVisibility(INVISIBLE);
            }
        }

    }

    /**
     * 是否显示下划线
     */
    public void is_ShowLine(boolean isshow){
        if (under_line!=null){
            if (isshow){
                under_line.setVisibility(VISIBLE);
            }else {
                under_line.setVisibility(INVISIBLE);
            }
        }

    }

    /**
     * 是否显示右边的箭头 默认显示
     */
    public void is_Showarrow(boolean isShow_arrow){
        if (arrow!=null){
            if (isShow_arrow){
                arrow.setVisibility(VISIBLE);
            }else {
                arrow.setVisibility(GONE);
            }
        }
    }
    public void is_ShowInput(boolean isShow_arrow){
        if (editText!=null){
            if (isShow_arrow){
                editText.setVisibility(VISIBLE);
            }else {
                editText.setVisibility(GONE);
            }
        }
    }

    /**
     * 返回左侧内容
     * @return
     */
    public String getNametext(){
        if (textview!=null){
            return textview.getText().toString().trim();
        }else {
            return "";
        }

    }

    /**
     * 返回右侧内容
     * @return
     */
    public String getRightText(){
        if (righttextview!=null){
            return righttextview.getText().toString().trim();
        }else {
            return "";
        }
    }

    /**
     * 返回输入框内容
     */
    public String getInput(){
        if (editText!=null){
            return editText.getText().toString().trim();
        }else {
            return "";
        }
    }

    /**
     * 是否显示右边的文字
     */
    public void is_showRightText(boolean isshow){
        if (righttextview!=null){
            if (isshow){
                righttextview.setVisibility(VISIBLE);
            } else {
                righttextview.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置输入类型
     */
    public void setInputType(int type){
        editText.setInputType(type);
    }
    /**
     * 设置输入长度
     */
    public void setInputLength(int length){
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)}); //最大输入长度
    }
}
