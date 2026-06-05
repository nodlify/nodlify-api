package com.pickdate.geocoding.infrastructure

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestClient
import spock.lang.Specification


@ContextConfiguration(classes = Config)
class NominatimClientSpec extends Specification {

    @Autowired
    private NominatimClient client

    @Autowired
    private MockWebServer server

    def "reverse geocoding caches present result"() {
        given:
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                    {
                      "place_id": 185644447,
                      "lat": "52.2173448",
                      "lon": "20.9971010",
                      "name": "VII Liceum Ogolnoksztalcace",
                      "display_name": "VII Liceum Ogolnoksztalcace, Wawelska, Warsaw, Poland",
                      "address": {
                        "road": "Wawelska",
                        "city": "Warsaw",
                        "postcode": "02-070"
                      }
                    }
                """))

        when:
        var firstResult = client.reverse(52.21698196165107d, 20.997490882873535d)
        var cachedResult = client.reverse(52.21698196165107d, 20.997490882873535d)

        then:
        firstResult.isPresent()
        firstResult == cachedResult
        firstResult.get().address() == "VII Liceum Ogolnoksztalcace, Wawelska, Warsaw, 02-070"
        server.requestCount == 1
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean(destroyMethod = "shutdown")
        MockWebServer mockWebServer() {
            var server = new MockWebServer()
            server.start()
            server
        }

        @Bean
        CacheManager cacheManager() {
            new ConcurrentMapCacheManager("geocoding")
        }

        @Bean
        GeocodingConfig geocodingConfig(MockWebServer server) {
            var config = new GeocodingConfig()
            config.setBaseUrl(server.url("/").toString())
            config.setUserAgent("nodlify-test")
            config.setSearchLimit(5)
            config
        }

        @Bean
        RestClient geocodingRestClient(GeocodingConfig config) {
            config.geocodingRestClient()
        }

        @Bean
        NominatimClient nominatimClient(RestClient geocodingRestClient, GeocodingConfig config) {
            new NominatimClient(geocodingRestClient, config)
        }
    }
}
