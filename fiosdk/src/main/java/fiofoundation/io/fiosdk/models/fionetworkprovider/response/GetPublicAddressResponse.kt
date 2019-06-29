package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class GetPublicAddressResponse: FIOResponse() {
    private val public_address: String = ""

    val publicAddress: String
        get(){return this.public_address}
}