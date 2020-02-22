package io.kangris.ExoPlayer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ogapants.playercontrolview.PlayerControlView;
import com.google.android.exoplayer.*;
import com.google.android.exoplayer.util.*;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.androidmanifest.ActivityElement;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;

@DesignerComponent(category = ComponentCategory.EXTENSION, description = "CustomExoPlayer<br>by kangris", iconName = "images/extension.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "exoplayer.jar, exoplayer.aar, playercontrolview.jar, playercontrolview.aar, exoplayer-core.jar, exoplayer-core.aar, exoplayer-dash.jar, exoplayer-dash, exoplayer-hls.jar, exoplayer-hls, exoplayer-smoothstreaming.jar, exoplayer-smoothstreaming.aar, exoplayer-ui.jar, exoplayer-ui.aar")
@SimpleObject(external = true)
public class CustomExoPlayer extends AndroidNonvisibleComponent implements Component{
    private static final String LOG_TAG = "ControlView";
    private ComponentContainer container;
    private Activity activity;
    private Context context;
    private View view;
    private String URL;
    private PlayerControlView playerControlView;
    private SurfaceView surfaceView;
    private AspectRatioFrameLayout videoFrame;
    private SampleExoPlayer sampleExoPlayer;
    private int timeout;

    public CustomExoPlayer(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        this.context = container.$context();
        this.activity = container.$context();;
    }
    
    @SimpleFunction
    public void InitializePlayer(AndroidViewComponent container){
        AspectRatioFrameLayout videoFrame = (AspectRatioFrameLayout) container.getView();
        //SurfaceView surfaceView = new SurfaceView()
        surfaceView.getHolder().addCallback(this.activity);
        playerControlView = new PlayerControlView(this.activity);
        playerControlView.attach(this.activity);
        sampleExoPlayer = new SampleExoPlayer();
    }
    

    public View getView() {
        //return this.surfaceView;
      return this.view;
    }
    
    @SimpleFunction
    public void PlayerControlHide(){
      playerControlView.hide();
    }
    
    @SimpleFunction
    public void PlayerControlShow(){
      playerControlView.show();
    }
    
    @SimpleFunction
    public boolean isShowingPlayerControl(){
      return playerControlView.isShowing();
    }
    
    @SimpleFunction
    public void Stop(){
      sampleExoPlayer.stop();
    }
    
    @SimpleFunction
    public void Start(String URL){
      sampleExoPlayer.start(this.activity, URL, surfaceView.getHolder().getSurface(), new SampleExoPlayer.VideoSizeChangedListener() {
            public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {
                videoFrame.setAspectRatio(height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
            }
        });
        playerControlView.setPlayer(new PlayerControl(sampleExoPlayer.getExoPlayer()));
    }
    
    @SimpleFunction
    public void show(int timeout) {
        playerControlView.show(timeout);
    }
  
    /*
    public void setEnabled(boolean enabled) {
        playerControlView.setEnabled(enabled);
    }
    */
  
}
