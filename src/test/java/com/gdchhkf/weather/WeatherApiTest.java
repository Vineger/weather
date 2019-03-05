package com.gdchhkf.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.impl.MapReduceServiceImpl;
import com.gdchhkf.weather.service.mapper.WeatherDayMapper;
import com.gdchhkf.weather.service.reducer.WeatherDayReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName WeatherApiTest
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/2/28 17:40
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherApiTest {

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
    private RestTemplateBuilder builder;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private HDFSService hdfsService;


    @Test
    public void readFileTest() throws IOException{
        System.out.println(LocalDateTime.now().getDayOfWeek().getValue());

//        Map<String, String> map = new HashMap<>();
//        String result = hdfsService.readFile("/demo/day/aaaa");
//        StringTokenizer tokenizer = new StringTokenizer(result);
//        while (tokenizer.hasMoreTokens()){
//            map.put(tokenizer.nextToken(), tokenizer.nextToken());
//        }
//        System.out.println(map);
    }

    @Test
    public void apiTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eightHoursAgo = now.minusHours(9).withMinute(0).withSecond(0);
        String str = eightHoursAgo.format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss"));

        String apiUrl = String.format(url, str, str);
        RestTemplate restTemplate = builder.build();
        String content = restTemplate.getForObject(apiUrl, String.class);

        System.out.println(content);

        JsonNode rootNode = mapper.readTree(content);
        JsonNode dsNode = rootNode.get("DS");
        JsonNode weatherNode = dsNode.elements().next();
        System.out.println(weatherNode.toString());
        Weather weather = mapper.convertValue(weatherNode, Weather.class);


        System.out.println(weather);
    }

    @Test
    public void mapReduceTest() {
        System.setProperty("HADOOP_USER_NAME", "root");

        String inputFile = "/demo/test";
        String outputTmp = "/demo/day/tmp/";
        String outputDir = "/demo/day/";

        Configuration conf = getConfiguration();
        boolean result = false;
        Job job = null;

        try{
            //判断HDFS中输出目录是否存在, 存在则将其删除
            hdfsService.deleteDir(outputDir);

            job = Job.getInstance(conf, "analysis weather hour");
            job.setJarByClass(MapReduceServiceImpl.class);
            job.setMapperClass(WeatherDayMapper.class);
            job.setCombinerClass(WeatherDayReducer.class);
            job.setReducerClass(WeatherDayReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FloatWritable.class);

            FileInputFormat.addInputPath(job, new Path(inputFile));
            FileOutputFormat.setOutputPath(job, new Path(outputTmp));
            result = job.waitForCompletion(true);

            //迁移处理出来的文件
            hdfsService.renameFile(outputTmp, outputDir, "/aaaa");

        } catch (IOException | InterruptedException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private Configuration getConfiguration(){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.254.100:9000");
        return conf;
    }

    private FileSystem getFileSystem() {
        Configuration conf = getConfiguration();
        FileSystem fileSystem = null;
        try{
            fileSystem = FileSystem.get(conf);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileSystem;
    }
}
