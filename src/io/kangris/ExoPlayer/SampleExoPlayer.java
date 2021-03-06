package io.kangris.ExoPlayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;

import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.mediacodec.*;
import com.google.android.exoplayer2.source.*;
import com.google.android.exoplayer2.source.dash.*;
import com.google.android.exoplayer2.source.hls.*;
import com.google.android.exoplayer2.source.smoothstreaming.*;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.ui.*;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.util.*;
import com.google.android.exoplayer2.video.*;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SampleExoPlayer {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;

    private ExoPlayer exoPlayer;
    private Surface surface;
    //private MediaCodecVideoTrackRenderer videoRenderer;
    private MediaCodecVideoRenderer videoRenderer;

    public SampleExoPlayer() {
    }

    public void start(Context context, String url, Surface surface, final VideoSizeChangedListener videoSizeChangedListener) {
        this.surface = surface;
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DataSource dataSource = new DefaultUriDataSource(context, "userAgent");
        SampleSource sampleSource = new ExtractorSampleSource(
                Uri.parse(url), dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
        videoRenderer = new MediaCodecVideoRenderer(context, sampleSource, MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, new Handler(), videoSizeChangedListener, 50);
        TrackRenderer audioRenderer = new MediaCodecAudioRenderer(sampleSource, MediaCodecSelector.DEFAULT);
        TrackRenderer[] rendererArray = {videoRenderer, audioRenderer};
        exoPlayer = ExoPlayer.Factory.newInstance(rendererArray.length);
        exoPlayer.prepare(rendererArray);
        exoPlayer.sendMessage(videoRenderer, MediaCodecVideoRenderer.MSG_SET_SURFACE, surface);
        exoPlayer.setPlayWhenReady(true);
    }

    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void stop() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
        surface = null;
        videoRenderer = null;
    }

    public void surfaceCreated() {
        if (exoPlayer != null) {
            exoPlayer.sendMessage(videoRenderer, MediaCodecVideoRenderer.MSG_SET_SURFACE, surface);
        }
    }

    public void surfaceDestroyed() {
        if (exoPlayer != null) {
            exoPlayer.blockingSendMessage(videoRenderer, MediaCodecVideoRenderer.MSG_SET_SURFACE, surface);
        }
    }

    public static abstract class VideoSizeChangedListener implements VideoRendererEventListener {

        public abstract void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio);

        @Override
        public void onDroppedFrames(int count, long elapsed) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            onVideoSizeChanged(width, height, pixelWidthHeightRatio);
        }

        @Override
        public void onDrawnToSurface(Surface surface) {

        }

        @Override
        public void onDecoderInitializationError(MediaCodecRenderer.DecoderInitializationException e) {

        }

        @Override
        public void onCryptoError(MediaCodec.CryptoException e) {

        }

        @Override
        public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {

        }
    }
}
