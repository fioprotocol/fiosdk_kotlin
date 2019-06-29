package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class GetRawAbiResponse: FIOResponse() {
    private val account_name: String = ""
    private val abi_hash: String = ""
    private val code_hash: String = ""

    val abi: String = ""

    val accountName: String
        get(){return this.account_name}

    val abiHash: String
        get(){return this.abi_hash}

    val codeHash: String
        get(){return this.code_hash}
}