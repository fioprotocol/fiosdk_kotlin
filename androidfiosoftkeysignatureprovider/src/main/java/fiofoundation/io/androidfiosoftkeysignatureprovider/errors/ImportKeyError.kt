package fiofoundation.io.androidfiosoftkeysignatureprovider.errors

import fiofoundation.io.fiosdk.errors.signatureprovider.SignatureProviderError

class ImportKeyError : SignatureProviderError
{
    constructor():super()
    constructor(message: String):super(message)
    constructor(exception: Exception):super(exception)
    constructor(message: String,exception: Exception):super(message,exception)
}