package zoothezoo.com.caluculor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var iptotal = findViewById<EditText>(R.id.ipTotal)
        var ipmen = findViewById<EditText>(R.id.ipMen)
        var ipwomen = findViewById<EditText>(R.id.ipWomen)

        var button = findViewById<Button>(R.id.doit)


        button.setOnClickListener{
            var total = iptotal.text.toString().toInt()
            var men = ipmen.text.toString().toInt()
            var women = ipwomen.text.toString().toInt()
            var ans = total / (men + women)
            Log.d("ans","answer is $ans")
        }
    }
}
