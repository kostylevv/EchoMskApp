package com.unionfind.android.echomskapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * XML parser for flights schedule (http://sevkin.ru/flights.xml). 
 * Receives InputStream as input, returns ArrayList of trips.
 */
public class CleverPumpkinParser {
    private static final String ns = null;

    public ArrayList<Trip> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, "UTF-8"); // just in case
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<Trip> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Trip> entries = new ArrayList<Trip>();
        //Require "result" tag to start parsing 
        parser.require(XmlPullParser.START_TAG, ns, "result");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("trip")) {
                entries.add(readEntry(parser));
            } 
        }
        return entries;
    }

    
    // Parses the contents of a trip. 
    private Trip readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "trip");
        String duration = parser.getAttributeValue(ns, "duration"); // extracting "duration" attribute 
        String takeoff = null;										// from start tag
        String landing = null;
        String flight = null;
        String price = null;
        
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            Log.v("name", name.toString());
        	
            if (name.equals("takeoff")) {
                takeoff = readTakeoff(parser);
            } else if (name.equals("landing")) {
                landing = readLanding(parser);
            } else if (name.equals("flight")) {
                flight = readFlight(parser);
            }  else if (name.equals("price")) {
                price = readPrice(parser);
            }
        }
        return new Trip(duration, takeoff, landing, flight, price);
    }
    
    // Extracts attributes from "takeoff" tags
    private String readTakeoff(XmlPullParser parser) throws IOException, XmlPullParserException {
        String takeOff = "";
        parser.require(XmlPullParser.START_TAG, ns, "takeoff");
        String tag = parser.getName();
        
        if (tag.equals("takeoff")) {            
        		takeOff = parser.getAttributeValue(null, "time") + "  " + 
        				  parser.getAttributeValue(null, "date") + "  " +
        				  parser.getAttributeValue(null, "city");
                parser.nextTag();
            
        }
        parser.require(XmlPullParser.END_TAG, ns, "takeoff");
        return takeOff;
    }
    
    
 // Extracts attributes from "landing" tags
    private String readLanding(XmlPullParser parser) throws IOException, XmlPullParserException {
        String takeOff = "";
        parser.require(XmlPullParser.START_TAG, ns, "landing");
        String tag = parser.getName();
        
        if (tag.equals("landing")) {            
        		takeOff = parser.getAttributeValue(null, "time") + "  " +  
        				  parser.getAttributeValue(null, "date") + "  " + 
        				  parser.getAttributeValue(null, "city");
                parser.nextTag();
            
        }
        parser.require(XmlPullParser.END_TAG, ns, "landing");
        return takeOff;
    }
    
 // Extracts attributes from "flight" tags
    private String readFlight(XmlPullParser parser) throws IOException, XmlPullParserException {
        String takeOff = "";
        parser.require(XmlPullParser.START_TAG, ns, "flight");
        String tag = parser.getName();
        
        if (tag.equals("flight")) {            
        		takeOff = parser.getAttributeValue(null, "carrier")  + "  No." + 
        				  parser.getAttributeValue(null, "number")   + "  " + 
        				  parser.getAttributeValue(null, "eq");
                parser.nextTag();
            
        }
        parser.require(XmlPullParser.END_TAG, ns, "flight");
        return takeOff;
    }
    
    // Extracts value from "price" tags
    private String readPrice(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "price");
        String price = "";
        if (parser.next() == XmlPullParser.TEXT) {
            price = parser.getText();
            parser.nextTag();
        }
        
        parser.require(XmlPullParser.END_TAG, ns, "price");
        return price;
    }
}
