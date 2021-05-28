package com.wika.wikachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.wika.wikachat.R;
import com.wika.wikachat.activities.ProfileCreation2;
import com.wika.wikachat.templates.Profile;
import com.wika.wikachat.utils.Constants;


public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private Context context;
    private String[] countries;
    private Profile profile;

    public CountriesAdapter (Context context, String[] countries, Profile profile) {
        this.context = context;
        this.countries = countries;
        this.profile = profile;
    }

    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.country_row, parent, false);
        return new CountriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, final int position) {
        holder.country.setText(countries[position]);

        // Action that executes when the user selects a country
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileCreation2.class);
                profile.setCountryOfOrigin(countries[position]);
                intent.putExtra(Constants.CURRENT_PROFILE, profile);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return countries.length;
    }

    public class CountriesViewHolder extends RecyclerView.ViewHolder {

        TextView country;
        ConstraintLayout constraintLayout;

        public CountriesViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.lb_language);
            constraintLayout = itemView.findViewById(R.id.clayout_language);
        }

    }

}
