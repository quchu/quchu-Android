package co.quchu.quchu.utils;

import android.content.Context;
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

        SoundPool pool = new SoundPool(10, AudioManager.STREAM_RING, 5);
        pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1, 1, 0, 0, 1);
            }
        });
        pool.load(context, audioId, 1);
    }
}
