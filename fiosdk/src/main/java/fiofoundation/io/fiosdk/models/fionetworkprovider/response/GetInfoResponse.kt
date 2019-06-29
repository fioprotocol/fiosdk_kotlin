package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class GetInfoResponse: FIOResponse() {
    private val head_block_num: Long = 0
    private val last_irreversible_block_num: Long = 0
    private val server_version: String = ""
    private val chain_id: String = ""
    private val head_block_id: String = ""
    private val head_block_time: String = ""
    private val head_block_producer: String = ""
    private val virtual_block_cpu_limit: String = ""
    private val virtual_block_net_limit: Long = 0
    private val server_version_string: String = ""

    val headBlockNumber: Long
        get(){return this.head_block_num}

    val lastIrreversibleBlockNumber: Long
        get(){return this.last_irreversible_block_num}

    val virtualBlockNetLimit: Long
        get(){return this.virtual_block_net_limit}

    val serverVersion: String
        get(){return this.server_version}

    val chainId: String
        get(){return this.chain_id}

    val headBlockId: String
        get(){return this.head_block_id}

    val headBlockTime: String
        get(){return this.head_block_time}

    val headBlockProducer: String
        get(){return this.head_block_producer}

    val virtualBlockCpuLimit: String
        get(){return this.virtual_block_cpu_limit}

    val serverVersionString: String
        get(){return this.server_version_string}
}