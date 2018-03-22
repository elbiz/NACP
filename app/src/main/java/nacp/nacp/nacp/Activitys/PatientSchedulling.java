package nacp.nacp.nacp.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nacp.nacp.nacp.Driver.EPOSReceiptPrintSampleActivity;
import nacp.nacp.nacp.R;
import nacp.nacp.nacp.WebService.GetDataPostAction;

public class PatientSchedulling extends GetDataPostAction {

    private SearchView search_PatientId;

    private TextView tv_patientName,tv_patientAge,tv_patientGender,tv_patientMaritalStatus,tv_patientCode,tv_status;
    private Spinner sp_ServiceType,sp_SiteOfServices,sp_ReferedService;
    private EditText edt_ReferralDate,edt_NoofClients,edt_ParticipantsRelation,edt_InformationAssessment,edt_RiskAssessment,edt_DiscussionIssue,
            edt_RiskReduction,edt_ConsentTest,edt_NoOfDistributedIEC,edt_Remark;
    private Button btn_FormSave;
    private ImageView imv_BiometricScan,imv_refreshDevice,imv_serviceCalender;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    Calendar newCalendar = Calendar.getInstance();

    // Biometric Device
    private UsbManager manager;
    private boolean storeOpened = false;

    private Spinner rdServices=null;

    private int listCount = 0;
    List list = null;

    private boolean checkProcRunning = false;
    private boolean isRDReady = false;
    private List<String> rdList = null;
    private int foundPackCount = 0;
    private int resultCodeRet = 0;
    private String foundPackName = "";
    private int processedCount = 0;

    private String fCount="1";
    private String fType="FRM";
    private String fform="XML";
    private String fEnc="PRODUCTION";

    private String USERNAME;

    BroadcastReceiver UsbReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action= intent.getAction();
            try {
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){

                    UsbDevice device=intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    ScanRDServices();
                    Toast.makeText(PatientSchedulling.this, "disconnect", Toast.LENGTH_SHORT).show();
                    int pid,vid;
                    pid=device.getProductId();
                    vid=device.getVendorId();
                    if ((pid == 0x8226 || pid == 0x8220 || pid == 0x8225) && (vid == 0x0bca)) {
                        Toast toast = Toast.makeText(PatientSchedulling.this,"Startek FM220 Device Disconnected!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }

                }
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                    synchronized (this){
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        ScanRDServices();
                        Toast.makeText(PatientSchedulling.this, "connect", Toast.LENGTH_SHORT).show();
                        if (device != null) {
                            // call method to set up device communication
                            int pid, vid;
                            pid = device.getProductId();
                            vid = device.getVendorId();
                            if ((pid == 0x8226 || pid == 0x8220 || pid == 0x8225) && (vid == 0x0bca)) {
                                SearchPackageName();
                            }
                        }
                    }
                }

            }catch (Exception e){

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_schedulling);
        ActionBar ab= getSupportActionBar();
        ab.setTitle("Patient Schedulling");
        ab.setDisplayHomeAsUpEnabled(true);

        search_PatientId=(SearchView) findViewById(R.id.search_PatientId);
        tv_patientName=(TextView) findViewById(R.id.tv_patientName);
        tv_patientAge=(TextView) findViewById(R.id.tv_patientAge);
        tv_patientGender=(TextView) findViewById(R.id.tv_patientGender);
        tv_patientMaritalStatus=(TextView) findViewById(R.id.tv_patientMaritalStatus);
        tv_patientCode=(TextView) findViewById(R.id.tv_patientCode);
        tv_status=(TextView) findViewById(R.id.tv_status);

        sp_ServiceType=(Spinner) findViewById(R.id.sp_ServiceType);
        sp_SiteOfServices=(Spinner) findViewById(R.id.sp_SiteOfServices);
        sp_ReferedService=(Spinner) findViewById(R.id.sp_ReferedService);

        edt_ReferralDate=(EditText) findViewById(R.id.edt_ReferralDate);
        edt_NoofClients=(EditText) findViewById(R.id.edt_NoofClients);
        edt_ParticipantsRelation=(EditText) findViewById(R.id.edt_ParticipantsRelation);
        edt_InformationAssessment=(EditText) findViewById(R.id.edt_InformationAssessment);
        edt_RiskAssessment=(EditText) findViewById(R.id.edt_RiskAssessment);
        edt_DiscussionIssue=(EditText) findViewById(R.id.edt_DiscussionIssue);
        edt_RiskReduction=(EditText) findViewById(R.id.edt_RiskReduction);
        edt_ConsentTest=(EditText) findViewById(R.id.edt_ConsentTest);
        edt_NoOfDistributedIEC=(EditText) findViewById(R.id.edt_NoOfDistributedIEC);
        edt_Remark=(EditText) findViewById(R.id.edt_Remark);


