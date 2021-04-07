package vcpkg

import model.VcPackage

class VcpkgException(message: String) : Exception(message)

interface Vcpkg {
    fun list() : List<VcPackage>
    fun search(name: String) : List<VcPackage>
    fun install(pkg: VcPackage)
    fun remove(pkg: VcPackage)
}
