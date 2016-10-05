package io.brewday.hopslist;

import io.brewday.domain.Hops;
import io.brewday.domain.PercentRange;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HopslistScrape {

    private static final Logger log = LoggerFactory.getLogger(HopslistScrape.class);

    private final String url;

    private final Pattern percentRangePattern = Pattern.compile("([0-9]*\\.?[0-9]+)%-([0-9]*\\.?[0-9]+)%");

    public HopslistScrape(String url) {
        this.url = url;
    }

    public List<Hops> scrape() throws IOException, InterruptedException {
        Document doc = Jsoup.connect(this.url).timeout(20000).get();

        Elements hopsLinks = doc.select(".entry-content .listing-item > a");

        int i = 0;
        List<Hops> hops = new ArrayList<>();
        for (Element hopsLink : hopsLinks) {
            String url = hopsLink.attr("href");

            String slug = url.substring(url.lastIndexOf('/') + 1);
            String name = hopsLink.text();

            Hops h = new Hops(slug, name);

            try {
                scrapeHopsPage(h, url);
                log.info("Scraped: " + h.getName());
                hops.add(h);
            } catch (SocketTimeoutException ignore) {
                continue;
            }

            i++;
            if (i % 10 == 0) {
                //Thread.sleep(5000);
            }
        }

        return hops;
    }

    private Hops scrapeHopsPage(Hops h, String url) throws IOException {

        Document doc = Jsoup.connect(url).timeout(20000).get();

        Element infoTable = doc.select(".entry-wrap table[width=620] tbody").first();
        Elements infoTrs = infoTable.select("tr");

        for (Element tr : infoTrs) {
            Element tdName = tr.firstElementSibling();
            Element tdValue = tr.gets.nextElementSibling();

            switch (tdName.text()) {
                case "Also Known As":
                    h.setAlsoKnownAs(tdValue.text());
                    break;

                case "Purpose":
                    h.setType(tdValue.text());
                    break;

                case "Alpha Acid Composition":
                    h.setAlphaAcids(extractPercentRange(tdValue.text()));
                    break;
            }
        }


        /*Element infoDiv = doc.select(".product-info").first();
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
        m.setPrice(price);*/

        return h;

    }

    private PercentRange extractPercentRange(String text) {
        Matcher matcher = percentRangePattern.matcher(text);
        if (!matcher.find()) {
            throw new RuntimeException("Percent Range could not be matched: " + text);
        }
        String fromMatch = matcher.group(1);
        String toMatch = matcher.group(2);
        return new PercentRange(Double.parseDouble(fromMatch), Double.parseDouble(toMatch));
    }
}
