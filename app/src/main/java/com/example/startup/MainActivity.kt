package com.example.startup

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import com.example.notesApp.addNotes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {


    var listOfNotes=java.util.ArrayList<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }
    public fun LoadQuery(tita:String){
        var dbManager=DbManager(this)
        var selectionArgs= arrayOf(tita)
        var projection= arrayOf("ID","Title","Description")
        listOfNotes.clear()
        val cursor=dbManager.Query(projection,"Title like ?",selectionArgs,"Title")
        if(cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))
                listOfNotes.add(Note(ID,Title,Description))
            }while (cursor.moveToNext())
        }
        var myNotesAdapterq=myNotesAdapter(this,listOfNotes)
        lvNotes.adapter=myNotesAdapterq
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val searchView=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+ query +"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId!=null){
            when(item.itemId){
                R.id.addNote->{
                    var intent=Intent(this,addNotes::class.java)
                    startActivity(intent)
                }
                R.id.app_bar_search->{

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class myNotesAdapter:BaseAdapter{
        var listOfNotes=java.util.ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context,listOfNotes:java.util.ArrayList<Note>):super(){
            this.listOfNotes=listOfNotes
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            /*
            this will take the array list item and convert it to a note xml file.
            then the note xml file would be injected into the activity main xml file
             */
            var myView=layoutInflater.inflate(R.layout.ticket,null)
            var myNote=listOfNotes[position]
            myView.id=myNote.noteId!!
            myView.tvTitle.text=myNote.noteTitle
            myView.tvContent.text=myNote.noteContent
            myView.imageView2.setOnClickListener(View.OnClickListener {
                var dbManager=DbManager(this.context!!)
                var selectionArgs= arrayOf(myNote.noteId.toString())
                dbManager.delete("ID=?",selectionArgs)
                LoadQuery("%")
            })
            myView.imageViewEdit.setOnClickListener(View.OnClickListener {
                goToUpdate(myNote)
            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return listOfNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfNotes.size
        }
    }
    fun goToUpdate(note:Note){
        var intent=Intent(this,addNotes::class.java)
        intent.putExtra("ID",note.noteId)
        intent.putExtra("Title",note.noteTitle)
        intent.putExtra("Description",note.noteContent)
        startActivity(intent)
    }
}
