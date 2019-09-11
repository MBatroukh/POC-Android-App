package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private DrawerLayout drawer;

    Button play, pause, stop;
    MediaPlayer player;
    int audioFile;
    int pausePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        Spinner spinner = (Spinner) findViewById(R.id.file_selector);
        spinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.audio_file_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        play = (Button) findViewById(R.id.play_nyancat_button);
        pause = (Button) findViewById(R.id.pause_nyancat_button);
        stop = (Button) findViewById(R.id.stop_nyancat_button);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MessageFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_message);
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        TextView overwriteMe = (TextView) findViewById(R.id.overwrite);
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Resources res = getResources();
        int soundId = res.getIdentifier(parent.getItemAtPosition(pos).toString().toLowerCase(), "raw", getPackageName());

        audioFile = soundId;
        overwriteMe.setText(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play_nyancat_button:
                if(player == null){
                    player = MediaPlayer.create(getApplicationContext(), audioFile);
                    player.start();
                } else if(!player.isPlaying()){
                    player.seekTo(pausePosition);
                    player.start();
                }
                break;
            case R.id.pause_nyancat_button:
                if(player != null){
                    player.pause();
                    pausePosition = player.getCurrentPosition();
                }
                break;
            case R.id.stop_nyancat_button:
                if(player != null){
                    player.stop();
                    player.release();
                    player = null;
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String gitHubRepoUrl = "https://github.com/MBatroukh";

        String donateUrl = "https://www.paypal.com/ca/home";

        Intent i = new Intent(Intent.ACTION_VIEW);

        switch(menuItem.getItemId()){
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_github:
                i.setData(Uri.parse(gitHubRepoUrl));
                startActivity(i);
                break;
            case R.id.nav_donate:
                i.setData(Uri.parse(donateUrl));
                startActivity(i);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        // Intent takes two arguments, first is context, second is where to deliver the intent
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        // startActivity() method starts an instance

        //* if no content fire alert modal */
        if(message.isEmpty()){
            openDialog();
        } else {
            startActivity(intent);
        }
    }
}