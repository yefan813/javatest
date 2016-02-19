package com.yefan.testClass;

public class Husband extends Person{
	private Wife wife;

    Husband(){
        super("chenssy");
        System.out.println("Husband Constructor...");
    }

    public static void main(String[] args) {
        Husband husband  = new Husband();
    }
}
