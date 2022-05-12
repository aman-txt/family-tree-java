package main;
import main.BiologicalRelation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/*This class contains all the required Reporting
* functions of the program it's been called
* into the App.java file*/

public class funR {
    ArrayList<PersonIdentity> allNodes;

    //this method retrives all the people from the database and creates the node
    void getPersonIdfromDb() throws SQLException {
        allNodes = new ArrayList<PersonIdentity>();

        App.DbConnection db = new App.DbConnection();
        Connection conn= db.createDBConnection();
        Statement st= conn.createStatement();
        ResultSet rs=null;

        //this code creates a new node for every person in the database
        rs = st.executeQuery("select PersonId from FamilyTree");
        //adding each node into the arraylist
        while (rs.next()){
            PersonIdentity n =  new PersonIdentity(Integer.parseInt(rs.getString(1)));
            allNodes.add(n);
        }
    }

    //this method creates the tree from the database to derive the relations
    void treeCreationfromDb() throws SQLException {

        //setting up the connection
        App.DbConnection db = new App.DbConnection();
        Connection conn= db.createDBConnection();
        Statement st= conn.createStatement();
        ResultSet rs=null;

        //this function gets nodes from the tree
        getPersonIdfromDb();
        //adding joins of the each member
        rs= st.executeQuery("Select * from treeCreation");

        //this lines of code will compare the Ids from database with allNodes
        while (rs.next()){
            int val=0;
            int parent=Integer.parseInt(rs.getString(1));
            int child=Integer.parseInt(rs.getString(2));
            //traversing through each and every node
            for(int i=0;i<allNodes.size();i++){
                if(parent == allNodes.get(i).PersonId) {
                    for (int j = 0; j < allNodes.size(); j++) {

                        //when the child node matches it assigns the value to parent child and also checks
                        // for having two parents
                        if (child == allNodes.get(j).PersonId) {
                            if(allNodes.get(j).parent1==null){
                                allNodes.get(i).child.add(allNodes.get(j)) ;
                                allNodes.get(j).parent1= allNodes.get(i);
                            }else if(allNodes.get(j).parent2==null){
                                allNodes.get(i).child.add(allNodes.get(j)) ;
                                allNodes.get(j).parent2= allNodes.get(i);
                            }
                            else {
                                //if User tries to insert 3rd parent
                                System.out.println("Both parents occupide INVALId OPERATION");
                            }
                            break;
                        }
                    }
                }
            }
        }
    }


    //this function will find the PersonIdentity from the name
    PersonIdentity findPerson( String name ){
        try {
            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //getting the person's Id from his name
            rs = st.executeQuery("select PersonId from FamilyTree where Name = '"+name+"'");
            rs.next();

            //this will set up the nodes in array list
            getPersonIdfromDb();
            for(int i=0;i<allNodes.size();i++){
                if(allNodes.get(i).PersonId == Integer.parseInt(rs.getString(1))){
                    return allNodes.get(i);
                }
            }
            return null;

        } catch (Exception e) {
            return null;
        }

    }

    //this function will return the FileIdentifier from the name of the file
    FileIdentifier findMediaFile( String name ){
        try {
            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;

            //this query will get the id of media file from the database
            rs = st.executeQuery("select MediaId from MediaArchive where Name = '"+name+"'");
            rs.next();

            return new FileIdentifier(Integer.parseInt(rs.getString(1)));
        } catch (Exception e) {
            return null;
        }
    }

    //this function will retrive the id from the name
    String findName( PersonIdentity id ){
        try {
            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //this will retrive the name from the ID
            rs = st.executeQuery("select Name from FamilyTree where PersonId = '"+id.PersonId+"'");
            rs.next();
            return rs.getString(1);


        } catch (Exception e) {
            return null;
        }

    }

    //this method will get the media file from the fileId
    String findMediaFile( FileIdentifier fileId ){

        try {
            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //this query will retrive the media name from it's Id
            rs = st.executeQuery("select Name from MediaArchive where MediaId = '"+fileId.FileId+"'");
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            return null;
        }

    }
    /*this lists will be used to store the
    * outputs of every nodes later in the program
    * and find interconnections by comparing them*/
    List<Integer> temp = new ArrayList<>();
    List<Integer> temp1 = new ArrayList<>();
    List<List<Integer> > finAns = new ArrayList<>();

