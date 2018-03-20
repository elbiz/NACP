package nacp.nacp.nacp.WebService;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ravi on 11/3/16.
 */

public class GetData extends AsyncTask<String, Void, String> {
    //Context context = null;
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
        //String number=params[0];
        // TODO Auto-generated method stub
        //String returnData = null;
        WebService mAuth = new WebService();
        String data1 = mAuth.getData(query, "query");
        /*System.out.println("getdata sout " + data1);*/
        if(data1.equals("Error occured"))
        {
            data1="";
        }
        return data1;
    }

    protected void onPostExecute(String result) {
        //Log.e("REsult at Gedtdata", result);
        Vector<HashMap> p=parseResultXMLData(result);
        progressBar.dismiss();
        objActivityClzIns.onPostExecuteGetData(p, strActivityName);
    }

    public Vector parseResultXMLData(String xml){

        xml=xml.replace("&","&amp;");
        Vector<HashMap> v = new Vector<HashMap>();
        try {
            System.out.println("Parsing the XML ");
            DocumentBuilderFactory xmlfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = xmlfactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            document.getDocumentElement().normalize();
            Element rootElement = document.getDocumentElement();
            System.out.println("Root element :" +rootElement.getNodeName());
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
                            if(node1.getChildNodes().item(0) != null){
                                value = node1.getChildNodes().item(0).getNodeValue();
                            }
                            hm.put(name,value);
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
}