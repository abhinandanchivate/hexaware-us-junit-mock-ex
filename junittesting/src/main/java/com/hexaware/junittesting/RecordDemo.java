package com.hexaware.junittesting;
public class RecordDemo {
    public static void main(String[] args) {
        Employee emp = new Employee(101, "Abhi", 55000.0);

        System.out.println(emp);                // Employee[id=101, name=Abhi, salary=55000.0]
        System.out.println(emp.name());         // Abhi
        System.out.println(emp.salary());       // 55000.0
      

        
     
    }
}
