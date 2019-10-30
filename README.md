# Kotlin SDK
The FIO Foundation Kotlin SDK provides a Java and Android Compatible SDK, to the FIO Foundation Protocol API.  Allowing wallets to generate FIO private/public keys, register fio addresses and domains, and request funds (requests), view pending requests and sent requests.

# Version 0.5 
Targets the FIO Protocol API Version 0.5.

# Building the Kotlin SDK

Make sure your project is synced.  This should happen automatically, but if it didn't, go to
Files->Sync Project with Gradle Files

To build the Kotlin SDK project, select "Build" from the Android Studio menu and select
"Rebuild Project"

# Generating Documentation

To create the documentation, open a terminal in the root of the project and run ./gradlew dokka

The documentation files will be located in the "documentation" folder in the root of the project.

# Running Unit Tests

The unit tests are located in the "app" project under the "androidTest" folder.  These are
instrumented tests and will need to run in an emulator or on a device.

To run a test, proceed as follows:

    Be sure your project is synchronized with Gradle by clicking Sync Project in the toolbar.
    Run your test in one of the following ways:
        In the Project window, right-click a test and click Run .

        In the Code Editor, right-click a class or method in the test file and click Run to
        test all methods in the class.

        To run all tests, right-click on the test directory and click Run tests .

By default, your test runs using Android Studio's default run configuration.  If you'd like to
change some run settings such as the instrumentation runner and deployment options, you can
edit the run configuration in the Run/Debug Configurations dialog (click Run > Edit Configurations).

# Using the SDK
The SDK uses a singleton model.  Requiring initialization in the constructor, as these parameters are referenced in subsequent SDK Calls.  

The provider model is also used and the appropriate abi provider, needs to be passed into the SDK.  An androidfioserializationprovider is provided.

## Base URL for TestNet
	http://testnet.fioprotocol.io

# Workflow for using the SDK
Most Signed API calls now charge fees, to make the API call.  And most Signed API Calls require that a fio address, is registered with the user making the call. 

#### When registering a new address for the first time.  The account will not have any funds to do API calls.

This is the order of sequence to get funds in an account for the first time.
1. Call the registerFioNameOnBehalfOfUser method (this calls the mock server for registration), to register a fio address, for the first time (no fee charged)
2. request funds for that newly registered fio address from this fio address: "faucet:fio" (i.e. payer)
3. wait 60 seconds (for funds to arrive)
4. Now one will have funds available, to call Signed API calls.

#### When calling a Signed API call that charges FEES, this is the sequence to pass in the FEE to charge.
1. Call getFee to get the fee for the Signed API call.
2. Call the API Signed call with the above fee found.

# Creating your own FIO Private/Public Keys?
The SDK provides FIO Key generation.  Here are the key details, if the SDK is not used for Key Generation.

FIO Keys use SLIP-235 for BIP-0044.
https://github.com/satoshilabs/slips/blob/master/slip-0044.md

Following the EOS example of private and public key generation. We replace EOS slip '194' with '235'.  And 'EOS' public key prefix with 'FIO'.
https://eosio.stackexchange.com/questions/397/generate-eos-keys-from-mnemonic-seed

##  FIO Key Derivation Path:
"44'/235'/0'/0/0"

## FIO Key Generation Testing

Using this mnemonic phrase:
"valley alien library bread worry brother bundle hammer loyal barely dune brave"

This is the expected Private Key:
"5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu"

This is the expected Public Key:
"FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o"
