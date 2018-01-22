package com.example.fernando.controlekm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Flavia on 13/01/2018.
 */

public class Utilitario extends AppCompatActivity {
    TextView textInfo;
    TextView textMoreInfo;
    ProgressBar progressBar;
    Button buttonStart;
    Button buttonCancel;

    LongTask longTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilitario);


        try {
            getSupportActionBar().hide();
        } catch (NullPointerException E) {
            E.printStackTrace();
        }

        textInfo = (TextView) findViewById(R.id.textInfo);
        textMoreInfo = (TextView) findViewById(R.id.textMoreInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        progressBar.setScaleY(3f);
    }

    public void buttonStartCallback(View v) {
        longTask = new LongTask();
        longTask.setProgressBar(progressBar);
        longTask.setTextInfo(textInfo);
        longTask.setTextMoreInfo(textMoreInfo);
        longTask.execute();
    }

    public void buttonCancelCallback(View v) {
        if (longTask != null) longTask.cancel(true);
    }

    public class LongTask extends AsyncTask<Void, Integer, Void> {

        private TextView textInfo;
        private TextView textMoreInfo;
        private ProgressBar progressBar;

        public void setTextInfo(TextView textInfo) {
            this.textInfo = textInfo;
        }

        public void setTextMoreInfo(TextView textMoreInfo) {
            this.textMoreInfo = textMoreInfo;
        }

        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            if (textInfo != null)
                textInfo.setText("Realizando tarefa demorada...");
            if (textMoreInfo != null)
                textMoreInfo.setText("Clique em Cancelar para terminar a execução");
            if (this.progressBar != null) this.progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < 1000000; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.publishProgress(i / 10);
                if (this.isCancelled()) break;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (this.progressBar != null) this.progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onCancelled() {
            if (textInfo != null)
                textInfo.setText("Tarefa cancelada :(");
            if (textMoreInfo != null)
                textMoreInfo.setText("Clique em iniciar para tentar novamente");
        }

        @Override
        protected void onPostExecute(Void result) {
            if (textInfo != null)
                textInfo.setText("Tarefa realizada com sucesso :)");
            if (textMoreInfo != null)
                textMoreInfo.setText("Clique em iniciar para fazer novamente");
        }

    }
}
