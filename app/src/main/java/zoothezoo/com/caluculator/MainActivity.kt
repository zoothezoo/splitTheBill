package zoothezoo.com.caluculator

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity(){

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
        var ipdiff = findViewById<EditText>(R.id.ipDiff)

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
        var diff = 0

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
            if(!cbgrade.isChecked && !cbgender.isChecked){
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Select The Mode")
                    setPositiveButton("Yes", null)
                    setNegativeButton("Ok", null)
                    show()
                }
            }

            total = if (iptotal.text.toString() == "") 0 else iptotal.text.toString().toInt()
            val result1: Result<Unit> = runCatching {
                val e = 100 / total
            }.onSuccess {  }
                .onFailure {
                    AlertDialog.Builder(this).apply {
                        setTitle("Error")
                        setMessage("Input Total")
                        setPositiveButton("Yes", null)
                        setNegativeButton("Ok", null)
                        show()
                    }
                }
            diff = if (ipdiff.text.toString() == "") 0 else ipdiff.text.toString().toInt()
            if(cbgender.isChecked) {
                men = if (ipmen.text.toString() == "") 0 else ipmen.text.toString().toInt()
                women = if (ipwomen.text.toString() == "") 0 else ipwomen.text.toString().toInt()
                if (men == 0){
                    ipmen.setText("0")
                }
                if (women == 0){
                    ipwomen.setText("0")
                }
                val result2: Result<Unit> = runCatching {
                    val e = 100 / (men + women)
                }.onSuccess{
                }.onFailure {
                    AlertDialog.Builder(this).apply {
                        setTitle("Error")
                        setMessage("Can't devide by zero")
                        setPositiveButton("Yes", null)
                        setNegativeButton("Ok", null)
                        show()
                    }
                    return@setOnClickListener
                }
            }
            if(cbgrade.isChecked) {
                senior = if (ipsenior.text.toString() == "") 0 else ipsenior.text.toString().toInt()
                middle = if (ipmiddle.text.toString() == "") 0 else ipmiddle.text.toString().toInt()
                junior = if (ipjunior.text.toString() == "") 0 else ipjunior.text.toString().toInt()
                if (senior == 0){
                    ipmen.setText("0")
                }
                if (middle == 0){
                    ipwomen.setText("0")
                }
                if (junior == 0){
                    ipjunior.setText("0")
                }
                val result3: Result<Unit> = runCatching {
                    val e = 100 / (senior + middle + junior)
                }.onSuccess{
                }.onFailure {
                    AlertDialog.Builder(this).apply {
                        setTitle("Error")
                        setMessage("Can't devide by zero")
                        setPositiveButton("Yes", null)
                        setNegativeButton("Ok", null)
                        show()
                    }
                    return@setOnClickListener
                }
            }

            var ave1 = 10000
            var ave2 = 0
            var ave3 = 0
            var redundant = 0

            //TODO 答えをリストに入れてけば楽になるかも.
            when{
                cbgender.isChecked -> {
                    ave1 = total / (men + women)
                    ave2 = ave1
                    if (men == 0) {
                        ave1 = 0
                        ave2 = total / women
                    }
                    else if(women == 0){
                        ave1 = total / men
                        ave2 = 0
                    }
                    else {
                        while (Math.abs((ave1 - ave2) - diff) > 50) {
                            ave2 -= 10
                            ave1 = (total - ave2 * women) / men
                        }
                    }


                    var paymen = (ave1 + 100) / 100 * 100
                    var paywomen = (ave2 + 100) / 100 * 100

                    if (ave1 % 100 == 0) {
                        paymen = ave1
                    }
                    if (ave2 % 100 == 0) {
                        paywomen = ave2
                    }

                    redundant = (paymen * men + paywomen * women) - total

                    ans = "men: ${paymen}/ women: ${paywomen}/ redundant: $redundant"
                }
                cbgrade.isChecked -> {
                    ave1 = total / (senior + middle + junior)
                    ave2 = ave1
                    ave3 = ave1
                    if(senior == 0 || middle == 0 || junior == 0){
                        AlertDialog.Builder(this).apply {
                            setTitle("Error")
                            setMessage("Use Gender Mode")
                            setPositiveButton("Yes", null)
                            setNegativeButton("Ok", null)
                            show()
                        }
                    }
                    while(Math.abs((ave1 - ave2) - diff) > 100 || Math.abs((ave2 - ave3) - diff) > 100) {
                        ave3 -= 20
                        ave2 -= 10
                        ave1 -= (total - ave3*junior + ave2*middle) / senior
                    }
                    var paysenior = (ave1 + 100) / 100 * 100
                    var paymiddle = (ave2 + 100) / 100 * 100
                    var payjunior = (ave3 + 100) / 100 * 100

                    redundant = (payjunior*junior + paymiddle*middle + paysenior*senior) - total
                    ans = "senior : ${paysenior}/ middle : ${paymiddle}/ junior : ${payjunior} / redundant: $redundant"


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
     interface Text
 }



//TODO 10000,93,5 の時に負が出る.
//TODO lackをルーレットで誰に払わせるか.
//TODO lack or redundan{}t
