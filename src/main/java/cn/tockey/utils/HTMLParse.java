package cn.tockey.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class HTMLParse {
    public static void main(String[] args) throws Exception {

        URL url = new URL("http://www.moe.gov.cn");
        Document document = Jsoup.parse(url, 3000);

        Element element = document.getElementById("jyb_index_focus");
        Elements lis = element.getElementsByTag("li");

        for (Element li : lis) {
            li.getElementsByTag("a").forEach(a -> {
                System.out.println("链接：" + url + a.attr("href"));
                System.out.println("图片：" + url +a.getElementsByTag("img").attr("src"));
                System.out.println("标题：" + a.getElementsByTag("p").text());
            });
            System.out.println("=====================================");
        }

    }
}
