package com.example.android.activitygo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.activitygo.model.Challenge;
import com.example.android.activitygo.model.Corrida;
import com.example.android.activitygo.model.Grupo;
import com.example.android.activitygo.model.Ranking;
import com.example.android.activitygo.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RankingsFragment extends Fragment {

    private ArrayList<String> listaUsers = new ArrayList<>();

    private Button classificacaoCorridaGeral;
    private Button classificacaoCiclismoGeral;
    private Button classificacaoCorridaGrupo;
    private Button classificacaoCiclismoGrupo;

    private DatabaseReference databaseRankings;
    private DatabaseReference databaseCorridas;
    private DatabaseReference databaseUsers;
    private Corrida c;
    private User u;
    private Ranking r;

    private String username;
    private static final String TAG = "RankingsFragment";

    public RankingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rankings, container, false);

        ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("Rankings:");
        username = getArguments().getString("USERNAME");

        databaseRankings = FirebaseDatabase.getInstance().getReference("rankings");
        databaseCorridas = FirebaseDatabase.getInstance().getReference("corrida");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        classificacaoCorridaGeral = (Button) v.findViewById(R.id.buttonCorridaGeral);

        databaseUsers.orderByChild("pontos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    u = child.getValue(User.class);
                    if (u.getPontos() >= 0) {
                        // lista inversa com os users para obter os pontos de cada
                        listaUsers.add(u.getUsername());
                        Collections.reverse(listaUsers);
                    }
                }

                // OBTER RANKING GERAL CORRIDA
                databaseRankings.orderByChild("desporto").equalTo("corrida").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                r = child.getValue(Ranking.class);

                                // lista dos rankings de corrida
                                final ArrayList<String> lista = r.getRankings();
                                int lugar = lista.indexOf(username) + 1;
                                int total = lista.size();
                                classificacaoCorridaGeral.setText("Classificacao: " + lugar + "/" + total);
                            }
                        } else {
                            Ranking r = new Ranking("corrida", listaUsers);
                            String id = databaseRankings.push().getKey();
                            databaseRankings.child(id).setValue(r);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*
                Collections.reverse(listaUsers);

                //Ranking r = new Ranking("Classificacao Geral Corrida", "corrida", listaUsers);
                Ranking r = new Ranking("corrida", listaUsers);
                String id = databaseRankings.push().getKey();
                databaseRankings.child(id).setValue(r);*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        classificacaoCorridaGeral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment SelectedFragment = new TableRankingsFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, SelectedFragment, "RankingsFragment");
                ft.addToBackStack("RankingsFragment");
                ft.commit();
            }
        });
        return v;
    }
}