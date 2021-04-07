package vcpkg

import model.VcPackage
import model.parsePackage
import util.ExecutionResult
import util.runCommand
import java.io.File

class ExecVcpkg(private val vcpkgRoot: File) : Vcpkg {
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
                else -> throw Exception("Unsupported OS")
            }
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

    override fun list(): ExecutionResult<List<VcPackage>> {
        val result = execProc("$executable list")
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

        val result = execProc("$executable search $name")
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
        val result = execProc("$executable install ${pkg.name}")

        return ExecutionResult(
            result.first,
            result.second,
            Unit
        )
    }

    override fun remove(pkg: VcPackage) : ExecutionResult<Unit> {
        val result = execProc("$executable remove ${pkg.name} --recurse")

        return ExecutionResult(
            result.first,
            result.second,
            Unit
        )
    }
}