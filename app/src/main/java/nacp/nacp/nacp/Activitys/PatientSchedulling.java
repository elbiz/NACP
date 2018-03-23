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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
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
import nacp.nacp.nacp.Driver.MFS100Test;
import nacp.nacp.nacp.R;
import nacp.nacp.nacp.WebService.GetDataPostAction;

public class PatientSchedulling extends GetDataPostAction implements MFS100Event {

    private SearchView search_PatientId;

    private TextView tv_patientName,tv_patientAge,tv_patientGender,tv_patientMaritalStatus,tv_patientCode,tv_status;
    private Spinner sp_ServiceType,sp_SiteOfServices,sp_ReferedService;
    private EditText edt_ReferralDate,edt_NoofClients,edt_ParticipantsRelation,edt_InformationAssessment,edt_RiskAssessment,edt_DiscussionIssue,
            edt_RiskReduction,edt_ConsentTest,edt_NoOfDistributedIEC,edt_Remark;
    private Button btn_FormSave,btn_Scan;
    private ImageView imv_BiometricScan,imv_refreshDevice,imv_serviceCalender;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    Calendar newCalendar = Calendar.getInstance();


    private String USERNAME;

    //BarCodeReader

    private enum ScannerAction {
        Capture, Verify
    }
    byte[] Enroll_Template;
    byte[] Verify_Template;
    private FingerData lastCapFingerData = null;
    ScannerAction scannerAction = ScannerAction.Capture;

    int timeout = 10000;
    MFS100 mfs100 = null;

    public static String _testKey = "";

    BroadcastReceiver UsbReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action= intent.getAction();
            try {
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){
                    Toast.makeText(PatientSchedulling.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                    synchronized (this){

                        Toast.makeText(PatientSchedulling.this, "connect", Toast.LENGTH_SHORT).show();
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
        btn_Scan=(Button) findViewById(R.id.btn_scan);

        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }


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




        btn_Scan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerAction = ScannerAction.Capture;
                StartSyncCapture();
                //startActivity(new Intent(PatientSchedulling.this, MFS100Test.class));

            }
        });
        imv_refreshDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitScanner();
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
    protected void onStart() {
        if (mfs100 == null) {
            mfs100 = new MFS100(PatientSchedulling.this);
            mfs100.SetApplicationContext(this);
        } else {
            InitScanner();
        }
        super.onStart();
    }

    protected void onStop() {
        UnInitScanner();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mfs100 != null) {
            mfs100.Dispose();
        }
        super.onDestroy();
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
    private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", ""+ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        Log.e("Finger Data", fingerData.toString());
                        lastCapFingerData = fingerData;
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        PatientSchedulling.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imv_BiometricScan.setImageBitmap(bitmap);
                            }
                        });

                        SetTextOnUIThread("Capture Success");
                        String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                }
            }
        }).start();
    }
    private void SetTextOnUIThread(final String str) {

        tv_status.post(new Runnable() {
            public void run() {
                tv_status.setText(str);
            }
        });
    }
    public void SetData2(FingerData fingerData) {
        if (scannerAction.equals(ScannerAction.Capture)) {
            Enroll_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                    fingerData.ISOTemplate().length);
        } else if (scannerAction.equals(ScannerAction.Verify)) {
            Verify_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0,
                    fingerData.ISOTemplate().length);
            int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
            if (ret < 0) {
                SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
            } else {
                if (ret >= 1400) {
                    SetTextOnUIThread("Finger matched with score: " + ret);
                } else {
                    SetTextOnUIThread("Finger not matched, score: " + ret);
                }
            }
        }

        WriteFile("Raw.raw", fingerData.RawData());
        WriteFile("Bitmap.bmp", fingerData.FingerImage());
        WriteFile("ISOTemplate.iso", fingerData.ISOTemplate());
    }
    private void WriteFile(String filename, byte[] bytes) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "//FingerData";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(bytes);
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }
    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }
    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        if (vid == 1204 || vid == 11279) {
            if (pid == 34323) {
                ret = mfs100.LoadFirmware();
                if (ret != 0) {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                } else {
                    SetTextOnUIThread("Load firmware success");
                }
            } else if (pid == 4101) {
                String key = "Without Key";
                ret = mfs100.Init("");
                if (ret == -1322) {
                    key = "Test Key";
                    ret = mfs100.Init(_testKey);
                }
                if (ret == 0) {
                    Log.e("Key ",key);
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                }

            }
        }
    }
    @Override
    public void OnDeviceDetached() {
        UnInitScanner();
        SetTextOnUIThread("Device removed");
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }
}
