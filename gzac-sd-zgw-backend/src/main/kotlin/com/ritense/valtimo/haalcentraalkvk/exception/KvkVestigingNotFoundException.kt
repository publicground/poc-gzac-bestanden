package com.ritense.valtimo.haalcentraalkvk.exception

class KvkVestigingNotFoundException(message: String) : RuntimeException("vestiging nummer: $message")