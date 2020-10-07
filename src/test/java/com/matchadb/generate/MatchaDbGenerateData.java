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
     *
     * @return The expected generated table from the TestFileClothesWebsiteAPI.json
     *         file.
     */
    public static HashMap<String, Object> generateClothesWebsiteAPITable() {
        HashMap<String, Object> table = new HashMap<String, Object>();

        // Add in the Shirts
        HashMap<String, Object> shirtsIndex = new HashMap<String, Object>();
        
        HashMap<String, Object> ghjklShirtIndex = new HashMap<String, Object>();
        HashMap<String, Object> zxcvbShirtIndex = new HashMap<String, Object>();

        HashMap<String, Object> buttonDown = buildClothesObject(
            "Button Down in Black", "ghjkl", "You're new favorite Black Button Down!",
            20.99
        );
        HashMap<String, Object> greenT = buildClothesObject(
            "Nice Green T-Shirt", "ghjkl", "Get ready for Earth Day",
            15.99
        );
        // ghjklShirtIndex.put("Button Down in Black", buttonDown);
        // ghjklShirtIndex.put("Nice Green T-Shirt", greenT);
        // shirtsIndex.put("ghjkl", ghjklShirtIndex);
        shirtsIndex.put("0", buttonDown);
        shirtsIndex.put("1", greenT);

        HashMap<String, Object> artT = buildClothesObject(
            "T-Shirt w/Artwork", "zxcvb", "Show off your favorite painting",
            15.99
        );
        HashMap<String, Object> sleevelessT = buildClothesObject(
            "Sleeveless T", "zxcvb", "Your arms can move so much more!",
            9.95
        );
        // zxcvbShirtIndex.put("T-Shirt w/Artwork", artT);
        // zxcvbShirtIndex.put("Sleeveless T", sleevelessT);
        // shirtsIndex.put("zxcvb", zxcvbShirtIndex);
        shirtsIndex.put("2", artT);
        shirtsIndex.put("3", sleevelessT);

        // Add in the Pants
        HashMap<String, Object> pantsIndex = new HashMap<String, Object>();
        
        HashMap<String, Object> ghjklPantsIndex = new HashMap<String, Object>();
        HashMap<String, Object> zxcvbPantsIndex = new HashMap<String, Object>();

        HashMap<String, Object> workPants = buildClothesObject(
            "Work Pants", "ghjkl", "Going to a meeting today?",
            51.76
        );
        HashMap<String, Object> coolJeans = buildClothesObject(
            "Cool Jeans", "ghjkl", "It's almost time for Summer!",
            29.99
        );
        // ghjklPantsIndex.put("Work Pants", workPants);
        // ghjklPantsIndex.put("Cool Jeans", coolJeans);
        // pantsIndex.put("ghjkl", ghjklPantsIndex);
        pantsIndex.put("0", workPants);
        pantsIndex.put("1", coolJeans);

        HashMap<String, Object> tanPants = buildClothesObject(
            "Tan Slim Pants", "zxcvb", "For that look",
            29.99
        );
        HashMap<String, Object> moreWorkPants = buildClothesObject(
            "More Work Pants", "zxcvb", "Another meeting?",
            59.99
        );
        // zxcvbShirtIndex.put("Tan Slim Pants", tanPants);
        // zxcvbShirtIndex.put("More Work Pants", moreWorkPants);
        // shirtsIndex.put("zxcvb", zxcvbShirtIndex);
        pantsIndex.put("2", tanPants);
        pantsIndex.put("3", moreWorkPants);

        // Add in the Shoes
        HashMap<String, Object> shoesIndex = new HashMap<String, Object>();
        
        HashMap<String, Object> ghjklShoesIndex = new HashMap<String, Object>();
        HashMap<String, Object> zxcvbShoesIndex = new HashMap<String, Object>();

        HashMap<String, Object> dressShoes = buildClothesObject(
            "Dress Shoes", "ghjkl", "To go with those work pants",
            89.70
        );
        HashMap<String, Object> highTops = buildClothesObject(
            "High Tops", "ghjkl", "Going back to high school?",
            15.99
        );
        // ghjklShoesIndex.put("Dress Shoes", dressShoes);
        // ghjklShoesIndex.put("High Tops", highTops);
        // shoesIndex.put("ghjkl", ghjklShoesIndex);
        shoesIndex.put("0", dressShoes);
        shoesIndex.put("1", highTops);

        HashMap<String, Object> skateShoes = buildClothesObject(
            "Skate Shoes", "zxcvb", "Going back to high school?",
            39.99
        );
        HashMap<String, Object> lightUp = buildClothesObject(
            "Light Up Shoes", "zxcvb", "Show them off when you walk!",
            1000.99
        );
        // zxcvbShoesIndex.put("Skate Shoes", skateShoes);
        // zxcvbShoesIndex.put("Light Up Shoes", lightUp);
        // shoesIndex.put("zxcvb", zxcvbShoesIndex);
        shoesIndex.put("2", skateShoes);
        shoesIndex.put("3", lightUp);

        // Add in the Hats
        HashMap<String, Object> hatsIndex = new HashMap<String, Object>();
        
        HashMap<String, Object> ghjklHatsIndex = new HashMap<String, Object>();
        HashMap<String, Object> zxcvbHatsIndex = new HashMap<String, Object>();

        HashMap<String, Object> topHat = buildClothesObject(
            "Top Hat", "ghjkl", "Fancy right?",
            9.99
        );
        HashMap<String, Object> baseballHat = buildClothesObject(
            "Baseball Hat", "ghjkl", "Show off your favorite team!",
            18.99
        );
        // ghjklHatsIndex.put("Top Hat", topHat);
        // ghjklHatsIndex.put("Baseball Hat", baseballHat);
        // hatsIndex.put("ghjkl", ghjklHatsIndex);
        hatsIndex.put("0", topHat);
        hatsIndex.put("1", baseballHat);

        HashMap<String, Object> dadHat = buildClothesObject(
            "Dad Hat", "zxcvb", "Really just a Baseball Hat",
            19.87
        );
        HashMap<String, Object> beanie = buildClothesObject(
            "Beanie", "zxcvb", "So warm...",
            19.00
        );
        // zxcvbHatsIndex.put("Dad Hat", artT);
        // zxcvbHatsIndex.put("Beanie", sleevelessT);
        // hatsIndex.put("zxcvb", zxcvbHatsIndex);
        hatsIndex.put("2", dadHat);
        hatsIndex.put("3", beanie);

        return table; 
    }

    /**
     * A helper method to build a clothes object.
     *
     * @param name The item name
     * @param brand The item brand
     * @param description A description of the item
     * @param price The price of the item
     *
     * @return A clothes object
     */
    private static HashMap<String, Object> buildClothesObject(String name, String brand,
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