package nacp.nacp.nacp.Activitys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import nacp.nacp.nacp.Activitys.PatientSchedulling;
import nacp.nacp.nacp.R;
import nacp.nacp.nacp.WebService.GetDataPostAction;
import nacp.nacp.nacp.WebService.WebService;

public class MainForm extends GetDataPostAction implements MFS100Event {

    private ImageView imv_BiometricScan,imv_refreshDevice,imv_serviceCalender;
    private TextView tv_status,tv_otherDrugUsingLocation,tv_otherWhereStayAtNight,tv_otherCurruntUsingDrug,
            tv_otherMethodUseDrug,tv_otherDrugInjectingAtFirt,tv_otherSyringePlace,
            tv_otherInjectedBy,tv_otherTreatmentBy,tv_otherHealthDiseases,tv_otherContactPerson,
            tv_otherLastContactPerson,tv_otherInformationsource,tv_otherInfromationchannel,tv_otherFamilyRelation,tv_otherFamilyDiseases,tv_DocName;

    //Spinner
    private Spinner sp_NGO,sp_City,sp_ServiceType,sp_SiteOfServices,sp_Sex,sp_Ethnicity,
            sp_OriginDistrict,sp_OriginVillage,sp_OriginProvince
            ,sp_CurrentDistrict,sp_CurrentVillage,sp_CurrentProvince,
            sp_Occupation,sp_Education,sp_MaritalStatus,sp_DrugUsingLocation,sp_WhereStayAtNight,
            sp_FirstTimeInjectingDrugReason,sp_WhereStartUsingDrug,sp_CurruntUsingDrug,
            sp_MethodUseDrug,sp_HistoryYear,sp_DrugInjectingAtFirt,sp_SyringeType,sp_SyringePlace,sp_InjectedBy,
            sp_TreatmentBy,sp_BehavedWithYou,sp_HealthDiseases,sp_ContactPerson,sp_LastContactPerson,
            sp_Informationsource,sp_Infromationchannel,sp_familyBehaviour,sp_FamilyRelation,sp_FamilyDiseases,spn_YearImrisoned,
            spn_YearHowLongSoldBlood,spn_YearSexualContact,sp_ReferedService,sp_ReferedCenter,sp_ReferedBy;

    //EditText
    private EditText edt_ServiceCode,edt_ReferralDate,edt_Name,edt_FatherName
            ,edt_GeneralCode,edt_Age,edt_DatlyExp,edt_DailyDrugUsingTime,edt_IncomeAMT,edt_incomeSRC
            ,edt_HistoryYear,edt_InjectedDrugReason,edt_NumOfInjectionPerDay,edt_HowOftenImprisoned,edt_YearImrisoned,edt_HowLongSoldBlood,
            edt_YearSexualContact,edt_FirstWayOfTransmission,edt_SecondWayOfTransmission,edt_ThirdWayOfTransmission,
            edt_NameOfPreparer,edt_DesugnationOfPreparer,edt_Remark,edt_otherDrugUsingLocation,edt_otherWhereStayAtNight,
            edt_otherCurruntUsingDrug,edt_otherMethodUseDrug,edt_otherDrugInjectingAtFirt,edt_otherSyringePlace,
            edt_otherInjectedBy,edt_otherTreatmentBy,edt_otherHealthDiseases,edt_otherContactPerson,edt_otherLastContactPerson,
            edt_otherInformationsource,edt_otherInfromationchannel,edt_otherFamilyRelation,edt_otherFamilyDiseases;

    //radioButton
    private RadioButton rdo_YesInjectedDrug,rdo_NoInjectedDrug,rdo_YesInjectedDrugLast3Month,
            rdo_NoInjectedDrugLast3Month,rdo_YesEverImprisoned,rdo_NoEverImprisoned,rdo_YesSoldBlood
            ,rdo_NoSoldBlood,rdo_YesSexualContact,rdo_NoSexualContact,rdo_YesHeardAboutCondom,rdo_NoHeardAboutCondom,
            rdo_YesUseCondom,rdo_NoUseCondom,rdo_YesLastTimeUseCondom,rdo_NoLastTimeUseCondom,
            rdo_YesDoNeedleSyringe,rdo_NoDoNeedleSyringe,rdo_YesInfoAboutHIV,rdo_NoInfoAboutHIV,
            rdo_YesInfoAboutHBV,rdo_NoInfoAboutHBV,rdo_YesInfoAboutHCV,rdo_NoInfoAboutHCV,
            rdo_YesFamilyMemberUsingDrug,rdo_NoFamilyMemberUsingDrug;

    private ProgressDialog progressBar1;
    //Button
    private Button btn_FormSave,btn_FormReset,btn_FormCancel,btn_Scan;


    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    Calendar newCalendar = Calendar.getInstance();

    private String fhealthfacilitycenterid,fcityid,fservicetypeid,
            fserviceid,fage,fgenderid,fethnicityid,fprovinceid,
            fcurrentprovinceid,fmaritalstatusid,fdrugusinglocationid,
            fnightstayid,fwhenlasttimebloodsold,freferedservicetypeid,freferedhealthfacilitycenterid,freferedbyserviceproviderid;

    private static final int PICKFILE_RESULT_CODE=12;


    private String USERNAME;

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

