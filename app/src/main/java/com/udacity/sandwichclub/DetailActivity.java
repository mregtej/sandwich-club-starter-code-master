package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    /** Log TAG - Class name*/
    private static final String TAG = DetailActivity.class.getName();

    /** String-label to save the sandwich's image-URL on savedInstanceState Bundle */
    private static final String SANDWICH_IMAGE_LABEL = "image";
    /** String-label to save the sandwich's alias on savedInstanceState Bundle */
    private static final String ALSO_KNOWN_AS_TEXT_LABEL = "alsoKnownAs";
    /** String-label to save the sandwich's origin on savedInstanceState Bundle */
    private static final String ORIGIN_TEXT_LABEL = "origin";
    /** String-label to save the sandwich's description on savedInstanceState Bundle */
    private static final String DESCRIPTION_TEXT_LABEL = "description";
    /** String-label to save the sandwich's ingredients on savedInstanceState Bundle */
    private static final String INGREDIENTS_TEXT_LABEL = "ingredients";
    /** String-label to save the sandwich's name on savedInstanceState Bundle */
    private static final String TITLE_TEXT_LABEL = "title";

    /** Sandwich's Alias TextView (UI) */
    @BindView(R.id.also_known_tv)
    TextView mAlsoKnownAsTextView;
    /** Sandwich's Origin TextView (UI) */
    @BindView(R.id.origin_tv)
    TextView mOriginTextView;
    /** Sandwich's Description TextView (UI) */
    @BindView(R.id.description_tv)
    TextView mDescriptionTextView;
    /** Sandwich's Ingredients TextView (UI) */
    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTextView;
    /** Sandwich's ImageView (UI) */
    @BindView(R.id.image_iv)
    ImageView mSandwichImageView;
    /** Sandwich's Image-URL (savedInstanceState Bundle)*/
    private String sSandwichImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // ViewBinding with ButterKnife
        ButterKnife.bind(this);
        
        if (savedInstanceState != null) {

            String message;
            message = savedInstanceState.getString(ALSO_KNOWN_AS_TEXT_LABEL);
            if(message != null && !message.equals("")) {
                mAlsoKnownAsTextView.setText(message);
            }
            message = savedInstanceState.getString(ORIGIN_TEXT_LABEL);
            if(message != null && !message.equals("")) {
                mOriginTextView.setText(message);
            }
            message = savedInstanceState.getString(DESCRIPTION_TEXT_LABEL);
            if(message != null && !message.equals("")) {
                mDescriptionTextView.setText(message);
            }
            message = savedInstanceState.getString(SANDWICH_IMAGE_LABEL);
            if(message != null && !message.equals("")) {
                // Set sandwich image
                Picasso.with(this)
                        .load(message)
                        .into(mSandwichImageView);
                sSandwichImageView = message;
            }
            message = savedInstanceState.getString(INGREDIENTS_TEXT_LABEL);
            if(message != null && !message.equals("")) {
                mIngredientsTextView.setText(message);
            }
            message = savedInstanceState.getString(TITLE_TEXT_LABEL);
            if(message != null && !message.equals("")) {
                setTitle(message);
            }

            Log.d(TAG, "onSavedInstance loaded");

        } else {

            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }

            int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
            if (position == DEFAULT_POSITION) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }

            String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
            String json = sandwiches[position];
            Sandwich sandwich = JsonUtils.parseSandwichJson(json, this);
            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            // Set detailed sandwich-info
            populateUI(sandwich);
            sSandwichImageView = sandwich.getImage();
            // Set sandwich image
            Picasso.with(this)
                    .load(sSandwichImageView)
                    .into(mSandwichImageView);
            // Set sandwich mainName as Activity title
            setTitle(sandwich.getMainName());
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Fills in all TextView fields of DetailActivity (Sandwich additional info)
     *
     * @param sandwich  Sandwich object for populating the UI
     */
    private void populateUI(Sandwich sandwich) {

        int size = 0;

        // Set Sandwich AlsoKnownAs
        size = sandwich.getAlsoKnownAs().size();
        for(int i = 0; i < size; i++) {
            mAlsoKnownAsTextView.append("* " + sandwich.getAlsoKnownAs().get(i));
            // Avoid additional "\n" after last element
            if(i != size - 1) {
                mAlsoKnownAsTextView.append("\n");
            }
        }

        // Set Sandwich Origin
        mOriginTextView.setText(sandwich.getPlaceOfOrigin());

        // Set Sandwich Description
        mDescriptionTextView.setText(sandwich.getDescription());

        // Set Sandwich Ingredients
        size = sandwich.getIngredients().size();
        for(int i = 0; i < size; i++) {
            mIngredientsTextView.append("* " + sandwich.getIngredients().get(i));
            // Avoid additional "\n" after last element
            if(i != size - 1) {
                mIngredientsTextView.append("\n");
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TITLE_TEXT_LABEL, getTitle().toString());
        outState.putString(ALSO_KNOWN_AS_TEXT_LABEL, mAlsoKnownAsTextView.getText().toString());
        outState.putString(ORIGIN_TEXT_LABEL, mOriginTextView.getText().toString());
        outState.putString(DESCRIPTION_TEXT_LABEL, mDescriptionTextView.getText().toString());
        outState.putString(SANDWICH_IMAGE_LABEL, sSandwichImageView);
        outState.putString(INGREDIENTS_TEXT_LABEL, mIngredientsTextView.getText().toString());
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }
}
