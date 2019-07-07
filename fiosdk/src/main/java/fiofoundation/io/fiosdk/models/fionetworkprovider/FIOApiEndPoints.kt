package fiofoundation.io.fiosdk.models.fionetworkprovider

class FIOApiEndPoints {
     companion object {
         const val get_public_address = "v1/chain/pub_address_lookup"
         const val get_fio_names = "v1/chain/get_fio_names"
         const val availability_check = "v1/chain/avail_check"
         const val get_fio_balance = "v1/chain/get_fio_balance"
         const val get_fee = "v1/chain/get_fee"
         const val get_info = "v1/chain/get_info"
         const val get_block = "v1/chain/get_block"
         const val get_raw_abi = "v1/chain/get_raw_abi"
         const val register_fio_domain = "v1/chain/register_fio_domain"
         const val register_fio_address = "v1/chain/register_fio_address"
         const val transfer_tokens_pub_key = "v1/chain/transfer_tokens_pub_key"
         const val transfer_tokens_fio_address = "v1/chain/transfer_tokens_fio_address"
         const val get_account = "v1/chain/get_account"
         const val push_transaction = "v1/chain/push_transaction"
         const val get_required_keys = "v1/chain/get_required_keys"

         val no_fioaddress_endpoints: List<String> = listOf(register_fio_domain,register_fio_address,transfer_tokens_pub_key,transfer_tokens_fio_address)

    }
}