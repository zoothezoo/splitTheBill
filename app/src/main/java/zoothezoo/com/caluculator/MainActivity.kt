package zoothezoo.com.caluculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //input
        var iptotal = findViewById<EditText>(R.id.ipTotal)
        var ipmen = findViewById<EditText>(R.id.ipMen)
        var ipwomen = findViewById<EditText>(R.id.ipWomen)
        var ipsenior = findViewById<EditText>(R.id.ipSenior)
        var ipjunior = findViewById<EditText>(R.id.ipJunior)
        var ipmiddle = findViewById<EditText>(R.id.ipMiddle)

        //output
        var conclusion = findViewById<TextView>(R.id.conclusion)

        //button
        var btdoit = findViewById<Button>(R.id.btDoit)
        var btclear = findViewById<Button>(R.id.btClear)

        //checkbox
        var cbgender = findViewById<CheckBox>(R.id.cbGender)
        var cbgrade = findViewById<CheckBox>(R.id.cbGrade)

        //variable
        var total = 0
        var men = 0
        var women = 0
        var senior = 0
        var junior = 0
        var middle = 0
        var ans = ""

        //checkboxの操作
        //TODO　チェックが入っていないときEdittextの入力を禁止する.
        cbgender.setOnClickListener{
            if(cbgender.isChecked()){
                cbgrade.isChecked = false
                ipsenior.setText("")
                ipmiddle.setText("")
                ipjunior.setText("")
            }
        }

        cbgrade.setOnClickListener {
            if(cbgrade.isChecked()){
                cbgender.isChecked = false
                ipmen.setText("")
                ipwomen.setText("")
            }
        }


        btdoit.setOnClickListener{
            total = iptotal.text.toString().toInt()
            if(cbgender.isChecked) {
                men = ipmen.text.toString().toInt()
                women = ipwomen.text.toString().toInt()
            }
            if(cbgrade.isChecked) {
                senior = ipsenior.text.toString().toInt()
                middle = ipmiddle.text.toString().toInt()
                junior = ipjunior.text.toString().toInt()
            }
            var diff = 1000
            var ave1 = 10000
            var ave2 = 0


            //TODO 答えをリストに入れてけば楽になるかも.
            when{
                cbgender.isChecked -> {
                    if(men != 0 && women != 0) {
                        ave1 = total / (men + women)
                        ave2 = (total - ave1 * men) / women
                        while (Math.abs((ave1 - ave2) - diff) > 200) {
                            ave1 += 50
                            ave2 = (total - ave1 * men) / women
                        }
                    }
                    else if(men == 0){
                        ave1 = 0
                        ave2 = total / women
                    }
                    else{
                        ave1 = total / men
                        ave2 = 0
                    }

                    var paymen = ave1 / 100 * 100
                    var paywomen = ave2 / 100 * 100
                    var lack = total - (paymen * men + paywomen * women)

                    ans = "men: ${paymen}/ women: ${paywomen}/ lack: $lack"
                }
                cbgrade.isChecked -> {
                }
            }
            conclusion.setText(ans)
        }

        btclear.setOnClickListener{
            total = 0
            men = 0
            women = 0
            senior = 0
            junior = 0
            middle = 0
            ans = ""

            iptotal.setText("")
            ipmen.setText("")
            ipwomen.setText("")
            ipsenior.setText("")
            ipmiddle.setText("")
            ipjunior.setText("")

        }
    }
}


//TODO 10000,93,5 の時に負が出る.
//TODO lackをルーレットで誰に払わせるか.
//TODO lack or redundant
