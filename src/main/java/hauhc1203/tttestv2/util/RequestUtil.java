package hauhc1203.tttestv2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestUtil {
    private static final Logger LOGGER = LogManager.getLogger(RequestUtil.class);

    public static String doGetRequest(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            boolean setTimeout,
            int connectionRequestTimeout,
            int connectTimeout,
            int socketTimeout
    ) throws URISyntaxException, IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        if (setTimeout)
            client = HttpClients
                    .custom()
                    .setDefaultRequestConfig(
                            RequestConfig
                                    .custom()
                                    .setConnectionRequestTimeout(connectionRequestTimeout)
                                    .setConnectTimeout(connectTimeout)
                                    .setSocketTimeout(socketTimeout)
                                    .build()
                    ).build();


        URIBuilder builder = new URIBuilder(url, StandardCharsets.UTF_8);

        if (!params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        String urlDecode = URLDecoder.decode(builder.toString(), StandardCharsets.UTF_8);
        LOGGER.info("url after decode utf 8 " + urlDecode);
        URI uri = new URI(urlDecode);

        HttpGet httpGet = new HttpGet(uri);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = client.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity());
        LOGGER.info("content resp => " + content);
        response.close();
        return content;

    }


    public static String doPostRequest(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            String accept,
            String contentType
    ) throws IOException {
        if (ValidateUtil.isEmptyString(accept))
            accept = "application/json";

        LOGGER.info(params.toString());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", accept);
        httpPost.setHeader("Content-type", contentType);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        if (contentType.equals("application/x-www-form-urlencoded")) {
            List<NameValuePair> formParams = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            LOGGER.info(formParams.toString());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpPost.setEntity(entity);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            StringEntity stringEntity = new StringEntity(mapper.writeValueAsString(params), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = client.execute(httpPost);
        response.getStatusLine();
        String responseStr = EntityUtils.toString(response.getEntity());
        LOGGER.info("resp=>" + responseStr);
        response.close();
        return responseStr;
    }


}
