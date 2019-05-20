package exceptions

class ProcedureNotFoundException(val name: String, line: Int) :
        Exception("Undefined procedure \"$name\" called at line $line")
