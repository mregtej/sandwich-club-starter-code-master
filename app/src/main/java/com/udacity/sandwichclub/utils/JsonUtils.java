package com.udacity.sandwichclub.utils;

import android.content.Context;
import android.util.Log;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    /* Log TAG - Class name */
    private static final String TAG = JsonUtils.class.getName();

    /* JSON Labels */
    private static final String NAME_JSON_LABEL = "name";
    private static final String MAINNAME_JSON_LABEL = "mainName";
    private static final String ALSOKNOWNAS_JSON_LABEL = "alsoKnownAs";
    private static final String PLACEOFORIGIN_JSON_LABEL = "placeOfOrigin";
    private static final String DESCRIPTION_JSON_LABEL = "description";
    private static final String IMAGE_JSON_LABEL = "image";
    private static final String INGREDIENTS_JSON_LABEL = "ingredients";

    /**
     * Parses a Sandwich JSON response and retrieves a well-format
     * Sandwich objetc to populate the UI
     *
     * JSON Format:
     * {
     *   name: {
     *      mainName : String,
     *      alsoKnownAs : String[]
     *   },
     *
     *   placeOfOrigin : String,
     *   description : String,
     *   image : String(URL),
     *   ingredients : String[]
     * }
     *
     * @param   json        Sandwich JSON response
     * @return  Sandwich    Object-container to populate the UI
     */
    public static Sandwich parseSandwichJson(String json, Context context) {

        try {

            // Initialize local variables
            String sMainName = new String("");
            ArrayList<String>sAlsoKnownAs = new ArrayList<String>();
            String sOrigin = new String("");
            String sDescription = new String("");
            String sImage = new String("");
            ArrayList<String>sIngredients = new ArrayList<String>();

            // Get Initial JSONObject: sandwich
            JSONObject sandwich = new JSONObject(json);

            if (sandwich != null) {

                // Parse name JSONObject. Sub-elements:
                // * mainName : String
                // * alsoKnownAs : String[]
                JSONObject name = sandwich.getJSONObject(NAME_JSON_LABEL);
                if (name != null) {
                    sMainName = name.optString(MAINNAME_JSON_LABEL, context.getString(R.string.no_main_name_json_value));
                    JSONArray alsoKnownAs = name.getJSONArray(ALSOKNOWNAS_JSON_LABEL);
                    if (alsoKnownAs != null && alsoKnownAs.length() > 0) {
                        for (int i = 0; i < alsoKnownAs.length(); i++) {
                            sAlsoKnownAs.add(alsoKnownAs.getString(i));
                        }
                    } else {
                        sAlsoKnownAs.add(context.getString(R.string.no_also_known_json_value));
                    }
                }

                // Parse placeOfOrigin : String
                sOrigin = sandwich.optString(PLACEOFORIGIN_JSON_LABEL, context.getString(R.string.no_origin_json_value));

                // Parse description : String
                sDescription = sandwich.optString(DESCRIPTION_JSON_LABEL, context.getString(R.string.no_description_json_value));

                // Parse image : String
                sImage = sandwich.optString(IMAGE_JSON_LABEL, context.getString(R.string.no_image_json_value));

                // Parse ingredients : String[]
                JSONArray ingredients = sandwich.getJSONArray(INGREDIENTS_JSON_LABEL);
                if (ingredients != null && ingredients.length() > 0) {
                    for (int i = 0; i < ingredients.length(); i++) {
                        sIngredients.add(ingredients.getString(i));
                    }
                } else {
                    sIngredients.add(context.getString(R.string.no_ingredients_json_value));
                }

            }

            // Create and return Sandwich
            return new Sandwich(
                    sMainName,
                    sAlsoKnownAs,
                    sOrigin,
                    sDescription,
                    sImage,
                    sIngredients
            );

        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return null;
        }

    }
}
