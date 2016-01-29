package brainbreaker.witty.Classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by brainbreaker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductClass implements Serializable {
    private String name;
    private String image;
    private Integer price;
    private String description;
    private String seller;
    private String id;
    private String category;
    private Integer quantity;
    public ProductClass(){
    }

    public ProductClass(String name, String image, Integer price){
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public ProductClass(String name, String image, Integer price, String description, String seller){
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.seller = seller;
    }

    public ProductClass(String name, String image, Integer price, String description, String seller, String id, String category){
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.seller = seller;
        this.id = id;
        this.category = category;
    }
    public ProductClass(String name, String image, Integer price, String description, String seller, String id, Integer quantity){
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.seller = seller;
        this.id = id;
        this.quantity = quantity;
    }
    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getDescription(){
        return description;
    }
    public Integer getprice(){
        return price;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public String getSeller(){
        return seller;
    }
    public String getId(){
        return id;
    }
    public String getCategory(){
        return category;
    }
}

