package models

class Person {
    private var id:Int = 0
    private var age:Int = 0
    private var name:String=""
    private var type:String=""

    constructor(name: String,age: Int,  type: String) {
        this.age = age
        this.name = name
        this.type = type
    }

    fun getId():Int{
        return id
    }

    fun getAge():Int{
        return age
    }

    fun getName():String{
        return name
    }

    fun getType():String{
        return type
    }

    override fun toString(): String {
        return name
    }


}