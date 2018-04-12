package com.example.rasmus.menucomplete.models;

public class ShoppingListItemModel {

    boolean isSelected;
    String itemName;

    public ShoppingListItemModel(boolean isSelected, String itemName) {
        this.isSelected = isSelected;
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
