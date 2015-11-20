package com.gazette.app.model.productInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Gudigar on 11/20/2015.
 */
public class ProductInfoResponse {

    private String gtin;
    private String outpanUrl;
    private String name;
    private Attributes attributes;
    private List<Object> images = new ArrayList<Object>();
    private List<Object> videos = new ArrayList<Object>();
    private List<Object> categories = new ArrayList<Object>();

    /**
     * @return The gtin
     */
    public String getGtin() {
        return gtin;
    }

    /**
     * @param gtin The gtin
     */
    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    /**
     * @return The outpanUrl
     */
    public String getOutpanUrl() {
        return outpanUrl;
    }

    /**
     * @param outpanUrl The outpan_url
     */
    public void setOutpanUrl(String outpanUrl) {
        this.outpanUrl = outpanUrl;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * @param attributes The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * @return The images
     */
    public List<Object> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<Object> images) {
        this.images = images;
    }

    /**
     * @return The videos
     */
    public List<Object> getVideos() {
        return videos;
    }

    /**
     * @param videos The videos
     */
    public void setVideos(List<Object> videos) {
        this.videos = videos;
    }

    /**
     * @return The categories
     */
    public List<Object> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }


}
