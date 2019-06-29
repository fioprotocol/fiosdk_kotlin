package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class GetBlockResponse(val id: String,val producer: String,val confirmed: Long,val previous: String,val transactions: List<String>,val timestamp: String): FIOResponse(){
    private val block_num: Long = 0
    private val ref_block_prefix: Long = 0
    private val transaction_mroot: String = ""
    private val schedule_version: Long = 0
    private val new_producers: String = ""
    private val header_extensions: List<String>? = null
    private val producer_signature: String = ""
    private val block_extensions: List<String>? = null

    val blockNumber: Long
        get(){return this.block_num}

    val refBlockPrefix: Long
        get(){return this.ref_block_prefix}

    val scheduleVersion: Long
        get(){return this.schedule_version}

    val transactionMRoot: String
        get(){return this.transaction_mroot}

    val newProducers: String
        get(){return this.new_producers}

    val producerSignature: String
        get(){return this.producer_signature}

    val headerExtensions: List<String>?
        get(){return this.header_extensions}

    val blockExtensions: List<String>?
        get(){return this.block_extensions}
}