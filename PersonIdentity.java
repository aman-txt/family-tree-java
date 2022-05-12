package main;
import java.util.ArrayList;
import java.sql.*;


import main.App.DbConnection;

//this class will store the Id of person as well as the person's
// parents as well as children
public class PersonIdentity{
    Integer PersonId;
    ArrayList<PersonIdentity> child = new ArrayList<PersonIdentity>();
    PersonIdentity parent1 = null;
    PersonIdentity parent2 = null;
    //constructor to store the unique ID
    PersonIdentity(Integer PersonId) throws SQLException {
        this.PersonId=PersonId;
    }
}
