package me.brahian.superapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/product/{ean}")
    public ResponseEntity<Object> getProductByEan(@PathVariable String ean) {
        String[] urls = {
                "https://www.comodinencasa.com.ar/api/catalog_system/pub/products/search/?fq=alternateIds_Ean:" + ean,
                "https://www.carrefour.com.ar/api/catalog_system/pub/products/search/?fq=alternateIds_Ean:" + ean,
                "https://www.vea.com.ar/api/catalog_system/pub/products/search/?fq=alternateIds_Ean:" + ean
        };

        RestTemplate restTemplate = new RestTemplate();
        for (String url : urls) {
            try {
                Object response = restTemplate.getForObject(url, Object.class);
                if (response instanceof List && !((List<?>) response).isEmpty()) {
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } catch (HttpStatusCodeException e) {
                return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
            } catch (RestClientException e) {
                return new ResponseEntity<>("Error fetching product data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("No data found", HttpStatus.NOT_FOUND);
    }
}