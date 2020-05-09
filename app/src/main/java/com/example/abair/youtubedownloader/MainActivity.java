package com.example.abair.youtubedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    Button downloadbtn;
    String url = "http://m.youtube.com/";
    String url_new = "https://m.youtube.com/watch?v=V_eZAoeS_-0";
    String newLink;
    String webUrl ;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111");
        //MobileAds.initialize(this,"ca-app-pub-9574408052302165~1361920346");



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }


        webView = findViewById(R.id.wb1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


    }

    public void btndownloadm(View view) {
        webUrl = webView.getUrl();
        downloadvideo(view);
    }


    public void downloadvideo(View v){
        YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(MainActivity.this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if(ytFiles!=null){
                    int tag =22;
                    newLink=ytFiles.get(tag).getUrl();
                    String Title = "Video";
                    DownloadManager.Request request= new DownloadManager.Request(Uri.parse(newLink));
                    request.setTitle(Title);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,Title+".mp4");
                    DownloadManager downloadManager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    request.allowScanningByMediaScanner();
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
                    downloadManager.enqueue(request);
                }
            }
        };

        youTubeUriExtractor.execute(webUrl);
        Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show();

    }


}
