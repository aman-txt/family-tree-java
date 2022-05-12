package main;

import java.sql.*;
import java.util.List;
import java.util.Map;

import main.App.*;

//this class mainly provides functionality of all the adding of data related to the media
// into the MediaArchive database
public class funM {

    //this method adds media file to the database using the fileLocation
    FileIdentifier addMediaFile( String fileLocation ){
        try {
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();


            //this query adds media file row to the database using the fileLocation
            st.execute("insert into MediaArchive (Name )values('"+fileLocation+"')");

            ResultSet rs= st.executeQuery("SELECT MediaId FROM MediaArchive WHERE MediaId=(SELECT max(MediaId) FROM MediaArchive)");
            rs.next();

            //creation of new object from the resultset
            FileIdentifier fi = new FileIdentifier(Integer.parseInt(rs.getString(1)));

            conn.close();
            return fi;
            
        } catch (Exception e) {

            return null;    
        }
    }

    //this function is used to delete all the current data from the database
    void DeleteRows() throws SQLException{
        DbConnection db = new DbConnection();
        Connection conn= db.createDBConnection();  
        Statement st= conn.createStatement();

        //delete queries//
        st.execute("Delete from MediaArchive");
        
    }

    //this function records media file attributes
    Boolean recordMediaAttributes( FileIdentifier fileIdentifier, Map<String, String> attributes ){
        try {
            int name= fileIdentifier.FileId;
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();  

            //the data will be taken from the map
            for(Map.Entry<String, String> entry : attributes.entrySet()){
                
                Statement st= conn.createStatement();
                String key= entry.getKey();
                String val= entry.getValue();

                //if the key equals to the Year
                if(key.equals("Year")){
                    st.execute("UPDATE MediaArchive SET Date = '"+val+"' WHERE MediaId = '"+name+"'");
                }
                //if the key equals to the City
                if(key.equals("City")){
                    st.execute("UPDATE MediaArchive SET Location = '"+val+"' WHERE MediaId = '"+name+"'");
                }
               
                st.close();
                
            }
            conn.close();
            return true;
        } catch (Exception e) {
            return false;    
        }
    }

    //this function adds the people into the media
    Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people ){
        try {
            int mediaID= fileIdentifier.FileId;
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();
            Statement st= conn.createStatement();
            //adding all the people into the database query
            for(int i=0;i<people.size();i++){
                st.execute("Insert into peopleInMedia(PersonId,MediaId) values('"+people.get(i).PersonId+"','"+mediaID+"')");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //this will add tags to the media file
    Boolean tagMedia( FileIdentifier fileIdentifier, String tag ){
        try {
            DbConnection db = new DbConnection();
            Connection conn= db.createDBConnection();  
            Statement st= conn.createStatement();
            int name= fileIdentifier.FileId;
            //this query will update the tag and add tag on appropriate place
            st.execute("UPDATE MediaArchive SET Tags = '"+tag+"' WHERE MediaId = '"+name+"'");
           
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
