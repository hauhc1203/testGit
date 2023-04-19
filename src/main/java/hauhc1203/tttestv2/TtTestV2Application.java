package hauhc1203.tttestv2;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
public class TtTestV2Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TtTestV2Application.class, args);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=
                new SimpleDateFormat("yyyy-MM-dd'T'hh:MM:ss.uuuuuuZ:z");
        System.out.println(simpleDateFormat.format(date));

    }




    public static void readFile() throws IOException {

        File file = new File("C:\\Users\\Admin\\Desktop\\code.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> stringList = new ArrayList<>();
        try {

            String s = bufferedReader.readLine();
            while (s != null && !s.equals("")) {
                stringList.add(s);
                s = bufferedReader.readLine();
            }
            StringBuilder stringBuilder = new StringBuilder("");
            for (String ss : stringList
            ) {
                stringBuilder.append('\"' + ss + "\",");
            }
            System.out.println(stringBuilder.toString());
        } catch (Exception e) {
            bufferedReader.close();
            fileReader.close();
        }

    }

    public static void abc(String ab) {
        ExecutorService t = Executors.newSingleThreadExecutor();
        t.execute(() -> {


            System.out.println("hello " + ab);


            System.out.println("hello " + ab);
            System.out.println("hello " + ab);
            System.out.println("hello " + ab);
        });
    }

    public static void syncOff() throws IOException {
        String url = "http://localhost:8088/offline-sync";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-type", "text/csv");


//        byte[] data=covertFileToBinary("C:\\Users\\Admin\\Desktop\\receiveCSV\\abc.csv");

//        ByteArrayEntity entity = new ByteArrayEntity(data);
        FileEntity entity = new FileEntity(new File("C:\\Users\\Admin\\Desktop\\receiveCSV\\abc.csv"));
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        response.getStatusLine();
        String responseStr = EntityUtils.toString(response.getEntity());
        response.close();
        System.out.println(responseStr);

    }

    public static byte[] covertFileToBinary(String filePath) {
        File file = new File(filePath);
        byte[] fileData = new byte[(int) file.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            in.read(fileData);
            in.close();
            return fileData;
        } catch (IOException e) {
            return new byte[0];
        }

    }

}
