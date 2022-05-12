package main;

//this class will store the relationship between two people
public class BiologicalRelation {
    int degOfCous=0;
    int degOfRem=0;
    String name1 , name2;
    //constructor to store the name and degree of cousinship and removal
    BiologicalRelation(int degOfCous,int degOfRem,String name1, String name2){
        this.degOfCous=degOfCous;
        this.degOfRem =degOfRem;
        this.name1=name1;
        this.name2=name2;
    }
    //printing the output whenever the method is called
    void PrintRelation(){
        System.out.println("Two people are:"+name1+" and "+name2);
        System.out.println("Degree of cousinship is:"+degOfCous+"  Degree of removal is:"+degOfRem);
    }
}
