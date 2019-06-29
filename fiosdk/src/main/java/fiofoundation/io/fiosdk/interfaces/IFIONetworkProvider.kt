package fiofoundation.io.fiosdk.interfaces

import fiofoundation.io.fiosdk.models.fionetworkprovider.response.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.*

interface IFIONetworkProvider {

    fun getPublicAddress(getPublicAddressRequest: GetPublicAddressRequest): GetPublicAddressResponse
    fun getFIONames(getFioNamesRequest: GetFIONamesRequest): GetFIONamesResponse
    fun isFIONameAvailable(fioNameAvailabilityCheckRequest: FIONameAvailabilityCheckRequest): FIONameAvailabilityCheckResponse
    fun getFIOBalance(getFioBalanceRequest: GetFIOBalanceRequest): GetFIOBalanceResponse
    fun getFee(getFeeRequest: GetFeeRequest): GetFeeResponse
    fun getInfo(): GetInfoResponse
    fun getBlock(getBlockRequest: GetBlockRequest): GetBlockResponse
    fun getRawAbi(getRawAbiRequest: GetRawAbiRequest): GetRawAbiResponse
}