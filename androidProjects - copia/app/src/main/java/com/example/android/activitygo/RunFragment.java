package com.example.android.activitygo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment {

    private Button firstLineButton;
    private TextView tv2;
    private TextView tv2V;
    private TextView data;
    private TextView dataV;
    private TextView distancia;
    private TextView distance;

    private String duracao;
    private String duracaoValue;
    private String dataValue;
    private String dataVal;
    private String distanciaV;
    private String distanciaVal;


    public RunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_run, container, false);

        firstLineButton = (Button) v.findViewById(R.id.plusButtonLine1);
        firstLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*T*/
                tv2 = (TextView) getView().findViewById(R.id.duracaoTextView);
                tv2V = (TextView) getView().findViewById(R.id.FirstValueDuration);
                data = (TextView) getView().findViewById(R.id.dataTextView);
                dataV = (TextView) getView().findViewById(R.id.firstValueData);
                distancia = (TextView) getView().findViewById(R.id.distanciaTextView);
                distance = (TextView) getView().findViewById(R.id.firstValueDistance);

                duracao = tv2.getText().toString();
                duracaoValue = tv2V.getText().toString();
                dataValue = data.getText().toString();
                dataVal = dataV.getText().toString();
                distanciaV = distancia.getText().toString();
                distanciaVal = distance.getText().toString();

                Fragment fr = new RunEstatisticsFragment();
                FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();

                args.putString("CID", duracao);
                args.putString("Duração", duracaoValue);
                args.putString("data", dataValue);
                args.putString("date", dataVal);
                args.putString("distancia", distanciaV);
                args.putString("distance", distanciaVal);

                fr.setArguments(args);
                ft.replace(R.id.fragment_container, fr);
                ft.commit();
            }
        });
        return v;
    }
}