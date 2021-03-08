package fiofoundation.io.fiosdk.models

class FIOAddress {
    private var fio_address: String = ""
    private var expiration: String = ""
    private var remaining_bundled_tx: Int = 0

    var fioAddress: String
        get(){return this.fio_address}
        set(value){this.fio_address = value}

    var remainingBundledTx: Int
        get(){return this.remaining_bundled_tx}
        set(value){this.remaining_bundled_tx = value}
}