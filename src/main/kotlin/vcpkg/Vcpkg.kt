package vcpkg

import model.VcPackage
import util.ExecutionResult

class VcpkgException(
    message: String,
    val stream: String
) : Exception(message)

interface Vcpkg {
    fun list() : ExecutionResult<List<VcPackage>>
    fun search(name: String) : ExecutionResult<List<VcPackage>>
    fun install(pkg: VcPackage) : ExecutionResult<Unit>
    fun remove(pkg: VcPackage) : ExecutionResult<Unit>
}
