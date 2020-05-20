package com.example.teamproject;

public class Menu_item {
    private int profile; //가게 이름
    private String name; //메뉴 이름
    private String price; //메뉴 가격

    public int getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Menu_item(int profile, String name, String price){
        this.profile=profile;
        this.name=name;
        this.price=price;
    }
}
