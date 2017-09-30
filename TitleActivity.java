package cisc181.caterpillarcrawl;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static cisc181.caterpillarcrawl.MainActivity.startI;

public class TitleActivity extends AppCompatActivity {
    Spinner spin;
    static final int GET_SUMMARY_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        TextView title = (TextView) findViewById(R.id.title);
        View background = this.getWindow().getDecorView();
        background.setBackgroundColor(Color.WHITE);
        title.setTextColor(Color.RED);
        title.setTextSize(25);
        //Player picks caterpillar color
        spin = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("Blue");
        list.add("Gray");
        list.add("Magenta");
        list.add("Red");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        ArrayList<String> inst = new ArrayList<>();
        //Reads in instructions
        InputStream input = getResources().openRawResource(R.raw.instructions);
        Scanner scnr = new Scanner(input);
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            inst.add(line);
        }
        TextView instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText(inst.get(0));
        instructions.setTextColor(Color.RED);
        instructions.setTextSize(45);
    }
    public void start(View V){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("color",spin.getSelectedItem().toString());
        startActivityForResult(intent,GET_SUMMARY_RESULT);
    }
    public void quit(View V){
        finish();
    }
    //When game ends, player score and high score are shown
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        TextView tv = (TextView) findViewById(R.id.my_summary_text);
        tv.setTextSize(25);
        int score = startI.getIntExtra("score",0);
        int hScore = startI.getIntExtra("hScore",0);
        tv.setText("             Game Over!\n"+"Your score: " + Integer.toString(score) + " High Score: " + Integer.toString(hScore));

    }
}
