package model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import util.Log
import vcpkg.ExecVcpkg
import vcpkg.VcpkgException
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VcpkgModel(vcpkgRoot: File) {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val log = Log { logContent.add(it) }
    private val api = ExecVcpkg(vcpkgRoot)
    private var cancellation : () -> Unit = {}

    val logContent = mutableStateListOf<Message>()
    val installedPackages = mutableStateListOf<VcPackage>()
    val selectedPackages = mutableStateListOf<VcPackage>()
    val searchedPackages = mutableStateListOf<VcPackage>()
    val fullLog = mutableStateOf(false)
    var isRunning = mutableStateOf(false)

    private fun <T> safe(def: T, f: () -> T) : T {
        try {
            return f()
        } catch (e: VcpkgException) {
            log.logError(e.message ?: "Unknown error")
            log.logError("Output:")
            log.logSecondary(e.stream)
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
                    log.logPrimary("Cancelled")
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
                        log.logPrimary("Removing '${it.name}'...")

                        val result = api.remove(it)
                        installedPackages.remove(it)

                        log.logPrimary("Successfully removed '${it.name}'!")
                        if (fullLog.value) {
                            log.logPrimary("Output:")
                            log.logSecondary(result.stream)
                        }
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
                log.logPrimary("Searching for '$name'...")

                val result = api.search(name)
                searchedPackages.addAll(result.result)

                log.logPrimary("Search for '$name' finished")
                if (fullLog.value) {
                    log.logPrimary("Output:")
                    log.logSecondary(result.stream)
                }
            }
        }
    }

    fun install() {
        submit {
            selectedPackages.forEach {
                if (it !in installedPackages) {
                    safe(Unit) {
                        log.logPrimary("Installing '${it.name}'...")

                        val result = api.install(it)
                        installedPackages.add(it)

                        log.logPrimary("Successfully installed '${it.name}'!")
                        if (fullLog.value) {
                            log.logPrimary("Output:")
                            log.logSecondary(result.stream)
                        }
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
                log.logPrimary("Updating installed packages list...")

                val result = api.list()
                installedPackages.addAll(result.result)

                log.logPrimary("Updated")
                if (fullLog.value) {
                    log.logPrimary("Output:")
                    log.logSecondary(result.stream)
                }
            }
        }
    }
}
