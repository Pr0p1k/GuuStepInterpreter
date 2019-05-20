package exceptions

class ProcedureNotFoundException(name: String, line: Int) :
        Exception("Undefined procedure \"$name\" called at line $line")
