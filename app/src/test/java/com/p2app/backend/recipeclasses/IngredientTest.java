package com.p2app.backend.recipeclasses;

import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientTest {

    @Test
    public void uniqueIngredientTest01(){
        Ingredient ing1 = new Ingredient(1, "name", 1, "bag", "500 gram");
        Ingredient ing2 = new Ingredient(2, "name2", 1, "bag", null);
        assertNotEquals(ing1, ing2);
    }

    @Test
    public void uniqueIngredientTest02(){
        Ingredient ing1 = new Ingredient(1, "name", 1, "bag", "500 gram");
        Ingredient ing2 = new Ingredient(2, "name2", 2, "bag", null);
        ing2.setID(ing1.getID());
        ing2.setAmount(ing1.getAmount());
        ing2.setName(ing1.getName());
        ing2.setUnit(ing1.getUnit());
        ing2.setInParentheses(ing1.getInParentheses());
        assertEquals(ing1, ing2);
    }
}