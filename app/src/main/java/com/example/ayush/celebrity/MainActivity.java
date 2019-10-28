package com.example.ayush.celebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class MainActivity extends AppCompatActivity {

    String img;
    ImageView imageView;


    public class Download extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... urls)
        {
            String result="";
            URL url;
            HttpURLConnection urlConnection= null;
            try
            {
                url=new URL(urls[0]);
                urlConnection =(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }
        }
    }
    ArrayList<String> imgUrl = new ArrayList<>();
    ArrayList<String> imgName = new ArrayList<>();
    int quesno = 0;

    public class DownloadImage extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls)
        {
            try
            {
                URL url=new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    Bitmap myImage;
    Button button1, button2, button3, button4;
    int a;

    public void updateQuestion()
    {
        DownloadImage newImage=new DownloadImage();
        try
        {
            myImage=newImage.execute(imgUrl.get(quesno)).get();
            imageView.setImageBitmap(myImage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Random rand=new Random();
        a = rand.nextInt(4);
        int b;
        for(int i=0;i<4;i++)
        {
            if(i==0)
            {
                if(a==i)
                    button1.setText(imgName.get(quesno));
                else
                {
                    b = rand.nextInt(50);
                    while (b == quesno) {
                        b = rand.nextInt(50);
                    }
                    button1.setText(imgName.get(b));
                }
            }
            if(i==1)
            {
                if(a==i)
                    button2.setText(imgName.get(quesno));
                else
                {
                    b = rand.nextInt(50);
                    while (b == quesno) {
                        b = rand.nextInt(50);
                    }
                    button2.setText(imgName.get(b));
                }
            }
            if(i==2)
            {
                if(a==i)
                    button3.setText(imgName.get(quesno));
                else
                {
                    b = rand.nextInt(50);
                    while (b == quesno) {
                        b = rand.nextInt(88);
                    }
                    button3.setText(imgName.get(b));
                }
            }
            if(i==3)
            {
                if(a==i)
                    button4.setText(imgName.get(quesno));
                else
                {
                    b = rand.nextInt(50);
                    while (b == quesno) {
                        b = rand.nextInt(88);
                    }
                    button4.setText(imgName.get(b));
                }
            }


        }
    }

    public void onClick(View view)
    {
        if (Integer.parseInt(view.getTag().toString()) == (a + 1)) {
            if (!MainActivity.this.isFinishing())
                Toast.makeText(this, "Correct Answer  :)", Toast.LENGTH_SHORT).show();
        } else if (!MainActivity.this.isFinishing())
            Toast.makeText(this, "Wrong Answer  :(", Toast.LENGTH_SHORT).show();

        try
        {
            quesno++;
            if (quesno < 49)
                updateQuestion();
            else
                Toast.makeText(this, "No more questions are left", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img="";
        imageView=findViewById(R.id.imageView);
        Download task = new Download();
        img=null;
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);

        try
        {
            img=task.execute("http://www.posh24.se/kandisar").get();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Pattern imgPattern= Pattern.compile("<img src=\"(.*?)\"");
        Matcher imgMatcher=imgPattern.matcher(img);
        while(imgMatcher.find())
        {
            imgUrl.add(imgMatcher.group(1));
        }
        Pattern namePattern=Pattern.compile("alt=\"(.*?)\"/>");
        Matcher nameMatcher=namePattern.matcher(img);
        while(nameMatcher.find())
        {
            imgName.add(nameMatcher.group(1));
        }
        updateQuestion();
    }
}
