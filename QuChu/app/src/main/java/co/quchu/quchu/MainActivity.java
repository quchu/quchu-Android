package co.quchu.quchu;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

import co.quchu.quchu.base.BaseActivity;

public class MainActivity extends BaseActivity {
    TextView txt;
    TextView text_test;
    private TextToSpeech tts;
    Speaker speaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_test= (TextView) findViewById(R.id.text_test);

        speaker = new Speaker(this);
        ((Button) findViewById(R.id.speak)).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        speaker.allow(true);
        speaker.speak("你好");
    }
});
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        txt = (TextView) findViewById(R.id.mian_txt);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speaker.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    class Speaker implements TextToSpeech.OnInitListener {

        private TextToSpeech tts;

        private boolean ready = false;

        private boolean allowed = false;

        public Speaker(Context context){
            tts = new TextToSpeech(context, this);
        }

        public boolean isAllowed(){
            return allowed;
        }

        public void allow(boolean allowed){
            this.allowed = allowed;
        }

        @Override
        public void onInit(int status) {
            if(status == TextToSpeech.SUCCESS){
                // Change this to match your
                // locale
                tts.setLanguage(Locale.CHINA);
                ready = true;
            }else{
                ready = false;
            }
        }

        public void speak(String text){

            // Speak only if the TTS is ready
            // and the user has allowed speech

            if(ready && allowed) {
                HashMap<String, String> hash = new HashMap<String,String>();
                hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                        String.valueOf(AudioManager.STREAM_NOTIFICATION));
                tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
            }
        }

        public void pause(int duration){
            tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
        }

        // Free up resources
        public void destroy(){
            tts.shutdown();
        }

    }
}
