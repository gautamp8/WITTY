package brainbreaker.witty.Classes;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by brainbreaker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DemandProductClass implements Serializable {
    private String name;
    private String image;
    private Integer price;
    private String comment;
    private String brand;
    private String id;
    private String category;

    public DemandProductClass(){
    }

    public DemandProductClass(String name, String image, Integer price, String description, String brand, String id, String category){
        this.name = name;
        this.image = image;
        this.price = price;
        this.comment = comment;
        this.brand = brand;
        this.id = id;
        this.category = category;
    }

    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getComment(){
        return comment;
    }
    public Integer getprice(){
        return price;
    }
    public String getBrand(){
        return brand;
    }
    public String getId(){
        return id;
    }
    public String getCategory(){
        return category;
    }
}

