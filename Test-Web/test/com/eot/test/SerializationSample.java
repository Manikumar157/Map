package com.eot.test;

import java.io.*;
import java.util.*;

// This class implements "Serializable" to let the system know
// it's ok to do it. You as programmer are aware of that.
public class SerializationSample implements Serializable {

    // These attributes conform the "value" of the object.

    // These two will be serialized;
    private String aString = "The value of that string";
    private int    someInteger = Integer.MAX_VALUE;

    // But this won't since it is marked as transient.
    private transient List<File> unInterestingLongLongList;

    // Main method to test.
    public static void main( String [] args ) throws IOException  { 

        // Create a sample object, that contains the default values.
        SerializationSample instance = new SerializationSample();

        // The "ObjectOutputStream" class have the default 
        // definition to serialize an object.
        ObjectOutputStream oos = new ObjectOutputStream( 
                                     // By using "FileOutputStream" we will 
                                     // Write it to a File in the file system
                                     // It could have been a Socket to another 
                                     // machine, a database, an in memory array, etc.
                                     new FileOutputStream(  new File("o.ser")) ) ;

        // do the magic  
        oos.writeObject( instance );
        // close the writing.
        oos.close();
    }
}
