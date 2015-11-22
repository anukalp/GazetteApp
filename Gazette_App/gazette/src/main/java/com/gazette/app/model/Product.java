
package com.gazette.app.model;

import android.text.TextUtils;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@code Product} is an individual item for sale in a Shopify store.
 */
public class Product {

    private int productId;
    private String productBarCode;
    private String productBrand;
    private String productName;
    private String productSerialNumber;
    private String productCategory;
    private String productWarrantyDuration;
    private String productWarrantyProvider;
    private String productPurchaseDate;
    private String productVendor;
    private String productPrice;
    private Image ProductInvoice;
    private Image ProductImage;

    public void setProductId(int productId) {
        this.productId = productId;
    }
    /**
     * @return The unique identifier for this product.
     */
    public int getProductId() {
        return productId;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductWarrantyDuration() {
        return productWarrantyDuration;
    }

    public void setProductWarrantyDuration(String productWarrantyDuration) {
        this.productWarrantyDuration = productWarrantyDuration;
    }

    public String getProductWarrantyProvider() {
        return productWarrantyProvider;
    }

    public void setProductWarrantyProvider(String productWarrantyProvider) {
        this.productWarrantyProvider = productWarrantyProvider;
    }

    public String getProductPurchaseDate() {
        return productPurchaseDate;
    }

    public void setProductPurchaseDate(String productPurchaseDate) {
        this.productPurchaseDate = productPurchaseDate;
    }

    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Image getProductInvoice() {
        return ProductInvoice;
    }

    public void setProductInvoice(Image productInvoice) {
        ProductInvoice = productInvoice;
    }

    public Image getProductImage() {
        return ProductImage;
    }

    public void setProductImage(Image productImage) {
        ProductImage = productImage;
    }

    public String getProductSerialNumber() {
        return productSerialNumber;
    }

    public void setProductSerialNumber(String productSerialNumber) {
        this.productSerialNumber = productSerialNumber;
    }
}
