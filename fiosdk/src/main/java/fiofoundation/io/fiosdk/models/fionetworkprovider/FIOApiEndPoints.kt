package fiofoundation.io.fiosdk.models.fionetworkprovider

object FIOApiEndPoints {
    const val get_public_address = "get_pub_address"
    const val get_fio_names = "get_fio_names"
    const val availability_check = "avail_check"
    const val get_fio_balance = "get_fio_balance"
    const val get_locks = "get_locks"
    const val transfer_locked_tokens = "transfer_locked_tokens"
    const val transfer_fio_address = "transfer_fio_address"
    const val get_fee = "get_fee"
    const val get_info = "get_info"
    const val get_block = "get_block"
    const val get_raw_abi = "get_raw_abi"
    const val register_fio_domain = "register_fio_domain"
    const val register_fio_address = "register_fio_address"
    const val transfer_tokens_pub_key = "transfer_tokens_pub_key"
    const val renew_fio_domain = "renew_fio_domain"
    const val burn_fio_address = "burn_fio_address"
    const val renew_fio_address = "renew_fio_address"
    const val push_transaction = "push_transaction"
    const val get_required_keys = "get_required_keys"
    const val get_pending_fio_requests = "get_pending_fio_requests"
    const val get_sent_fio_requests = "get_sent_fio_requests"
    const val get_obt_data = "get_obt_data"
    const val new_funds_request = "new_funds_request"
    const val reject_funds_request = "reject_funds_request"
    const val record_obt_data = "record_obt_data"
    const val register_fio_name_behalf_of_user = "register_fio_name"
    const val add_public_address = "add_pub_address"
    const val remove_public_addresses = "remove_pub_address"
    const val set_domain_visibility = "set_fio_domain_public"
    const val cancel_funds_request = "cancel_funds_request"
    const val get_cancelled_fio_requests = "get_cancelled_fio_requests"
    const val get_account = "get_account"
    const val get_fio_domains = "get_fio_domains"
    const val get_fio_addresses = "get_fio_addresses"
    const val transfer_fio_domain = "transfer_fio_domain"

    enum class FeeEndPoint (val endpoint: String) {
        RegisterFioDomain(register_fio_domain),
        RegisterFioAddress(register_fio_address),
        RenewFioDomain(renew_fio_domain),
        RenewFioAddress(renew_fio_address),
        TransferTokens(transfer_tokens_pub_key),
        TransferLockedTokens(transfer_locked_tokens),
        SetDomainVisibility(set_domain_visibility),
        RecordObtData(record_obt_data),
        RejectFunds(reject_funds_request),
        AddPublicAddress(add_public_address),
        RemovePublicAddresses(remove_public_addresses),
        CancelFundsRequest(cancel_funds_request),
        TransferFIODomain(transfer_fio_domain)
    }
}

