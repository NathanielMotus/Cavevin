package com.nathaniel.motus.cavevin.controller;

import android.content.Context;
import android.widget.Toast;

import com.nathaniel.motus.cavevin.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CellarInputUtils {
    //a utilitary class to check all inputs

//    **********************************************************************************************
//    Static declaration
//    **********************************************************************************************

    private static final ArrayList<String> forbiddenCharacters= new ArrayList<>(Arrays.asList("\"", "\\","{","}","[","]"));
    private static final String replacementCharacter="*";


//    **********************************************************************************************
//    Constructor, private not to instantiate the class
//    **********************************************************************************************

    private CellarInputUtils() {
    }

//    **********************************************************************************************
//    Input check subs
//    **********************************************************************************************

    public static String replaceForbiddenCharacters(Context context, String inputString){
        //replace forbidden characters by replacementCharacter

        String initialInputString=inputString;
        for (int i=0;i<forbiddenCharacters.size();i++){
            inputString=inputString.replace(forbiddenCharacters.get(i),replacementCharacter);
        }

        if (initialInputString.compareTo(inputString)!=0){
            Toast.makeText(context, R.string.cellar_input_utils_forbidden_characters,Toast.LENGTH_SHORT).show();
        }

        return inputString;
    }

}
