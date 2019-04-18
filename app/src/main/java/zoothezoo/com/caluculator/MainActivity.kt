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
                alert("mode")
            }

            total = if (iptotal.text.toString() == "") 0 else iptotal.text.toString().toInt()
            val result1: Result<Unit> = runCatching {
                val e = 100 / total
            }.onSuccess {  }
                .onFailure {
                    alert("total")
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
                    alert("zero")
                    return@setOnClickListener
                }
            }
            if(cbgrade.isChecked) {
                senior = if (ipsenior.text.toString() == "") 0 else ipsenior.text.toString().toInt()
                middle = if (ipmiddle.text.toString() == "") 0 else ipmiddle.text.toString().toInt()
                junior = if (ipjunior.text.toString() == "") 0 else ipjunior.text.toString().toInt()
                if (senior == 0){
                    ipsenior.setText("0")
                }
                if (middle == 0){
                    ipmiddle.setText("0")
                }
                if (junior == 0){
                    ipjunior.setText("0")
                }
                val result3: Result<Unit> = runCatching {
                    val e = 100 / (senior + middle + junior)
                }.onSuccess{
                }.onFailure {
                    alert("zero")
                    return@setOnClickListener
                }
            }

            var paymen = 0
            var paywomen = 0
            var paysenior = 0
            var paymiddle = 0
            var payjunior = 0
            var redundant: Int

            //TODO 答えをリストに入れてけば楽になるかも.
            when{
                cbgender.isChecked -> {
                    if (men == 0) {
                        paywomen = total / women
                    }
                    else if(women == 0){
                        paymen = total / men
                    }
                    else {
                        val x = (total - men * diff) / (men + women)
                        paymen = roundUp(x + diff)
                        paywomen = roundUp(x)
                    }

                    redundant = (paymen * men + paywomen * women) - total

                    if(paywomen < 0){
                        val nega = 0 - paywomen
                        paywomen += nega
                        redundant += nega * women
                    }

                    ans = "men : ${paymen}\nwomen : ${paywomen}\nredundant : $redundant"
                }
                cbgrade.isChecked -> {
                    if(senior == 0 || middle == 0 || junior == 0){
                        alert("gender")
                    }
                    else{
                        val x = (total - diff*middle - 2*diff*senior) / (senior + middle + junior)
                        paysenior = roundUp(x + diff * 2)
                        paymiddle = roundUp(x + diff)
                        payjunior = roundUp(x)
                    }

                    redundant = (payjunior*junior + paymiddle*middle + paysenior*senior) - total
                    if(payjunior < 0){
                        val nega = 0 - payjunior
                        payjunior += nega
                        redundant += nega * junior
                    }
                    ans = "senior : ${paysenior}\nmiddle : ${paymiddle}\njunior : ${payjunior}\nredundant: $redundant"
                }
            }
           if(ans != "") {
               AlertDialog.Builder(this).apply {
                   setTitle("Conclusion")
                   setMessage(ans)
                   setPositiveButton("ok", null)
                   show()
               }
           }
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
            ipdiff.setText("")

        }
    }

    //100の位で切り上げる関数
    fun roundUp(num :Int): Int{
        return if(num % 100 == 0) num else (num + 100) / 100 * 100
    }

    fun alert(str: String): Unit {
        when(str) {
            "zero" ->
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Can't devide by zero")
                    setPositiveButton("OK", null)
                    show()
                }
            "total" ->
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Input Total")
                    setNegativeButton("OK", null)
                    show()
                }
            "gender" ->
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Use Gender Mode")
                    setPositiveButton("OK", null)
                    show()
                }
            "mode" ->
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage("Select The Mode")
                    setPositiveButton("Yes", null)
                    setNegativeButton("Ok", null)
                    show()
                }
        }
    }
}



//TODO 10000,93,5 の時に負が出る.
//TODO lackをルーレットで誰に払わせるか.
//TODO lack or redundan{}t
