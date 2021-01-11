package com.fw.spider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author yqf
 * @date 2020/11/9 下午6:46
 */
public class YSpider {

    public YSpider(String url) {
        this.url = url;
    }

    private String url;

    public String getResponse(){
        HttpGet httpGet = new HttpGet(this.url);


        httpGet.setConfig(
                RequestConfig
                        .custom()
                        .setSocketTimeout(30000)
                        .setConnectTimeout(30000)
                        .build()
        );

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseStr = null;

        try {
            httpClient = HttpClientBuilder.create().build();
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpGet, context);
            int state = response.getStatusLine().getStatusCode();

            if (state != HttpStatus.SC_OK) {
                responseStr = "";
            }

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                responseStr = EntityUtils.toString(entity, "utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseStr;
    }
}
