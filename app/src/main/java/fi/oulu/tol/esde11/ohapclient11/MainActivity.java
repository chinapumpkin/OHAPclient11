package fi.oulu.tol.esde11.ohapclient11;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.opimobi.ohap.CentralUnit;
import com.opimobi.ohap.Container;
import com.opimobi.ohap.Device;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CentralUnit centralUnit = null;

        try {
            centralUnit = new CentralUnit(new URL("http://ohap.opimobi.com:8080/")) {
                @Override
                protected void listeningStateChanged(Container container, boolean listening) {

                }
            };
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }






        centralUnit.setName("OHAP Test Server");
        Device device = new Device(centralUnit, 1);
        device.setName("Ceiling Lamp");
        device.setDescription("the lamp in the dinner room");
        TextView pathText=(TextView)findViewById(R.id.textView1);
        TextView nameText = (TextView)findViewById(R.id.textView2);
        TextView descriptiontext=(TextView)findViewById(R.id.textView3);
        nameText.setText(device.getName());
        descriptiontext.setText(device.getDescription());
        pathText.setText(device.getCentralUnit().getName()+">"+device.getParent().getName());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
