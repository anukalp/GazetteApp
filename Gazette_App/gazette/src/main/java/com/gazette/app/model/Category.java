package com.gazette.app.model;

/**
 * Created by Anil Gudigar on 11/22/2015.
 */
public class Category {
    private String Name;
    private Image Image;
    private int Id;
    private int Count;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public com.gazette.app.model.Image getImage() {
        return Image;
    }

    public void setImage(com.gazette.app.model.Image image) {
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
