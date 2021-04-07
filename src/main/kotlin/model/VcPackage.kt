package model

class VcPackage(val name: String, val version: String, val desc: String)

fun parsePackage(str: String) : VcPackage {
    val spl = str.split(" ").filter { it.isNotEmpty() }
    return VcPackage(spl[0], spl[1], spl.subList(2, spl.size).joinToString(" "))
}