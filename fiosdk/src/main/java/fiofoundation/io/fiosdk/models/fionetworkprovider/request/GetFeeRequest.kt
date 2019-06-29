package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints

class GetFeeRequest (endPoint: String, fioAddress:String) : FIORequest(){

    private var fio_address: String = fioAddress
    private var end_point: String = endPoint

    init{
        if(FIOApiEndPoints.no_fioaddress_endpoints.indexOf(this.end_point)>-1)
            this.fio_address = ""
    }

    var fioAddress: String
        get(){return this.fio_address}
        set(value){
            if(FIOApiEndPoints.no_fioaddress_endpoints.indexOf(this.end_point)>-1)
                this.fio_address = ""
            else
                this.fio_address = value
        }

    var endPoint: String
        get(){return this.end_point}
        set(value){this.end_point = value}
}