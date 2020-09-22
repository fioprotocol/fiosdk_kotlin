# Request Funds Workflow

From an App Workflow perspective.  This is the workflow for requesting funds between two parties.  Alice the Payee and Bob the Payer.

Alice (Payee) initiates a request for payment from Bob (Payer).  Once sent, this request shows up in Alice's (Payee), sent requests list.  The request also shows up in Bob's (Payer) pending requests list.  Bob can either accept the request OR reject the request.

If Bob accepts the request for payment, the workflow should allow bob to initiate the payment and close the 'pending request' out.  The accepted request will no longer display in Bob's pending request list.

If Bob rejects the request for payment, the workflow should allow the user to reject the request.  The rejected request will no longer display in Bob's pending request list.

## SDK Interactions:

	For the Kotlin SDK, example code is located in the unit test file:
		app/src/androidTest/java/fiofoundation/io/fiokotlinsdktestapp/
		TestNetSdkTests.kt

		method:
			fun testFundsRequest()

### Alice (Payee) Initiating a request:

	Example code here:
			TestNetSdkTests.kt

		method:
			fun testFundsRequest()

	SDK Calls:
		getFeeForNewFundsRequest()
		requestFunds()

	SDK Workflow:
		Get the fee for the request funds call (i.e. getFeeForNewFundsRequest() ).  This fee is passed into the request funds call.  Call the requestFunds() method.

### Alice (Payee) Seeing Sent Payment Requests:

	Example code here:
		TestNetSdkTests.kt

		method:
			fun testFundsRequest()

	SDK Calls:
		getSentFioRequests()

	SDK Workflow:
		To display the list of sent requests.  Use the method: getSentFioRequests().

### Bob (Payer) Displaying Pending Requests:

	Example code here:
		TestNetSdkTests.kt

		method:
			fun testFundsRequest()

	SDK Calls:
		getPendingFioRequests()

	SDK Workflow:
		Get the Pending Fio Requests (i.e. getPendingFioRequests() ).	

### Bob (Payer) Accepting a Pending Fio Request:
	Example code here:
		TestNetSdkTests.kt

		method:
			fun testFundsRequest()

	SDK Calls:
		getFeeForRecordObtData()
		recordObtData()

	SDK Workflow:	
		Use the pending fio request object properties, for details for intiating payment.  If payment is successful, close the pending fio request by first calling the getFeeForRecordObtData() method, then the recordObtData() method.  This will remove the pending fio request from the pending list (i.e. getPendingFioRequests() ).

### Bob (Payer) Rejecting a Pending Fio Request:
	Example code here:
		TestNetSdkTests.kt

		method:
			fun testFundsRequest()

	SDK Calls:
		getFeeForRejectFundsRequest()
		rejectFundsRequest()

	SDK Workflow:	
		Close the pending fio request, by calling the getFeeForRejectFundsRequest() method, then the rejectFundsRequest() method.  This will remove the pending fio request from the pending list (i.e. getPendingFioRequests() ).
