package cisc181.caterpillarcrawl;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static String color;
    static Intent startI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startI = getIntent();
        color = startI.getStringExtra("color");
        setContentView(R.layout.activity_main);
        View background = this.getWindow().getDecorView();
        background.setBackgroundColor(Color.GREEN);
    }
    //Gets called from MyAnimatedSurfaceView
    public void setResult1(int r, Intent i){
        setResult(r,i);
    }

}
