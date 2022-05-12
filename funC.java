package main;

import java.sql.*;
import java.sql.SQLException;
import java.util.*;

import main.App.DbConnection;


//this class mainly provides functionality of all the adding of data related to the person
// into the FamilyTree database
public class funC {

    //this function will add the new person everytime been called
    PersonIdentity addPerson(String name) throws SQLException{

        DbConnection db = new DbConnection();
        Connection conn= db.createDBConnection();
        Statement st= conn.createStatement();

        //this will insert the person into the tree
        st.execute("insert into FamilyTree values(PersonId,'"+name+"',DOB,DOD,BLoc,DLoc,Gender,Occupation)");
        //storing the person Id and adding a new object
        ResultSet rs= st.executeQuery("SELECT PersonId FROM FamilyTree WHERE PersonId=(SELECT max(PersonId) FROM FamilyTree)");
        rs.next();
        Integer PersonId= Integer.parseInt(rs.getString(1));
        PersonIdentity pi = new PersonIdentity(PersonId);
        //PersonId = st.execute(sql);
        conn.close();
        return pi;
    }

    //this function will record the remaining attributes of the person
    Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes){
        try {

            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();  

            //the attributes will be retrived from the hashmap
            for(Map.Entry<String, String> entry : attributes.entrySet()){
                int name = person.PersonId;
                Statement st= conn.createStatement();
                String key= entry.getKey();
                String val= entry.getValue();

                //if the key is Date of birth then...
                if(key.equals("Date of birth")){
                    st.execute("UPDATE FamilyTree SET DOB = '"+val+"' WHERE PersonId = '"+name+"'");
                }

                //if the key is Date of death then..
                if(key.equals("Date of death")){
                    st.execute("UPDATE FamilyTree SET DOD = '"+val+"' WHERE PersonId = '"+name+"'");
                }
                //if the key is location of birth then..

                if(key.equals("location of birth")){
                    st.execute("UPDATE FamilyTree SET BLoc = '"+val+"' WHERE PersonId = '"+name+"'");
                }

                //if the key is location of death then..
                if(key.equals("location of death")){
                    st.execute("UPDATE FamilyTree SET Dloc = '"+val+"' WHERE PersonId = '"+name+"'");
                }

                //if the key is Gender then..
                if(key.equals("Gender")){
                    st.execute("UPDATE FamilyTree SET Gender = '"+val+"' WHERE PersonId = '"+name+"'");
                }

                //if the key is Occupation then..
                if(key.equals("Occupation")){
                    st.execute("UPDATE FamilyTree SET Occupation = '"+val+"' WHERE PersonId = '"+name+"'");
                }
                st.close();
            }
            conn.close();
            return true;
        } catch (Exception e) {
            return false;    
        }
    }

    //this function will record the references
    Boolean recordReference( PersonIdentity person, String reference ){
        try {
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();  
            Statement st= conn.createStatement();
            int name= person.PersonId;
            //the query will update the references for the given name
            st.execute("Insert into notesAndRef (PersonID,details) values ('"+person.PersonId+"','"+reference+"')");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //this function will record the notes
    Boolean recordNote( PersonIdentity person, String note ){
        try {
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();  
            Statement st= conn.createStatement();
            int name= person.PersonId;
            //the query will update the notes for the given name
            st.execute("Insert into notesAndRef (PersonID,details) values ('"+person.PersonId+"','"+note+"')");
            
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //this function is used to delete all the current data from the database
    void DeleteRows() throws SQLException{
        DbConnection db = new DbConnection();
        Connection conn= db.createDBConnection();  
        Statement st= conn.createStatement();

        //delete queries//

        st.execute("Delete from FamilyTree");
        st.execute("Delete from notesAndRef");
        st.execute("Delete from treeCreation");
        st.execute("Delete from peopleInMedia");
        
    }

    //this function records the parent child relationship
    Boolean recordChild( PersonIdentity parent, PersonIdentity child ){
        try {
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            //this will insert a new row into the treeCreation database
            st.execute("Insert into treeCreation (parentId,childId) values ('"+parent.PersonId+"','"+child.PersonId+"')");
            return true;
        } catch (Exception e) {
            return false;    
        }
    }

    //this function records the partnership between two people
    Boolean recordPartnering( PersonIdentity partner1, PersonIdentity partner2 ){
        try {

            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            //This query records the partnering between two people into the marTable table
            st.execute("Insert into marTable (malId,femId) values ('"+partner1.PersonId+"','"+partner2.PersonId+"')");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //this function records the dissolution between two people
    Boolean recordDissolution( PersonIdentity partner1, PersonIdentity partner2 ){
        try {

            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            //This query removes the partnering between two people into the marTable table
            //in short it delets the row
            st.execute("delete from marTable where malId='"+partner1.PersonId+"' and femId='"+partner2.PersonId+"'");

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
