package util

class ExecutionResult<T>(
    val exitCode: Int,
    val stream: String,
    val result: T
)
