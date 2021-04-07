package vcpkg

import model.Message
import model.VcPackage
import model.parsePackage
import util.runCommand
import java.io.File

class ExecVcpkg(private val vcpkgRoot: File, private val log: (Message) -> Unit) : Vcpkg {
    private val executable =
        System.getProperty("os.name").toLowerCase().let {
            when {
                it.contains("win") -> {
                    "vcpkg.exe"
                }
                it.contains("nix") || it.contains("nux") || it.contains("aix") -> {
                    "./vcpkg"
                }
                it.contains("mac") -> {
                    "./vcpkg"
                }
                else -> throw VcpkgException("Unsupported OS")
            }
        }

    private fun execProc(execLine: String): String {
        log(Message("Executing: '$execLine'"))
        return execLine.runCommand(vcpkgRoot)
            ?: throw VcpkgException("Error in executing '$execLine'")
    }

    override fun list(): List<VcPackage> {
        val result = execProc("$executable list")
        if (result.startsWith("No packages")) return arrayListOf()
        val list = result.split(System.lineSeparator())
        return list.subList(0, list.size - 1).map {
            parsePackage(it)
        }
    }

    override fun search(name: String): List<VcPackage> {
        if (name.isEmpty()) return arrayListOf()
        val result = execProc("$executable search $name")
        val list = result.split(System.lineSeparator())
        return list.subList(0, list.size - 4).map {
            parsePackage(it)
        }
    }

    override fun install(pkg: VcPackage) {
        val result = execProc("$executable install ${pkg.name}")
        log(Message(result))
    }

    override fun remove(pkg: VcPackage) {
        val result = execProc("$executable remove ${pkg.name} --recurse")
        log(Message(result))
    }
}