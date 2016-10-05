package io.brewday.maltmagnus;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.brewday.domain.Malt;
import io.brewday.maltmagnus.MaltScrape;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaltScrapeTest {

    @Autowired
    ObjectMapper mapper;

    MaltScrape scraper;

    @Before
    public void before() {
        scraper = new MaltScrape("http://maltmagnus.se/malt");
    }

    @Test
    public void scrape() throws Exception {
        List<Malt> scrape = scraper.scrape();

        Path base = Paths.get("json").toAbsolutePath();

        Files.createDirectories(base);

        Path malts = base.resolve("malts.json");

        mapper.writerWithDefaultPrettyPrinter().writeValue(malts.toFile(), scrape);

        assertNotNull(scrape);
    }

}