package com.technoguff.mymusicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.technoguff.mymusicplayer.adapter.MusicFileAdapter;
import com.technoguff.mymusicplayer.model.MusicFile;
import com.technoguff.mymusicplayer.service.MusicService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    ArrayList<MusicFile> musicFileLists;
    Intent serviceIntent;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        musicFileLists = new ArrayList<MusicFile>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new MusicSearchTask().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



   class MusicSearchTask extends AsyncTask<Void, Void, Void>{
       @Override
       protected Void doInBackground(Void... voids) {
           getMusicFiles();
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);

          MusicFileAdapter adapter =  new MusicFileAdapter(musicFileLists);
           adapter.setMusicSelectedListener(new MusicFileAdapter.OnMusicSelectedListener() {
               @Override
               public void onMusicSelected(int position) {
                   musicService.playMusic(position);
               }
           });
           recyclerView.setAdapter(adapter);
       }
   }



    private void getMusicFiles(){

        ContentResolver musicDataResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicDataResolver.query(uri, null, null, null, null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                MusicFile music = new MusicFile();
                music.setId(id);
                music.setTitle(title);
                music.setArtist(artist);

                musicFileLists.add(music);
            }
            while (cursor.moveToNext());
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        serviceIntent = new Intent(MainActivity.this, MusicService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        //startService(serviceIntent);
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyMusicBinder binder = (MusicService.MyMusicBinder)iBinder;
            musicService = binder.getService();
            musicService.setMusicList(musicFileLists);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

}
