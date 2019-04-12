package com.gdchhkf.weather.timeTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class WeatherHourTask {

    @Autowired
    private RestTemplateBuilder restBuilder;
    @Autowired
    private FileOperation fileOperation;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    @Qualifier("cache")
    private Map cache;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    private void weatherHourListener(){
        //9小时之前
        LocalDateTime nineHoursAgo = LocalDateTime.now().minusHours(9).withMinute(0).withSecond(0);
        String str = nineHoursAgo.format(DateTimeFormatter.ofPattern(TimeUtils.HOUR));

        String url = "http://api.data.cma.cn:8090/api?" +
                "userId=551236215397hG3d9" +
                "&pwd=FqeJ8xS" +
                "&dataFormat=json" +
                "&interfaceId=getSurfEleByTimeRangeAndStaID" +
                "&dataCode=SURF_CHN_MUL_HOR" +
                "&timeRange=[%s,%s]" +
                "&staIDs=59488" +
                "&elements=TEM,TEM_Max,TEM_Min,tigan,PRS,PRS_Sea,VAP,RHU,windpower,PRE_1h,VIS,CLO_Cov,CLO_Cov_Low,CLO_COV_LM,WEP_Now,Station_Id_C,Year,Mon,Day,Hour";
        //构造url, 并发出请求
        String apiUrl = String.format(url, str, str);
        RestTemplate restTemplate = restBuilder.build();
        String content = restTemplate.getForObject(apiUrl, String.class);
        Weather weather = readWeatherFromJson(content);

        //存入缓存
        cache.put("now", weather);

        //写入HDFS
        String fileName = nineHoursAgo.format(DateTimeFormatter.ofPattern(TimeUtils.DAY));
        String path = FileOperation.HOUR + fileName;
        fileOperation.appendFile(path, weather.toString() + "\n");

    }

    private Weather readWeatherFromJson(String content){
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
