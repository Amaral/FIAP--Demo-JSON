package br.com.fiap.mob.moedas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;


public class MainActivity extends ActionBarActivity {

    private final String URL_SERVICO = "http://developers.agenciaideias.com.br/cotacoes/json";

    private ProgressDialog dialog;

    private TextView tvBovespa, tvDolar, tvEuro, tvAtualizacao, tvBovespaVariacao;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBovespa = (TextView) findViewById(R.id.tvBovespa);
        tvBovespaVariacao = (TextView) findViewById(R.id.tvBovespaVariacao);
        tvDolar = (TextView) findViewById(R.id.tvDolar);
        tvEuro = (TextView) findViewById(R.id.tvEuro);
        tvAtualizacao = (TextView) findViewById(R.id.tvAtualizacao);
    }


    public void atualizarCotacoes(View v){
        dialog = ProgressDialog.show(MainActivity.this,"Atualizando","Atualizando itens");
        DownloadAssync download = new DownloadAssync();
        download.execute(URL_SERVICO);
    }


private class DownloadAssync extends AsyncTask<String,Void, String>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

      try {
          HttpClient httpclient = new DefaultHttpClient();
          HttpGet get = new HttpGet(params[0]);
          HttpResponse response = httpclient.execute(get);
          InputStream content = response.getEntity().getContent();
          StringBuilder sb = new StringBuilder();
          BufferedReader reader = new BufferedReader(new InputStreamReader(content));
          String linha = null;

          while ((linha = reader.readLine()) != null){
              sb.append(linha+"\n");
          }

          return sb.toString();
      }

      catch (Exception e){
          e.printStackTrace();
      }


        return null;
    }

    @Override
    protected void onPostExecute(String json) {

        dialog.dismiss();


        if(json!=null){

            try {
                JSONObject resultado = new JSONObject(json);
                tvDolar.setText(resultado.getJSONObject("dolar").getString("cotacao"));
                tvEuro.setText(resultado.getJSONObject("euro").getString("cotacao"));
                tvBovespa.setText(resultado.getJSONObject("bovespa").getString("cotacao"));
                tvBovespaVariacao.setText("Variação - " + resultado.getJSONObject("bovespa").getString("variacao"));
                tvAtualizacao.setText(resultado.getString("atualizacao"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



        super.onPostExecute(json);
    }
}



}


