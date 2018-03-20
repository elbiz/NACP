package nacp.nacp.nacp.WebService;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by ravi on 11/3/16.
 */

public class SaveData extends AsyncTask<String, Void, String> {
    GetDataPostAction objActivityClzIns = null;
    String query="";
    String strActivityName;
    ProgressDialog progressBar;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressDialog(objActivityClzIns);
        progressBar.setTitle("Progress....");
        progressBar.setMessage("Please Wait...");
        progressBar.setProgressStyle(objActivityClzIns.TRIM_MEMORY_RUNNING_LOW);
        progressBar.show();
    }
    @Override
    protected String doInBackground(String... params) {
        WebService mAuth = new WebService();
        String data1 = mAuth.saveData(query, "query");
        return data1;
    }

    protected void onPostExecute(String result) {
        progressBar.dismiss();
        objActivityClzIns.onPostExecuteSaveData(result, strActivityName);
    }



}