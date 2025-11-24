package com.drew.department_store.service;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.drew.department_store.models.Product;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public final class StoreInformation {

    private static final Map<Long, Product> products = new HashMap<>();

    private StoreInformation() {}

    /**
     * Init default products
     */
    public static void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        File json = new File("src/main/resources/default_products.json");

        try {
            JsonNode rootNode = objectMapper.readTree(json);

            Iterator<JsonNode> it = rootNode.path("products").iterator();
            while (it.hasNext()) {
                JsonNode node = it.next();
                Product product = objectMapper.treeToValue(node, Product.class);
                products.put(product.id(), product);
            }
        } catch (JacksonException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a list of products filtered with the provided filter
     * @param filter
     * @return
     */
    public static List<Product> filteredProducts(ProductFilter filter) {
        if (filter instanceof CategoryFilter) {
            return products.values().stream().filter(filter::isValid).collect(Collectors.toList());
        } else if (filter instanceof PriceFilter) {
            return products.values()
                           .stream()
                           .filter(filter::isValid)
                           .sorted(Comparator.comparingDouble(Product::salePrice))
                           .collect(Collectors.toList());
        }

        return products.values().stream().filter(filter::isValid).collect(Collectors.toList());
    }

    /**
     * Returns the total money spent on the inventory of the products that satisfy the given filter
     * @param filter
     * @return
     */
    public static double totalSpent(ProductFilter filter) {
        return products.values()
                       .stream()
                       .filter(filter::isValid)
                       .mapToDouble(Product::cost)
                       .sum();
    }

    /**
     * Returns the total profits of the products that satisfy the given filter
     * @param filter
     * @return
     */
    public static double profitsByCategory(String category) {
        return products.values()
                       .stream()
                       .filter(CategoryFilter.of(category)::isValid)
                       .mapToDouble(Product::cost)
                       .sum();
    }

    public static String getProductInfo(Product product) {
        return product.toString();
    }

    public static void addProduct(Product product) {
        products.putIfAbsent(product.id(), product);
    }
}
