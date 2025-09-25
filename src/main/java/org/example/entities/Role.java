package org.example.entities;

public class Role {
    private int id;
    private String role_name;
    private String description;

    public Role(int id, String role_name , String description) {
        this.id = id;
        this.role_name = role_name;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getRoleName() {
        return role_name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setRole_name(String role_name){
        this.role_name = role_name;
    }

    public void setDescription(String description){
        this.description = description ;
    }

    @Override
    public String toString() {
        return role_name;
    }
}
