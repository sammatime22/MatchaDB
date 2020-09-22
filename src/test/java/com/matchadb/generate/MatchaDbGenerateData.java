package com.matchadb.generate;

import java.util.HashMap;
import java.util.HashMap;

/**
 * Generates mock data for the Unit tests to use.
 */
public class MatchaDbGenerateData {

    /**
     * Generates the expected table to be created by the MatchaDbTable when 
     * the TestFileClothesWebsiteAPI.json file is given.
     *
     * In this test:
     *   b - 2  // Branching Factor
     *   m - 2  // Number of Children per Node
     */
    public HashMap<String, Object> generateClothesWebsiteAPITable() {
        HashMap<String, Object> table = new HashMap<String, Object>();

        // Add in the Shirts
        HashMap<String, Object> shirtsIndex = new HashMap<String, Object>();
        
        HashMap<String, Object> ghjklIndex = new HashMap<String, Object>();
        HashMap<String, Object> zxcvbIndex = new HashMap<String, Object>();

        HashMap<String, Object> buttonDown = buildClothesObject(
            "Button Down in Black", "ghjkl", "You're new favorite Black Button Down!",
            20.99
        );
        ghjklIndex.put("Button Down in Black", buttonDown);

        shirtsIndex.put("ghjkl", ghjklIndex);

        // Add in the Pants


        // Add in the Shoes


        // Add in the Hats


        return table; // Not implemented yet
    }

    /**
     * A helper method to build a clothes object.
     */
    private HashMap<String, Object> buildClothesObject(String name, String brand,
        String description, double price) {
        HashMap<String, Object> clothesObject = new HashMap<String, Object>() {{
            put("Item Name", name);
            put("Item Brand", brand);
            put("Item Description", description);
            put("Item Price", price);
        }};

        return clothesObject;
    }

}