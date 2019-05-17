package exceptions

class ProcedureNotFoundException(override val message: String?) : Exception(message) {
}
