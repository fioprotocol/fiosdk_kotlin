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
    fun getPendingFIORequests(getPendingFioRequests: GetPendingFIORequestsRequest): GetPendingFIORequestsResponse
    fun getSentFIORequests(getSentFioRequests: GetSentFIORequestsRequest): GetSentFIORequestsResponse
    fun pushTransaction(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun getRequiredKeys(getRequiredKeysRequest: GetRequiredKeysRequest): GetRequiredKeysResponse
    fun registerFioAddress(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun registerFioDomain(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun renewFioDomain(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun renewFioAddress(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun transferTokensToPublicKey(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun registerFioNameOnBehalfOfUser(request: RegisterFIONameForUserRequest): RegisterFIONameForUserResponse
    fun requestNewFunds(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun rejectNewFunds(pushTransaction: PushTransactionRequest): PushTransactionResponse
    fun recordSend(pushTransaction: PushTransactionRequest): PushTransactionResponse
}