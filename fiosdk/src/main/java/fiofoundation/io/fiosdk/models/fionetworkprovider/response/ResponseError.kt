package fiofoundation.io.fiosdk.models.fionetworkprovider.response

class ResponseError(val type:String,val message: String,var code: Int,var fields: List<FieldError>)