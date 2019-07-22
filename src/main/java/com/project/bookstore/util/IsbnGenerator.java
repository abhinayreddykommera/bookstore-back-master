package com.project.bookstore.util;

import java.util.Random;

public class IsbnGenerator implements NumberGenerator{
    @Override
    public String generateNumber(){
        return "13-1478-"+Math.abs(new Random().nextInt());
    }
}
