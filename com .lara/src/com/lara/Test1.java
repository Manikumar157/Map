package com.lara;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class Test1 {
public static void main(String[] args)
{
	HashMap<String, String> map = new HashMap<>();
	map.put("mani","kumar");
	map.put("kanta","mamatha");
	System.out.println(map);
	
	Set<java.util.Map.Entry<String,String>> set =map.entrySet();
 
 for( java.util.Map.Entry<String,String> mani: set)
	 System.out.println(mani.getKey() +"====="+mani.getValue());
 
 TreeMap<String, String> sorted = new TreeMap<>(map);
 Set<java.util.Map.Entry<String,String>> set1 =sorted.entrySet();
 for(java.util.Map.Entry<String,String> mmm : set1)
 {
	 System.out.println(mmm.getKey() +"===="+ mmm.getValue());
 }
	 
map.remove("mani");
	}

}