        imv_serviceCalender=(ImageView) findViewById(R.id.imv_serviceCalender);
        imv_BiometricScan=(ImageView) findViewById(R.id.imv_BiometricScan);
        imv_refreshDevice=(ImageView) findViewById(R.id.imv_refreshDevice);

        btn_FormSave=(Button) findViewById(R.id.btn_FormSave);


        search_PatientId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String qry= "SELECT tp.*, round(fage) AS fage, tg.fname AS fgender, tm.fname AS fmaritalstatus, tps.freferedservicetypeid, tps.fid AS fscheduleid FROM pm.tpatientregistration AS tp " +
                        " LEFT JOIN public.tgender AS tg ON tg.fid = tp.fgenderid" +
                        " LEFT JOIN public.tmaritalstatus AS tm ON tm.fid = tp.fmaritalstatusid" +
                        " LEFT JOIN pm.tpatientschedule AS tps ON tp.fid = tps.fpatienthistoryid AND fstatus = 'open' " +
                        " WHERE tp.fpatientid = '" +s+"' ";
                executeGetdataService(PatientSchedulling.this,qry,"1");
                Log.e("Query", qry);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        fromDatePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_ReferralDate.setText(dateFormatter.format(newDate.getTime()));


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        imv_serviceCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });


        manager= (UsbManager) getSystemService(Context.USB_SERVICE);
        IntentFilter filter=new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(UsbReceiver,filter);

        for (UsbDevice mdevice : manager.getDeviceList().values()) {

            int pid, vid;

            pid = mdevice.getProductId();
            vid = mdevice.getVendorId();

            if (((pid == 0x8220) && (vid == 0x0bca))
                    || ((pid == 0x8220) && (vid == 0x0b39))
                    || ((pid == 0x8210) && (vid == 0x0b39))
                    ||  (pid == 0x8225) && (vid == 0x0bca)) {
                //SearchPackageName();
                break;
            }

        }
        ScanRDServices();
        rdServices= new Spinner(this);
        imv_BiometricScan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String selectedPackage = rdServices.getSelectedItem().toString().split(":")[0];
                    Intent intent1 = new Intent("in.gov.uidai.rdservice.fp.CAPTURE", null);
                    String pidOptXML  = createPidOptXML();
                    Log.e("pidOptXML", pidOptXML);

                    intent1.putExtra("PID_OPTIONS", pidOptXML);
                    intent1.setPackage(selectedPackage);
                    startActivityForResult(intent1, 2);
                } catch (Exception e) {
                    showMessageDialogue("EXCEPTION- " + e.getMessage(),"EXCEPTION");
                }

            }
        });
        imv_refreshDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ScanRDServices();
                } catch (Exception e) {
                    showMessageDialogue("EXCEPTION- " + e.getMessage(),"EXCEPTION");
                }
            }
        });


        btn_FormSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = getPackageManager().getLaunchIntentForPackage("com.epson.epos2_printer");
                in.putExtra("data","12345");
                startActivity(in);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);}
    }

    @Override
    public void onPostExecuteGetData(Vector<HashMap> result, String activity) {
        if (activity.equals("1"))
        {
            if (!result.equals("[]")){

                for (int i=0;i<result.size();i++){
                    tv_patientName.setText(result.get(0).get("fname").toString());
                    tv_patientCode.setText(result.get(0).get("fcode").toString());
                    tv_patientAge.setText(result.get(0).get("fage").toString());
                    tv_patientGender.setText(result.get(0).get("fgender").toString());
                    tv_patientMaritalStatus.setText(result.get(0).get("fmaritalstatus").toString());
                }
            }
            setCachedDataSet(this,"pm.tservicetype");
            setCachedDataSet(this,"pm.tservice");
        }
    }

    @Override
    public void getCachedDataSet(HashMap<String, ArrayList> lvHasmap, String lvname) {
        if(lvname.equals("pm.tservicetype")){
            sp_ServiceType.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_ServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //fservicetypeid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            sp_ReferedService.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_ReferedService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //freferedservicetypeid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

        }else if(lvname.equals("pm.tservice")){
            sp_SiteOfServices.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_SiteOfServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //fserviceid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
    }
    private void ScanRDServices() {

        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent("in.gov.uidai.rdservice.fp.INFO", null);
        list=pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        rdList = new ArrayList<>();
        foundPackCount = 0;
        foundPackName = "";
        processedCount = 0;
        listCount = 0;
        if (list.size() > 0) {
            boolean containsFm220Rd = false;
            for (Object rInfo : list) {
                final String tmpPackName = ((ResolveInfo) rInfo).activityInfo.applicationInfo
                        .packageName.trim();
                if(tmpPackName.contains("com.acpl.registersdk")){
                    containsFm220Rd = true;
                    break;
                }
            }
            foundPackCount = list.size();

            if (list.size() > 1) {
                Object rInfo = list.get(0);
                final String packageName = ((ResolveInfo) rInfo).activityInfo.applicationInfo
                        .packageName.trim();
                foundPackName = packageName;
                Intent intent1 = new Intent("in.gov.uidai.rdservice.fp.INFO", null);
                intent1.setPackage(packageName);
                startActivityForResult(intent1, 9000);
//                for (Object rInfo : list) {
//                    final String packageName = ((ResolveInfo) rInfo).activityInfo.applicationInfo
//                            .packageName.trim();
//                    foundPackName = packageName;
//                    checkProcRunning = true;
//                    boolean isReady = false;
//                    Intent intent1 = new Intent("in.gov.uidai.rdservice.fp.INFO", null);
//                    intent1.setPackage(packageName);
//                    tmpResultCode = tmpResultCode+1;
//                    resultCodeRet = tmpResultCode;
//                    startActivityForResult(intent1, tmpResultCode);
//                }
            } else {
                for (Object rInfo : list) {
                    final String packageName = ((ResolveInfo) rInfo).activityInfo.applicationInfo
                            .packageName.trim();
                    foundPackName = packageName;

                    Intent intent1 = new Intent("in.gov.uidai.rdservice.fp.INFO", null);
                    intent1.setPackage(packageName);
                    startActivityForResult(intent1, 9000);
                }
            }
        }else{
            if(!storeOpened){
                /*storeOpened = true;
                Toast toast = Toast.makeText(MainForm.this,"Please install `ACPL FM220 Registered Device` Service", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();*/
                storeOpened = true;
                Toast toast = Toast.makeText(PatientSchedulling.this,"Please install `ACPL FM220 Registered Device` Service", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                Intent intentPlay = new Intent(Intent.ACTION_VIEW);
                intentPlay.setData(Uri.parse("market://details?id=com.acpl.registersdk"));
                startActivity(intentPlay);
            }

        }

    }
    private void SearchPackageName(){
        PackageManager pm = this.getPackageManager();
        String packageName = "com.acpl.registersdk";
        Intent intent = new Intent();
        intent.setPackage(packageName);
        List listTemp = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);

        if(listTemp.size() <= 0 &&  !storeOpened){
            storeOpened = true;
            Toast toast = Toast.makeText(PatientSchedulling.this,"Please install `ACPL FM220 Registered Device` Service.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

        }else{
            storeOpened = false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data == null) {
                if (requestCode == 3) {
                    showMessageDialogue("No change in setting!","Message");
                } else if (resultCode == Activity.RESULT_OK) {
                    showMessageDialogue("Scan Data Missing!","Message");
//                    btnAuthenticate.setEnabled(false);
                } else if (resultCode == Activity.RESULT_CANCELED) {
//                    btnAuthenticate.setEnabled(false);
                    showMessageDialogue("Scan Failed/Aborted!","Message");
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == 9000) {
                        String rd_info1 = data.getStringExtra("RD_SERVICE_INFO");
                        if (rd_info1 != null && rd_info1.contains("NOTREADY")) {
                            isRDReady = false;
                            //CALLBACK METHOD
                            AddIntoList(foundPackName);
                        } else if (rd_info1 != null && rd_info1.contains("READY")) {
                            isRDReady = true;
                            //CALLBACK METHOD
                            AddIntoList(foundPackName);
                        }

                        listCount = listCount + 1;
                        if(listCount < foundPackCount){
                            Object rInfo = list.get(listCount);
                            final String packageName = ((ResolveInfo) rInfo).activityInfo.applicationInfo
                                    .packageName.trim();
                            foundPackName = packageName;
                            Intent intent1 = new Intent("in.gov.uidai.rdservice.fp.INFO", null);
                            intent1.setPackage(packageName);
                            startActivityForResult(intent1, 9000);
                        }
                    } else if (requestCode >= 100) {
                        String rd_info = data.getStringExtra("RD_SERVICE_INFO");
                        if (rd_info != null) {
                            showMessageDialogue(rd_info,"RD SERVICE INFO XML");
                        }else{
                            showMessageDialogue("NULL STRING RETURNED","RD SERVICE INFO XML");
                        }

                        String dev_info = data.getStringExtra("DEVICE_INFO");
                        if (dev_info != null) {
                            showMessageDialogue(dev_info,"DEVICE INFO XML");
                        }else{
                            showMessageDialogue("NULL STRING RETURNED","DEVICE INFO XML");
                        }
                    } else if (requestCode == 2) {
                        String pidDataXML = data.getStringExtra("PID_DATA");
                        if(pidDataXML!= null){
                            showMessageDialogue(pidDataXML,"PID DATA XML");
                            System.out.println("Pid "+ pidDataXML);
                        }else{
                            showMessageDialogue("NULL STRING RETURNED","PID DATA XML");
                        }
//
                    } else if (requestCode == 3) {
                        //CAPTURE ONLY
                    } else if (requestCode == 13) {
                        String value = data.getStringExtra("CLAIM");
                        if (value != null) {
                            showMessageDialogue(value,"INTERFACE CLAIM RESULT");
                        }
                    } else if (requestCode == 14) {
                        String value = data.getStringExtra("RELEASE");
                        if (value != null) {
                            showMessageDialogue(value,"INTERFACE RELEASE RESULT");
                        }
                    } else if (requestCode == 15) {
                        String value = data.getStringExtra("SET_REG");
                        if (value != null) {
                            showMessageDialogue(value,"REGISTRATION FLAG SET RESULT");
                        }
                    } else if (requestCode == 16) {
                        String value = data.getStringExtra("GET_REG");
                        if (value != null) {
                            showMessageDialogue(value,"REGISTRATION FLAG GET RESULT");
                        }
                    } else if (requestCode == 17) {
                        String value = data.getStringExtra("REVOKEREG");
                        if (value != null) {
                            showMessageDialogue(value,"REGISTRATION FLAG REVOKE RESULT");
                        }
                    }  else if (requestCode == 19) {
                        String value = data.getStringExtra("SETLINKS");
                        if (value != null) {
                            showMessageDialogue(value,"SET LINK RESULT");
                        }
                    }else {
//                        String str = data.getStringExtra("Setting");
//                        AuthProcessor aupro = new AuthProcessor();
//                        aupro.SetSettingsvalue(mcontext);
//                        txtmsg.setText(str);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
//                    btnAuthenticate.setEnabled(false);
                    showMessageDialogue("Scan Failed/Aborted!","CAPTURE RESULT");
                }
            }
        } catch (Exception ex) {
            showMessageDialogue("Error:-" + ex.getMessage(),"EXCEPTION");
            ex.printStackTrace();
        }
    }
    private void showMessageDialogue(String messageTxt, String argTitle) {
        new AlertDialog.Builder(PatientSchedulling.this)
                .setCancelable(false)
                .setTitle(argTitle)
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    private void AddIntoList(String argPackName) {
        if(foundPackCount > 1){
            if (isRDReady) {
                rdList.add(argPackName + ": READY");
            } else {
                rdList.add(argPackName + ": NOTREADY");
            }
        }else{
            if (isRDReady) {
                rdList.add(argPackName + ": READY");
            } else {
                rdList.add(argPackName + ": NOTREADY");
            }
        }
        processedCount++;
        if(processedCount == foundPackCount){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, rdList);
            rdServices.setAdapter(adapter);
            tv_status.setText(rdServices.getSelectedItem().toString().split(":")[1]);
        }
    }
    private String createPidOptXML() {
        String tmpOptXml = "";
        try{
            String fTypeStr = "0";
            String formatStr = "0";
            String timeOutStr = "20000";
            String envStr = "P";

            if(fType.equals("FMR")){
                fTypeStr = "0";
            }

            if(fform.equals("XML")){
                formatStr = "0";
            }
            timeOutStr = "2000";


            if(fEnc.equals("PRODUCTION")){
                envStr = "P";
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = null;

            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);

            Element opts = doc.createElement("Opts");
            rootElement.appendChild(opts);

            Attr attr = doc.createAttribute("fCount");
            attr.setValue(String.valueOf(fCount));
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("fType");
            attr.setValue(fTypeStr);
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("iCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("iType");
            attr.setValue("");
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("pCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("pType");
            attr.setValue("");
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("format");
            attr.setValue(formatStr);
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("pidVer");
            attr.setValue("2.0");
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("timeout");
            attr.setValue(timeOutStr);
            opts.setAttributeNode(attr);

            attr = doc.createAttribute("env");
            attr.setValue(envStr);
            opts.setAttributeNode(attr);


            attr = doc.createAttribute("posh");
            attr.setValue("UNKNOWN");
            opts.setAttributeNode(attr);

            Element custotp = doc.createElement("CustOpts");
            rootElement.appendChild(custotp);

            Element param = doc.createElement("Param");
            custotp.appendChild(param);


            attr = doc.createAttribute("name");
            attr.setValue("ValidationKey");
            param.setAttributeNode(attr);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            tmpOptXml = writer.getBuffer().toString().replaceAll("\n|\r", "");
            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            tmpOptXml = tmpOptXml.replace("<Demo><Demo","<Demo");
            tmpOptXml = tmpOptXml.replace("</Demo></Demo>","</Demo>");

            return tmpOptXml;
        }catch(Exception ex){
            showMessageDialogue("EXCEPTION- " + ex.getMessage(),"EXCEPTION");
            return "";
        }
    }
}
