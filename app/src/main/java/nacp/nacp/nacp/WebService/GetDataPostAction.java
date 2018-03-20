package nacp.nacp.nacp.WebService;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by ravi on 11/3/16.
 */

public class GetDataPostAction extends AppCompatActivity {

    private String lov_Query;
    HashMap<String,ArrayList> lvReturnHM;
    public void onPostExecuteGetData(Vector<HashMap> result, String activity) {

    }

    public void onPostExecuteSaveData(String result, String activity) {

    }
    public void getCachedDataSet(HashMap<String, ArrayList> lvHasmap,String lvname) {


    }

    public void executeGetdataService(GetDataPostAction objActivityClzIns, String query, String activity) {
        GetData obj = new GetData();
        obj.objActivityClzIns = objActivityClzIns;
        obj.query = query;
        obj.strActivityName = activity;
        obj.execute();
    }

    public void executeSavedataService(GetDataPostAction objActivityClzIns, String query, String activity) {
        SaveData obj = new SaveData();
        obj.objActivityClzIns = objActivityClzIns;
        obj.query = query;
        obj.strActivityName = activity;
        obj.execute();
    }

    public void setCachedDataSet(final GetDataPostAction objActivityClzIns, final String listOfValueName) {

        /*GetListOfValue getListOfValue=new GetListOfValue();
        getListOfValue.objActivityClzIns=objActivityClzIns;
        getListOfValue.listofvaluename=listOfValueName;
        getListOfValue.execute();*/

        new AsyncTask<String, Void, String>() {

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
            protected String doInBackground(String... voids) {
                WebService mAuth = new WebService();
                String data1 = mAuth.getData("SELECT fquery from tlistofvalues where fname ='" + listOfValueName + "'", "query");
                if(data1.equals("Error occured"))
                {
                    data1="";
                }
                return data1;
            }

            protected void onPostExecute(String result) {
                Vector<HashMap> p = parseResultXMLData(result);
                HashMap hashMap=p.get(0);
                lov_Query=hashMap.get("fquery").toString();
                MyTask myTask= new MyTask();
                myTask.objActivityClzIns=objActivityClzIns;
                myTask.lov_query=lov_Query;
                myTask.lvname=listOfValueName;
                myTask.execute();
                progressBar.dismiss();
            }


        }.execute();
    }

    public class GetListOfValue extends AsyncTask<String, Void, String> {
        GetDataPostAction objActivityClzIns = null;
        String listofvaluename="";
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
            String data1 = mAuth.saveData("SELECT fquery from tlistofvalues where fname ='"+listofvaluename+"'", "query");
            return data1;
        }

        protected void onPostExecute(String result) {

            progressBar.dismiss();
            //objActivityClzIns.onPostExecuteSaveData(result);
        }

    }

    public Vector parseResultXMLData(String xml) {

        xml = xml.replace("&", "&amp;");
        Vector<HashMap> v = new Vector<HashMap>();
        try {
            System.out.println("Parsing the XML ");
            DocumentBuilderFactory xmlfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = xmlfactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            document.getDocumentElement().normalize();
            Element rootElement = document.getDocumentElement();
            System.out.println("Root element :" + rootElement.getNodeName());
            NodeList nodeList = rootElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList nodeList1 = node.getChildNodes();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    HashMap<String, String> hm = new HashMap<String, String>();

                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        Node node1 = nodeList1.item(j);
                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            String name = node1.getNodeName();
                            String value = "";
                            if (node1.getChildNodes().item(0) != null) {
                                value = node1.getChildNodes().item(0).getNodeValue();
                            }
                            hm.put(name, value);
                        }
                    }
                    v.addElement(hm);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    class MyTask extends AsyncTask<String,Void,String>{
        ProgressDialog progressBar;
        GetDataPostAction objActivityClzIns;
        String lov_query,lvname;
        HashMap<String,ArrayList> lvHasmap;
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
        protected String doInBackground(String... strings) {
            WebService mAuth = new WebService();
            String data1 = mAuth.getData(lov_query, "query");
            if(data1.equals("Error occured"))
            {
                data1="";
            }
            return data1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Vector<HashMap> pi = parseResultXMLData(s);
            HashMap hashMap;
            ArrayList<String> fnameList= new ArrayList();
            ArrayList<String> fidList= new ArrayList();
            fnameList.add("");
            fidList.add("");
            for(int j=0;j<pi.size();j++)
            {
                hashMap=pi.get(j);
                fnameList.add(hashMap.get("fname").toString());
                fidList.add(hashMap.get("fid").toString());
            }
            lvHasmap=new HashMap<>();
            lvHasmap.put("fname",fnameList);
            lvHasmap.put("fid",fidList);

            getCachedDataSet(lvHasmap,lvname);
            progressBar.dismiss();
        }
    }
}