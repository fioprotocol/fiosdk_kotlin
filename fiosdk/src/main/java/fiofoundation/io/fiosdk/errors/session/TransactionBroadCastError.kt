package fiofoundation.io.fiosdk.errors.session

import fiofoundation.io.fiosdk.models.fionetworkprovider.request.PushTransactionRequest

open class TransactionBroadCastError: TransactionProcessorError{
    constructor():super()
    constructor(message: String):super(message)
    constructor(exception: Exception):super(exception)
    constructor(message: String,exception: Exception):super(message,exception)
    constructor(message: String,exception: Exception,pushTransactionRequest: PushTransactionRequest?):super(message,exception)
    {
        originalPushTransactionRequest = pushTransactionRequest
    }

    var originalPushTransactionRequest:PushTransactionRequest? = null
}