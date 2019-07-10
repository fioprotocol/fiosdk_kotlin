package fiofoundation.io.fiosdk.interfaces

import fiofoundation.io.fiosdk.models.fionetworkprovider.response.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.*

import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetRequiredKeysRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.GetRequiredKeysResponse

interface IFIONetworkProvider {

    fun getPublicAddress(getPublicAddressRequest: GetPublicAddressRequest): GetPublicAddressResponse
    fun getFIONames(getFioNamesRequest: GetFIONamesRequest): GetFIONamesResponse
    fun isFIONameAvailable(fioNameAvailabilityCheckRequest: FIONameAvailabilityCheckRequest): FIONameAvailabilityCheckResponse
    fun getFIOBalance(getFioBalanceRequest: GetFIOBalanceRequest): GetFIOBalanceResponse
    fun getFee(getFeeRequest: GetFeeRequest): GetFeeResponse
    fun getInfo(): GetInfoResponse
    fun getBlock(getBlockRequest: GetBlockRequest): GetBlockResponse
    fun getRawAbi(getRawAbiRequest: GetRawAbiRequest): GetRawAbiResponse
    fun pushTransaction(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun getRequiredKeys(getRequiredKeysRequest: GetRequiredKeysRequest): GetRequiredKeysResponse
    fun registerFioAddress(registerFIOAddressRequest: RegisterFIOAddressRequest): PushTransactionResponse
}