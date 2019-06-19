package com.example.andreea.bookhunt;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.Genre;
import com.example.andreea.bookhunt.models.SpinnerGenre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class GenresGraphActivity extends AppCompatActivity {
    private Spinner spinnerGenre1;
    private Spinner spinnerGenre2;
    private Spinner spinnerGenre3;
    private Spinner spinnerGenre4;
    private Spinner spinnerGenre5;
    private GraphView barChart;
    private List<String> genres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres_graph);

        initView();

        genres = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Genres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        Genre genre = ds.getValue(Genre.class);
                        String currentGenre = genre.getGenre();
                        genres.add(currentGenre);
                    }
                    ArrayAdapter<String> genresAdapter = new ArrayAdapter<String>(GenresGraphActivity.this, android.R.layout.simple_spinner_item, genres);
                    genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    if(genres.size() == 1) {
                        spinnerGenre1.setAdapter(genresAdapter);
                        spinnerGenre2.setVisibility(View.GONE);
                        spinnerGenre3.setVisibility(View.GONE);
                        spinnerGenre4.setVisibility(View.GONE);
                        spinnerGenre5.setVisibility(View.GONE);
                    } else if(genres.size() == 2) {
                        spinnerGenre1.setAdapter(genresAdapter);
                        spinnerGenre2.setAdapter(genresAdapter);
                        spinnerGenre3.setVisibility(View.GONE);
                        spinnerGenre4.setVisibility(View.GONE);
                        spinnerGenre5.setVisibility(View.GONE);
                    } else if(genres.size() == 3) {
                        spinnerGenre1.setAdapter(genresAdapter);
                        spinnerGenre2.setAdapter(genresAdapter);
                        spinnerGenre3.setAdapter(genresAdapter);
                        spinnerGenre4.setVisibility(View.GONE);
                        spinnerGenre5.setVisibility(View.GONE);
                    } else if(genres.size() == 4) {
                        spinnerGenre1.setAdapter(genresAdapter);
                        spinnerGenre2.setAdapter(genresAdapter);
                        spinnerGenre3.setAdapter(genresAdapter);
                        spinnerGenre4.setAdapter(genresAdapter);
                        spinnerGenre5.setVisibility(View.GONE);
                    } else if(genres.size() >= 5) {
                        spinnerGenre1.setAdapter(genresAdapter);
                        spinnerGenre2.setAdapter(genresAdapter);
                        spinnerGenre3.setAdapter(genresAdapter);
                        spinnerGenre4.setAdapter(genresAdapter);
                        spinnerGenre5.setAdapter(genresAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGenreGraph);
        setSupportActionBar(toolbar);
        spinnerGenre1 = (Spinner) findViewById(R.id.spinner1);
        spinnerGenre2 = (Spinner) findViewById(R.id.spinner2);
        spinnerGenre3 = (Spinner) findViewById(R.id.spinner3);
        spinnerGenre4 = (Spinner) findViewById(R.id.spinner4);
        spinnerGenre5 = (Spinner) findViewById(R.id.spinner5);
        barChart = (GraphView) findViewById(R.id.barChart);
    }

    public void btnView(View view) {
        final ArrayList<SpinnerGenre> genres = new ArrayList<>();
        //final String[] genreTitle;
        if(spinnerGenre1.getVisibility() == View.VISIBLE) {
            genres.add(new SpinnerGenre(spinnerGenre1.getSelectedItem().toString(), 0));
        }
        if(spinnerGenre2.getVisibility() == View.VISIBLE) {
            genres.add(new SpinnerGenre(spinnerGenre2.getSelectedItem().toString(), 0));
        }
        if(spinnerGenre3.getVisibility() == View.VISIBLE) {
            genres.add(new SpinnerGenre(spinnerGenre3.getSelectedItem().toString(), 0));
        }
        if(spinnerGenre4.getVisibility() == View.VISIBLE) {
            genres.add(new SpinnerGenre(spinnerGenre4.getSelectedItem().toString(), 0));
        }
        if(spinnerGenre5.getVisibility() == View.VISIBLE) {
            genres.add(new SpinnerGenre(spinnerGenre5.getSelectedItem().toString(), 0));
        }

        FirebaseDatabase.getInstance().getReference("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot user:dataSnapshot.getChildren()) {
                        for(DataSnapshot book:user.getChildren()) {
                            Book currentBook = book.getValue(Book.class);
                            ArrayList<String> currentBookGenres = currentBook.getGenres();
                            if(currentBookGenres != null) {
                                for (int i = 0; i < genres.size(); i++) {
                                    if (currentBookGenres.contains(genres.get(i).getGenre())) {
                                        int number = genres.get(i).getNumber() + 1;
                                        genres.get(i).setNumber(number);
                                    }
                                }
                            }
                        }
                    }
//                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                     BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                    String[] genreTitle = new String[genres.size()];
                     for(int i = 0; i < genres.size(); i++) {
                         genreTitle[i] = genres.get(i).getGenre();
                        series.appendData(new DataPoint(i, genres.get(i).getNumber()), true, genres.size());
                        series.setTitle(genres.get(i).getGenre());
                         series.setDrawValuesOnTop(true);
                     }
                     series.setDrawValuesOnTop(true);
                     barChart.addSeries(series);
                     StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(barChart);
                    staticLabelsFormatter.setHorizontalLabels(genreTitle);
                    barChart.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//                    BarDataSet barDataSet = new BarDataSet(barEntries,"Genres");
//                    BarData barData = new BarData(barDataSet);

//                    barChart.setData(barData);
//                    XAxis xAxis= barChart.getXAxis();
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(genreTitle));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
