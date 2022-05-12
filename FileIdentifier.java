package main;

import java.sql.*;

import main.App.*;

//this class will simpaly store the uniqueid of the file
public class FileIdentifier {
    int FileId=0;

    //constructor to store the FileId
    FileIdentifier(int FileId) throws SQLException{
        this.FileId = FileId;
    }
}
