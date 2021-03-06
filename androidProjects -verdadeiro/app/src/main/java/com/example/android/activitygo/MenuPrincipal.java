package com.example.android.activitygo;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private Fragment SelectedFragment;
    private Toolbar myToolbar;
    private Toolbar toolbarCima;
    private String selectedSport;
    private Dialog myDialog;
    private Dialog dialogTerminarSessao;
    private CheckBox caminhadaCheckBox;
    private CheckBox corridaCheckBox;
    private int corridaChecked = 0;
    private int caminhadaChecked = 0;
    private int futebolChecked = 0;
    private int ciclismoChecked = 0;
    private MenuItem mi;
    private ArrayList<CheckBox> checkboxesPopup = new ArrayList<CheckBox>();
    private String username;
    private String firstName = "";
    private String lastName = "";
    private String image_path = "";
    private String pontos = "";

    private static final String TAG = "MenuPrincipal";

    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            username = getIntent().getStringExtra("USERNAME");
            image_path = getIntent().getStringExtra("URI");
            if (!TextUtils.isEmpty(image_path)) {
                Uri fileUri = Uri.parse(image_path);
            }
        } else {
            username = "";
            image_path = "";
        }

        myDialog = new Dialog(this);
        dialogTerminarSessao = new Dialog(this);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        databaseUsers.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    firstName = String.valueOf(child.child("firstName").getValue());
                    lastName = String.valueOf(child.child("lastName").getValue());
                    pontos = String.valueOf(child.child("pontos").getValue());
                    toolbarCima = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbarCima);
                    getSupportActionBar().setTitle("ActivityGO");
                    //toolbarCima.setNavigationIcon(R.drawable.backbutton);


                    getSupportActionBar().setSubtitle("" + firstName.charAt(0) + lastName.charAt(0) + ":" + " " + pontos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Bundle toRunMenuInicial = new Bundle();
        toRunMenuInicial.putString("USERNAME", username);
        toRunMenuInicial.putString("URI", image_path);
        SelectedFragment = new RunMenuInicial();
        SelectedFragment.setArguments(toRunMenuInicial);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, SelectedFragment, "RunMenuInicialFragment");
        ft.commit();

        BottomNavigationView mMainNav = findViewById(R.id.NavBar);
        mMainNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment SelectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.run: //Fragmento Corrida

                            SelectedFragment = new RunMenuInicial();
                            Bundle toRunMenuInicial = new Bundle();
                            toRunMenuInicial.putString("USERNAME", username);
                            toRunMenuInicial.putString("URI", image_path);
                            SelectedFragment.setArguments(toRunMenuInicial);
                            FragmentManager fm = getFragmentManager();
                            /*while (fm.getBackStackEntryCount() > 0){
                                fm.popBackStackImmediate();
                            }*/
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fragment_container, SelectedFragment, "RunMenuInicialFragment");
                            ft.addToBackStack("RunMenuInicialFragment");
                            ft.commit();
                            break;

                        case R.id.chalengeItem: //Fragmento Desafios

                            menuItem.setIcon(R.drawable.bicepe_icon23);
                            SelectedFragment = new ChalengeFragment();
                            Bundle toChallengeFragment = new Bundle();
                            toChallengeFragment.putString("USERNAME", username);
                            SelectedFragment.setArguments(toChallengeFragment);
                            FragmentManager fman = getFragmentManager();
                          /*  while (fman.getBackStackEntryCount() > 0){
                                fman.popBackStackImmediate();
                            }*/
                            FragmentTransaction ftra = fman.beginTransaction();
                            ftra.replace(R.id.fragment_container, SelectedFragment, "ChalengeFragment");
                            ftra.addToBackStack("ChalengeFragment");
                            ftra.commit();
                            break;

                        case R.id.achievementItem: // Fragmento Achievements

                            Bundle bundle = new Bundle();
                            bundle.putString("USERNAME", username);
                            SelectedFragment = new AchievementsFragment();
                            SelectedFragment.setArguments(bundle);
                            FragmentManager f = getFragmentManager();
                            /*while (f.getBackStackEntryCount() > 0){
                                f.popBackStackImmediate();
                            }*/

                            FragmentTransaction fte = f.beginTransaction();
                            fte.replace(R.id.fragment_container, SelectedFragment, "AchievementFragment");
                            fte.addToBackStack("AchievementFragment");
                            fte.commit();
                            break;

                        case R.id.RankingItem: //Fragmento Ranking

                            Bundle b1 = new Bundle();
                            b1.putString("USERNAME", username);
                            SelectedFragment = new RankingsFragment();
                            SelectedFragment.setArguments(b1);
                            FragmentManager fmanag = getFragmentManager();
                           /* while (fmanag.getBackStackEntryCount() > 0){
                                fmanag.popBackStackImmediate();
                            }*/
                            menuItem.setIcon(R.drawable.trophy_icon333);
                            FragmentTransaction ftransactio = fmanag.beginTransaction();
                            ftransactio.replace(R.id.fragment_container, SelectedFragment, "RankingsFragment");
                            ftransactio.addToBackStack("RankingsFragment");
                            ftransactio.commit();
                            break;

                        case R.id.GroupsItem: // Fragmento Groups

                            Bundle args = new Bundle();
                            args.putString("USERNAME", username);
                            SelectedFragment = new GroupFragment();
                            FragmentManager fmana = getFragmentManager();
                            /*while (fmana.getBackStackEntryCount() > 0){
                                fmana.popBackStackImmediate();
                            }*/
                            SelectedFragment.setArguments(args);
                            FragmentTransaction ftransacti = fmana.beginTransaction();
                            ftransacti.replace(R.id.fragment_container, SelectedFragment, "GroupFragment");
                            ftransacti.addToBackStack("GroupFragment");
                            ftransacti.commit();
                            /*
                            Bundle args = new Bundle();
                            args.putString("USERNAME", username);
                            SelectedFragment = new MergeGroupFragment();
                            FragmentManager fmana = getFragmentManager();
                            SelectedFragment.setArguments(args);
                            FragmentTransaction ftransacti = fmana.beginTransaction();
                            ftransacti.replace(R.id.fragment_container, SelectedFragment, "MergeGroupFragment");
                            ftransacti.addToBackStack("MergeGroupFragment");
                            ftransacti.commit();
                            break;*/
                    }

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, SelectedFragment);
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        // Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + ViewPager.getCurrentItem());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View viewRun = findViewById(R.id.run);
                final View viewChallenge = findViewById(R.id.chalengeItem);
                final View viewAchievement = findViewById(R.id.achievementItem);
                final View viewRanking = findViewById(R.id.RankingItem);
                final View viewGroups = findViewById(R.id.GroupsItem);

                if (viewRun != null) {
                    viewRun.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Corridas:", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextColor(getResources().getColor(R.color.BlueSeparator));
                            toast.show();
                            return true;
                        }
                    });
                }
                if (viewChallenge != null) {
                    viewChallenge.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Desafios:", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextColor(getResources().getColor(R.color.BlueSeparator));
                            toast.show();
                            return true;
                        }
                    });
                }
                if (viewAchievement != null) {
                    viewAchievement.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Conquistas:", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextColor(getResources().getColor(R.color.BlueSeparator));
                            toast.show();
                            return true;
                        }
                    });
                }
                if (viewRanking != null) {
                    viewRanking.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Rankings:", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextColor(getResources().getColor(R.color.BlueSeparator));
                            toast.show();
                            return true;
                        }
                    });
                }
                if (viewGroups != null) {
                    viewGroups.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Grupos:", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextColor(getResources().getColor(R.color.BlueSeparator));
                            toast.show();
                            return true;
                        }
                    });
                }
            }
        });

        if (currentFragment.isVisible() && (currentFragment instanceof RunMenuInicial)) {
            //MenuItem item = menu.findItem(R.id.BackButton);
            //item.setVisible(false);
        }


        // Fragment whichFragment=getVisibleFragment();
        /*
        if(whichFragment.getTag().toString().equals("RunFragment")){
            MenuItem item = menu.findItem(R.id.BackButton);
            item.setVisible(false);
        }else{
            MenuItem item = menu.findItem(R.id.BackButton);
            item.setVisible(true);
        }*/

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Fragment getVisibleFragment() {
        FragmentManager fmana = getFragmentManager();
        //FragmentManager fragmentManager = MenuPrincipal.this.getSupportFragmentManager();
        List<Fragment> fragments = fmana.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent i = new Intent(this, SettingsActivity.class);
                i.putExtra("USERNAME", username);
                i.putExtra("URI", image_path);
                startActivity(i);
                break;

            case R.id.BackButton:
                RunMenuInicial runMenuInicialFragment = (RunMenuInicial) getFragmentManager().findFragmentByTag("RunMenuInicialFragment");
                RankingsFragment myFragment = (RankingsFragment) getFragmentManager().findFragmentByTag("RankingsFragment");
                TableRankingsFragment myFragment2 = (TableRankingsFragment) getFragmentManager().findFragmentByTag("TableRankingsFragment");
                MergeGroupFragment mergeGroupFragment = (MergeGroupFragment) getFragmentManager().findFragmentByTag("MergeGroupFragment");
                RunFragment myRunFragment = (RunFragment) getFragmentManager().findFragmentByTag("historialCorrida");
                HistoricoCorridas historicoFragment = (HistoricoCorridas) getFragmentManager().findFragmentByTag("historicoCorridas");
                IrCorridaFragment irCorridaFragment = (IrCorridaFragment) getFragmentManager().findFragmentByTag("IrCorridaFragment");
                MyGroupsFragment myGroupsFragment = (MyGroupsFragment) getFragmentManager().findFragmentByTag("MyGroupsFragment");
                GroupFragment myGroupFragment = (GroupFragment) getFragmentManager().findFragmentByTag("GroupFragment");
                ProcuraGrupos myProcuraGrupos = (ProcuraGrupos) getFragmentManager().findFragmentByTag("ProcuraGrupos");
                CreateGroup myCreateGroup = (CreateGroup) getFragmentManager().findFragmentByTag("CreateGroup");
                JoinGroup myJoinGroup = (JoinGroup) getFragmentManager().findFragmentByTag("JoinGroup");
                DisplayRankingGroups myDisplayRankingGroupsFragment = (DisplayRankingGroups) getFragmentManager().findFragmentByTag("DisplayRankingGroupsFragment");
                ListaDeElementosJuntarGrupo myListaDeElementosJuntarGrupo = (ListaDeElementosJuntarGrupo) getFragmentManager().findFragmentByTag("ListaDeElementosJuntarGrupo");
                MyGroupFragmentElements myGroupFragmentElements = (MyGroupFragmentElements) getFragmentManager().findFragmentByTag("MyGroupFragmentElements");
                HistoricoCorridas myHistorico = (HistoricoCorridas) getFragmentManager().findFragmentByTag("HistoricoCorridas");
                RunHistoricStatus myRunHistoricStatus = (RunHistoricStatus) getFragmentManager().findFragmentByTag("RunHistoricStatus");

                if (myFragment != null && myFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myFragment2 != null && myFragment2.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (mergeGroupFragment != null && mergeGroupFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myRunFragment != null && myRunFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (historicoFragment != null && historicoFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (irCorridaFragment != null && irCorridaFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (runMenuInicialFragment != null && runMenuInicialFragment.isVisible()) {
                    showTerminarSessaoPopup();
                } else if (myGroupsFragment != null && myGroupsFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myProcuraGrupos != null && myProcuraGrupos.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myListaDeElementosJuntarGrupo != null && myListaDeElementosJuntarGrupo.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myGroupFragment != null && myGroupFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myCreateGroup != null && myCreateGroup.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myJoinGroup != null && myJoinGroup.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myDisplayRankingGroupsFragment != null && myDisplayRankingGroupsFragment.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myGroupFragmentElements != null && myGroupFragmentElements.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myHistorico != null && myHistorico.isVisible()) {
                    getFragmentManager().popBackStack();
                } else if (myRunHistoricStatus != null && myRunHistoricStatus.isVisible()) {
                    getFragmentManager().popBackStack();
                }
        }
        return true;
    }

    public void showTerminarSessaoPopup() {
        Button yesButton;
        Button noButton;
        TextView close;
        TextView popupId;
        dialogTerminarSessao.setContentView(R.layout.popup_terminar_sessao);
        yesButton = (Button) dialogTerminarSessao.findViewById(R.id.yesButton);
        noButton = (Button) dialogTerminarSessao.findViewById(R.id.noButton);
        close = (TextView) dialogTerminarSessao.findViewById(R.id.txtClose);
        popupId = (TextView) dialogTerminarSessao.findViewById(R.id.popUpId);
        popupId.setText("Vai terminar a sessão. Tem a certeza?");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTerminarSessao.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTerminarSessao.dismiss();
            }
        });
        dialogTerminarSessao.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTerminarSessao.show();
    }

    public void getCorridaItemPopup(View v) {
        corridaChecked = 1;
        checkboxesPopup.add(corridaCheckBox);

        //desporto que a pessoa irá praticar
        selectedSport = corridaCheckBox.getText().toString();
    }

    public void getCaminhadaItemPopup(View v) {
        caminhadaChecked = 1;
        checkboxesPopup.add(caminhadaCheckBox);

        //desporto que a pessoa irá praticar
        selectedSport = caminhadaCheckBox.getText().toString();
    }

    public String getSelectedSport() {
        return this.selectedSport;
    }

    @Override
    public void onBackPressed() {

        //YA, MONTES DE CODIGO REPETIDO, SIGA PARA BINGO, SE FUNCIONA, NAO MUDA
        RunMenuInicial runMenuInicialFragment = (RunMenuInicial) getFragmentManager().findFragmentByTag("RunMenuInicialFragment");
        RankingsFragment myFragment = (RankingsFragment) getFragmentManager().findFragmentByTag("RankingsFragment");
        TableRankingsFragment myFragment2 = (TableRankingsFragment) getFragmentManager().findFragmentByTag("TableRankingsFragment");
        MergeGroupFragment mergeGroupFragment = (MergeGroupFragment) getFragmentManager().findFragmentByTag("MergeGroupFragment");
        RunFragment myRunFragment = (RunFragment) getFragmentManager().findFragmentByTag("historialCorrida");
        HistoricoCorridas historicoFragment = (HistoricoCorridas) getFragmentManager().findFragmentByTag("historicoCorridas");
        IrCorridaFragment irCorridaFragment = (IrCorridaFragment) getFragmentManager().findFragmentByTag("IrCorridaFragment");
        MyGroupsFragment myGroupsFragment = (MyGroupsFragment) getFragmentManager().findFragmentByTag("MyGroupsFragment");
        ProcuraGrupos myProcuraGrupos = (ProcuraGrupos) getFragmentManager().findFragmentByTag("ProcuraGrupos");
        GroupFragment myGroupFragment = (GroupFragment) getFragmentManager().findFragmentByTag("GroupFragment");
        CreateGroup myCreateGroup = (CreateGroup) getFragmentManager().findFragmentByTag("CreateGroup");
        JoinGroup myJoinGroup = (JoinGroup) getFragmentManager().findFragmentByTag("JoinGroup");
        HistoricoCorridas myHistorico = (HistoricoCorridas) getFragmentManager().findFragmentByTag("HistoricoCorridas");
        RunHistoricStatus myRunHistoricStatus = (RunHistoricStatus) getFragmentManager().findFragmentByTag("RunHistoricStatus");
        DisplayRankingGroups myDisplayRankingGroupsFragment = (DisplayRankingGroups) getFragmentManager().findFragmentByTag("DisplayRankingGroupsFragment");
        ListaDeElementosJuntarGrupo myListaDeElementosJuntarGrupo = (ListaDeElementosJuntarGrupo) getFragmentManager().findFragmentByTag("ListaDeElementosJuntarGrupo");
        MyGroupFragmentElements myGroupFragmentElements = (MyGroupFragmentElements) getFragmentManager().findFragmentByTag("MyGroupFragmentElements");


        if (myFragment != null && myFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myFragment2 != null && myFragment2.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (mergeGroupFragment != null && mergeGroupFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myRunFragment != null && myRunFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (historicoFragment != null && historicoFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (irCorridaFragment != null && irCorridaFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (runMenuInicialFragment != null && runMenuInicialFragment.isVisible()) {
            showTerminarSessaoPopup();
        } else if (myGroupsFragment != null && myGroupsFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myProcuraGrupos != null && myProcuraGrupos.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myListaDeElementosJuntarGrupo != null && myListaDeElementosJuntarGrupo.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myGroupFragment != null && myGroupFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myCreateGroup != null && myCreateGroup.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myJoinGroup != null && myJoinGroup.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myDisplayRankingGroupsFragment != null && myDisplayRankingGroupsFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myGroupFragmentElements != null && myGroupFragmentElements.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myHistorico != null && myHistorico.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (myRunHistoricStatus != null && myRunHistoricStatus.isVisible()) {
            getFragmentManager().popBackStack();
        }
    }
}