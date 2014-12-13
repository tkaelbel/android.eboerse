package com.com.android.eboerse.webrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.com.android.eboerse.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.SymbolsGoodToKnow;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by tok on 11.12.2014.
 */
public class DetailNewsActivity extends Activity{

    private TextView txtHeader;
    private ImageView imageView;
    private TextView txtBody;
    private TextView txtUnderHeader;

    private ProgressDialog pd;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        setContentView(R.layout.detail_news);

        txtHeader = (TextView) findViewById(R.id.txt_header);
        imageView = (ImageView) findViewById(R.id.imageView);
        txtBody = (TextView) findViewById(R.id.txt_body);
        txtUnderHeader = (TextView) findViewById(R.id.txt_underHeader);

        txtBody.setMovementMethod(new ScrollingMovementMethod());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Nachrichten");


        handleIntent(getIntent());
    }

    private void initLayoutSec() {
        LinearLayout layoutFirst = (LinearLayout) findViewById(R.id.layoutWithPicture);
        layoutFirst.setVisibility(View.GONE);

        txtHeader = (TextView) findViewById(R.id.txt_header_sec);
        txtUnderHeader = (TextView) findViewById(R.id.txt_underHeader_sec);
        txtBody = (TextView) findViewById(R.id.txt_body_sec);

        LinearLayout layoutSec = (LinearLayout) findViewById(R.id.layoutWithOutPicture);
        layoutSec.setVisibility(View.VISIBLE);

        txtBody.setMovementMethod(new ScrollingMovementMethod());
    }

    private void handleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();

        String header = bundle.getString("header");
        String img = bundle.getString("img");
        String body = bundle.getString("body");

        if(header != null){
            body = header + " "+ body;
        }

        String headerFull = bundle.getString("fullheader");
        String time = bundle.getString("time");

        if(img != null && !img.contains("background")){
            LoadImage image = new LoadImage();
            image.execute(img);
        }else{
            initLayoutSec();
        }

        body = checkBodyForSigns(body);

        txtHeader.setText(headerFull);
        txtUnderHeader.setText(time);
        txtBody.setText(body);
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DetailNewsActivity.this);
            pd.setMessage("Loading Image...");
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(strings[0]).getContent());
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
                pd.dismiss();
            }else{
                pd.dismiss();
                MyErrorToast.doToast(DetailNewsActivity.this, "Image does not Exist!", 2);
            }
        }
    }

    private String checkBodyForSigns(String input){
        if(input.contains(SymbolsGoodToKnow.HTML_AN)){
            input = input.replace(SymbolsGoodToKnow.HTML_AN, "\"");
        }
        return input;
    }
}
