package io.brewday.maltmagnus;

import io.brewday.domain.Malt;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaltScrape {

    private static final Logger log = LoggerFactory.getLogger(MaltScrape.class);

    private final String url;

    private final Pattern pricePattern = Pattern.compile("([0-9]*\\.?[0-9]+)kr");
    private final Pattern ebcPattern = Pattern.compile("°EBC\\s([\\d\\s-]+)");

    public MaltScrape(String url) {
        this.url = url;
    }

    public List<Malt> scrape() throws IOException, InterruptedException {
        Document doc = Jsoup.connect(this.url).timeout(20000).get();

        Elements productDivs = doc.select(".product-list > div");

        int i = 0;
        List<Malt> malts = new ArrayList<>();
        for (Element productDiv : productDivs) {
            String thumbUrl = productDiv.select(".image img").attr("src");
            String url = productDiv.select(".image a").attr("href");
            try {
                Malt m = scrapeMaltPage(url);
                log.info("Scraped: " + m.getName());
                malts.add(m);
            } catch (SocketTimeoutException ignore) {
                continue;
            }

            i++;
            if (i % 10 == 0) {
                Thread.sleep(5000);
            }
        }

        return malts;
    }

    private Malt scrapeMaltPage(String url) throws IOException {
        Malt m = new Malt();
        Document doc = Jsoup.connect(url).timeout(20000).get();

        Element infoDiv = doc.select(".product-info").first();
        Element productDescriptionDiv = infoDiv.select(".description").first();

        String imageUrl = infoDiv.select(".image img").first().attr("src");

        // Id
        String description = productDescriptionDiv.text();
        m.setId(extractLabeled("Artikelnummer", description, true));

        // Name
        String name = doc.select("#content").first().select("h1").first().text();
        m.setName(name);

        // Producer
        String producer = productDescriptionDiv.select("span").first().text();
        m.setProducer(extractLabeled("Märke", producer, false));

        // Origin
        String flagSrc = productDescriptionDiv.select("span > img").attr("src");

        // Description

        String descriptionHtml = doc.select("#tab-description").html();
        m.setDescription(descriptionHtml);

        // EBC
        try {
            Element ebcParagraph = doc.select("#tab-description > p").last();
            if (ebcParagraph != null) {
                String ebcString = ebcParagraph.text();
                m.setEbc(extractEbc(extractLabeled("Färg", ebcString, false)));
            }
        } catch (RuntimeException ignore) {}

        // Price
        double price = extractPrice(infoDiv.select(".price").text());
        m.setPrice(price);

        return m;

    }

    private double extractPrice(String text) {
        Matcher matcher = pricePattern.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("Price could not be matched: " + text);
        }
        String priceMatch = matcher.group(1);
        return Double.parseDouble(priceMatch);
    }

    private String extractLabeled(String label, String text, boolean noSpace) {
        Pattern pattern = Pattern.compile(label + ":.?" + (noSpace ? "(\\S*)": "(\\S.*)"));
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException(label + " could not be matched: " + text);
        }
        String match = matcher.group(1);

        return match;
    }

    private String extractEbc(String text) {
        Matcher matcher = ebcPattern.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("EBC could not be matched: " + text);
        }
        String match = matcher.group(1);

        return match;
    }

}
