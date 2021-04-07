package vcpkg

import model.VcPackage
import model.parsePackage
import util.ExecutionResult
import util.runCommand
import java.io.File

class ExecVcpkg(
    private var vcpkgRoot: File
    ) : Vcpkg {
    private var executable : String = ""
    private var execPrefix : String = ""

    private fun setupVars() {
        System.getProperty("os.name").toLowerCase().let {
            when {
                it.contains("win") -> {
                    executable = "vcpkg.exe"
                    execPrefix = "${vcpkgRoot}/"
                }
                it.contains("nix") || it.contains("nux") || it.contains("aix") -> {
                    executable = "./vcpkg"
                }
                it.contains("mac") -> {
                    executable = "./vcpkg"
                }
                else -> throw Exception("Unsupported OS")
            }
        }
    }

    init {
        setupVars()
    }

    private fun getExec() : String {
        return "$execPrefix$executable"
    }

    private fun execProc(execLine: String): Pair<Int, String> {
        val result = execLine.runCommand(vcpkgRoot)

        if (result.first != 0) {
            throw VcpkgException(
                "Vcpkg exited with non-zero code: ${result.first}",
                result.second
            )
        }

        return result
    }

    fun checkRoot() {
        check(vcpkgRoot.toString().isNotEmpty()) { "Vcpkg directory is not provided" }
    }

    fun changeRoot(root: File) {
        vcpkgRoot = root
        setupVars()
    }

    override fun list(): ExecutionResult<List<VcPackage>> {
        checkRoot()

        val result = execProc("${getExec()} list")
        val stream = result.second

        if (stream.startsWith("No packages"))
            return ExecutionResult(
                result.first,
                result.second,
                arrayListOf()
            )

        val list = stream.split(System.lineSeparator())

        return ExecutionResult(
            result.first,
            result.second,
            list.subList(0, list.size - 1).map {
                parsePackage(it)
            }
        )
    }

    override fun search(name: String): ExecutionResult<List<VcPackage>> {
        if (name.isEmpty())
            return ExecutionResult(
                0, "", arrayListOf()
            )

        checkRoot()

        val result = execProc("${getExec()} search $name")
        val stream = result.second
        val list = stream.split(System.lineSeparator())

        return ExecutionResult(
            result.first,
            result.second,
            list.subList(0, list.size - 4).map {
                parsePackage(it)
            }
        )
    }

    override fun install(pkg: VcPackage) : ExecutionResult<Unit> {
        checkRoot()
        val result = execProc("${getExec()} install ${pkg.name}")

        return ExecutionResult(
            result.first,
            result.second,
            Unit
        )
    }

    override fun remove(pkg: VcPackage) : ExecutionResult<Unit> {
        checkRoot()
        val result = execProc("${getExec()} remove ${pkg.name} --recurse")

        return ExecutionResult(
            result.first,
            result.second,
            Unit
        )
    }
}