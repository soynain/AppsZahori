package com.example.appszahori

import android.content.Context
import android.os.Build

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import models.Person
import java.util.stream.Collectors

//ANDROID_SDK_ROOT = C:\Users\moise\AppData\Local\Android\Sdk

class MainActivity() : AppCompatActivity(),TextWatcher
    , OnClickListener, CompoundButton.OnCheckedChangeListener
    , OnItemClickListener
    , OnItemLongClickListener, SearchView.OnQueryTextListener {//MainActivity

    private var edtName: EditText? = null
    private var edtAge: EditText? = null
    private var txtName: TextView? = null
    private var txtAge: TextView? = null
    private var rdbGender1: RadioButton? = null
    private var rdbGender2: RadioButton? = null
    private var btnGo: Button? = null
    private var ltvGrid: ListView? = null

    private var sName: String? = null
    private var sAge: String? = null
    private var sGender: String? = null

    private var iItemPos = 0
    private var sItemAction = "insert"

    private var sMensaje: String? = null

    private var iNum = 10

    private var arNames: Array<String>? = null
    private var arAges: Array<String>? = null
    private var arGenders: Array<String>? = null

    private var oVibrator: Vibrator? = null
    private lateinit var search : SearchView
    private var list: MutableList<Person> =ArrayList()
    private var listData: MutableList<Person> =ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //R.layout.activity_main: app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

        this.load_inputs()
        this.add_listeners()

        this.edtName!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        this.oVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?


        this.arNames = Array(this.iNum){""}
        this.arAges = Array(this.iNum){""}
        this.arGenders = Array(this.iNum){""}

      /*  list.add("Alex")
        list.add("Joel")
        var count=list.size
        var dataaa=""
        for(i in 0 until count){
            dataaa=list.get(i)
        }*/
        //this.operacion()
        //this.operacion_2()
        //this.operacion_3()
        //this.operacion_4()
    }//onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        search= menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        search.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        search.setOnQueryTextListener(this)
        return true
    }
    private fun toast_it(sMessage:String?)
    {
        Toast.makeText(this,sMessage,Toast.LENGTH_LONG).show()
    }//toast_it

    private fun alert_it(i:Int)
    {
      //  var arAdapter: Array<String>
       // var iNames = this.arNames?.size as Int
        val oAlert = AlertDialog.Builder(this)
        oAlert.setIcon(R.mipmap.ic_myalert)
            .setTitle(R.string.alert_delete)
            .setPositiveButton("Remove"){dialog,which ->
               // toast_it("Removing...")
                list.removeAt(i)
                seeData(list)
             /*   arNames?.set(i,"")
                arAges?.set(i,"")
                arGenders?.set(i,"")
                arAdapter = Array<String>(iNames,{ "" })
                for(j in 0..i)
                    arAdapter[j] = arNames?.get(j).toString()

                val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arAdapter)
                ltvGrid!!.adapter = oAdapter*/
                toast_it("Removed")
            }
            .setNegativeButton("Cancel"){dialog,which ->}
            .show()
    }//alert_it

    private fun load_inputs()
    {
        //cajas input
        edtName = findViewById(R.id.edtName)
        edtAge = findViewById(R.id.edtAge)
        //labels
        txtName = findViewById(R.id.txtName)
        txtAge = findViewById(R.id.txtAge)
        //gender
        rdbGender1 = findViewById(R.id.rdbGender1)
        rdbGender2 = findViewById(R.id.rdbGender2)

        //buttons
        btnGo = findViewById(R.id.btnGo)

        ltvGrid = findViewById(R.id.ltvGrid)

        edtName!!.setText("")
        edtAge!!.setText("")
    }//load_inputs

    private fun add_listeners()
    {
        edtName!!.addTextChangedListener(this)

        //Radios
        rdbGender1!!.setOnCheckedChangeListener(this)
        rdbGender2!!.setOnCheckedChangeListener(this)
        rdbGender1!!.setOnClickListener(this)
        rdbGender2!!.setOnClickListener(this)

        //Bot??n
        btnGo!!.setOnClickListener(this)

        //Lista
        ltvGrid!!.onItemClickListener = this
        ltvGrid!!.onItemLongClickListener = this

    }//add_listeners

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        var sMessage: String ? = ""

        when(buttonView!!.id){
            R.id.rdbGender1 ->
                if(isChecked) sMessage = "Ha seleccionado Femenino"
            R.id.rdbGender2 ->
                if(isChecked) sMessage = "Ha seleccionado Masculino"

        }
        //if(sMessage!="") toast_it(sMessage)
    }//onCheckedChanged

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnGo -> operacion_5()
            //R.id.rdbGender1 -> toast_it("Ha seleccionado Femenino")
            //R.id.rdbGender2 -> toast_it("Ha seleccionado Masculino")
        }//when(v.id)

        //this.operacion()
    }//view.onclick

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        edtName!!.setText(list.get(position).getName())
        edtAge!!.setText(list.get(position).getAge().toString())

        when(list.get(position).getType()){
            "Man" -> rdbGender2!!.isChecked = true
            "Woman" -> rdbGender1!!.isChecked = true
        }//when

        this.iItemPos = position
        this.sItemAction = "update"
    }//onItemClick

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean
    {
        //VIDEO 30 https://youtu.be/HOrQtrxcKsI?list=PLfkODrpjGnhmzRSUC5L-M_BjkyavnSKXS&t=421
        //si todas las versiones de android son mayores o igual a la nueva version de android
        //si en nuestro disp tenemos la versi??n mas actual del SO android podremos ejecutar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //toast_it("vibrando...")
            oVibrator?.vibrate(VibrationEffect.createOneShot(5,20))
            alert_it(position)
        }
        else
        {
            oVibrator?.vibrate(3)
            alert_it(position)
        }
        return true
    }//onItemLongClick

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Toast.makeText(this,s.toString(),Toast.LENGTH_SHORT).show()
        sName=edtName?.text.toString()
        if(sName?.equals("")?:(""===null)){
            edtName!!.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        }else{
            edtName!!.inputType=InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_NORMAL
        }
    }//onTextChanged

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }//beforeTextChanged

    override fun afterTextChanged(s: Editable?) {

    }//afterTextChanged

    private fun add_datos()
    {
        list.add(Person(sName as String,Integer.valueOf(sAge),sGender as String))
        seeData(list)
        //array que se vincular?? al adapter que se pasar?? al listview grid
     /*   var arAdapter: Array<String>
        var iNames = this.arNames?.size as Int

        for(i in 0.until(iNames-1))
        {
            //si no se ha guardado un nombre en esa posici??n
            if(arNames?.get(i).equals(""))
            {
                //guardo el nuevo nombre
                arNames?.set(i, sName as String)
                arAges?.set(i, sAge as String)
                arGenders?.set(i, sGender as String)

                //creo el adapter con el tama??o de arNames
                arAdapter = Array<String>(iNames,{ "" })
                //recargo el array del adapter con todo lo que hubiera en names
                for(j in 0..i)
                    arAdapter[j] = arNames?.get(j).toString()

                val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arAdapter)
                ltvGrid!!.adapter = oAdapter
                toast_it("Data inserted!!")
                break
            }//endif(nombre en blanco)
        }//for i .. iNum

      */

        edtName!!.setText("")
        edtAge!!.setText("")

    }//add_datos

    private fun upd_datos()
    {
        list.set(iItemPos,Person(sName as String,Integer.valueOf(sAge),sGender as String))
        seeData(list)
        //array que se vincular?? al adapter que se pasar?? al listview grid
      /*  var arAdapter: Array<String>
        var iNames = this.arNames?.size as Int

        for(i in 0.until(iNames-1))
        {
            //si no se ha guardado un nombre en esa posici??n
            if(!arNames?.get(i).equals(""))
            {
                if(this.iItemPos === i)
                {
                    //guardo el nuevo nombre
                    arNames?.set(i, sName as String)
                    arAges?.set(i, sAge as String)
                    arGenders?.set(i, sGender as String)
                    toast_it("Data Updated!!")
                }

                //creo el adapter con el tama??o de arNames
                arAdapter = Array<String>(iNames,{ "" })
                //recargo el array del adapter con todo lo que hubiera en names
                for(j in 0..i)
                    arAdapter[j] = arNames?.get(j).toString()

                val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arAdapter)
                ltvGrid!!.adapter = oAdapter

            }
        }*/
        edtName!!.setText("")
        edtAge!!.setText("")
    }//upd_datos

    //video26 - listView: https://youtu.be/7Hl24amE8lo?list=PLfkODrpjGnhmzRSUC5L-M_BjkyavnSKXS&t=437
    private fun operacion_5()
    {
        this.sName = edtName?.text.toString()
        this.sAge = edtAge?.text.toString()

        if(sAge?.equals("")?:false)
        {
            edtAge!!.requestFocus()
            toast_it("Missing Age")
            return
        }

        if(sName?.equals("")?:false)
        {
            edtName!!.requestFocus()
            toast_it("Missing Name")
            return
        }

        if(!(rdbGender1!!.isChecked || rdbGender2!!.isChecked))
        {
            toast_it("Choose Gender")
            return
        }
        else
            sGender = (if(rdbGender1!!.isChecked) rdbGender1!!.text.toString() else rdbGender2!!.text.toString())

        when(this.sItemAction)
        {
            "insert" -> this.add_datos()
            "update" -> this.upd_datos()
        }//when(action)

    }//operacion_5

    //video:25 https://youtu.be/XNkohi44ghA?list=PLfkODrpjGnhmzRSUC5L-M_BjkyavnSKXS&t=313
    private fun operacion_4()
    {
        var sData = ""
        var arNames = arrayOf("Alex","AJ","PDHN")
        var arNulls = Array<String?>(3){null}
        arNulls = arrayOfNulls<String>(3)

        var iNulls = arNames.size
        iNulls--

        for(i in 0..iNulls)
            arNulls.set(i,arNames.get(i))

        arNulls.forEach {
                sItem -> sData = sItem.toString()
        }
    }//operacion_4

    //video: [tipos de array](https://youtu.be/PE22BxijE7M?list=PLfkODrpjGnhmzRSUC5L-M_BjkyavnSKXS)
    private fun operacion_3()
    {
        var arInts = IntArray(10)
        var arDoubles = DoubleArray(size = 10)
        var arChars = CharArray(10)
        var arBools = BooleanArray(10)
        var arFloats = FloatArray(10)
        var arBites = ByteArray(10)

        var sData = ""
        val arString = arrayOf("Alex","Aj","Eaf","PDHN")

        arString[1] = "hola mundo"
        sData = arString[1]
        arString.set(1,"Posicion Uno")
        sData = arString.get(1)

        for( s in arString) sData = s
        for(i in 0..3) sData = arString.get(i)

        var iString = arString.count()-1
        for(i in 0..iString) sData =  arString.get(i)

        var arStrings2 = Array<String>(3){""}
        var arStrings3 = Array(3){""}
        var arInts1 = Array<Int>(3){0}
        var arInts2 = Array(3){0}

        arString.forEach {
                sItem -> sData = sItem
        }//froeach

    }//operacion_3

    private fun operacion()
    {
        //toast_it("this.operacion()")
        sName = edtName?.text.toString()
        val sAge =  edtAge?.text.toString()

        //si sname==""
        if( !(sName?.equals("") ?: false) )
            txtName?.text = sName
        if(sAge != "")
            txtAge?.text = sAge

    }//operacion

    private fun operacion_2()
    {
        var sValue = "Java"
        var iEntero = 1

        when{
            1 == iEntero ->
                sMensaje = sValue
            2 < iEntero
            -> sMensaje = sValue
            else ->
                sMensaje = "No es correcto"
        }//when

    }//operacion_2

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        var count=1
        var arAdapter: Array<String>
        var iNames = this.arNames?.size as Int
        arAdapter = Array<String>(iNames,{ "" })
        if(!p0.equals("",ignoreCase = true)){
            search!!.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            listData=list.stream().filter{p->p.getName().startsWith(p0.toString())}
                .collect(Collectors.toList())
            seeData(listData)
            //System.out.println(p0.toString())

            /*for(i in 0 until iNum){
                    if(arNames!!.get(i).startsWith(p0.toString())){
                        for(j in 0 until count) {
                               arAdapter[j] = arNames?.get(i).toString()
                           }
                        count++
                        val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arAdapter)
                        ltvGrid!!.adapter = oAdapter
                }
            }*/

        }else{
            search!!.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            /*for(i in 0 until iNum)
                arAdapter[i] = arNames?.get(i).toString()

            val oAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arAdapter)
            ltvGrid!!.adapter = oAdapter*/
            listData=list.stream().collect(Collectors.toList())
            seeData(listData)
        }
        return false
    }

    private fun seeData(listData:MutableList<Person>){
        val adapter=ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, listData)
        ltvGrid!!.adapter=adapter
    }
}
