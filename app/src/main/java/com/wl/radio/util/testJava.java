package com.wl.radio.util;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.SystemClock;

import com.wl.radio.receiver.CustomMediaButtonReceiver;
import com.ximalaya.ting.android.opensdk.player.receive.WireControlReceiver;

public class testJava {

    public static void testReceiver(final Context mContext) {
        final CustomMediaButtonReceiver wireControlReceiver = new CustomMediaButtonReceiver();

        if (android.os.Build.VERSION.SDK_INT >= 21) {

            MediaSession mSession = new MediaSession(mContext, "MusicService");

            mSession.setCallback(new MediaSession.Callback() {

                @Override

                public boolean onMediaButtonEvent(Intent mediaButtonIntent) {

                    wireControlReceiver.onReceive(mContext, mediaButtonIntent);

                    return super.onMediaButtonEvent(mediaButtonIntent);

                }

            });

            mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS

                    | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

            ComponentName mediaButtonReceiverComponent = new ComponentName(mContext, WireControlReceiver.class);

            mediaButtonIntent.setComponent(mediaButtonReceiverComponent);

            PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), 0,

                    mediaButtonIntent, 0);

            mSession.setMediaButtonReceiver(mediaPendingIntent);

            AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();

            audioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);

            mSession.setPlaybackToLocal(audioAttributesBuilder.build());

            mSession.setActive(true);

            PlaybackState state = new PlaybackState.Builder().setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PLAY_FROM_MEDIA_ID | PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS).setState(PlaybackState.STATE_PLAYING, 0, 1, SystemClock.elapsedRealtime()).build();
            mSession.setPlaybackState(state);


        }

//获得AudioManager对象

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        ComponentName mbCN = new ComponentName(mContext.getPackageName(), CustomMediaButtonReceiver.class.getName());

        audioManager.registerMediaButtonEventReceiver(mbCN);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

        mediaButtonIntent.setComponent(mbCN);

        PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(mContext, 0, mediaButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

// create and register the remote control client

        RemoteControlClient mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);

        audioManager.registerRemoteControlClient(mRemoteControlClient);

        int flags = RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS | RemoteControlClient.FLAG_KEY_MEDIA_NEXT | RemoteControlClient.FLAG_KEY_MEDIA_PLAY | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_RATING;

        mRemoteControlClient.setTransportControlFlags(flags);

        IntentFilter inFilter = new IntentFilter("android.intent.action.MEDIA_BUTTON");
        inFilter.setPriority(10000);
        mContext.registerReceiver(wireControlReceiver, inFilter);
    }
}
