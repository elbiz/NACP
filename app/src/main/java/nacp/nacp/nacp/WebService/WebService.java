package nacp.nacp.nacp.WebService;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
    private static String NAMESPACE;
    private static String SOAP_ACTION;
    private static String URL;
   
    static {
        NAMESPACE = "http://webservice.ejbsession.indama.elbiz.com/";

        /*Live */
        URL="http://indama.in:8008/indama-session/SCommonWebService/SCommonWebServiceEJB?wsdl";
        /*Locacl*/
        //URL="http://192.168.1.189:8088/indama-session/SCommonWebService/SCommonWebServiceEJB?wsdl";


        SOAP_ACTION = NAMESPACE;
    }
    // LOGIN INDORE
    public static String invokeNACP(String strWebMethodName, String data) {
        String wsparam0 = "arg0";
        Log.e("Soap Object", " AAAAAAAAAAAAAAAAAA " + strWebMethodName);
        SoapObject request = new SoapObject(NAMESPACE, strWebMethodName);
        request.addProperty(wsparam0, (Object) data);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            Log.e("Calling ", "Callinggggg");
            androidHttpTransport.call(SOAP_ACTION + strWebMethodName, envelope);
            return ((SoapPrimitive) envelope.getResponse()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occured";
        }
    }
    public static String getData(String query, String param0) {
        String wsparam0 = "arg0";
        if(param0 != null){
            wsparam0 = param0;
        }
        SoapObject request = new SoapObject(NAMESPACE, "GetData");
        request.addProperty(wsparam0, (Object) query);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION + "GetData", envelope);
            return ((SoapPrimitive) envelope.getResponse()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occured";
        }
    }
    public static String saveData(String query, String param0) {
        String wsparam0 = "arg0";
        if(param0 != null){
            wsparam0 = param0;
        }
        SoapObject request = new SoapObject(NAMESPACE, "saveData");
        request.addProperty(wsparam0, (Object) query);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION + "saveData", envelope);
            return ((SoapPrimitive) envelope.getResponse()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occured";
        }
    }
}