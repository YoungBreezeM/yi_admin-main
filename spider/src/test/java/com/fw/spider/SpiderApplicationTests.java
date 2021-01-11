package com.fw.spider;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.service.PredictionCategoryService;
import com.fw.core.service.PredictionService;
import com.fw.core.service.YaoService;
import com.fw.core.entity.Prediction;
import com.fw.core.entity.PredictionCategory;
import com.fw.core.entity.Yao;
import com.fw.core.mapper.YaoMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class SpiderApplicationTests {

    @Autowired
    private YaoService yaoService;
    @Autowired
    private YaoMapper yaoMapper;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private PredictionCategoryService predictionCategoryService;

    private void getYaoData(String url, Yao yao) {
        YSpider ySpider = new YSpider(url);
        String response = ySpider.getResponse();
        Document doc = Jsoup.parse(response);
        Elements articles = doc.getElementsByClass("article-container");
        articles.select("div.mbd_ad").remove();
        yao.setYaoName(articles.select("h1").text());
        yao.setYaoText(articles.html());
        System.out.println(yao.getPos()+":"+articles.select("h1").text());
        yaoService.save(yao);
    }

    private void getPredictionData(String url,Prediction prediction){
        YSpider ySpider = new YSpider(url);
        String response = ySpider.getResponse();
        Document doc = Jsoup.parse(response);
        Elements articles = doc.getElementsByClass("article-content").select("p");

        for (int i = 0; i < articles.size() ; i++) {
            String text = articles.get(i).select("p>strong").text();


            String str = text.replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×:]" , "");
            PredictionCategory predictionCategory = new PredictionCategory();
            predictionCategory.setCategoryName(str);

            QueryWrapper<PredictionCategory> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.setEntity(predictionCategory);
            PredictionCategory one = predictionCategoryService.getOne(objectQueryWrapper);
            prediction.setCategoryId(one.getId());
            prediction.setPredictionText(articles.get(i).html());
            predictionService.save(prediction);
        }

    }

    @Test
    void initYaoData() throws Exception {
        YSpider ySpider = new YSpider("https://yijing.5000yan.com/zhouyi/");
        String response = ySpider.getResponse();
        Document doc = Jsoup.parse(response);

        Elements select = doc.getElementsByClass("price-info");


        for (int i = 0; i < select.size(); i++) {
            Yao yao = new Yao();
            yao.setBaseGuaId(i+1);
            Elements href = select.get(i).select("ul>li>a");

            if (href.size() == 11 || href.size() == 10) {
                for (int j = 1; j < href.size() - 3; j++) {
                    yao.setPos(j);
                    getYaoData(href.get(j).attr("href"), yao);
                }
            } else if (href.size() == 9) {
                for (int j = 1; j < href.size() - 2; j++) {
                    yao.setPos(j);
                    getYaoData(href.get(j).attr("href"), yao);
                }
            } else {
                System.out.println(href);
            }
            System.out.println("11111111111111111111111");
        }


    }

    @Test
    void initPrediction() throws Exception {
        YSpider ySpider = new YSpider("https://yijing.5000yan.com/zhouyi/");
        String response = ySpider.getResponse();
        Document doc = Jsoup.parse(response);

        Elements select = doc.getElementsByClass("price-info");

        for (int i = 0; i < select.size(); i++) {

            Elements href = select.get(i).select("ul>li>a");
            Prediction prediction = new Prediction();
            prediction.setBaseGuaId(i+1);
            Yao yao1 = new Yao();
            QueryWrapper<Yao> yaoQueryWrapper = new QueryWrapper<>();
            yao1.setBaseGuaId(i+1);
            yaoQueryWrapper.setEntity(yao1);
            List<Yao> list = yaoMapper.selectList(yaoQueryWrapper);
            if (href.size()==9){

                for (Yao yao : list) {
                    prediction.setYaoId(yao.getId());
                    getPredictionData(href.get(href.size()-2).attr("href"),prediction);
                }

            }else {
                for (Yao yao : list) {
                    prediction.setYaoId(yao.getId());
                    getPredictionData(href.get(href.size()-3).attr("href"),prediction);
                }


            }
//            if (href.size() == 11 || href.size() == 10) {
//                System.out.println(href.get());
//            } else if (href.size() == 9) {
//
//            } else {
//                System.out.println(href);
//            }
            System.out.println("11111111111111111111111");
        }


    }

    @Test
    void parasBiLiBiLi(){
        HttpResponse res = HttpRequest.
                get("https://www.bilibili.com/video/BV18x411s7rv?from=search&seid=14556866795028769639")
                .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("accept-encoding","gzip, deflate, br")
                .header("accept-language","zh-CN,zh;q=0.9")
                .header("cache-control","max-age=0")
                .header("cookie","finger=990794906; _uuid=40E6B329-6C1E-4C77-0844-DB13F1F28E8673715infoc; buvid3=795AC714-AC3A-438F-97DF-D1E5508522FF143096infoc; CURRENT_FNVAL=80; blackside_state=1; rpdid=|(um~JJkuluY0J'uY|RmuY|Ju; PVID=1; sid=629n90oc; DedeUserID=258425956; DedeUserID__ckMd5=d3a9c269a88248e0; SESSDATA=d4f12e1f%2C1621212084%2Cd1bfd*b1; bili_jct=e2ad21a94058e6c5c13a70865c6b7732; bp_video_offset_258425956=458663504620996832; bsource=search_google")
                .header("referer","https://search.bilibili.com/")
                .header("sec-fetch-dest","document")
                .header("sec-fetch-mode", "navigate")
                .header("sec-fetch-site","same-site")
                .header("sec-fetch-user", "?1")
                .header("upgrade-insecure-requests", "1")
                .timeout(2000)
                .execute();

        String html = res.body();
        System.out.println(html);
        Document document = Jsoup.parse(html);
        Element title = document.getElementsByTag("title").first();
        // 视频名称
        String text = title.text();
        System.out.println(text);
        // 截取视频信息
        Pattern pattern = Pattern.compile("(?<=<script>window.__playinfo__=).*?(?=</script>)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            System.out.println(new JSONObject(matcher.group())); ;
        } else {
            System.err.println("未匹配到视频信息，退出程序！");
            return;
        }
    }
}
