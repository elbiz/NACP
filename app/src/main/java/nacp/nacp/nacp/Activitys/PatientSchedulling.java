package nacp.nacp.nacp.Activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import nacp.nacp.nacp.R;
import nacp.nacp.nacp.WebService.GetDataPostAction;

public class PatientSchedulling extends GetDataPostAction {

    private SearchView search_PatientId;

    private TextView tv_patientName,tv_patientAge,tv_patientGender,tv_patientMaritalStatus,tv_patientCode;
    private Spinner sp_ServiceType,sp_SiteOfServices,sp_ReferedService;
    private EditText edt_ReferralDate,edt_NoofClients,edt_ParticipantsRelation,edt_InformationAssessment,edt_RiskAssessment,edt_DiscussionIssue,
            edt_RiskReduction,edt_ConsentTest,edt_NoOfDistributedIEC,edt_Remark;
    private ImageView imv_serviceCalender;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    Calendar newCalendar = Calendar.getInstance();
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
}
