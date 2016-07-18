package co.quchu.quchu.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;

/**
 * Created by no21 on 2016/7/5.
 * email:437943145@qq.com
 * desc :
 */
public class AudioUtils {

    public static void playAudio(Context context, @RawRes int audioId) {
        SoundPool pool;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            pool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build())
                    .build();
        } else {
            pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        }
        pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, .5f, .5f, 0, 0, 1);
            }
        });
        pool.load(context, audioId, 1);
    }
}
