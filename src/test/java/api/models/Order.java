package api.models;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("ingredients")
    private String[] ingredients;

    // Конструкторы
    public Order() {
        this.ingredients = new String[0];
    }

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    // Геттеры и сеттеры
    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}