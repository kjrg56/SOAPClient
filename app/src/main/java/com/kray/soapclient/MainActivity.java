package com.kray.soapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private EditText txtNum1;
    private EditText txtNum2;
    private Button btnSumar;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNum1 = (EditText) findViewById(R.id.txtNum1);
        txtNum2 = (EditText) findViewById(R.id.txtNum2);
        txtResultado = (TextView) findViewById(R.id.lblResultado);
        btnSumar = (Button) findViewById(R.id.btnSumar);

    }

    public void sumar(View view) {
        OperacionSoap service = new OperacionSoap();
        service.execute(txtNum1.getText().toString().trim(), txtNum2.getText().toString().trim());
    }

    private class OperacionSoap extends AsyncTask<String, String, String> {

        static final String NAMESPACE = "http://soap.kray.com/";
        static final String METHODNAME = "Sumar";
        static final String URL = "http://192.168.1.3:8080/Operations/Operations?WSDL";
        static final String SOAP_ACTION = NAMESPACE + METHODNAME;

        private String soapResponse;

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            soapResponse = "";
            SoapObject request = new SoapObject(NAMESPACE, METHODNAME);
            request.addProperty("num1", params[0]);
            request.addProperty("num2", params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive envelopeResponse = (SoapPrimitive) envelope.getResponse();
                response = envelopeResponse.toString();
            } catch (Exception ex) {
                Log.d("SOAP Exception", ex.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setSoapResponse(s);
            txtResultado.setText(s);
            Log.d("SOAP Response", soapResponse);
        }

        public String getSoapResponse() {
            return soapResponse;
        }

        public void setSoapResponse(String soapResponse) {
            this.soapResponse = soapResponse;
        }

    }

}