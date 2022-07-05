package com.example.emi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    double EMI;
    int term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CalculateEmi(View view) {
        try {
            double downPayment = Double.parseDouble(((EditText)findViewById(R.id.DownPayment_amount)).getText().toString());
            double principleAmount = Double.parseDouble(((EditText)findViewById(R.id.Principal_amount)).getText().toString());
            double ROI = Double.parseDouble(((EditText)findViewById(R.id.Interest_rate)).getText().toString())/12.0/100.0;
             term = Integer.parseInt(((EditText)findViewById(R.id.Loan_term)).getText().toString());
            String debugStr = "downPayment : " + downPayment + ", principleAmount : " + principleAmount + ", ROI" + ROI + ", term : " + term;
            System.out.println(debugStr);
            if (downPayment >= principleAmount) {
                Toast.makeText(getBaseContext(), "DownPayment should be less than principal amount", Toast.LENGTH_LONG).show();
                return;
            }else {
                principleAmount -= downPayment;
            }

            // e = p * (r * ((1 + r) ^ n)) / ((1 + r)^n - 1)
            double rt=Math.pow((1 + ROI), term);
            EMI = ((principleAmount * ROI * rt) / (rt - 1));
            ((TextView)findViewById(R.id.EMI)).setText("₹" + String.format("%.4f", EMI));
            ((Button)findViewById(R.id.SetReminder)).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Enter valid values", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void SetReminder(View view) {
        System.out.println("SetReminder");
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "PAY EMI ")
                .putExtra(CalendarContract.Events.DESCRIPTION, "₹" + String.format("%.4f", EMI)+" must be paid")
                .putExtra("rrule","FREQ=MONTHLY;COUNT="+term);
        startActivity(intent);


    }
}