package com.matchadb.generate;

import java.lang.CharSequence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Generates mock data for the Unit tests to use.
 */
public class MatchaDbGenerateData {

    /**
     * Generates the expected table to be created by the MatchaDbTable when 
     * the TestFileClothesWebsiteAPI.json file is given.
     *
     * @return The expected generated table from the TestFileClothesWebsiteAPI.json
     *         file.
     */
    public static HashMap<String, Object> generateClothesWebsiteAPITable() {
        HashMap<String, Object> table = new HashMap<String, Object>();

        // Add in the Shirts
        List<Object> shirts = new ArrayList<Object>();

        HashMap<String, Object> buttonDown = buildClothesObject(
            "Button Down in Black", "ghjkl", "You're new favorite Black Button Down!",
            20.99
        );
        HashMap<String, Object> greenT = buildClothesObject(
            "Nice Green T-Shirt", "ghjkl", "Get ready for Earth Day",
            15.99
        );

        shirts.add(buttonDown);
        shirts.add(greenT);

        HashMap<String, Object> artT = buildClothesObject(
            "T-Shirt with Artwork", "zxcvb", "Show off your favorite painting",
            15.99
        );
        HashMap<String, Object> sleevelessT = buildClothesObject(
            "Sleeveless T", "zxcvb", "Your arms can move so much more!",
            9.95
        );

        shirts.add(artT);
        shirts.add(sleevelessT);

        table.put("Shirts", shirts);


        // Add in the Pants
        List<Object> pants = new ArrayList<Object>();

        HashMap<String, Object> workPants = buildClothesObject(
            "Work Pants", "ghjkl", "Going to a meeting today?",
            51.76
        );
        HashMap<String, Object> coolJeans = buildClothesObject(
            "Cool Jeans", "ghjkl", "It's almost time for Summer!",
            29.99
        );

        pants.add(workPants);
        pants.add(coolJeans);

        HashMap<String, Object> tanPants = buildClothesObject(
            "Tan Slim Pants", "zxcvb", "For that look",
            29.99
        );
        HashMap<String, Object> moreWorkPants = buildClothesObject(
            "More Work Pants", "zxcvb", "Another meeting?",
            59.99
        );

        pants.add(tanPants);
        pants.add(moreWorkPants);

        table.put("Pants", pants);


        // Add in the Shoes
        List<Object> shoes = new ArrayList<Object>();

        HashMap<String, Object> dressShoes = buildClothesObject(
            "Dress Shoes", "ghjkl", "To go with those work pants",
            89.79
        );
        HashMap<String, Object> highTops = buildClothesObject(
            "High Tops", "ghjkl", "Going back to high school?",
            39.99
        );

        shoes.add(dressShoes);
        shoes.add(highTops);

        HashMap<String, Object> skateShoes = buildClothesObject(
            "Skate Shoes", "zxcvb", "Going back to high school?",
            39.99
        );
        HashMap<String, Object> lightUp = buildClothesObject(
            "Light Up Shoes", "zxcvb", "Show them off when you walk!",
            1000.99
        );
        
        shoes.add(skateShoes);
        shoes.add(lightUp);

        table.put("Shoes", shoes);


        // Add in the Hats
        List<Object> hats = new ArrayList<Object>();
        
        HashMap<String, Object> topHat = buildClothesObject(
            "Top Hat", "ghjkl", "Fancy right?",
            9.99
        );
        HashMap<String, Object> baseballHat = buildClothesObject(
            "Baseball Hat", "ghjkl", "Show off your favorite team!",
            18.99
        );

        hats.add(topHat);
        hats.add(baseballHat);

        HashMap<String, Object> dadHat = buildClothesObject(
            "Dad Hat", "zxcvb", "Really just a Baseball Hat",
            19.87
        );
        HashMap<String, Object> beanie = buildClothesObject(
            "Beanie", "zxcvb", "So warm...",
            19.01
        );

        hats.add(dadHat);
        hats.add(beanie);

        table.put("Hats", hats);

        return table; 
    }

    /**
     * A generator method that returns a clothes item to insert.
     *
     * @return a clothes items to insert in the form of an object for insert to the table.
     */
    public static Object newClothesItemToInsert() {
        HashMap<String, Object> newClothingItem = new HashMap<String, Object>() {{
            put("Item Name", "Trendy Hat");
            put("Item Brand", "qwertu");
            put("Item Description", "A hat with a feather for a feather.");
            put("Item Price", 9000000.95);
        }};

        return newClothingItem;
    }

