package brainbreaker.witty.Classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by brainbreaker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BargainProductClass extends ProductClass implements Serializable{
    private String name;
    private String image;
    private Integer price;
    private Integer bidValue;
    private Integer status;
    private String id;
    private String seller;
    private String description;
    private String additionals;
    private Integer finalprice;
    private Integer quantity;
    private long timestamp;
    public BargainProductClass(){
    }

    public BargainProductClass(String name, String image, Integer price, Integer bidValue
            ,String description,String seller, Integer status
            , String id, String additionals, Integer finalprice, Integer quantity, long timestamp){
        this.name = name;
        this.image = image;
        this.price = price;
        this.bidValue = bidValue;
        this.description = description;
        this.seller = seller;
        this.status = status;
        this.id = id;
        this.additionals = additionals;
        this.finalprice = finalprice;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public Integer getbidValue(){
        return bidValue;
    }
    public Integer getPrice(){
        return price;
    }
    public String getSeller(){
        return seller;
    }
    public String getDescription(){
        return description;
    }
    public Integer getStatus(){
        return status;
    }
    public String getId(){
        return id;
    }
    public String getAdditionals(){
        return additionals;
    }
    public Integer getFinalprice(){
        return finalprice;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public long getTimestamp(){
        return timestamp;
    }

}

