package ru.kuzstu.android_lections.lection2v2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle fragmentsBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Круглый кнопас в углу
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Я контролирую ситуацию", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Лэйаут самой менюхи
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Вьюшка-контейнер
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Если у активити нет сохранённого состояния то отображаем первый элемент
        if(savedInstanceState == null) {
            displayContent(R.id.nav_lection1);
        }
    }

    @Override
    public void onBackPressed() {
        //Обработка нажатия кнопки "назад" если менюха открыта то закрываем иначе передаём обратоку
        //родительскому классу(сворачиваем приложение)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Меню действий. правая кнопка на тулбаре или кнопка меню в старых андроидах
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Обработка выбранного элемента меню действий
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Вызов окна настроек
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayContent(int id){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment frame = null;
        String tag = String.valueOf(id);
        switch(id){
            case R.id.nav_lection1:
                frame = new Lection1();
                break;
            case R.id.nav_lection2:
                frame = new Lection2();
                break;
            case R.id.nav_lection2a:
                frame = new Lection2PreferenceFragment();
                break;
            case R.id.nav_lection3:
            case R.id.nav_lection4:
            default:
                Toast.makeText(getApplicationContext(),"Не делай так больше",Toast.LENGTH_LONG).show();
                break;
        }

        if(frame != null){
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, frame);
            ft.commit();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Обработка переключения меню навигации
        int id = item.getItemId();

        displayContent(id);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
