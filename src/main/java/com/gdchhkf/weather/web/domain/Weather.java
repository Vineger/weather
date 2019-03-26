package com.gdchhkf.weather.web.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * 从HDFS文件中读取气象数据所使用的数据容器
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@Data
public class Weather {

    public static final String TEM = "tem";
    public static final String TEM_MAX = "temMax";
    public static final String TEM_MIN = "temMin";
    public static final String TIGAN = "tigan";
    public static final String PRS = "prs";
    public static final String PRS_SEA = "prsSea";
    public static final String VAP = "vap";
    public static final String RHU = "rhu";
    public static final String WINDPOWER = "windpower";
    public static final String PRE_1H ="pre1h";
    public static final String PRE = "pre";
    public static final String VIS = "vis";
    public static final String CLO_COV = "cloCov";
    public static final String CLO_COV_LOW = "cloCovLow";
    public static final String CLO_COV_LM = "cloCovLm";
    public static final String WEP_NOW = "wepNow";


    //温度
    @JsonSetter("TEM")
    private float tem;
    //最高气温
    @JsonSetter("TEM_Max")
    private float temMax;
    //最低气温
    @JsonSetter("TEM_Min")
    private float temMin;
    //体感温度
    private float tigan;
    //气压
    @JsonSetter("PRS")
    private float prs;
    //海平面气压
    @JsonSetter("PRS_Sea")
    private float prsSea;
    //水气压
    @JsonSetter("VAP")
    private float vap;
    //相对湿度
    @JsonSetter("RHU")
    private int rhu;
    //风力
    private int windpower;
    //过去1小时降雨量
    @JsonSetter("PRE_1h")
    private float pre1h;
    //水平能见度
    @JsonSetter("VIS")
    private float vis;
    //总云量
    @JsonSetter("CLO_Cov")
    private int cloCov;
    //低云量
    @JsonSetter("CLO_Cov_Low")
    private int cloCovLow;
    //云量
    @JsonSetter("CLO_Cov_LM")
    private int cloCovLm;
    //现在天气
    @JsonSetter("WEP_Now")
    private float wepNow;

    @Override
    public String toString() {
        return  "tem=" + tem +
                ",temMax=" + temMax +
                ",temMin=" + temMin +
                ",tigan=" + tigan +
                ",prs=" + prs +
                ",prsSea=" + prsSea +
                ",vap=" + vap +
                ",rhu=" + rhu +
                ",windpower=" + windpower +
                ",pre1h=" + pre1h +
                ",vis=" + vis +
                ",cloCov=" + cloCov +
                ",cloCovLow=" + cloCovLow +
                ",cloCovLm=" + cloCovLm +
                ",wepNow=" + wepNow;
    }
}