    //this code will find all the parents for any particular node
    List<Integer> ParentFinder(PersonIdentity person){
        //adding the id of current node
        temp.add(person.PersonId);

        //till the person have parent1, traversing to them
        if((person.parent1 == null && person.parent2!= null)  ){
            //adding the ids for current recursive iteration, will be
            // used later if the future iteration will fail, this will
            //remain as a backup
            temp1.add(person.parent2.PersonId);
        }
        //till the person have parent1, traversing to them
        if (person.parent1 != null && person.parent2 == null){
            //adding the ids for current recursive iteration, will be
            // used later if the future iteration will fail, this will
            //remain as a backup
            temp1.add(person.parent1.PersonId);
        }
        //calling recursive function by passing the prent

        if(person.parent1 !=null){
            temp=ParentFinder(person.parent1);
        }
        //calling recursive function by passing the prent

        if(person.parent2 !=null){
            temp=ParentFinder(person.parent2);
        }
        if(person.parent1 == null && person.parent2 == null){
            //returning the final temp
            finAns.add(temp);
            return temp1;
        }
        return null;
    }


    //this function will find the relationship between two people, it will use ParentFinder recursive
    // function and treeCreationfromDb() to achieve this goal
    BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 ){

        try {

            //first of all it creates tree from the database
            treeCreationfromDb();
            List<List<Integer>> p1 = new ArrayList<>();
            List<List<Integer>> p2 = new ArrayList<>();

            //if there is no relation then it will show -2,-2
            int degofCous=-2;
            int degOfRem=-2;

            //if the person1's Id and the id of any node will math then that node
            // will be passed into parentFinder program
            for (PersonIdentity allNode : allNodes) {
                if (Objects.equals(allNode.PersonId, person1.PersonId)) {
                    System.out.println("yes");
                    temp1.add(allNode.PersonId);
                    ParentFinder(allNode);
                    temp = new ArrayList<>();
                    temp1 = new ArrayList<>();
                    p1 = finAns;
                    finAns = new ArrayList<>();
                    break;
                }
            }

            //if the person1's Id and the id of any node will math then that node
            // will be passed into parentFinder program
            for (PersonIdentity allNode : allNodes) {
                if (Objects.equals(person2.PersonId, allNode.PersonId)) {
                    temp1.add(allNode.PersonId);
                    ParentFinder(allNode);
                    temp = new ArrayList<>();
                    temp1 = new ArrayList<>();
                    p2 = finAns;
                    finAns = new ArrayList<>();
                    break;
                }
            }

            //this function will find the intersection and calculate
            // the
            for (List<Integer> integers : p1) {
                for (List<Integer> integerList : p2) {
                    for (int k = 0; k < integers.size(); k++) {
                        for (int l = 0; l < integerList.size(); l++) {
                            if (Objects.equals(integers.get(k), integerList.get(l))) {
                                //this eqn derives the values of deg of cousinship and
                                // deg of removal
                                degofCous = (Math.min(k, l) - 1);
                                degOfRem = Math.abs(k - l);
                            }
                        }
                    }
                }
            }

            //returning the new BiologicalRelation() object with required values
            return new BiologicalRelation(degofCous,degOfRem,findName(person1),findName(person2));

        } catch (Exception e) {
            return null;
        }

    }
    ArrayList<Integer> al1 = new ArrayList<Integer>();

    //recursive function to find decendents
    void decRecurr(PersonIdentity person, Integer gene){
        al1.add(person.PersonId);
        //recursion till the given max generation
        for(PersonIdentity p : person.child){
            if (gene != 0) {
                decRecurr(p, gene - 1);
            }
        }
    }

    //this will return the set of personIdentity of all the people available
    // in the given generation
    Set<PersonIdentity> descendents( PersonIdentity person, Integer generations ){
        try {
            Set<PersonIdentity> temp = new HashSet<>();
            //creation of the tree
            treeCreationfromDb();

            //whenever the node will match it will pass that into the recursive function
            for (PersonIdentity allNode : allNodes) {
                if (Objects.equals(person.PersonId, allNode.PersonId)) {
                    decRecurr(allNode, generations);
                    break;
                }
            }

            //adding all the people returned by the recursive function
            for (Integer integer : al1) {
                temp.add(new PersonIdentity(integer));
            }
            al1 = new ArrayList<>();
            return temp;
        } catch (Exception e) {
            return null;
        }

    }
    ArrayList<Integer> al = new ArrayList<Integer>();

    //recursive function to find ancestors
    void ancestorRecurr(PersonIdentity person, Integer gene){
        al.add(person.PersonId);
        //recursion till any parent is available
        if(person.parent1 != null && gene !=0){
            ancestorRecurr(person.parent1,gene-1);
        }
        if(person.parent2 != null && gene !=0){
            ancestorRecurr(person.parent2,gene-1);
        }

    }
    //this will return the set of personIdentity of all the people available
    // in the given generation
    Set<PersonIdentity> ancestores( PersonIdentity person, Integer generations ){
        try {
            Set<PersonIdentity> temp = new HashSet<>();
            //creation of the tree
            treeCreationfromDb();

            //whenever the node will match it will pass that into the recursive function
            for (PersonIdentity allNode : allNodes) {
                if (Objects.equals(person.PersonId, allNode.PersonId)) {
                    ancestorRecurr(allNode, generations);
                    break;
                }
            }

            //adding all the people returned by the recursive function
            for (Integer integer : al) {
                temp.add(new PersonIdentity(integer));
            }
            al = new ArrayList<>();
            return temp;
        } catch (Exception e) {
            return null;
        }

    }

    //this function will return the notes and references of the user
    List<String> notesAndReferences( PersonIdentity person ){
        try {
            //don't forget to sequence them.

            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //retriving the notes and references of the person
            rs = st.executeQuery("select details from notesAndRef where PersonId = '"+person.PersonId+"'");

            List<String> NandR = new ArrayList<String>();
            //storing them into the db
            while (rs.next()){
                NandR.add(rs.getString(1));
            }
            return NandR;
        } catch (Exception e) {
            return null;
        }
    }

    //this function will find media by tags and within given time frame
    Set<FileIdentifier> findMediaByTag( String tag , String startDate, String endDate){
        try {

            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //this query will retrive the mediaId from the tag and given dates
            rs = st.executeQuery("select MediaId from MediaArchive where Tags like '%"+tag+"%' AND Date between '"+startDate+"' AND '"+endDate+"'");
            Set<FileIdentifier> Ids = new HashSet<FileIdentifier>();

            //storing the resultset into the set
            while (rs.next()){
                Ids.add(new FileIdentifier(Integer.parseInt(rs.getString(1))));
            }

            return Ids;
        } catch (Exception e) {
            System.out.println("masti");
            return null;
        }

    }

    //this function will find media by location and within given time frame

    Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate){
        try {
            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //this query will retrive the mediaId from the location and given dates

            rs = st.executeQuery("select MediaId from MediaArchive where Location ='"+ location +"'AND Date between '"+startDate+"' AND '"+endDate+"'");
            Set<FileIdentifier> Ids = new HashSet<FileIdentifier>();
            //adding the resultset into the set
            while (rs.next()){
                Ids.add(new FileIdentifier(Integer.parseInt(rs.getString(1))));
            }
            return Ids;
        } catch (Exception e) {
            return null;
        }
    }

    // this function will find media by people and within given time frame

    List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String endDate){
        try {
            //the query size will be indefinite so we need to use temp string
            String temp = "select distinct peopleInMedia.MediaId,count(distinct peopleInMedia.PersonId) as count  from peopleInMedia Inner join MediaArchive as ma On peopleInMedia.MediaId= ma.MediaId where ";

            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs=null;
            //adding the part of the query for every person
            for (PersonIdentity f: people){
                temp+= "peopleInMedia.PersonId ='"+f.PersonId+"' or ";
            }

            temp=temp.substring(0,temp.length()-4);
            //ending the query by adding time limits
            temp+=" And Date between '"+startDate+"' AND '"+endDate+"'";
            rs =  st.executeQuery(temp);
            List<FileIdentifier> media = new ArrayList<FileIdentifier>();
            //adding the new objects of fileIdentifier into the list and returning it
            while (rs.next()){
                media.add(new FileIdentifier(Integer.parseInt(rs.getString(1))));
            }
            return media;
        } catch (Exception e) {
            return null;
        }

    }

    // this function will find media by a single person

    List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person){
        try {
            List<FileIdentifier> media = new ArrayList<FileIdentifier>();

            App.DbConnection db = new App.DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            ResultSet rs= null;
            //this query will return the distinct media ids of the people
            rs = st.executeQuery("select distinct MediaId from peopleInMedia where PersonId like '%"+person.PersonId+"%'");
            //adding the new objects of fileIdentifier into the list and returning it
            while (rs.next()){
                media.add(new FileIdentifier(Integer.parseInt(rs.getString(1))));
            }

            return media;
        } catch (Exception e) {
            return null;
        }

    }
}
