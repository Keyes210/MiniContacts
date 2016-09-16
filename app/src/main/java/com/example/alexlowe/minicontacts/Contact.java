package com.example.alexlowe.minicontacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexlowe on 9/13/16.
 */
public class Contact {
    private String name;
    private HashMap<String,String> numbers;

    public Contact(String name, String number, String type){
        this.name = name;
        numbers = new HashMap<>();
        addNumber(number, type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getNumbers() {
        return numbers;
    }

    public void addNumber(String number, String type) {
        this.numbers.put(number, type);
    }

    public String getOnlyNumber(){
        ArrayList<String> arr = new ArrayList<>();
        for(Map.Entry<String, String> entry : numbers.entrySet()){
            arr.add(entry.getKey());
        }
        return arr.get(0);
    }
}
