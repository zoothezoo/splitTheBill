package zoothezoo.com.caluculator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState
        )
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

        val people = mutableListOf<Int>()


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


        var ans = ""
        var paypay = mutableListOf<Int>()
        btdoit.setOnClickListener{
            val diff = if (ipdiff.text.toString() != "") ipdiff.text.toString().toInt() else 0
            val total = if (iptotal.text.toString() != "") iptotal.text.toString().toInt() else 0
            people.clear()
            var flag = true

            if(!cbgrade.isChecked && !cbgender.isChecked){
                alert("mode")
                flag = false
            }
            else if(cbgrade.isChecked){
                ans = ""
                people.add(if(ipsenior.text.toString() != "") ipsenior.text.toString().toInt() else 0)
                people.add(if(ipmiddle.text.toString() != "") ipmiddle.text.toString().toInt() else 0)
                people.add(if(ipjunior.text.toString() != "") ipjunior.text.toString().toInt() else 0)

                paypay = calculate(people, total, diff)

            }
            else if(cbgender.isChecked){
                ans = ""
                people.add(if(ipmen.text.toString() != "") ipmen.text.toString().toInt() else 0)
                people.add(if(ipwomen.text.toString() != "") ipwomen.text.toString().toInt() else 0)
                paypay = calculate(people, total, diff)
            }

            for(i in 0 until paypay.size-1){
                ans += "person$i:${paypay[i]}\n"
            }
            ans += "お釣り:${paypay[paypay.size-1]}\n"

            if(people.sum() == 0){
                alert("zero")
                flag = false
            }

            if(flag){
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("ans",ans)
                startActivity(intent)
            }

        }

        btclear.setOnClickListener{
            people.clear()
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

    fun calculate(list: MutableList<Int>, total: Int, diff: Int): MutableList<Int>{
        val payment = mutableListOf<Int>()
        var sum = 0
        //TODO:alertできひん
        if(list.sum() == 0){
            alert("zero")
        }

        for(i in 1 until list.size){
            sum =+ i*list[i-1]*diff
        }
        //最低支払い価格
        val base = (total - sum) / list.sum()
        for(i in 0 until list.size){
            if(list[i] != 0) {
                payment.add(roundUp(base + (list.size - 1 - i) * diff))
            }
            else{
                payment.add(0)
            }
        }

        var sumPayment = 0
        for(i in 0 until list.size){
            sumPayment += payment[i]*list[i]
        }

        //paymentの末尾をおつりとする
        payment.add(sumPayment - total)
        return payment

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
