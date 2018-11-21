package ru.mgvk.megaplayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private int               currentPosition = 0;
    private View              prevView;
    private TextView          pathTextView;
    private String            dirPath;
    private MediaPlayer       mediaPlayer;
    private ListView          listView;
    private ArrayList<String> files           = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dirPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                  + File.separator + "megaPlayerMusic" + File.separator;
        pathTextView = findViewById(R.id.text);
        pathTextView.setText(dirPath);
        listView = findViewById(R.id.listview);
        mediaPlayer = new MediaPlayer();

        initFilesList();
        initListAdapter();
        if (files.size() > 0) {
            initPlayer(files.get(0));
        }
    }

    private void initListAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, files);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    private void initFilesList() {
        File file = new File(dirPath);
        File[] fs = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.getName().endsWith("mp3"));
            }
        });
        for (File f : fs) {
            files.add(f.getName());
        }
    }

    private void initPlayer(String filename) {

        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(dirPath + filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.next) {
            if (currentPosition < files.size() - 1) {
                View newView = listView.getChildAt(++currentPosition);
                selectFile(currentPosition, newView);
            }
        }
        if (view.getId() == R.id.prev) {
            if (currentPosition > 1) {
                View newView = listView.getChildAt(--currentPosition);
                selectFile(currentPosition, newView);
            }
        }
        if (view.getId() == R.id.play && mediaPlayer != null) {


            view.setBackgroundResource(mediaPlayer.isPlaying() ?
                    android.R.drawable.ic_media_play
                    : android.R.drawable.ic_media_pause);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }

        }
    }

    void selectFile(int position, View view) {
        initPlayer(files.get(currentPosition = position));
        Toast.makeText(this, "Выбран файл " + files.get(position), Toast.LENGTH_SHORT).show();
        if (prevView != null) prevView.setBackgroundColor(Color.TRANSPARENT);
        view.setBackgroundResource(R.color.selectedFile);
        prevView = view;
        performOnPlayClick();
    }

    void performOnPlayClick() {
        onClick(findViewById(R.id.play));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectFile(position, view);
    }


}