    /**
     * A generator to make an object for a MatchaPostQuery.
     *
     *
     * @return an item to insert in the form of a 2D String array to insert via MatchaPostQuery.
     */
    public static String[][] generateObjectForMatchaPostQuery() {
        return new String[][] {{
            "Item Name=Moon Earring", "Item Brand=Ceetz",
            "Item Description=You are gonna like the way your ears look", "Item Price=9000000000.1"
        }};
    }

    
    /**
     * A generation method for two items, which will be inserted into the Hats table.
     *
     * @return two clothings items to be inserted, as described above.
     */
    public static Object generateTwoClothingsItemsToInsert() {
        return new ArrayList<Object>() {{
            add(new HashMap<String, Object>() {{
                put("Item Name", "Bear Hat");
                put("Item Brand", "Howdy There");
                put("Item Description", "Have you seen this hat?");
                put("Item Price", 12.45);
            }});
            add(new HashMap<String, Object>() {{
                put("Item Name", "Elephant Hat");
                put("Item Brand", "Howdy There");
                put("Item Description", "Lets go to the beach");
                put("Item Price", 12.45);
            }});
        }};
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
    public static HashMap<String, Object> buildClothesObject(String name, String brand,
        String description, double price) {
        HashMap<String, Object> clothesObject = new HashMap<String, Object>() {{
            put("Item Name", name);
            put("Item Brand", brand);
            put("Item Description", description);
            put("Item Price", price);
        }};

        return clothesObject;
    }

    /**
     * This method allows other test methods to gather the exact items that would be 
     * returned provided the query presented to the MatchaDBTable.
     *
     * @param nameContainsChar A char that should exist in every Item Name of every
     *                         item returned from the query. (Case Sensitive)
     * @param brandContainsChar A char that should exist in every Brand Name of every
     *                         item returned from the query. (Case Sensitive)
     * @param descriptionContainsChar A char that should exist in every Description Name of 
     *                                every item returned from the query. (Case Sensitive)
     * @param priceGreaterThanOrEqualTo A double that represents the price each item returned
     *                                  should be greater than or equal to.
     * @param priceLessThan A double that represents the price each item returned
     *                      should be less than.
     * @param subtableKey The subtable key that the item should belong to.
     *
     * @return An exact replica of what Clothes Website items would be returned 
     *         given the parameters provided by the user.
     */
    public static List<HashMap<String,Object>> getClothesWebsiteItemsViaQueryParams(
        CharSequence nameContainsChar, CharSequence brandContainsChar, 
        CharSequence descriptionContainsChar, Double priceGreaterThanOrEqualTo, 
        Double priceLessThan, String subtableKey) {
        
        List<HashMap<String, Object>> quaryParamViableClothesWebsiteItems = new ArrayList<>();
        HashMap<String, Object> currentTableItemsWithTableHeads = generateClothesWebsiteAPITable();

        for (Object tableKey : ((HashMap)currentTableItemsWithTableHeads).keySet().toArray()) {
            String tableKeyAsString = (String) tableKey;
            if (tableKeyAsString.equals(subtableKey) || subtableKey == null) {
                Object subTable = ((HashMap)currentTableItemsWithTableHeads).get(tableKeyAsString);
                for (Object item : (List) subTable) {
                    HashMap<String, Object> itemAsMap = ((HashMap) item);
                    // check name contains
                    if (nameContainsChar != null 
                        && !((String) itemAsMap.get("Item Name")).contains(nameContainsChar)) {
                        continue;
                    }
                    // check brand contains
                    if (brandContainsChar != null 
                        && !((String) itemAsMap.get("Item Brand")).contains(brandContainsChar)) {
                        continue;
                    }
                    // check description contains
                    if (descriptionContainsChar != null 
                        && !((String) itemAsMap.get("Item Description"))
                                                .contains(descriptionContainsChar)) {
                        continue;
                    }
                    // check price greater than or equal to
                    if (priceGreaterThanOrEqualTo != null 
                        && !(((Double) itemAsMap.get("Item Price"))
                                         >= priceGreaterThanOrEqualTo)) {
                        continue;
                    }
                    // check price less than
                    if (priceLessThan != null 
                        && !(((Double) itemAsMap.get("Item Price")) < priceLessThan)) {
                        continue;
                    }
                    // If we met all query parameters, add the item
                    quaryParamViableClothesWebsiteItems.add(itemAsMap);
                }
            }
        }
        
        return quaryParamViableClothesWebsiteItems;
    }
}