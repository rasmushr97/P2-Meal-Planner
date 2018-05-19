package com.p2app.frontend.models;

    // Holds the information of one item in the shoppinglist
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
