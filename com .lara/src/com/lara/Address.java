package com.lara;
	import java.text.ParseException;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Comparator;
	import java.util.HashMap;
	import java.util.LinkedHashMap;
	import java.util.List;
	import java.util.Map.Entry;
	import java.util.Set;
	import java.util.TreeMap;

	
	public class Address
	{
	    public static void main(String args[]) throws ParseException {
	        
	        // let's create a map with Java releases and their code names
	        HashMap<String, String> map = new HashMap<String, String>();
	        
	        map.put("mani", "Sparkler");
	        map.put("kumar", "Playground");
	        map.put("devarinti", "Kestrel");
	        map.put("kanta", "Merlin");
	     
	        System.out.println("HashMap before sorting, random order ");
	        Set<Entry<String, String>> entries = map.entrySet();
	       
	        for(Entry<String, String> entry : entries){
	            System.out.println(entry.getKey() + " ==> " + entry.getValue());
	        }
	        
	        
	        
	        // Now let's sort HashMap by keys first 
	        // all you need to do is create a TreeMap with mappings of HashMap
	        // TreeMap keeps all entries in sorted order
	        
	        TreeMap<String, String> sorted = new TreeMap<>(map);
	        Set<Entry<String, String>> mappings = sorted.entrySet();
	        
	        System.out.println("HashMap after sorting by keys in ascending order ");
	        for(Entry<String, String> mapping : mappings){
	            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
    }
	        
	        
	     // Removing the existing key mapping 
	     map.remove("kumar");
	      
	  
	      
	        
        
//	        // Now let's sort the HashMap by values
//	        // there is no direct way to sort HashMap by values but you
//	        // can do this by writing your own comparator, which takes
//	        // Map.Entry object and arrange them in order increasing 
//	        // or decreasing by values.
//	        
//	        Comparator<Entry<String, String>> valueComparator = new Comparator<Entry<String,String>>() {
//	            
//	            @Override
//	            public int compare(Entry<String, String> e1, Entry<String, String> e2) {
//	                String v1 = e1.getValue();
//	                String v2 = e2.getValue();
//	                return v1.compareTo(v2);
//	            }
//	        };
//	        
//	        // Sort method needs a List, so let's first convert Set to List in Java
//	        List<Entry<String, String>> listOfEntries = new ArrayList<Entry<String, String>>(entries);
//	        
//	        // sorting HashMap by values using comparator
//	        Collections.sort(listOfEntries, valueComparator);
//	        
//	        LinkedHashMap<String, String> sortedByValue = new LinkedHashMap<String, String>(listOfEntries.size());
//	        
//	        // copying entries from List to Map
//	        for(Entry<String, String> entry : listOfEntries){
//	            sortedByValue.put(entry.getKey(), entry.getValue());
//	        }
//	        
//	        System.out.println("HashMap after sorting entries by values ");
//	        Set<Entry<String, String>> entrySetSortedByValue = sortedByValue.entrySet();
//	        
//	        for(Entry<String, String> mapping : entrySetSortedByValue){
//	            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
//	        }
//	    }

	    
	}
}
