package com.stargazing.suitcase.database.entities;

import androidx.annotation.Size;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private int quantity;
    private double price;
    private String image;
    private boolean finish;
    private String userEmail;
    private Date created;
    private Date updated;

    public Item() {
    }

    public Item(String description, int quantity, double price, String image, boolean finish, String userEmail) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.finish = finish;
        this.userEmail = userEmail;
        this.created = new Date();
        this.updated = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return quantity == item.quantity && Double.compare(price, item.price) == 0 && finish == item.finish && Objects.equals(description, item.description) && Objects.equals(image, item.image) && Objects.equals(userEmail, item.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, quantity, price, image, finish, userEmail);
    }

    public Item update(Item item) {
        this.description = item.description;
        this.quantity = item.quantity;
        this.price = item.price;
        this.image = item.image;
        this.finish = item.finish;
        this.userEmail = item.userEmail;
        this.updated = new Date();
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", finish=" + finish +
                ", userEmail='" + userEmail + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
