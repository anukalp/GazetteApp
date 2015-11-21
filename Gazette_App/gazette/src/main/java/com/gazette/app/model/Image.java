package com.gazette.app.model;

import android.graphics.Bitmap;


/**
 */
public class Image {

    private long productId;

    private int src;

    private Bitmap bitmap;

    /**
     * @return Specifies the location of the product image.
     */
    public int getSrc() {
        return src;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
