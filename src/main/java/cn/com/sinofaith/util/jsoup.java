package cn.com.sinofaith.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jsoup {
    public static void main(String[] args)throws Exception {
        String id = "5293888";
        String[] ids = id.split(",");

        for (int i=0;i<ids.length;i++){
            System.out.println(loadInfo(ids[i]));
        }

    }

    public static Map<String,Object> loadInfo(String id)throws IOException {
        String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)";
        String firstURl = "https://www.baidu.com/s?cl=3&wd=http%3A%2F%2Fwww.dianping.com%2Fshop%2F" + id;
        Map<String,Object> resultMap = new HashMap<>();
        //回去百度快照链接
        Document doc = Jsoup.connect(firstURl)
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cache-Control","no-cache")
                .header("Pragma","no-cache")
                .header("Proxy-Connection","keep-alive")
                .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                .get();
        String url = doc.select("#content_left [data-click=\"{'rsv_snapshot':'1'}\"]").attr("href");
        //如果没有就是百度没有收录
        if(null != url &&  !"".equals(url.trim())){
            //请求百度快照
            doc = Jsoup.connect(url)
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding","gzip, deflate")
                    .header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Cache-Control","no-cache")
                    .header("Pragma","no-cache")
                    .header("Proxy-Connection","keep-alive")
                    .header("User-Agent",ua)
                    .header("Referer",firstURl)
                    .get();
            String shopname = doc.select(".shop-name").text();
            String address = doc.select(".expand-info.address").text();
            String tel = doc.select("p.expand-info.tel").text();
            //店铺名称
            resultMap.put("shopname",shopname);
            //店铺地址
            resultMap.put("address",address);
            //店铺电话
            resultMap.put("tel",tel);
            //TODO 需要其他信息自己取
        }
        return resultMap;
    }

}
