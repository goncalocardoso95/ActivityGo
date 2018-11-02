package com.example.android.activitygo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunMenuInicial extends Fragment {

    public RunMenuInicial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_run_menu_inicial, container, false);

        ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("Activity GO:");

        final Button historial = (Button) v.findViewById(R.id.buttonHistorial);
        Button irCorrida = (Button) v.findViewById(R.id.buttonIrCorrida);
        Button meusGrupos = (Button) v.findViewById(R.id.buttonMeusGrupos);

        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment SelectedFragment = new RunFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,  SelectedFragment,"RunMenuInicial");
                ft.addToBackStack("RunFragment");
                ft.commit();
            }
        });

        irCorrida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment SelectedFragment = new IrCorridaFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,  SelectedFragment,"RunMenuInicial");
                ft.addToBackStack("RunFragment");
                ft.commit();

            }
        });

        meusGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment SelectedFragmentMeusGrupos = new MyGroupsFragment();
                FragmentManager fmMeusGrupos = getFragmentManager();
                FragmentTransaction ftMeusGrupos = fmMeusGrupos.beginTransaction();
                ftMeusGrupos.replace(R.id.fragment_container,  SelectedFragmentMeusGrupos,"RunMenuInicial");
                ftMeusGrupos.addToBackStack("RunFragment");
                ftMeusGrupos.commit();

            }
        });

        return v;
    }




}