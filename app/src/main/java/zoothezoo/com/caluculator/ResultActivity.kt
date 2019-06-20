package zoothezoo.com.caluculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val result = findViewById<TextView>(R.id.resultText)
        result.text = "0"

        val ans = intent.getStringExtra("ans" )



        result.text = ans

    }

}
