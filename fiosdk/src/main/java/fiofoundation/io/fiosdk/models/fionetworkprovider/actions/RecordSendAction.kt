package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.Validator
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordSendContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RecordSendAction(payerFioAddress:String,
                        payeeFioAddress: String,
                        content: String,
                        fioRequestId: BigInteger,
                        maxFee: BigInteger,
                        walletFioAddress: String,
                        actorPublicKey: String) : IAction
{
    override var account = "fio.reqobt"
    override var name = "recordsend"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData = RecordSendData(
            payerFioAddress, payeeFioAddress, content, maxFee,fioRequestId,
            auth.actor, walletFioAddress)

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class RecordSendData(
        @field:SerializedName("payer_fio_address") var payerFioAddress:String,
        @field:SerializedName("payee_fio_address") var payeeFioAddress:String,
        @field:SerializedName("content") var content:String,
        @field:SerializedName("max_fee") var maxFee:BigInteger,
        @field:SerializedName("fio_request_id") var fioRequestId:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var walletFioAddress:String): FIORequestData()
}