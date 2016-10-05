package io.brewday.hopslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.brewday.domain.Hops;
import io.brewday.hopslist.HopslistScrape;
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

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HopslistScrapeTest {

    @Autowired
    ObjectMapper mapper;

    HopslistScrape scraper;

    @Before
    public void before() {
        scraper = new HopslistScrape("http://www.hopslist.com/hops/");
    }

    @Test
    public void scrape() throws Exception {
        List<Hops> scrape = scraper.scrape();

        Path base = Paths.get("json").toAbsolutePath();

        Files.createDirectories(base);

        Path hops = base.resolve("hops.json");

        mapper.writerWithDefaultPrettyPrinter().writeValue(hops.toFile(), scrape);

        assertNotNull(scrape);
    }

}