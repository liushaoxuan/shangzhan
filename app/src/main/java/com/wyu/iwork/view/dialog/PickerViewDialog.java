package com.wyu.iwork.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.wyu.iwork.R;
import com.wyu.iwork.model.JsonBean;
import com.wyu.iwork.util.GetJsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lx on 2017/5/4.
 */

public class PickerViewDialog {

    private Context context;
    /**
     * 时间选择器
     */
    private TimePickerView pvTime;

    /**
     * 时间选择器(年月日时分)
     */
    private TimePickerView pvTime_h_m;
    /**
     * 城市选择器
     */
    private  OptionsPickerView pvOptions;
    private  OptionsPickerView Options;
    public ArrayList<JsonBean> options1Items = new ArrayList<>();
    public ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;


    private TimePickerView.OnTimeSelectListener  timeSelectListener;

    private OptionsPickerView.OnOptionsSelectListener optionsSelectListener;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了
//                        Toast.makeText(context,"开始解析数据",Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    initCityPickerView();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(context,"解析数据失败",Toast.LENGTH_SHORT).show();
                    thread.start();
                    break;

            }
        }
    };


    //时间对话框
    public PickerViewDialog(Context context, TimePickerView.OnTimeSelectListener timeSelectListener) {
        this.context = context;
        this.timeSelectListener = timeSelectListener;
        initCustomTimePicker();
    }

    //时间对话框(时分)
    public PickerViewDialog(Context context, TimePickerView.OnTimeSelectListener timeSelectListener,String start) {
        this.context = context;
        this.timeSelectListener = timeSelectListener;
        initTimePicker();
    }

    //地区对话框
    public PickerViewDialog(Context context, OptionsPickerView.OnOptionsSelectListener optionsSelectListener) {
        this.context = context;
        this.optionsSelectListener = optionsSelectListener;
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    //条件对话框 需要自己传入数据集 数据集需要实现IPickerViewData接口
    public PickerViewDialog(Context context,List list, OptionsPickerView.OnOptionsSelectListener optionsSelectListener) {
        this.context = context;
        this.optionsSelectListener = optionsSelectListener;
        initOptionPickerView(list);
    }


    /**
     * 年月日时间选择器
     */
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
     /*   Calendar startDate = Calendar.getInstance();
        startDate.set(2014,1,23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027,2,28);*/
        //时间选择器 ，自定义布局
        pvTime = new TimePickerView.Builder(context,timeSelectListener)
                .setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
                .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.parseColor("#414141"))//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                .gravity(Gravity.CENTER)// default is center*/
                .setDate(selectedDate)
                .setOutSideCancelable(true)
//                .setRangDate(startDate,endDate)
                .setLayoutRes(R.layout.layout_custom_timer, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView cancel = (TextView) v.findViewById(R.id.cancel_action);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.parseColor("#ececec"))
                .build();

    }



    /**
     * 年月日时分时间选择器
     */
    private void initTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.YEAR,selectedDate.MONTH+1,selectedDate.DAY_OF_MONTH);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.YEAR+100,selectedDate.MONTH+1,selectedDate.DAY_OF_MONTH);
        //时间选择器 ，自定义布局
        pvTime_h_m = new TimePickerView.Builder(context,timeSelectListener)
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
                .setLabel("年", "月", "日", "时", "分", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.parseColor("#414141"))//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                .gravity(Gravity.CENTER)// default is center*/
                .setDate(selectedDate)
                .setOutSideCancelable(true)
//                .setRangDate(startDate,endDate)
                .setLayoutRes(R.layout.layout_custom_timer, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView cancel = (TextView) v.findViewById(R.id.cancel_action);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime_h_m.returnData();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime_h_m.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.parseColor("#ececec"))
                .build();

    }

    public void show_timepicker(){
        if (!pvTime.isShowing()){
            pvTime.show();
        }
    }

    public void show_timepicker_h_m(){
        if (!pvTime_h_m.isShowing()){
            pvTime_h_m.show();
        }
    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context,"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    //省市区选择器
    private void initCityPickerView() {// 弹出选择器

          pvOptions = new OptionsPickerView.Builder(context, optionsSelectListener)
                  .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                      @Override
                      public void customLayout(View v) {
                          final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                          tvSubmit.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  pvOptions.returnData();
                              }
                          });
                      }
                  })
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(true)// default is true
                  .isDialog(false)
                  .setTitleBgColor(Color.parseColor("#ececec"))
                  .setTextColorCenter(Color.parseColor("#414141"))
                  .build();
//        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
    }

    public void show_cityPicker(){
        if (pvOptions!=null&& isLoaded){
            pvOptions.show();
        }
    }


    //一级pickerview
    private void initOptionPickerView(List list) {// 弹出选择器

        Options = new OptionsPickerView.Builder(context, optionsSelectListener)
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Options.returnData();
                            }
                        });
                    }
                })
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(true)
                .isDialog(false)
                .setTitleBgColor(Color.parseColor("#ececec"))
                .setTextColorCenter(Color.parseColor("#414141"))
                .build();
        Options.setPicker(list);//一级选择器
    }

    /**
     * 显示一级 选择器
     */
    public void show_Options(){
        if (Options!=null){
            Options.show();
        }
    }
}
