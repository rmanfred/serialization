package io.adelina.serialization.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class UserProfile implements Serializable {
    private String name;
    private String email;
    private int age;
    private boolean isDeveloper;
    private float salary;
    private List<String> friends;
    private HashMap<String, Integer> relatives;

    public UserProfile(String name, String email, int age, boolean isDeveloper, float salary,
                       List<String> friends, HashMap<String, Integer> relatives){
        this.name = name;
        this.email = email;
        this.age = age;
        this.isDeveloper = isDeveloper;
        this.salary = salary;
        this.friends = friends;
        this.relatives = relatives;
    }

    @Override
    public String toString()
    {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", email=" + email +
                ", age='" + age + '\'' +
                ", isDeveloper=" + isDeveloper +
                ", salary=" + salary +
                ", friends=" + friends +
                ", relatives=" + relatives +
                '}';
    }

    public UserProfile(){}

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    public float getSalary(){return this.salary;}

    public void setSalary(float salary){this.salary = salary;}

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public HashMap<String, Integer> getRelatives() {
        return relatives;
    }

    public void setRelatives(HashMap<String, Integer> relatives) {
        this.relatives = relatives;
    }
}
