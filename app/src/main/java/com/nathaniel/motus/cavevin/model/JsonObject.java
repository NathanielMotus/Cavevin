package com.nathaniel.motus.cavevin.model;

import android.util.Log;

import java.util.ArrayList;

public class JsonObject extends Object {

//    **********************************************************************************************
//    class fields
//    **********************************************************************************************
    private String mJsonString;

//    **********************************************************************************************
//    constructors
//    **********************************************************************************************

    public JsonObject() {
//        create an empty JsonObject

        mJsonString = "{}";
    }

//    **********************************************************************************************
//    Converters
//    **********************************************************************************************
    public String jsonToString(){
        //return a String

        return mJsonString;
    }

    public static JsonObject stringToJsonObject(String string){
        //return a JsonObject which mJsonString is string

        JsonObject jsonObject=new JsonObject();
        jsonObject.mJsonString=string;
        return jsonObject;
    }

//    **********************************************************************************************
//    Writers
//    **********************************************************************************************

    public void addJsonObject(JsonObject jsonObject){
        //append a JsonObject to this

        String root=mJsonString.substring(0,mJsonString.length()-1);
        String rootLastChar=root.substring(root.length()-1);
        if (rootLastChar.compareTo("{")!=0 && rootLastChar.compareTo("[")!=0) root=root+",";

        mJsonString=root+jsonObject.mJsonString+"}";
    }

    public void addKeyValue(String key, String value){
        //append a key/value pair to this JsonObject
        //value is a String

        String root=mJsonString.substring(0,mJsonString.length()-1);
        if (root.length()>1) root=root+",";
        mJsonString=root+"\""+key+"\":\""+value+"\"}";
    }

    public void addKeyValue(String key, ArrayList<Object> objectArrayList){
        //append a key/value pair to this JsonObject
        //value can be :
        //      ArrayList<Integer>
        //      ArrayList<JsonObject>
        //      (to be continued if need be)

        String root=mJsonString.substring(0,mJsonString.length()-1);
        if (root.length()>1) root=root+",";

        mJsonString=root+"\""+key+"\":[";

        for (int i=0;i<objectArrayList.size();i++){
            if (i!=0) mJsonString=mJsonString+",";
            String objectClassString=objectArrayList.get(i).getClass().toString();
            switch (objectClassString){
                case "class java.lang.Integer":
                    mJsonString=mJsonString+"\""+objectArrayList.get(i)+"\"";
                    break;
                case "class com.nathaniel.motus.cavevin.model.JsonObject":
                    JsonObject jsonObject=(JsonObject)objectArrayList.get(i);
                    mJsonString=mJsonString+ jsonObject.mJsonString;
                    break;
                default:
                    mJsonString=mJsonString+objectClassString;
            }
        }
        mJsonString=mJsonString+"]}";
    }

//    **********************************************************************************************
//    Readers
//    **********************************************************************************************

    public String getKeyValue(String key){
        //return the value of the first occurrence of key in this JsonObject

        int keyIndex=mJsonString.indexOf(key);

        if (keyIndex!=-1) {
            int startValueIndex = keyIndex + key.length() + 3;
            if (mJsonString.substring(startValueIndex-1,startValueIndex).compareTo("\"")==0) {
                int endValueIndex = mJsonString.indexOf("\"", startValueIndex);
                return mJsonString.substring(startValueIndex, endValueIndex);
            }
            else{
                String error="NOT A VALUE ! : "+mJsonString.substring(startValueIndex-1,startValueIndex);
                return error;
            }
        }
        else return "";
    }

    public ArrayList<Object> getKeyValueArray(String key){
        //return an ArrayList<Object> from a key
        //Object can be
        //      Integer
        //      JsonObject
        //      (to be continued if need be)

        ArrayList<Object> objectArrayList=new ArrayList<>();

        int keyIndex=mJsonString.indexOf(key);

        if (keyIndex!=-1){
            int startValueIndex=keyIndex+key.length()+3;
            String currentString;
            if (mJsonString.substring(startValueIndex-1,startValueIndex).compareTo("[")==0){
                int endValueIndex=getClosureIndex(startValueIndex-1)-1;
                int i=startValueIndex;
                while (i<endValueIndex){
                    currentString=mJsonString.substring(i,getClosureIndex(i)+1);
                    if (currentString.substring(0,1).compareTo("{")==0) objectArrayList.add(JsonObject.stringToJsonObject(currentString));
                    else objectArrayList.add(Integer.parseInt(currentString.substring(1,currentString.length()-1)));
                    i=i+currentString.length()+1;
                }
            }
            else objectArrayList.add("NOT AN ARRAY ! : "+mJsonString.substring(startValueIndex-1,startValueIndex));
        }

        return objectArrayList;
    }

    private int getClosureIndex(int openIndex){
        //return the closure index of the JsonObject or Array
        //parse the string in search of closure character ("]" or "}")
        //ignoring those between 2 quotation marks

        String openChar=mJsonString.substring(openIndex,openIndex+1);
        String closeChar;

        switch (openChar){
            case "[":
                closeChar="]";
                break;
            case "{":
                closeChar="}";
                break;
            case "\"":
                closeChar="\"";
                break;
            default:
                return -1;
        }

        int nbOpenChar=1;   //+1 when openChar met, -1 when closeChar met
        int nbQuote=0;
        int i=openIndex+1;

        while (i<mJsonString.length()&&nbOpenChar>0){
            if (mJsonString.substring(i,i+1).compareTo("\"")==0) nbQuote=(nbQuote+1)%2;
            if (mJsonString.substring(i,i+1).compareTo(openChar)==0 && nbQuote==0) nbOpenChar++;
            if (mJsonString.substring(i,i+1).compareTo(closeChar)==0 && nbQuote==0) nbOpenChar--;

            //special case for "\""
            if (mJsonString.substring(i,i+1).compareTo(closeChar)==0 && closeChar.compareTo(openChar)==0) nbOpenChar=0;

            i++;
        }
        if (nbOpenChar==0) return i-1;
        else return -1;
    }

}
