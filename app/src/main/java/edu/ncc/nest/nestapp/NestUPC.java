package edu.ncc.nest.nestapp;

/**
 * this POJO holds the related upc, product and category fields
 * needed for lookup and processing of a Nest UPC code
 */
public class NestUPC {
    private String upc, brand, description;
    private int productId;
    private String productName, productSubtitle;
    private int categoryId;
    private String catDesc;

    public NestUPC(String upc, String brand, String description, int productId, String productName, String productSubtitle, int categoryId, String catDesc) {
        this.upc = upc;
        this.brand = brand;
        this.description = description;
        this.productId = productId;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.categoryId = categoryId;
        this.catDesc = catDesc;
    }

    public String getUpc() {
        return upc;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCatDesc() {
        return catDesc;
    }
}
