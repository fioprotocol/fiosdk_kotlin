package fiofoundation.io.fiosdk.interfaces

import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.*

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

import fiofoundation.io.fiosdk.models.fionetworkprovider.request.PushTransactionRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.PushTransactionResponse

interface IFIONetworkProviderApi {

    @POST(FIOApiEndPoints.get_public_address)
    fun getPublicAddress(@Body getPublicAddressRequest: GetPublicAddressRequest): Call<GetPublicAddressResponse>

    @POST(FIOApiEndPoints.get_fio_names)
    fun getFIONames(@Body getFioNamesRequest: GetFIONamesRequest): Call<GetFIONamesResponse>

    @POST(FIOApiEndPoints.availability_check)
    fun isFIONameAvailable(@Body fioNameAvailabilityCheckRequest: FIONameAvailabilityCheckRequest): Call<FIONameAvailabilityCheckResponse>

    @POST(FIOApiEndPoints.get_fio_balance)
    fun getFIOBalance(@Body getFioBalanceRequest: GetFIOBalanceRequest): Call<GetFIOBalanceResponse>

    @POST(FIOApiEndPoints.get_fee)
    fun getFee(@Body getFeeRequest: GetFeeRequest): Call<GetFeeResponse>

    @POST(FIOApiEndPoints.get_info)
    fun getInfo(): Call<GetInfoResponse>

    @POST(FIOApiEndPoints.get_block)
    fun getBlock(@Body getBlockRequest: GetBlockRequest): Call<GetBlockResponse>

    @POST(FIOApiEndPoints.get_raw_abi)
    fun getRawAbi(@Body getRawAbiRequest: GetRawAbiRequest): Call<GetRawAbiResponse>

    @POST(FIOApiEndPoints.push_transaction)
    fun pushTransaction(@Body pushTransactionRequest: PushTransactionRequest): Call<PushTransactionResponse>

    @POST(FIOApiEndPoints.get_required_keys)
    fun getRequiredKeys(@Body getRequiredKeysRequest: GetRequiredKeysRequest): Call<GetRequiredKeysResponse>
}