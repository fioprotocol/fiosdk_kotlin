package fiofoundation.io.fiosdk.models.fionetworkprovider

class FIOApiEndPoints {
     companion object {
         const val get_public_address = "chain/pub_address_lookup"
         const val get_fio_names = "chain/get_fio_names"
         const val availability_check = "chain/avail_check"
         const val get_fio_balance = "chain/get_fio_balance"
         const val get_fee = "chain/get_fee"
         const val get_info = "chain/get_info"
         const val get_block = "chain/get_block"
         const val get_raw_abi = "chain/get_raw_abi"
         const val register_fio_domain = "chain/register_fio_domain"
         const val register_fio_address = "chain/register_fio_address"
         const val transfer_tokens_pub_key = "chain/transfer_tokens_pub_key"
         const val renew_fio_domain = "chain/renew_fio_domain"
         const val renew_fio_address = "chain/renew_fio_address"
         const val transfer_tokens_fio_address = "chain/transfer_tokens_fio_address"
         const val get_account = "chain/get_account"
         const val push_transaction = "chain/push_transaction"
         const val get_required_keys = "chain/get_required_keys"
         const val get_pending_fio_requests = "chain/get_pending_fio_requests"
         const val get_sent_fio_requests = "chain/get_sent_fio_requests"
         const val new_funds_request = "chain/new_funds_request"
         const val reject_funds_request = "chain/reject_funds_request"
         const val record_send = "chain/record_send"
         const val register_fio_name_behalf_of_user = "register_fio_name"

         val no_fioaddress_endpoints: List<String> = listOf(register_fio_domain,register_fio_address,transfer_tokens_pub_key,transfer_tokens_fio_address)

    }
}