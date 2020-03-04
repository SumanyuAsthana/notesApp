package com.example.startup
class Note{
    var noteId:Int?=null
    var noteTitle:String?=null
    var noteContent:String?=null
    constructor(noteId:Int,noteTitle:String,noteContent:String){
        this.noteId=noteId
        this.noteContent=noteContent
        this.noteTitle=noteTitle
    }

}