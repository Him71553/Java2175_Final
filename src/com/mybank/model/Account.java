package com.mybank.model;

public class Account {
    public enum Role {
        Admin,
        User
    }
    protected int money;
    protected String name;
    protected String id;
    public Role role;
    public void deposit(int m){
        money+=m;
    }
    public int getMoney(){
        return money;
    }
    public void withdraw(int m){
        if(money<m){
            System.out.println("Invalid input");
        }
        else{
            money-=m;
        }
    }
}
