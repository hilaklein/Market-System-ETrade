package com.workshop.ETrade.Domain.Users;

import com.workshop.ETrade.Domain.Pair;
import com.workshop.ETrade.Domain.Stores.Store;

import java.util.HashMap;

public class SystemManager extends Member{
    public SystemManager(String userName, String password,String name,String lastName) {
        super(userName, password,name,lastName);
    }

    @Override
    public boolean exitSystem() {
        return super.exitSystem();
    }

    @Override
    public ShoppingCart getMyShopCart() {
        return super.getMyShopCart();
    }

    @Override
    public String addProdToCart(Store store, int quantity, String prodName) {
        return super.addProdToCart(store, quantity, prodName);
    }

    @Override
    public String removeProd(Store s, int quantity, String prodName) {
        return super.removeProd(s, quantity, prodName);
    }

    @Override
    public HashMap<String, Pair<Integer, String>> displayCart() {
        return super.displayCart();
    }

    @Override
    public String purchase(CreditCard card, SupplyAddress address) {
        return super.purchase(card, address);
    }

    @Override
    public String getStoreInfo(Store s) {
        return super.getStoreInfo(s);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public Member signUp(String userName, String password, String name, String lastName) {
        return super.signUp(userName, password, name, lastName);
    }

    @Override
    public void addSecurityQuest(String quest, String ans) {
        super.addSecurityQuest(quest, ans);
    }

    @Override
    public void removeSecurityQuest(String quest) {
        super.removeSecurityQuest(quest);
    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    @Override
    public String getMail() {
        return super.getMail();
    }

    @Override
    public void setMail(String mail) {
        super.setMail(mail);
    }

    @Override
    public int getSecurityLvl() {
        return super.getSecurityLvl();
    }

    @Override
    public SupplyAddress getAddress() {
        return super.getAddress();
    }

    @Override
    public void setCity(String city) {
        super.setCity(city);
    }

    @Override
    public void setStreet(String street) {
        super.setStreet(street);
    }

    @Override
    public void setStreetNum(int sNum) {
        super.setStreetNum(sNum);
    }

    @Override
    public void setApartmentNum(int apartmentNum) {
        super.setApartmentNum(apartmentNum);
    }

    @Override
    public void setDiscount(int discount) {
        super.setDiscount(discount);
    }

    @Override
    public CreditCard getCard() {
        return super.getCard();
    }

    @Override
    public int getDiscount() {
        return super.getDiscount();
    }

    @Override
    public void setCard(CreditCard card) {
        super.setCard(card);
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void setConnected(boolean connected) {
        super.setConnected(connected);
    }

    @Override
    public void addAddress(String country,String city, String street, int streetNum, int apartmentNum, int zip) {
        super.addAddress(country, city, street, streetNum, apartmentNum, zip);
    }

    @Override
    public boolean logIn(String userName, String password) {
        return super.logIn(userName, password);
    }

    @Override
    public void setAddress(SupplyAddress address) {
        super.setAddress(address);
    }
}