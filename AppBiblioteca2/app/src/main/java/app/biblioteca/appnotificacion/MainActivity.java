package app.biblioteca.appnotificacion;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private List<Libro> libros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        libros = new ArrayList<>();

        new FetchBooksTask().execute("http://192.168.126.133/xampp/AppBiblio/leer.php");
    }
    private class FetchBooksTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {
                    Log.e("Error", "Esta vacio.");
                    return;
                }

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int isbn = jsonObject.getInt("isbn");
                    String titulo = jsonObject.getString("titulo");
                    String autor = jsonObject.getString("autor");
                    String idioma = jsonObject.getString("idioma");
                    int ejemplares = jsonObject.getInt("ejemplares");

                    Libro libro = new Libro(isbn, titulo, autor, idioma, ejemplares);
                    libros.add(libro);
                }
                addRowsToTable(libros);

            } catch (JSONException e) {
                Log.e("Error", e.toString());
            }
        }
    }

    private void addRowsToTable(List<Libro> libros) {
        for (Libro libro : libros) {
            TableRow row = new TableRow(this);
            row.setPadding(8, 8, 8, 8);

            TextView libroInfo = new TextView(this);
            libroInfo.setText(
                    "ISBN: " + libro.getIsbn() + "\n" +
                            "Título: " + libro.getTitulo() + "\n" +
                            "Autor: " + libro.getAutor() + "\n" +
                            "Idioma: " + libro.getIdioma()
            );
            libroInfo.setPadding(8, 8, 8, 8);

            TextView ejemplaresInfo = new TextView(this);
            ejemplaresInfo.setText(String.valueOf(libro.getEjemplares()));
            ejemplaresInfo.setPadding(8, 8, 8, 8);


            if (libro.getEjemplares() <= 0) {
                row.setBackgroundColor(getResources().getColor(R.color.rojo_claro));
            }


            row.addView(libroInfo);
            row.addView(ejemplaresInfo);

            tableLayout.addView(row);
        }
    }
}
