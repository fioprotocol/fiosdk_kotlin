package fiofoundation.io.fiosdk.models.fionetworkprovider.request

class GetPublicAddressRequest (fioAddress: String, tokenCode:String){
    private var fio_address: String = fioAddress
    private var token_code: String = tokenCode

    var fioAddress: String
        get(){return this.fio_address}
        set(value){this.fio_address = value}

    var tokenCode: String
        get(){return this.token_code}
        set(value){this.token_code = value}
}