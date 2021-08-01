package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebUrls = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    ImageView imageView;
    Button bt1,bt2,bt3,bt4;
    int celebChosen;
public class downloadTask extends AsyncTask<String, Void , String>{

    @Override
    protected String doInBackground(String... urls) {
        String result = "" ;
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            while(data != -1){
                char current = (char) data;
                result+= current;
                data = reader.read();
            }return result;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
public class downloadingImages extends AsyncTask<String, Void, Bitmap>{

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url =new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap mybitmap = BitmapFactory.decodeStream(inputStream);
            return mybitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageview);
        bt1 = findViewById(R.id.button4);
        bt2 = findViewById(R.id.button3);
        bt3 = findViewById(R.id.button2);
        bt4 = findViewById(R.id.button);
        downloadTask task = new downloadTask();
        String result = null;
        try {
                result = task.execute("https://www.imdb.com/list/ls005303343/").get();
                String [] spiltResult = result.split("<div class=\"row text-center lister-working hidden\"></div>");
            Pattern p = Pattern.compile("src=\" (.*?) \" width=\"140\">\n");
            Matcher m = p.matcher(spiltResult[0]);
            while(m.find()){
               celebUrls.add(m.group(1));
            }
            p = Pattern.compile("<img alt=\" (.*?) \" width=\"140\"> ");
            m = p.matcher(spiltResult[0]);
            while(m.find()){
                celebNames.add(m.group(1));
            }

        }
         catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void newQuestion() throws ExecutionException, InterruptedException {
        Random random = new Random();
        celebChosen = random.nextInt(celebUrls.size());
        downloadingImages imageTask = new downloadingImages();
        Bitmap celebImage = imageTask.execute(celebUrls.get(celebChosen)).get();
        imageView.setImageBitmap(celebImage);


    }

    public void chosenCeleb(View view) {
    }
}