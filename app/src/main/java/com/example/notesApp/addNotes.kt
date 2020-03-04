package com.example.notesApp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.startup.DbManager
import com.example.startup.MainActivity
import com.example.startup.R
import kotlinx.android.synthetic.main.activity_ad_notes.*
import java.lang.Exception

class addNotes : AppCompatActivity() {
    val dbTable="Notes"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_notes)


            try{
                var bundle:Bundle?=intent.extras
                id=bundle!!.getInt("ID",0)
                if(id!=0){
                    etTitle.setText(bundle.getString("Title").toString())
                    etDes.setText(bundle.getString("Description").toString())
                }
            }catch (ex:Exception){}

    }
    fun buAdd(view: View){
        var dbManager=DbManager(this)
        var value=ContentValues( )
        value.put("Title",etTitle.text.toString())
        value.put("Description",etDes.text.toString())
        if(id==0){
            val ID=dbManager.Insert(value)
            if(ID>0){
                Toast.makeText(this," note added",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this," note not added",Toast.LENGTH_LONG).show()
            }
        }
        else{
            var selectionArs=arrayOf(id.toString())
            val ID=dbManager.update(value,"ID=?",selectionArs)
        }
    }
}
