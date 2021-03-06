package com.example.android.activitygo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriarGrupoFragment extends android.support.v4.app.Fragment {



    private static final String TAG = "CriarGrupo";


    private Button criarGrupo;
    private String txtPesquisa;
    private Bundle bundle;
    private String descricaoTxt;
    private String desportoTxt;

    public CriarGrupoFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_criar_grupo, container, false);

        criarGrupo = (Button) v.findViewById(R.id.buttonCriar);
        final EditText nomeGrupo = (EditText) v.findViewById(R.id.NomeCriarGrupo);
        final EditText descricao = (EditText) v.findViewById(R.id.Descricao);
        final EditText desporto = (EditText) v.findViewById(R.id.DesportoEscolhido);

        criarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtPesquisa = nomeGrupo.getText().toString();
                descricaoTxt = descricao.getText().toString();
                desportoTxt = desporto.getText().toString();

                nomeGrupo.getText().clear();
                descricao.getText().clear();
                desporto.getText().clear();

                Toast toast = Toast.makeText(getActivity(), "O seu grupo " +txtPesquisa+" foi criado!",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 800);
                toast.show();
            }
        });
        return v;
    }
}