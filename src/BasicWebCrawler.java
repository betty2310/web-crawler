import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BasicWebCrawler {
    private HashSet<String> links;
    FileWriter writer;
    JSONArray nvls;

    public BasicWebCrawler() {
        links = new HashSet<String>();
        try {
            writer = new FileWriter("output.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nvls = new JSONArray();
    }

    public void getPageLinks(String url) {
        if (!links.contains(url)) {
            links.add(url);
            try {
                Document document = Jsoup.connect(url).get();
                Elements articleLinks = document.select("h2 a[href^='/nhan-vat/']");
                for (Element article : articleLinks) {
                    Document articleDoc = Jsoup.connect(article.attr("abs:href")).get();
                    Elements infobox = articleDoc.select("table.infobox");
                    String infoBirth = "";
                    String infoDeath = "";

                    if (!infobox.isEmpty()) {
                        infoBirth = articleDoc.select("th[scope='row']:matchesOwn(Sinh)").next().text();
                        infoDeath = articleDoc.select("th[scope='row']:matchesOwn(Mất)").next().text();
                    } else {
                        Element paragraph = articleDoc.select("p").first();
                        String[] years = new String[2];
                        Pattern pattern = Pattern.compile("(\\d{3,4}|\\?)");
                        Matcher matcher = pattern.matcher(paragraph.text());
                        int i = 0;
                        while (matcher.find() && i < 2) {
                            String year = matcher.group();
                            if (year.equals("?")) {
                                years[i] = "?";
                            } else {
                                years[i] = year;
                            }
                            i++;
                        }
                        infoBirth = years[0];
                        infoDeath = years[1];
                    }
                    JSONObject obj = new JSONObject();
                    obj.put("name", "ten");
                    obj.put("sinh", infoBirth);
                    obj.put("mat", infoDeath);
                    obj.put("url", article.attr("abs:href"));
                    System.out.println(obj.toJSONString());
                    nvls.add(obj);
                }
                Elements linksOnPage = document.select("a[href^='/nhan-vat?start=']");
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.print("For '" + url + "': " + e.getMessage());
            }
        }
    }

    public void writeToFile() throws IOException {
        writer.write(nvls.toJSONString());
        writer.flush();
        writer.close();
        System.out.println("end!");
    }

//    public void extract(String input) {
//        List<String> result = new ArrayList<>();
//        StringBuilder currentDigitString = new StringBuilder();
//        int lastDigitCharIndex = -1;
//        for (int i = 0; i < input.length(); ++i) {
//            char c = input.charAt(i);
//            if (Character.isDigit(c)) {
//                currentDigitString.append(c);
//                lastDigitCharIndex = i;
//            } else {
//                if (currentDigitString.length() > 0) {
//                    result.add(currentDigitString.toString());
//                    currentDigitString.setLength(0);
//                }
//            }
//        }
//        if (currentDigitString.length() > 0) {
//            result.add(currentDigitString.toString());
//        }
//        DateCustom date = new DateCustom();
//        if (result.size() >= 3) {
//            int year = Integer.parseInt(result.get(2));
//            int month = Integer.parseInt(result.get(1));
//            int day = Integer.parseInt(result.get(0));
//            date = new DateCustom(day, month, year);
//        }
//        if (result.size() == 2) {
//            int year = Integer.parseInt(result.get(1));
//            int month = Integer.parseInt(result.get(0));
//            date = new DateCustom(-1, month, year);
//        }
//        if (result.size() == 1) {
//            int year = Integer.parseInt(result.get(0));
//            date = new DateCustom(-1, -1, year);
//        }
//        System.out.println(date.toString() + input.substring(lastDigitCharIndex + 1));
//    }


    public static void main(String[] args) throws IOException {
        BasicWebCrawler webCrawler = new BasicWebCrawler();
        webCrawler.getPageLinks("https://nguoikesu.com/nhan-vat");
        webCrawler.writeToFile();
//        webCrawler.extract("16 tháng 4, 2005 (83 tuổi) Bệnh viện Hữu nghị, Hà Nội");
    }
}