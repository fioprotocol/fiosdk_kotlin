package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class FIONameAvailabilityCheckResponse: FIOResponse() {

    private val is_registered: Boolean = false

    val isAvailable: Boolean
        get(){return !this.is_registered}

}