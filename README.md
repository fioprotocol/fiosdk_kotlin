# FIO Kotlin SDK
The Foundation for Interwallet Operability (FIO) is a consortium of leading blockchain wallets, exchanges and payments providers that seeks to accelerate blockchain adoption by reducing the risk, complexity, and inconvenience of sending and receiving cryptoassets.

For information on FIO, visit the [FIO website](https://fio.foundation).

For information on the FIO Chain, API, and SDKs visit the [FIO Protocol Developer Hub](https://developers.fioprotocol.io).

# Version 
Visit the [FIO Protocol Developer Hub](https://developers.fioprotocol.io) to get information on FIO SDK versions. Only use an SDK that has a major version number that matches the current FIO Protocol blockchain major version number (e.g. 1.x.x).
Make sure your project is synced.  This should happen automatically, but if it didn't, go to
Files->Sync Project with Gradle Files

To build the Kotlin SDK project, select "Build" from the Android Studio menu and select
"Rebuild Project"

# Generating Documentation

To create the documentation, open a terminal in the root of the project and run ./gradlew dokka

The documentation files will be located in the "documentation" folder in the root of the project.

# Running Unit Tests

The unit tests are located in the "app" project under the "androidTest" folder.  For running unit tests on testnet, 
the TestNetSdkTests file, will need to run in an emulator or on a device.

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
	http://testnet.fioprotocol.io/v1/

## TestNet Monitor Tool
	https://monitor.testnet.fioprotocol.io/

# Workflow for using the SDK with TestNet
Most Signed API calls charge fees and require a FIO address that is associated with the user making the call. 

#### Creating a test account with FIO tokens
When running a test you will want to register addresses and transfer funds. But, registering a new address for the first time requires FIO tokens. Therefore, some manual setup is required to associate FIO tokens with a FIO public key. To set up a FIO public key with FIO tokents in a test environment:
 
1. Manually create two private/public FIO key pairs 
	1. Navigate to the website: https://monitor.testnet.fioprotocol.io
	2. Select the 'Create Keypair' button (top left of the website)
	3. Copy the keypairs and FIO Internal Account 
2. Manually register a FIO address for both of these FIO key pairs. 
	1. Navigate to the website: https://monitor.testnet.fioprotocol.io
	2. Select the 'Register Address' button
	3. Type in a FIO address 
	4. Paste in one of the public keys (created above)
	5. Select the 'Create' button
	6. Do this for each public key pair (twice).  The created FIO address will be in this format, "mytest:fiotestnet"
3. Manually transfer funds into these FIO addresses.
	1. Navigate to the website: https://monitor.testnet.fioprotocol.io
	2. Select the 'Faucet' button
	3. Paste in one of the public keys (created above)
	4. Select the 'Send Coins' button
	5. Do this for each public key pair (twice)
4. These FIO public addresses now have funds available for making Signed API calls.
5. Edit the TestNetSdkTests.kt file for unit tests, to add these FIO addresses and the private/public FIO key pairs
	1. Edit the alicePrivateKey, alicePublicKey, bobPrivateKey, bobPublicKey, aliceFioAddress, bobFioAddress variables in the TestNetSdkTests.kt file (/app/src/java/fiofoundation/io/fiokotlinsdktestapp/TestNetSdkTests.kt)
6. Run the tests: 
	-> see running unit tests (above)

#### When calling a Signed API call that charges FEES, 
Use the following steps to determine the fee and pass it to the signed call.

	1. Call getFee to get the fee for the Signed API call
	2. Call the API Signed call with the fee

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
