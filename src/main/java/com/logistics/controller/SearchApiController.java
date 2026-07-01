package com.logistics.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchApiController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/api/search")
    public List<SearchResult> search(@RequestParam("q") String q) {
        List<SearchResult> results = new ArrayList<>();
        if (q == null || q.trim().length() < 2) {
            return results;
        }

        String query = q.trim();

        // 1. Search Products
        try {
            List<Object[]> products = entityManager.createQuery(
                "SELECT p.id, p.name, p.sku, p.brand FROM Product p WHERE LOWER(p.name) LIKE LOWER(:q) OR LOWER(p.sku) LIKE LOWER(:q)",
                Object[].class)
                .setParameter("q", "%" + query + "%")
                .setMaxResults(4)
                .getResultList();
            for (Object[] p : products) {
                results.add(new SearchResult(
                    "Products",
                    (String) p[1] + " (" + (String) p[2] + ")",
                    "Brand: " + (String) p[3],
                    "/products/" + p[0] + "/edit"
                ));
            }
        } catch (Exception e) {
            // Log warning
        }

        // 2. Search Orders
        try {
            List<Object[]> orders = entityManager.createQuery(
                "SELECT o.id, o.orderNumber, o.status, o.customer.name, o.totalAmount FROM CustomerOrder o WHERE LOWER(o.orderNumber) LIKE LOWER(:q)",
                Object[].class)
                .setParameter("q", "%" + query + "%")
                .setMaxResults(4)
                .getResultList();
            for (Object[] o : orders) {
                results.add(new SearchResult(
                    "Orders",
                    (String) o[1],
                    "Status: " + o[2] + " • Customer: " + o[3] + " • Total: $" + o[4],
                    "/orders/" + o[0]
                ));
            }
        } catch (Exception e) {
            // Log warning
        }

        // 3. Search Shipments
        try {
            List<Object[]> shipments = entityManager.createQuery(
                "SELECT s.id, s.trackingNumber, s.status, s.driverName FROM Shipment s WHERE LOWER(s.trackingNumber) LIKE LOWER(:q) OR LOWER(s.driverName) LIKE LOWER(:q)",
                Object[].class)
                .setParameter("q", "%" + query + "%")
                .setMaxResults(4)
                .getResultList();
            for (Object[] s : shipments) {
                results.add(new SearchResult(
                    "Shipments",
                    (String) s[1],
                    "Status: " + s[2] + " • Driver: " + (s[3] != null ? s[3] : "N/A"),
                    "/shipments/" + s[0]
                ));
            }
        } catch (Exception e) {
            // Log warning
        }

        // 4. Search Warehouses
        try {
            List<Object[]> warehouses = entityManager.createQuery(
                "SELECT w.id, w.name, w.code, w.city, w.state FROM Warehouse w WHERE LOWER(w.name) LIKE LOWER(:q) OR LOWER(w.code) LIKE LOWER(:q)",
                Object[].class)
                .setParameter("q", "%" + query + "%")
                .setMaxResults(4)
                .getResultList();
            for (Object[] w : warehouses) {
                results.add(new SearchResult(
                    "Warehouses",
                    (String) w[1] + " (" + (String) w[2] + ")",
                    "Location: " + w[3] + ", " + w[4],
                    "/warehouses/" + w[0] + "/edit"
                ));
            }
        } catch (Exception e) {
            // Log warning
        }

        return results;
    }

    @Getter @Setter
    public static class SearchResult {
        private String category;
        private String title;
        private String subtitle;
        private String url;

        public SearchResult(String category, String title, String subtitle, String url) {
            this.category = category;
            this.title = title;
            this.subtitle = subtitle;
            this.url = url;
        }
    }
}