                    Toast.makeText(MainForm.this, "disconnect", Toast.LENGTH_SHORT).show();

                }
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                    synchronized (this){
                        Toast.makeText(MainForm.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                }

            }catch (Exception e){

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form2);

        ActionBar ab= getSupportActionBar();
        ab.setTitle("Registration Form");
        ab.setDisplayHomeAsUpEnabled(true);

        imv_BiometricScan=(ImageView) findViewById(R.id.imv_BiometricScan);
        imv_refreshDevice=(ImageView) findViewById(R.id.imv_refreshDevice);
        imv_serviceCalender=(ImageView) findViewById(R.id.imv_serviceCalender);

        tv_status=(TextView) findViewById(R.id.tv_status);
        tv_otherDrugUsingLocation=(TextView) findViewById(R.id.tv_otherDrugUsingLocation);
        tv_otherWhereStayAtNight=(TextView) findViewById(R.id.tv_otherWhereStayAtNight);
        tv_otherCurruntUsingDrug=(TextView) findViewById(R.id.tv_otherCurruntUsingDrug);
        tv_otherMethodUseDrug=(TextView) findViewById(R.id.tv_otherMethodUseDrug);
        tv_otherDrugInjectingAtFirt=(TextView) findViewById(R.id.tv_otherDrugInjectingAtFirt);
        tv_otherSyringePlace=(TextView) findViewById(R.id.tv_otherSyringePlace);
        tv_otherInjectedBy=(TextView) findViewById(R.id.tv_otherInjectedBy);
        tv_otherTreatmentBy=(TextView) findViewById(R.id.tv_otherTreatmentBy);
        tv_otherHealthDiseases=(TextView) findViewById(R.id.tv_otherHealthDiseases);
        tv_otherContactPerson=(TextView) findViewById(R.id.tv_otherContactPerson);
        tv_otherLastContactPerson=(TextView) findViewById(R.id.tv_otherLastContactPerson);
        tv_otherInformationsource=(TextView) findViewById(R.id.tv_otherInformationsource);
        tv_otherInfromationchannel=(TextView) findViewById(R.id.tv_otherInfromationchannel);
        tv_otherFamilyRelation=(TextView) findViewById(R.id.tv_otherFamilyRelation);
        tv_otherFamilyDiseases=(TextView) findViewById(R.id.tv_otherFamilyDiseases);
        tv_DocName=(TextView) findViewById(R.id.tv_DocName);

        sp_NGO=(Spinner) findViewById(R.id.sp_NGO);
        sp_City=(Spinner) findViewById(R.id.sp_City);
        sp_ServiceType=(Spinner) findViewById(R.id.sp_ServiceType);
        sp_SiteOfServices=(Spinner) findViewById(R.id.sp_SiteOfServices);
        sp_Sex=(Spinner) findViewById(R.id.sp_Sex);
        sp_Ethnicity=(Spinner) findViewById(R.id.sp_Ethnicity);
        sp_OriginDistrict=(Spinner) findViewById(R.id.sp_OriginDistrict);
        sp_OriginVillage=(Spinner) findViewById(R.id.sp_OriginVillage);
        sp_OriginProvince=(Spinner) findViewById(R.id.sp_OriginProvince);
        sp_CurrentDistrict=(Spinner) findViewById(R.id.sp_CurrentDistrict);
        sp_CurrentVillage=(Spinner) findViewById(R.id.sp_CurrentVillage);
        sp_CurrentProvince=(Spinner) findViewById(R.id.sp_CurrentProvince);
        sp_Occupation=(Spinner) findViewById(R.id.sp_Occupation);
        sp_Education=(Spinner) findViewById(R.id.sp_Education);
        sp_MaritalStatus=(Spinner) findViewById(R.id.sp_MaritalStatus);
        sp_DrugUsingLocation=(Spinner) findViewById(R.id.sp_DrugUsingLocation);
        sp_WhereStayAtNight=(Spinner) findViewById(R.id.sp_WhereStayAtNight);
        sp_FirstTimeInjectingDrugReason=(Spinner) findViewById(R.id.sp_FirstTimeInjectingDrugReason);
        sp_WhereStartUsingDrug=(Spinner) findViewById(R.id.sp_WhereStartUsingDrug);
        sp_CurruntUsingDrug=(Spinner) findViewById(R.id.sp_CurruntUsingDrug);
        sp_MethodUseDrug=(Spinner) findViewById(R.id.sp_MethodUseDrug);
        sp_HistoryYear=(Spinner) findViewById(R.id.sp_HistoryYear);
        sp_DrugInjectingAtFirt=(Spinner) findViewById(R.id.sp_DrugInjectingAtFirt);
        sp_SyringeType=(Spinner) findViewById(R.id.sp_SyringeType);
        sp_SyringePlace=(Spinner) findViewById(R.id.sp_SyringePlace);
        sp_InjectedBy=(Spinner) findViewById(R.id.sp_InjectedBy);
        sp_TreatmentBy=(Spinner) findViewById(R.id.sp_TreatmentBy);
        sp_BehavedWithYou=(Spinner) findViewById(R.id.sp_BehavedWithYou);
        sp_HealthDiseases=(Spinner) findViewById(R.id.sp_HealthDiseases);
        sp_ContactPerson=(Spinner) findViewById(R.id.sp_ContactPerson);
        sp_LastContactPerson=(Spinner) findViewById(R.id.sp_LastContactPerson);
        sp_Informationsource=(Spinner) findViewById(R.id.sp_Informationsource);
        sp_Infromationchannel=(Spinner) findViewById(R.id.sp_Infromationchannel);
        sp_familyBehaviour=(Spinner) findViewById(R.id.sp_familyBehaviour);
        sp_FamilyRelation=(Spinner) findViewById(R.id.sp_FamilyRelation);
        sp_FamilyDiseases=(Spinner) findViewById(R.id.sp_FamilyDiseases);
        spn_YearImrisoned=(Spinner) findViewById(R.id.spn_YearImrisoned);
        spn_YearHowLongSoldBlood=(Spinner) findViewById(R.id.spn_YearHowLongSoldBlood);
        spn_YearSexualContact=(Spinner) findViewById(R.id.spn_YearSexualContact);
        sp_ReferedService=(Spinner) findViewById(R.id.sp_ReferedService);
        sp_ReferedCenter=(Spinner) findViewById(R.id.sp_ReferedCenter);
        sp_ReferedBy=(Spinner) findViewById(R.id.sp_ReferedBy);

        edt_ServiceCode=(EditText) findViewById(R.id.edt_ServiceCode);
        edt_ReferralDate=(EditText) findViewById(R.id.edt_ReferralDate);
        edt_Name=(EditText) findViewById(R.id.edt_Name);
        edt_FatherName=(EditText) findViewById(R.id.edt_FatherName);
        edt_GeneralCode=(EditText) findViewById(R.id.edt_GeneralCode);
        edt_Age=(EditText) findViewById(R.id.edt_Age);
        edt_DatlyExp=(EditText) findViewById(R.id.edt_DatlyExp);
        edt_DailyDrugUsingTime=(EditText) findViewById(R.id.edt_DailyDrugUsingTime);
        edt_IncomeAMT=(EditText) findViewById(R.id.edt_IncomeAMT);
        edt_incomeSRC=(EditText) findViewById(R.id.edt_incomeSRC);
        edt_HistoryYear=(EditText) findViewById(R.id.edt_HistoryYear);
        edt_InjectedDrugReason=(EditText) findViewById(R.id.edt_InjectedDrugReason);
        edt_NumOfInjectionPerDay=(EditText) findViewById(R.id.edt_NumOfInjectionPerDay);
        edt_HowOftenImprisoned=(EditText) findViewById(R.id.edt_HowOftenImprisoned);
        edt_YearImrisoned=(EditText) findViewById(R.id.edt_YearImrisoned);
        edt_HowLongSoldBlood=(EditText) findViewById(R.id.edt_HowLongSoldBlood);
        edt_YearSexualContact=(EditText) findViewById(R.id.edt_YearSexualContact);
        edt_FirstWayOfTransmission=(EditText) findViewById(R.id.edt_FirstWayOfTransmission);
        edt_SecondWayOfTransmission=(EditText) findViewById(R.id.edt_SecondWayOfTransmission);
        edt_ThirdWayOfTransmission=(EditText) findViewById(R.id.edt_ThirdWayOfTransmission);
        edt_NameOfPreparer=(EditText) findViewById(R.id.edt_NameOfPreparer);
        edt_DesugnationOfPreparer=(EditText) findViewById(R.id.edt_DesugnationOfPreparer);
        edt_Remark=(EditText) findViewById(R.id.edt_Remark);
        edt_otherDrugUsingLocation=(EditText) findViewById(R.id.edt_otherDrugUsingLocation);
        edt_otherWhereStayAtNight=(EditText) findViewById(R.id.edt_otherWhereStayAtNight);
        edt_otherCurruntUsingDrug=(EditText) findViewById(R.id.edt_otherCurruntUsingDrug);
        edt_otherMethodUseDrug=(EditText) findViewById(R.id.edt_otherMethodUseDrug);
        edt_otherDrugInjectingAtFirt=(EditText) findViewById(R.id.edt_otherDrugInjectingAtFirt);
        edt_otherSyringePlace=(EditText) findViewById(R.id.edt_otherSyringePlace);
        edt_otherInjectedBy=(EditText) findViewById(R.id.edt_otherInjectedBy);
        edt_otherTreatmentBy=(EditText) findViewById(R.id.edt_otherTreatmentBy);
        edt_otherHealthDiseases=(EditText) findViewById(R.id.edt_otherHealthDiseases);
        edt_otherContactPerson=(EditText) findViewById(R.id.edt_otherContactPerson);
        edt_otherLastContactPerson=(EditText) findViewById(R.id.edt_otherLastContactPerson);
        edt_otherInformationsource=(EditText) findViewById(R.id.edt_otherInformationsource);
        edt_otherInfromationchannel=(EditText) findViewById(R.id.edt_otherInfromationchannel);
        edt_otherFamilyRelation=(EditText) findViewById(R.id.edt_otherFamilyRelation);
        edt_otherFamilyDiseases=(EditText) findViewById(R.id.edt_otherFamilyDiseases);



        rdo_YesInjectedDrug=(RadioButton) findViewById(R.id.rdo_YesInjectedDrug);
        rdo_NoInjectedDrug=(RadioButton) findViewById(R.id.rdo_NoInjectedDrug);
        rdo_YesInjectedDrugLast3Month=(RadioButton) findViewById(R.id.rdo_YesInjectedDrugLast3Month);
        rdo_NoInjectedDrugLast3Month=(RadioButton) findViewById(R.id.rdo_NoInjectedDrugLast3Month);
        rdo_YesEverImprisoned=(RadioButton) findViewById(R.id.rdo_YesEverImprisoned);
        rdo_NoEverImprisoned=(RadioButton) findViewById(R.id.rdo_NoEverImprisoned);
        rdo_YesSoldBlood=(RadioButton) findViewById(R.id.rdo_YesSoldBlood);
        rdo_NoSoldBlood=(RadioButton) findViewById(R.id.rdo_NoSoldBlood);
        rdo_YesSexualContact=(RadioButton) findViewById(R.id.rdo_YesSexualContact);
        rdo_NoSexualContact=(RadioButton) findViewById(R.id.rdo_NoSexualContact);
        rdo_YesHeardAboutCondom=(RadioButton) findViewById(R.id.rdo_YesHeardAboutCondom);
        rdo_NoHeardAboutCondom=(RadioButton) findViewById(R.id.rdo_NoHeardAboutCondom);
        rdo_YesUseCondom=(RadioButton) findViewById(R.id.rdo_YesUseCondom);
        rdo_NoUseCondom=(RadioButton) findViewById(R.id.rdo_NoUseCondom);
        rdo_YesLastTimeUseCondom=(RadioButton) findViewById(R.id.rdo_YesLastTimeUseCondom);
        rdo_NoLastTimeUseCondom=(RadioButton) findViewById(R.id.rdo_NoLastTimeUseCondom);
        rdo_YesDoNeedleSyringe=(RadioButton) findViewById(R.id.rdo_YesDoNeedleSyringe);
        rdo_NoDoNeedleSyringe=(RadioButton) findViewById(R.id.rdo_NoDoNeedleSyringe);
        rdo_YesInfoAboutHIV=(RadioButton) findViewById(R.id.rdo_YesInfoAboutHIV);
        rdo_NoInfoAboutHIV=(RadioButton) findViewById(R.id.rdo_NoInfoAboutHIV);
        rdo_YesInfoAboutHBV=(RadioButton) findViewById(R.id.rdo_YesInfoAboutHBV);
        rdo_NoInfoAboutHBV=(RadioButton) findViewById(R.id.rdo_NoInfoAboutHBV);
        rdo_YesInfoAboutHCV=(RadioButton) findViewById(R.id.rdo_YesInfoAboutHCV);
        rdo_NoInfoAboutHCV=(RadioButton) findViewById(R.id.rdo_NoInfoAboutHCV);
        rdo_YesFamilyMemberUsingDrug=(RadioButton) findViewById(R.id.rdo_YesFamilyMemberUsingDrug);
        rdo_NoFamilyMemberUsingDrug=(RadioButton) findViewById(R.id.rdo_NoFamilyMemberUsingDrug);

        //Button
        btn_FormSave=(Button) findViewById(R.id.btn_FormSave);
        btn_FormReset=(Button) findViewById(R.id.btn_FormReset);
        btn_FormCancel=(Button) findViewById(R.id.btn_FormCancel);
        btn_Scan=(Button) findViewById(R.id.btn_scan);

        USERNAME= getIntent().getStringExtra("user");

        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

        ArrayList familyBehave=new ArrayList();
        familyBehave.add(" ");
        familyBehave.add("Better");
        familyBehave.add("Good");
        familyBehave.add("Bad");
        ArrayAdapter adapFamilybehave= new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,familyBehave);
        sp_familyBehaviour.setAdapter(adapFamilybehave);
        sp_BehavedWithYou.setAdapter(adapFamilybehave);

        ArrayList commonDate=new ArrayList();
        commonDate.add(" ");
        commonDate.add("Month");
        commonDate.add("Year");
        ArrayAdapter adapCommonDateadap= new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,commonDate);
        sp_HistoryYear.setAdapter(adapCommonDateadap);
        spn_YearHowLongSoldBlood.setAdapter(adapCommonDateadap);
        spn_YearImrisoned.setAdapter(adapCommonDateadap);
        spn_YearSexualContact.setAdapter(adapCommonDateadap);


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

            }
        });
        imv_refreshDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitScanner();
            }
        });
        onLoad();

        btn_FormSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
        btn_FormReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        btn_FormCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        if (mfs100 == null) {
            mfs100 = new MFS100(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainform, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            default:
                return super.onOptionsItemSelected(item);}
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
                    if(requestCode==PICKFILE_RESULT_CODE){
                        String FilePath = data.getData().getPath();
                        tv_DocName.setText(FilePath);
                    }
                    else {
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
        new AlertDialog.Builder(MainForm.this)
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
    private void onLoad(){

        //setCachedDataSet(this,"mm.tstorelocation");
        String Query ="SELECT thc.*, tem.fcityid, tem.fcode FROM pm.thealthfacilitycenter AS thc INNER JOIN pm.temployeecentermapping AS tem ON tem.fhealthfacilitycenterid = thc.fid AND tem.floginusername ='"+USERNAME+"'";
        Log.e("Query ",Query);
        executeGetdataService(this,Query,"5");
        setCachedDataSet(this,"public.tcity");
        setCachedDataSet(this,"pm.tservicetype");
        setCachedDataSet(this,"pm.tservice");
        setCachedDataSet(this,"public.tgender");
        setCachedDataSet(this,"hrm.tcaste");

        setCachedDataSet(this,"public.tstate");
/*			setCachedDataSet(this,"public.tdistrict");
			setCachedDataSet(this,"public.tcity");
*/
        setCachedDataSet(this,"public.toccupation");
        setCachedDataSet(this,"public.teducationalqualification");
        setCachedDataSet(this,"public.tmaritalstatus");
        setCachedDataSet(this,"pm.tdrugusinglocation");
        setCachedDataSet(this,"pm.tnightstay");

        setCachedDataSet(this,"pm.tfirsttimedrugreason");
        setCachedDataSet(this,"pm.tdrug");
        setCachedDataSet(this,"pm.tdrugusemethod");

        setCachedDataSet(this,"public.tcountry");

        setCachedDataSet(this,"pm.tfirsttimeinjectingdrugreason");
        setCachedDataSet(this,"pm.tsyringetype");
        setCachedDataSet(this,"pm.tsyringeplace");
        setCachedDataSet(this,"pm.tinjectedby");

        setCachedDataSet(this,"pm.ttreatmentby");
        setCachedDataSet(this,"pm.tdisease");

        setCachedDataSet(this,"pm.tcontactperson");
        setCachedDataSet(this,"pm.tinformationsource");

        setCachedDataSet(this,"hrm.trelation");

        setCachedDataSet(this,"pm.thealthfacilitycenter");
        setCachedDataSet(this,"pm.tserviceprovider");

    }

    @Override
    public void getCachedDataSet(final HashMap<String, ArrayList> lvHasmap, final String lvname) {
        super.getCachedDataSet(lvHasmap, lvname);
        if(lvname.equals("public.tcity")){
            sp_City.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fcityid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
        else if(lvname.equals("pm.tservicetype")){
            sp_ServiceType.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_ServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fservicetypeid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            sp_ReferedService.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_ReferedService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    freferedservicetypeid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
        else if(lvname.equals("pm.tservice")){
            sp_SiteOfServices.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_SiteOfServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fserviceid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
        else if(lvname.equals("public.tgender")){
            sp_Sex.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_Sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fgenderid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
        else if(lvname.equals("hrm.tcaste")){
            sp_Ethnicity.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_Ethnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fethnicityid=lvHasmap.get("fid").get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }

        else if(lvname.equals("public.tstate")){
            sp_OriginDistrict.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_OriginDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fprovinceid=lvHasmap.get("fid").get(i).toString();
                    //executeGetdataService(MainForm.this,"SELECT * FROM public.tdistrict where fstateid="+fprovinceid+"","1");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            sp_CurrentDistrict.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_CurrentDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fcurrentprovinceid=lvHasmap.get("fid").get(i).toString();
                    //executeGetdataService(MainForm.this,"SELECT * FROM public.tdistrict where fstateid="+fcurrentprovinceid+"","2");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
/*			m_cmbDistrict.setData(CListOfValuesClientCache.getCachedDataSet("public.tdistrict"));
			m_cmbVillage.setData(CListOfValuesClientCache.getCachedDataSet("public.tcity"));
*/
        else if(lvname.equals("public.toccupation")){


            sp_Occupation.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
        }
        else if(lvname.equals("public.teducationalqualification")){
            sp_Education.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
        }
        else if(lvname.equals("public.tmaritalstatus")){
            sp_MaritalStatus.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_MaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fmaritalstatusid=lvHasmap.get("fid").get(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tdrugusinglocation")){
            sp_DrugUsingLocation.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_DrugUsingLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_DrugUsingLocation.getSelectedItem().toString().equals("Other")+"");
                    fdrugusinglocationid=lvHasmap.get("fid").get(i).toString();
                    if (sp_DrugUsingLocation.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherDrugUsingLocation.setVisibility(View.VISIBLE);
                        edt_otherDrugUsingLocation.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherDrugUsingLocation.setVisibility(View.INVISIBLE);
                        edt_otherDrugUsingLocation.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tnightstay")){
            sp_WhereStayAtNight.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_WhereStayAtNight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_WhereStayAtNight.getSelectedItem().toString().equals("Other")+"");
                    fnightstayid=lvHasmap.get("fid").get(i).toString();
                    if (sp_WhereStayAtNight.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherWhereStayAtNight.setVisibility(View.VISIBLE);
                        edt_otherWhereStayAtNight.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherWhereStayAtNight.setVisibility(View.INVISIBLE);
                        edt_otherWhereStayAtNight.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tfirsttimedrugreason")){
            sp_FirstTimeInjectingDrugReason.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
        }
        else if(lvname.equals("pm.tdrug")){
            sp_CurruntUsingDrug.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_CurruntUsingDrug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_CurruntUsingDrug.getSelectedItem().toString().equals("Other")+"");

                    if (sp_CurruntUsingDrug.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherCurruntUsingDrug.setVisibility(View.VISIBLE);
                        edt_otherCurruntUsingDrug.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherCurruntUsingDrug.setVisibility(View.INVISIBLE);
                        edt_otherCurruntUsingDrug.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tdrugusemethod")){
            sp_MethodUseDrug.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_MethodUseDrug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_MethodUseDrug.getSelectedItem().toString().equals("Other")+"");

                    if (sp_MethodUseDrug.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherMethodUseDrug.setVisibility(View.VISIBLE);
                        edt_otherMethodUseDrug.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherMethodUseDrug.setVisibility(View.INVISIBLE);
                        edt_otherMethodUseDrug.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        else if(lvname.equals("public.tcountry")){
            sp_WhereStartUsingDrug.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
        }

        else if(lvname.equals("pm.tfirsttimeinjectingdrugreason")){
            sp_DrugInjectingAtFirt.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_DrugInjectingAtFirt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_DrugInjectingAtFirt.getSelectedItem().toString().equals("Other")+"");

                    if (sp_DrugInjectingAtFirt.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherDrugInjectingAtFirt.setVisibility(View.VISIBLE);
                        edt_otherDrugInjectingAtFirt.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherDrugInjectingAtFirt.setVisibility(View.INVISIBLE);
                        edt_otherDrugInjectingAtFirt.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tsyringetype")){
            sp_SyringeType.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_SyringePlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_SyringePlace.getSelectedItem().toString().equals("Other")+"");

                    if (sp_SyringePlace.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherSyringePlace.setVisibility(View.VISIBLE);
                        edt_otherSyringePlace.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherSyringePlace.setVisibility(View.INVISIBLE);
                        edt_otherSyringePlace.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tsyringeplace")){
            sp_SyringePlace.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
        }
        else if(lvname.equals("pm.tinjectedby")){
            sp_InjectedBy.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_InjectedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_InjectedBy.getSelectedItem().toString().equals("Other")+"");

                    if (sp_InjectedBy.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherInjectedBy.setVisibility(View.VISIBLE);
                        edt_otherInjectedBy.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherInjectedBy.setVisibility(View.INVISIBLE);
                        edt_otherInjectedBy.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        else if(lvname.equals("pm.ttreatmentby")){
            sp_TreatmentBy.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_TreatmentBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_TreatmentBy.getSelectedItem().toString().equals("Other")+"");

                    if (sp_TreatmentBy.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherTreatmentBy.setVisibility(View.VISIBLE);
                        edt_otherTreatmentBy.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherTreatmentBy.setVisibility(View.INVISIBLE);
                        edt_otherTreatmentBy.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else if(lvname.equals("pm.tdisease")){
            sp_HealthDiseases.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_HealthDiseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_HealthDiseases.getSelectedItem().toString().equals("Other")+"");

                    if (sp_HealthDiseases.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherHealthDiseases.setVisibility(View.VISIBLE);
                        edt_otherHealthDiseases.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherHealthDiseases.setVisibility(View.INVISIBLE);
                        edt_otherHealthDiseases.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp_FamilyDiseases.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_FamilyDiseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_FamilyDiseases.getSelectedItem().toString().equals("Other")+"");

                    if (sp_FamilyDiseases.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherFamilyDiseases.setVisibility(View.VISIBLE);
                        edt_otherFamilyDiseases.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherFamilyDiseases.setVisibility(View.INVISIBLE);
                        edt_otherFamilyDiseases.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        else if(lvname.equals("pm.tcontactperson")){
            sp_ContactPerson.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_ContactPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_ContactPerson.getSelectedItem().toString().equals("Other")+"");

                    if (sp_ContactPerson.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherContactPerson.setVisibility(View.VISIBLE);
                        edt_otherContactPerson.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherContactPerson.setVisibility(View.INVISIBLE);
                        edt_otherContactPerson.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp_LastContactPerson.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_LastContactPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_LastContactPerson.getSelectedItem().toString().equals("Other")+"");

                    if (sp_LastContactPerson.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherLastContactPerson.setVisibility(View.VISIBLE);
                        edt_otherLastContactPerson.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherLastContactPerson.setVisibility(View.INVISIBLE);
                        edt_otherLastContactPerson.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        else if(lvname.equals("pm.tinformationsource")){
            sp_Informationsource.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_Informationsource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_Informationsource.getSelectedItem().toString().equals("Other")+"");

                    if (sp_Informationsource.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherInformationsource.setVisibility(View.VISIBLE);
                        edt_otherInformationsource.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherInformationsource.setVisibility(View.INVISIBLE);
                        edt_otherInformationsource.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp_Infromationchannel.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_Infromationchannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_Infromationchannel.getSelectedItem().toString().equals("Other")+"");

                    if (sp_Infromationchannel.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherInfromationchannel.setVisibility(View.VISIBLE);
                        edt_otherInfromationchannel.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherInfromationchannel.setVisibility(View.INVISIBLE);
                        edt_otherInfromationchannel.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        else if(lvname.equals("hrm.trelation")){
            sp_FamilyRelation.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));

            sp_FamilyRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("try", sp_FamilyRelation.getSelectedItem().toString().equals("Other")+"");

                    if (sp_FamilyRelation.getSelectedItem().toString().equals("Other")){
                        Log.e("try", "AA");
                        tv_otherFamilyRelation.setVisibility(View.VISIBLE);
                        edt_otherFamilyRelation.setVisibility(View.VISIBLE);

                    }else {
                        Log.e("try", "BB");
                        tv_otherFamilyRelation.setVisibility(View.INVISIBLE);
                        edt_otherFamilyRelation.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else if(lvname.equals("pm.thealthfacilitycenter")){
            sp_ReferedCenter.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_ReferedCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    freferedhealthfacilitycenterid=lvHasmap.get("fid").get(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }else if (lvname.equals("pm.tserviceprovider")){
            sp_ReferedBy.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lvHasmap.get("fname")));
            sp_ReferedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    freferedbyserviceproviderid= lvHasmap.get("fid").get(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }

    @Override
    public void onPostExecuteGetData(Vector<HashMap> result, String activity) {
        if(activity.equals("1")){
            final ArrayList<String> fname= new ArrayList<>();
            final ArrayList<String> fid= new ArrayList<>();

            for(int i=0;i<result.size();i++){
                fname.add(result.get(i).get("fname").toString());
                fid.add(result.get(i).get("fid").toString());
            }
            ArrayAdapter<String> adap=new ArrayAdapter<String>(MainForm.this,android.R.layout.simple_spinner_dropdown_item,fname);
            sp_OriginVillage.setAdapter(adap);
            sp_OriginVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    executeGetdataService(MainForm.this,"SELECT * FROM public.tcity where fdistrictid="+fid.get(i)+"","3");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }else if(activity.equals("2")){

            final ArrayList<String> fname= new ArrayList<>();
            final ArrayList<String> fid= new ArrayList<>();

            for(int i=0;i<result.size();i++){
                fname.add(result.get(i).get("fname").toString());
                fid.add(result.get(i).get("fid").toString());
            }
            ArrayAdapter<String> adap=new ArrayAdapter<String>(MainForm.this,android.R.layout.simple_spinner_dropdown_item,fname);
            sp_CurrentVillage.setAdapter(adap);
            sp_CurrentVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    executeGetdataService(MainForm.this,"SELECT * FROM public.tcity where fdistrictid="+fid.get(i)+"","4");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }else if(activity.equals("3")){

            final ArrayList<String> fname= new ArrayList<>();
            final ArrayList<String> fid= new ArrayList<>();

            for(int i=0;i<result.size();i++){
                fname.add(result.get(i).get("fname").toString());
                fid.add(result.get(i).get("fid").toString());
            }
            ArrayAdapter<String> adap=new ArrayAdapter<String>(MainForm.this,android.R.layout.simple_spinner_dropdown_item,fname);
            sp_OriginProvince.setAdapter(adap);

        }else if(activity.equals("4")){

            final ArrayList<String> fname= new ArrayList<>();
            final ArrayList<String> fid= new ArrayList<>();

            for(int i=0;i<result.size();i++){
                fname.add(result.get(i).get("fname").toString());
                fid.add(result.get(i).get("fid").toString());
            }
            ArrayAdapter<String> adap=new ArrayAdapter<String>(MainForm.this,android.R.layout.simple_spinner_dropdown_item,fname);
            sp_CurrentProvince.setAdapter(adap);
        }else if (activity.equals("5")){

            final ArrayList<String> fname= new ArrayList<>();
            final ArrayList<String> fid= new ArrayList<>();
            fname.add("");
            fid.add("");
            for(int i=0;i<result.size();i++){
                fname.add(result.get(i).get("fname").toString());
                fid.add(result.get(i).get("fid").toString());
            }

            ArrayAdapter<String> adap=new ArrayAdapter<String>(MainForm.this,android.R.layout.simple_spinner_dropdown_item,fname);
            sp_NGO.setAdapter(adap);
            sp_NGO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fhealthfacilitycenterid=fid.get(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });


        }
    }

    private boolean checkValidation(){
        if (sp_NGO.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select center", Toast.LENGTH_SHORT).show();
            sp_NGO.requestFocus();
            return true;
        }
        if (edt_ServiceCode.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter center code", Toast.LENGTH_SHORT).show();
            edt_ServiceCode.requestFocus();
            return true;
        }
        if (sp_City.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select center city", Toast.LENGTH_SHORT).show();
            sp_City.requestFocus();
            return true;
        }
        if (edt_ReferralDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Refferral Date", Toast.LENGTH_SHORT).show();
            edt_ReferralDate.requestFocus();
            return true;
        }
        if (sp_ServiceType.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select service type", Toast.LENGTH_SHORT).show();
            sp_ServiceType.requestFocus();
            return true;
        }
        if (sp_SiteOfServices.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select service", Toast.LENGTH_SHORT).show();
            sp_SiteOfServices.requestFocus();
            return true;
        }

        if (edt_Name.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            edt_Name.requestFocus();
            return true;
        }
        if (edt_FatherName.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter father/husband name", Toast.LENGTH_SHORT).show();
            edt_FatherName.requestFocus();
            return true;
        }
        if (edt_GeneralCode.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter code", Toast.LENGTH_SHORT).show();
            edt_GeneralCode.requestFocus();
            return true;
        }
        if (edt_Age.getText().toString() == "0"){
            Toast.makeText(this, "Enter age", Toast.LENGTH_SHORT).show();
            edt_Age.requestFocus();
            return true;
        }

        if (sp_Sex.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            sp_Sex.requestFocus();
            return true;
        }

        if (sp_Ethnicity.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select ethnicity", Toast.LENGTH_SHORT).show();
            sp_Ethnicity.requestFocus();
            return true;
        }

        if (sp_OriginDistrict.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
            sp_OriginDistrict.requestFocus();
            return true;
        }
        if (sp_CurrentDistrict.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
            sp_CurrentDistrict.requestFocus();
            return true;
        }
/*		if (m_cmbEducation.getValue() == null){
			CWNotification.showNotification("Select education", CWNotification.TYPE_ERROR_MESSAGE);
			m_cmbEducation.focus();
			return true;
		}
*/
        if (edt_IncomeAMT.getText().toString() == "0"){
            Toast.makeText(this, "Enter income", Toast.LENGTH_SHORT).show();
            edt_IncomeAMT.requestFocus();
            return true;
        }

        if (sp_MaritalStatus.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select marital status", Toast.LENGTH_SHORT).show();
            sp_MaritalStatus.requestFocus();
            return true;
        }
        if (sp_DrugUsingLocation.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select Location", Toast.LENGTH_SHORT).show();
            sp_DrugUsingLocation.requestFocus();
            return true;
        }

        if (sp_WhereStayAtNight.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select where u stay at night", Toast.LENGTH_SHORT).show();
            sp_WhereStayAtNight.requestFocus();
            return true;
        }

/*		if (m_opDrugPlace.getSelectedItemValue("fid").toString().equals("1")){
			if (m_cmbDrugProvince.getValue() == null){
				CWNotification.showNotification("Select where u started using drugs", CWNotification.TYPE_ERROR_MESSAGE);
				m_cmbDrugProvince.focus();
				return true;
			}
		} else {
			if (m_cmbDrugConrty.getValue() == null){
				CWNotification.showNotification("Select where u started using drugs", CWNotification.TYPE_ERROR_MESSAGE);
				m_cmbDrugConrty.focus();
				return true;
			}
		}
*/
        if (rdo_YesInjectedDrug.isSelected()) {

            if (rdo_YesInjectedDrugLast3Month.isSelected() && edt_InjectedDrugReason.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter what kind of drug have you injected ", Toast.LENGTH_SHORT).show();
                edt_InjectedDrugReason.requestFocus();
                return true;
            }else if (rdo_NoInjectedDrugLast3Month.isSelected() && edt_InjectedDrugReason.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter why you stopped injecting drug ", Toast.LENGTH_SHORT).show();
                edt_InjectedDrugReason.requestFocus();
                return true;
            }

            if (sp_DrugInjectingAtFirt.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select reason of drug injecting at first", Toast.LENGTH_SHORT).show();
                sp_DrugInjectingAtFirt.requestFocus();
                return true;
            }

            if (sp_SyringeType.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select syringe you used at last time", Toast.LENGTH_SHORT).show();
                sp_SyringeType.requestFocus();
                return true;
            }

            if (sp_SyringePlace.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select from where you found last syringe", Toast.LENGTH_SHORT).show();
                sp_SyringePlace.requestFocus();
                return true;
            }

            if (sp_InjectedBy.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select who perform injection", Toast.LENGTH_SHORT).show();
                sp_InjectedBy.requestFocus();
                return true;
            }
        }

        if (rdo_YesSexualContact.isSelected()){
            if (sp_ContactPerson.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select with who you had sexual contact", Toast.LENGTH_SHORT).show();
                sp_ContactPerson.requestFocus();
                return true;
            }

            if (edt_YearSexualContact.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter when was your sexual contact last time", Toast.LENGTH_SHORT).show();
                edt_YearSexualContact.requestFocus();
                return true;
            }
            if (spn_YearSexualContact.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select when was your sexual contact last time", Toast.LENGTH_SHORT).show();
                spn_YearSexualContact.requestFocus();
                return true;
            }

            if (sp_LastContactPerson.getSelectedItem().toString() == ""){
                Toast.makeText(this, "Select with who you had sexual contact last time", Toast.LENGTH_SHORT).show();
                sp_LastContactPerson.requestFocus();
                return true;
            }
        }

        if (rdo_YesHeardAboutCondom.isSelected() && sp_Informationsource.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select from where you heard about condom use", Toast.LENGTH_SHORT).show();
            sp_Informationsource.requestFocus();
            return true;
        }

        if (rdo_YesInfoAboutHIV.isSelected() && sp_Infromationchannel.getSelectedItem().toString() == "") {
            Toast.makeText(this, "Select from where you have got information about hiv", Toast.LENGTH_SHORT).show();
            sp_Infromationchannel.requestFocus();
            return true;
        }

        if (sp_familyBehaviour.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select how is family behaviour with you", Toast.LENGTH_SHORT).show();
            sp_familyBehaviour.requestFocus();
            return true;
        }

        if (rdo_YesFamilyMemberUsingDrug.isSelected() && sp_FamilyRelation.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select which of your family member using drug", Toast.LENGTH_SHORT).show();
            rdo_YesFamilyMemberUsingDrug.requestFocus();
            return true;
        }
        if (sp_ReferedCenter.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select Refered Center", Toast.LENGTH_SHORT).show();
            sp_ReferedCenter.requestFocus();
            return true;
        }

        if (sp_ReferedService.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select Refered Service", Toast.LENGTH_SHORT).show();
            sp_ReferedService.requestFocus();
            return true;
        }
        if (sp_ReferedBy.getSelectedItem().toString() == ""){
            Toast.makeText(this, "Select Refered By", Toast.LENGTH_SHORT).show();
            sp_ReferedBy.requestFocus();
            return true;
        }


        return false;
    }
    private void onSave(){
        if (checkValidation())
            return;
        String fcentercode,fname,fdate,ffatherorhusbandname,fcode,fincomesource,fnightstayother,fcurrentlyusedrugother,fdrugusemethodother,fdrugusingperiod,
                fdailyexpensefordrug,fdailydrugtime,ftreatmentbyother,fdiseaseother,ffamilybehaviour,fmemberdiseaseother,fpreparedby,fpreparerdesignation,fremark
                ,fincome, fiseverinjecteddrug,fiseversoldyourblood,fiseverhadsexualcontact,fiseverheardaboutcondom,fisusecondom,fisusecondomatlasttime,
                fissyringsharingcausedisease,fisinformationabouthiv,fisinformationabouthbv,fisinformationabouthcv,fisanymemilymemberusingdrug,fdrugusinglocationother;


        fage=edt_Age.getText().toString();
        fwhenlasttimebloodsold=edt_HowLongSoldBlood.getText().toString();

        fcentercode=edt_ServiceCode.getText().toString();
        fdate=edt_ReferralDate.getText().toString();

        fname=edt_Name.getText().toString();
        ffatherorhusbandname=edt_FatherName.getText().toString();
        fcode=edt_GeneralCode.getText().toString();
        fincomesource=edt_incomeSRC.getText().toString();
        fnightstayother=edt_otherWhereStayAtNight.getText().toString();
        fcurrentlyusedrugother=edt_otherCurruntUsingDrug.getText().toString();
        fdrugusemethodother=edt_otherMethodUseDrug.getText().toString();
        fdrugusingperiod=edt_HistoryYear.getText().toString();
        fdailydrugtime=edt_DailyDrugUsingTime.getText().toString();
        fdailyexpensefordrug=edt_DatlyExp.getText().toString();
        ftreatmentbyother=edt_otherTreatmentBy.getText().toString();
        fdiseaseother=edt_otherHealthDiseases.getText().toString();
        fmemberdiseaseother=edt_otherFamilyDiseases.getText().toString();
        fpreparedby=edt_NameOfPreparer.getText().toString();
        fpreparerdesignation=edt_DesugnationOfPreparer.getText().toString();
        fremark=edt_Remark.getText().toString();
        ffamilybehaviour=sp_familyBehaviour.getSelectedItem().toString();
        fincome=edt_IncomeAMT.getText().toString();
        fiseverinjecteddrug=rdo_YesInjectedDrug.isChecked()+"";
        fiseversoldyourblood=rdo_YesSoldBlood.isChecked()+"";
        fiseverhadsexualcontact=rdo_YesSexualContact.isChecked()+"";
        fiseverheardaboutcondom=rdo_YesHeardAboutCondom.isChecked()+"";
        fisusecondom=rdo_YesUseCondom.isChecked()+"";
        fisusecondomatlasttime=rdo_YesLastTimeUseCondom.isChecked()+"";
        fissyringsharingcausedisease=rdo_YesDoNeedleSyringe.isChecked()+"";
        fisinformationabouthiv=rdo_YesInfoAboutHIV.isChecked()+"";
        fisinformationabouthbv=rdo_YesInfoAboutHBV.isChecked()+"";
        fisinformationabouthcv=rdo_YesInfoAboutHCV.isChecked()+"";
        fisanymemilymemberusingdrug=rdo_YesFamilyMemberUsingDrug.isChecked()+"";
        fdrugusinglocationother=edt_otherDrugUsingLocation.getText().toString();

        String tt;
        tt="<Data>\n"+
                "<tregdata>\n"
                + "<Record>\n" +
                "       <fhealthfacilitycenterid>int:"+fhealthfacilitycenterid+"</fhealthfacilitycenterid>\n" +
                "         <fcentercode>"+fcentercode+"</fcentercode>\n" +
                "         <fcityid>int:"+fcityid+"</fcityid>\n" +
                "         <fdate>date:"+fdate+"</fdate>\n" +
                "         <fservicetypeid>int:"+fservicetypeid+"</fservicetypeid>\n" +
                "         <fserviceid>int:"+fserviceid+"</fserviceid>\n" +
                "         <fname>"+fname+"</fname>\n" +
                "         <ffatherorhusbandname>"+ffatherorhusbandname+"</ffatherorhusbandname>\n" +
                "         <fcode>"+fcode+"</fcode>\n" +
                "         <fage>int:"+fage+"</fage>\n" +
                "         <fgenderid>int:"+fgenderid+"</fgenderid>\n" +
                "         <fethnicityid>int:"+fethnicityid+"</fethnicityid>\n" +
                "         <fprovinceid>int:"+fprovinceid+"</fprovinceid>\n" +
                "         <fcurrentprovinceid>int:"+fcurrentprovinceid+"</fcurrentprovinceid>\n" +
                "         <fincome>num:"+fincome+"</fincome>\n" +
                "         <fincomesource>"+fincomesource+"</fincomesource>\n" +
                "         <fmaritalstatusid>int:"+fmaritalstatusid+"</fmaritalstatusid>\n" +
                "         <fdrugusinglocationid>int:"+fdrugusinglocationid+"</fdrugusinglocationid>\n" +
                "         <fdrugusinglocationother>null</fdrugusinglocationother>\n" +
                "         <fnightstayid>int:"+fnightstayid+"</fnightstayid>\n" +
                "         <fnightstayother>"+fnightstayother+"</fnightstayother>\n" +
                "         <fcurrentlyusedrugother>"+fcurrentlyusedrugother+"</fcurrentlyusedrugother>\n" +
                "         <fdrugusemethodother>"+fdrugusemethodother+"</fdrugusemethodother>\n" +
                "         <fdrugusingperiod>"+fdrugusingperiod+"</fdrugusingperiod>\n" +
                "         <fdailyexpensefordrug>num:"+fdailyexpensefordrug+"</fdailyexpensefordrug>\n" +
                "         <fdailydrugtime>"+fdailydrugtime+"</fdailydrugtime>\n" +
                "         <fiseverinjecteddrug>bool:"+fiseverinjecteddrug+"</fiseverinjecteddrug>\n" +
                "         <ftreatmentbyother>"+ftreatmentbyother+"</ftreatmentbyother>\n" +
                "         <fdiseaseother>"+fdiseaseother+"</fdiseaseother>\n" +
                "         <fiseversoldyourblood>bool:"+fiseversoldyourblood+"</fiseversoldyourblood>\n" +
                "         <fwhenlasttimebloodsold>int:"+fwhenlasttimebloodsold+"</fwhenlasttimebloodsold>\n" +
                "         <fiseverhadsexualcontact>bool:"+fiseverhadsexualcontact+"</fiseverhadsexualcontact>\n" +
                "         <fiseverheardaboutcondom>bool:"+fiseverheardaboutcondom+"</fiseverheardaboutcondom>\n" +
                "         <fisusecondom>bool:"+fisusecondom+"</fisusecondom>\n" +
                "         <fisusecondomatlasttime>bool:"+fisusecondomatlasttime+"</fisusecondomatlasttime>\n" +
                "         <fissyringsharingcausedisease>bool:"+fissyringsharingcausedisease+"</fissyringsharingcausedisease>\n" +
                "         <fisinformationabouthiv>bool:"+fisinformationabouthiv+"</fisinformationabouthiv>\n" +
                "         <fhivtransmissionway1>"+edt_FirstWayOfTransmission.getText().toString()+"</fhivtransmissionway1>\n" +
                "         <fhivtransmissionway2>"+edt_SecondWayOfTransmission.getText().toString()+"</fhivtransmissionway2>\n" +
                "         <fhivtransmissionway3>"+edt_ThirdWayOfTransmission.getText().toString()+"</fhivtransmissionway3>\n" +
                "         <fisinformationabouthbv>bool:"+fisinformationabouthbv+"</fisinformationabouthbv>\n" +
                "         <fisinformationabouthcv>bool:"+fisinformationabouthcv+"</fisinformationabouthcv>\n" +
                "         <ffamilybehaviour>"+ffamilybehaviour+"</ffamilybehaviour>\n" +
                "         <fisanymemilymemberusingdrug>bool:"+fisanymemilymemberusingdrug+"</fisanymemilymemberusingdrug>\n" +
                "         <fmemberdiseaseother>"+fmemberdiseaseother+"</fmemberdiseaseother>\n" +
                "         <freferedservicetypeid>int:"+freferedservicetypeid+"</freferedservicetypeid>\n" +
                "         <fpreparedby>"+fpreparedby+"</fpreparedby>\n" +
                "         <fpreparerdesignation>"+fpreparerdesignation+"</fpreparerdesignation>\n" +
                "         <freferedhealthfacilitycenterid>int:"+freferedhealthfacilitycenterid+"</freferedhealthfacilitycenterid>\n" +
                "         <freferedbyserviceproviderid>int:"+freferedbyserviceproviderid+"</freferedbyserviceproviderid>\n" +
                "         <fremark>"+fremark+"</fremark>\n" +
                "         </Record>\n" +
                "	</tregdata>"+
                "       </Data>";
        Log.e("XML",tt );
        new SaveReceipt().execute(tt);
    }
    class SaveReceipt extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar1 = new ProgressDialog(MainForm.this);
            progressBar1.setTitle("Progress....");
            progressBar1.setMessage("Please Wait...");
            progressBar1.setProgressStyle(TRIM_MEMORY_RUNNING_LOW);
            progressBar1.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String datap=params[0];
            WebService mAuth = new WebService();
            String data1 = mAuth.invokeNACP("registerPatient", datap);
            return data1;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            Log.e("What Is Result", s);
            if(s=="Error occured" || s.equals("Error occured"))
            {
                Toast.makeText(MainForm.this, ""+s, Toast.LENGTH_SHORT).show();
            }
            else if(s.startsWith("f"))
            {
                Toast.makeText(MainForm.this, ""+s, Toast.LENGTH_SHORT).show();
            }
            else {

                AlertDialog.Builder builder= new AlertDialog.Builder(MainForm.this);
                builder.setTitle("Registration No Is :- "+s);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("Print Reg. No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent in = getPackageManager().getLaunchIntentForPackage("com.epson.epos2_printer");
                        in.putExtra("regno",s);
                        in.putExtra("center", sp_NGO.getSelectedItem().toString()+" ,"+sp_City.getSelectedItem().toString());
                        in.putExtra("date", edt_ReferralDate.getText().toString());
                        in.putExtra("servicetype", sp_ServiceType.getSelectedItem().toString());
                        in.putExtra("service", sp_SiteOfServices.getSelectedItem().toString());


                        startActivity(in);

                    }
                });
                builder.show();

                progressBar1.dismiss();
            }

            progressBar1.dismiss();

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
                        MainForm.this.runOnUiThread(new Runnable() {
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