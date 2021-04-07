package model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import vcpkg.ExecVcpkg
import vcpkg.VcpkgException
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VcpkgModel(vcpkgRoot: File) {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val api = ExecVcpkg(vcpkgRoot, this::log)
    val log = mutableStateListOf<Message>()
    val installedPackages = mutableStateListOf<VcPackage>()
    val selectedPackages = mutableStateListOf<VcPackage>()
    val searchedPackages = mutableStateListOf<VcPackage>()
    var isRunning = mutableStateOf(false)
    private var cancellation : () -> Unit = {}

    private fun log(message: Message) {
        log.add(message)
    }

    private fun <T> safe(def: T, f: () -> T) : T {
        try {
            return f()
        } catch (e: VcpkgException) {
            log(Message(e.message ?: "Unknown error"))
        }
        return def
    }

    private fun submit(f: () -> Unit) {
        check(!isRunning.value)

        val future = executor.submit {
            isRunning.value = true
            f()
            isRunning.value = false
        }

        cancellation = {
            if (!future.isDone)
                if (future.cancel(true))
                    log(Message("Cancelled"))
            isRunning.value = false
        }
    }

    fun cancel() { cancellation() }

    fun remove() {
        submit {
            selectedPackages.filter {
                if (it !in installedPackages) true
                else {
                    safe(true) {
                        api.remove(it)
                        installedPackages.remove(it)
                        false
                    }
                }
            }
        }
    }

    fun search(name: String) {
        submit {
            selectedPackages.filter { it !in searchedPackages }
            searchedPackages.clear()

            safe(Unit) {
                searchedPackages.addAll(api.search(name))
            }
        }
    }

    fun install() {
        submit {
            selectedPackages.forEach {
                if (it !in installedPackages) {
                    safe(Unit) {
                        api.install(it)
                        installedPackages.add(it)
                    }
                }
            }
        }
    }

    fun update() {
        submit {
            selectedPackages.clear()
            installedPackages.clear()
            safe(Unit) {
                installedPackages.addAll(api.list())
            }
        }
    }
}
