package com.example.examen.placeholder;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Placeholder {

    public static String placeholderImage () {

        try {

            FileInputStream fileInputStream = new FileInputStream("src/main/resources/placeholder/sdf");
            String placeholderImage = IOUtils.toString(fileInputStream, "UTF-8");

            //System.out.println("placeholderImage: " + placeholderImage.toString());
            return placeholderImage;

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
