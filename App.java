package main;
import java.sql.*;
import java.util.*;


/*---------------SAMPLE TREE---------------------
*                 Janvi             ___Mayank   |
*                /     \         __/     |      |
*            Dishant  Ihsa______/      Lokesh   |
*           /  / |      \  \             |      |
*          /   |  \      |   \         Kamini   |
*     Aman  Balvin Ca  Gem  Harshil             |
*                       /\                      |
*                      /  \                     |
*                    Ema  Falguni               |
*------------------------------------------------
*  */
public class App {
 
public static void main(String[] args) throws SQLException {
   funC fn = new funC();
   fn.DeleteRows();
   funR fr = new funR();
   funM fm = new funM();
   fm.DeleteRows();
   HashMap<String,String> attributes = new HashMap<String,String>();
   HashMap<String,String> attributes1 = new HashMap<String,String>();


    /*Family tree
     * Testing and data
     * Inclusion
     * Starts*/

   attributes.put("Date of birth", "1/10/2002");
   attributes.put("Date of death", "21/08/2020");
   attributes.put("location of birth"   , "Ahmedabad");
   attributes.put("location of death"   , "Halifax");
   attributes.put("Gender"   , "M");
   attributes.put("Occupation"   , "Doctor");
   attributes1.put("Date of birth", "11/11/2001");
   attributes1.put("Date of death", "21/01/2021");
   attributes1.put("location of birth"   , "Halifax");
   attributes1.put("location of death"   , "Ahmedabad");
   attributes1.put("Gender"   , "F");
   attributes1.put("Occupation"   , "Engineer");

   PersonIdentity person= fn.addPerson("Aman");
   PersonIdentity person2= fn.addPerson("Janvi");
   PersonIdentity person3= fn.addPerson("Dishant");
    PersonIdentity person4= fn.addPerson("Isha");
    PersonIdentity person6= fn.addPerson("Mayank");
    PersonIdentity person7= fn.addPerson("Lokesh");
    PersonIdentity person8= fn.addPerson("Kamini");
    PersonIdentity person9= fn.addPerson("Balvin");
    PersonIdentity person10= fn.addPerson("ca");
    PersonIdentity person11= fn.addPerson("Gem");
    PersonIdentity person12= fn.addPerson("Harshil");
    PersonIdentity person13= fn.addPerson("Ema");
    PersonIdentity person14= fn.addPerson("Falguni");

    fn.recordPartnering(person11,person12);
    fn.recordPartnering(person2,person4);
    fn.recordPartnering(person6,person8);
    fn.recordDissolution(person8,person6);
    fn.recordPartnering(person8,person10);


   fn.recordAttributes(person, attributes);
   fn.recordAttributes(person2, attributes1);
   fn.recordAttributes(person3, attributes);
   fn.recordAttributes(person4, attributes1);
   fn.recordAttributes(person6, attributes1);
   fn.recordAttributes(person7, attributes);
   fn.recordAttributes(person8, attributes1);
   fn.recordAttributes(person9, attributes);
   fn.recordAttributes(person10, attributes1);
   fn.recordAttributes(person11, attributes);
   fn.recordAttributes(person12, attributes1);
   fn.recordAttributes(person13, attributes);
   fn.recordAttributes(person14, attributes1);



    fn.recordNote(person, "He is good Person");
    fn.recordReference(person, "Www.Apple.com");
    fn.recordNote(person, "yaya toure");
    fn.recordNote(person, "hello world");

    fn.recordNote(person2, "she is clever");

   fn.recordReference(person2, "Www.kakakaki.com");

   fn.recordChild(person2,person3);
    fn.recordChild(person2,person4);
    fn.recordChild(person3,person);
    fn.recordChild(person3,person9);
    fn.recordChild(person3,person10);
    fn.recordChild(person4,person11);
    fn.recordChild(person4,person12);
    fn.recordChild(person11,person13);
    fn.recordChild(person11,person14);
    fn.recordChild(person6,person4);
    fn.recordChild(person6,person7);
    fn.recordChild(person7,person8);


    /**Media Archive testing
    * S
    T
    A
    R
    * T
    -----------------------*/



    FileIdentifier fi= fm.addMediaFile("D:/abcd/efgh.mp4");
    FileIdentifier fi1= fm.addMediaFile("D:/efgh.mp4");
    FileIdentifier fi2= fm.addMediaFile("C:/ad/efgh.jpeg");
    System.out.println(fi.FileId+"-----"+fi1.FileId+"-----"+ fi2.FileId);
    HashMap<String,String> hm1= new HashMap<String,String>();
    hm1.put("Year", "2000-12-31");
    hm1.put("City", "Karachi");
    fm.recordMediaAttributes(fi, hm1);
    hm1.put("Year","2002-08-21");
    hm1.put("City", "Korea");
    fm.recordMediaAttributes(fi1, hm1);
    hm1.put("Year", "2004-03-01");
    hm1.put("City", "Japan");
    fm.recordMediaAttributes(fi2, hm1);

    List<PersonIdentity> heheboie = new ArrayList<PersonIdentity>();
    heheboie.add(person);
    heheboie.add(person2);
    fm.peopleInMedia(fi,heheboie );
    heheboie.remove(person);
    heheboie.add(person3);
    heheboie.add(person4);
    fm.peopleInMedia(fi1,heheboie);
    heheboie.remove(person3);
    heheboie.remove(person4);
    heheboie.remove(person2);
    heheboie.add(person6);
    heheboie.add(person7);
    heheboie.add(person8);
    fm.peopleInMedia(fi2,heheboie);
    fm.tagMedia(fi,"#goa #Karachi #MenInBlack #HeheBoie");
    fm.tagMedia(fi1,"#Pakistan #goa");
    fm.tagMedia(fi2,"#DoDo");





    /* Reporting functions
    *      Testings      */
    List<String > temp1 =fr.notesAndReferences(person);
    for(int i=0; i <temp1.size();i++){
        System.out.println(temp1.get(i));
    }
    System.out.println(fr.findName(fr.findPerson("Janvi")));
    System.out.println(fr.findMediaFile(fr.findMediaFile("D:/efgh.mp4")));

    fr.findRelation(fr.findPerson("Aman"),fr.findPerson("Kamini")).PrintRelation();

    System.out.println(fr.ancestores(fr.findPerson("Ema"),2));
    System.out.println("---------------");
    System.out.println(fr.descendents(fr.findPerson("Janvi"),2));

    System.out.println(fr.notesAndReferences(person));

    System.out.println(fr.findMediaByTag("#goa","1999-12-31","2003-08-21"));

    System.out.println(fr.findMediaByLocation("Japan","2003-03-01","2005-03-01"));

    Set<PersonIdentity> temp = new HashSet<>();
    temp.add(person);
    temp.add(person2);
    System.out.println("Find ind media"+fr.findIndividualsMedia(temp,"1999-12-31","2003-08-21"));
    System.out.println(fr.findBiologicalFamilyMedia(person3));

}    

//this class will create the connection to the database
public static class DbConnection {

    //here provide the URL of the database
    static final String DB_URL = "jdbc:mysql://db.cs.dal.ca:3306/ampatel?serverTimezone=UTC&useSSL=false";
    static final String USER = "ampatel";
    static final String PASS = "B00888136";
    
    
    public Connection createDBConnection() { 
    Connection conn = null;   
    try{
        //this will create a connection using DriverManager
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return conn;
    }
}
}
    


