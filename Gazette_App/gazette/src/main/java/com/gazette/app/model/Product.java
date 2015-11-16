
package com.gazette.app.model;

import android.text.TextUtils;


import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@code Product} is an individual item for sale in a Shopify store.
 */
public class Product {

    private int productId;


    private String title;


    private String vendor;

    private String productType;

    private List<Image> images;


    private String tags;

    private Set<String> tagSet;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Set<String> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<String> tagSet) {
        this.tagSet = tagSet;
    }

    /**
     * @return The unique identifier for this product.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @return The title of this product.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The name of the vendor of this product.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @return The categorization that this product was tagged with, commonly used for filtering and searching.
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @return A list of additional categorizations that a product can be tagged with, commonly used for filtering and searching. Each tag has a character limit of 255.
     */
    public Set<String> getTags() {
        return tagSet;
    }


    /**
     * @return A list of {@link Image} objects, each one representing an image associated with this product.
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @return {code true} if this product has at least one image, {@code false} otherwise.
     */
    public boolean hasImage() {
        return images != null && !images.isEmpty();
    }


}
