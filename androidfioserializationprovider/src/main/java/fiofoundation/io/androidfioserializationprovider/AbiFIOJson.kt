package fiofoundation.io.androidfioserializationprovider

object AbiFIOJson {
    val abiFioJsonMap: Map<String, String> = mapOf("abi.abi.json" to """{
"version": "eosio::abi/1.1",
"structs": [
    {
        "name": "extensions_entry",
        "base": "",
        "fields": [
            {
                "name": "tag",
                "type": "uint16"
            },
            {
                "name": "value",
                "type": "bytes"
            }
        ]
    },
    {
        "name": "type_def",
        "base": "",
        "fields": [
            {
                "name": "new_type_name",
                "type": "string"
            },
            {
                "name": "type",
                "type": "string"
            }
        ]
    },
    {
        "name": "field_def",
        "base": "",
        "fields": [
            {
                "name": "name",
                "type": "string"
            },
            {
                "name": "type",
                "type": "string"
            }
        ]
    },
    {
        "name": "struct_def",
        "base": "",
        "fields": [
            {
                "name": "name",
                "type": "string"
            },
            {
                "name": "base",
                "type": "string"
            },
            {
                "name": "fields",
                "type": "field_def[]"
            }
        ]
    },
    {
        "name": "action_def",
        "base": "",
        "fields": [
            {
                "name": "name",
                "type": "name"
            },
            {
                "name": "type",
                "type": "string"
            },
            {
                "name": "ricardian_contract",
                "type": "string"
            }
        ]
    },
    {
        "name": "table_def",
        "base": "",
        "fields": [
            {
                "name": "name",
                "type": "name"
            },
            {
                "name": "index_type",
                "type": "string"
            },
            {
                "name": "key_names",
                "type": "string[]"
            },
            {
                "name": "key_types",
                "type": "string[]"
            },
            {
                "name": "type",
                "type": "string"
            }
        ]
    },
    {
        "name": "clause_pair",
        "base": "",
        "fields": [
            {
                "name": "id",
                "type": "string"
            },
            {
                "name": "body",
                "type": "string"
            }
        ]
    },
    {
        "name": "error_message",
        "base": "",
        "fields": [
            {
                "name": "error_code",
                "type": "uint64"
            },
            {
                "name": "error_msg",
                "type": "string"
            }
        ]
    },
    {
        "name": "variant_def",
        "base": "",
        "fields": [
            {
                "name": "name",
                "type": "string"
            },
            {
                "name": "types",
                "type": "string[]"
            }
        ]
    },
    {
        "name": "abi_def",
        "base": "",
        "fields": [
            {
                "name": "version",
                "type": "string"
            },
            {
                "name": "types",
                "type": "type_def[]"
            },
            {
                "name": "structs",
                "type": "struct_def[]"
            },
            {
                "name": "actions",
                "type": "action_def[]"
            },
            {
                "name": "tables",
                "type": "table_def[]"
            },
            {
                "name": "ricardian_clauses",
                "type": "clause_pair[]"
            },
            {
                "name": "error_messages",
                "type": "error_message[]"
            },
            {
                "name": "abi_extensions",
                "type": "extensions_entry[]"
            },
            {
                "name": "variants",
                "type": "variant_def[]$"
            }
        ]
    }
]
}""", "eosio.assert.abi.json" to """{
"version": "eosio::abi/1.0",
"structs": [
  {
     "name": "chain_params",
     "base": "",
     "fields": [
        {
           "name": "chain_id",
           "type": "checksum256"
        },
        {
           "name": "chain_name",
           "type": "string"
        },
        {
           "name": "icon",
           "type": "checksum256"
        }
     ]
  },
  {
     "name": "stored_chain_params",
     "base": "",
     "fields": [
        {
           "name": "chain_id",
           "type": "checksum256"
        },
        {
           "name": "chain_name",
           "type": "string"
        },
        {
           "name": "icon",
           "type": "checksum256"
        },
        {
           "name": "hash",
           "type": "checksum256"
        },
        {
           "name": "next_unique_id",
           "type": "uint64"
        }
     ]
  },
  {
     "name": "contract_action",
     "base": "",
     "fields": [
        {
           "name": "contract",
           "type": "name"
        },
        {
           "name": "action",
           "type": "name"
        }
     ]
  },
  {
     "name": "manifest",
     "base": "",
     "fields": [
        {
           "name": "account",
           "type": "name"
        },
        {
           "name": "domain",
           "type": "string"
        },
        {
           "name": "appmeta",
           "type": "string"
        },
        {
           "name": "whitelist",
           "type": "contract_action[]"
        }
     ]
  },
  {
     "name": "stored_manifest",
     "base": "",
     "fields": [
        {
           "name": "unique_id",
           "type": "uint64"
        },
        {
           "name": "id",
           "type": "checksum256"
        },
        {
           "name": "account",
           "type": "name"
        },
        {
           "name": "domain",
           "type": "string"
        },
        {
           "name": "appmeta",
           "type": "string"
        },
        {
           "name": "whitelist",
           "type": "contract_action[]"
        }
     ]
  },
  {
     "name": "del.manifest",
     "base": "",
     "fields": [
        {
           "name": "id",
           "type": "checksum256"
        }
     ]
  },
  {
     "name": "require",
     "base": "",
     "fields": [
        {
           "name": "chain_params_hash",
           "type": "checksum256"
        },
        {
           "name": "manifest_id",
           "type": "checksum256"
        },
        {
           "name": "actions",
           "type": "contract_action[]"
        },
        {
           "name": "abi_hashes",
           "type": "checksum256[]"
        }
     ]
  }
],
"actions": [
  {
     "name": "setchain",
     "type": "chain_params",
     "ricardian_contract": ""
  },
  {
     "name": "add.manifest",
     "type": "manifest",
     "ricardian_contract": ""
  },
  {
     "name": "del.manifest",
     "type": "del.manifest",
     "ricardian_contract": ""
  },
  {
     "name": "require",
     "type": "require",
     "ricardian_contract": ""
  }
],
"tables": [
  {
     "name": "chain.params",
     "type": "stored_chain_params",
     "index_type": "i64",
     "key_names": [
        "key"
     ],
     "key_types": [
        "uint64"
     ]
  },
  {
     "name": "manifests",
     "type": "stored_manifest",
     "index_type": "i64",
     "key_names": [
        "key"
     ],
     "key_types": [
        "uint64"
     ]
  }
],
"ricardian_clauses": [],
"abi_extensions": []
}""", "transaction.abi.json" to """{
"version": "eosio::abi/1.0",
"types": [
    {
        "new_type_name": "account_name",
        "type": "name"
    },
    {
        "new_type_name": "action_name",
        "type": "name"
    },
    {
        "new_type_name": "permission_name",
        "type": "name"
    }
],
"structs": [
    {
        "name": "permission_level",
        "base": "",
        "fields": [
            {
                "name": "actor",
                "type": "account_name"
            },
            {
                "name": "permission",
                "type": "permission_name"
            }
        ]
    },
    {
        "name": "action",
        "base": "",
        "fields": [
            {
                "name": "account",
                "type": "account_name"
            },
            {
                "name": "name",
                "type": "action_name"
            },
            {
                "name": "authorization",
                "type": "permission_level[]"
            },
            {
                "name": "data",
                "type": "bytes"
            }
        ]
    },
    {
        "name": "extension",
        "base": "",
        "fields": [
            {
                "name": "type",
                "type": "uint16"
            },
            {
                "name": "data",
                "type": "bytes"
            }
        ]
    },
    {
        "name": "transaction_header",
        "base": "",
        "fields": [
            {
                "name": "expiration",
                "type": "time_point_sec"
            },
            {
                "name": "ref_block_num",
                "type": "uint16"
            },
            {
                "name": "ref_block_prefix",
                "type": "uint32"
            },
            {
                "name": "max_net_usage_words",
                "type": "varuint32"
            },
            {
                "name": "max_cpu_usage_ms",
                "type": "uint8"
            },
            {
                "name": "delay_sec",
                "type": "varuint32"
            }
        ]
    },
    {
        "name": "transaction",
        "base": "transaction_header",
        "fields": [
            {
                "name": "context_free_actions",
                "type": "action[]"
            },
            {
                "name": "actions",
                "type": "action[]"
            },
            {
                "name": "transaction_extensions",
                "type": "extension[]"
            }
        ]
    }
]
}""", "fio.abi.json" to """{
"version": "eosio::abi/1.0",
"structs": [
    {
        "name": "new_funds_content",
        "base": "",
        "fields": [
            {
                "name": "payee_public_address",
                "type": "string"
            },
            {
                "name": "amount",
                "type": "string"
            },
            {
                "name": "chain_code",
                "type": "string"
            },
            {
                "name": "token_code",
                "type": "string"
            },
            {
                "name": "memo",
                "type": "string?"
            },
            {
                "name": "hash",
                "type": "string?"
            },
            {
                "name": "offline_url",
                "type": "string?"
            }
        ]
    },
    {
        "name": "record_obt_data_content",
        "base": "",
        "fields": [
            {
                "name": "payer_public_address",
                "type": "string"
            },
            {
                "name": "payee_public_address",
                "type": "string"
            },
            {
                "name": "amount",
                "type": "string"
            },
            {
                "name": "chain_code",
                "type": "string"
            },
            {
                "name": "token_code",
                "type": "string"
            },
            {
                "name": "status",
                "type": "string"
            },
            {
                "name": "obt_id",
                "type": "string"
            },
            {
                "name": "memo",
                "type": "string?"
            },
            {
                "name": "hash",
                "type": "string?"
            },
            {
                "name": "offline_url",
                "type": "string?"
            }
        ]
    }
]
}""")
}