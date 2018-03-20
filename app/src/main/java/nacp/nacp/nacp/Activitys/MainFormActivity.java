package nacp.nacp.nacp.Activitys;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import nacp.nacp.nacp.LocalDb.DatabaseHandler;
import nacp.nacp.nacp.R;

public class MainFormActivity extends AppCompatActivity {

    String[] web = {
            "Patient Form",
            "Patient Scheduling"

    };
    int[] imageId = {
            R.drawable.logo,
            R.drawable.shadule
    };
    private GridView gv;
    CustomGrid1 adap;
    LinearLayout linLayout;
    private DatabaseHandler db;
    private String username;

    Boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);
        android.support.v7.app.ActionBar ab= getSupportActionBar();
        ab.setTitle("Dashboard");
        gv= (GridView)findViewById(R.id.gridDashboard);
        linLayout= (LinearLayout) findViewById(R.id.linLayout);
        db= new DatabaseHandler(this);

        Cursor rs = db.getData(1);
        rs.moveToFirst();
        username=rs.getString(rs.getColumnIndex("fuser"));
        adap = new CustomGrid1(this, web, imageId);
        gv.setAdapter(adap);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in;
                switch (position) {
                    case 0:
                        in=new Intent(MainFormActivity.this, MainForm.class);
                        in.putExtra("user",username);                        startActivity(in);
                        break;
                    case 1:
                        in=new Intent(MainFormActivity.this, PatientSchedulling.class);
                        in.putExtra("user",username);
                        startActivity(in);
                        break;

                    default:
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomGrid1 extends BaseAdapter {
        private Context mContext;
        private final String[] web;
        private final int[] Imageid;

        public CustomGrid1(Context c,String[] web,int[] Imageid ) {
            mContext = c;
            this.Imageid = Imageid;
            this.web = web;
        }

        @Override
        public int getCount() {

            return web.length;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_layout, null);
                TextView textView = (TextView) grid.findViewById(R.id.grid_text);
                LinearLayout layoutGridview= (LinearLayout) grid.findViewById(R.id.layoutGridview);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

                textView.setText(web[position]);
                imageView.setImageResource(Imageid[position]);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(linLayout, "Press BACK again to exit NACP App", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
