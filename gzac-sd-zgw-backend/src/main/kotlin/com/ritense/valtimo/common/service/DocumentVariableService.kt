package com.ritense.valtimo.common.service

class DocumentVariableService {

    fun concatenateNotEmptyStrings(inputVars: List<String>): String {
        return inputVars
            .filter { it.isNotEmpty() }
            .joinToString(" ")
    }
}