package com.gdchhkf.weather.service.timeTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @ClassName WeatherHourTask
 * @Description 每小时执行一次从中国气象数据网采集数据
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 14:29
 * @Version 1.0
 **/
public class WeatherHourTask {

    private final String url = "http://api.data.cma.cn:8090/api?" +
            "userId=551236215397hG3d9" +
            "&pwd=FqeJ8xS" +
            "&dataFormat=json" +
            "&interfaceId=getSurfEleByTimeRangeAndStaID" +
            "&dataCode=SURF_CHN_MUL_HOR" +
            "&timeRange=[%s,%s]" +
            "&staIDs=59488" +
            "&elements=TEM,TEM_Max,TEM_Min,tigan,PRS,PRS_Sea,VAP,RHU,windpower,PRE_1h,VIS,CLO_Cov,CLO_Cov_Low,CLO_COV_LM,WEP_Now,Station_Id_C,Year,Mon,Day,Hour";

    @Autowired
    private RestTemplateBuilder restBuilder;
    @Autowired
    private HDFSService hdfsService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TimeUtils utils;
    @Autowired
    @Qualifier("cache")
    private Map<String, Weather> cache;

    /**
     * @Author gdchhkf@163.com
     * @Description 每隔一小时调用一次天气API, 获取气象数据
     * @Date 12:31 2019/2/28
     * @Param [event]
     * @return void
     **/
    @Scheduled(cron = "0 30 9/1 * * ? *")
    private void weatherHourListener(){
        //9小时之前
        LocalDateTime nineHoursAgo = LocalDateTime.now().minusHours(9).withMinute(0).withSecond(0);
        String str = nineHoursAgo.format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss"));

        //构造url, 并发出请求
        String apiUrl = String.format(url, str, str);
        RestTemplate restTemplate = restBuilder.build();
        String content = restTemplate.getForObject(apiUrl, String.class);
        Weather weather = readWeatherFromJson(content);

        //写入HDFS
        String fileName = nineHoursAgo.format(DateTimeFormatter.ofPattern("uuuu_MM_d"));
        String path = "/weather/hour/" + fileName;
        hdfsService.appendFile(path, weather.toString());

        //存入缓存
        cache.put("now", weather);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 对Json字符串进行解析并且放回Weather对象
     * @Date 22:16 2019/2/28
     * @Param [content]
     * @return com.gdchhkf.weather.domain.Weather
     **/
    public Weather readWeatherFromJson(String content){
        Weather weather = null;
        try {
            JsonNode rootNode = mapper.readTree(content);
            JsonNode dsNode = rootNode.get("DS");
            JsonNode weatherNode = dsNode.elements().next();
            weather = mapper.convertValue(weatherNode, Weather.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return weather;
    }
}
