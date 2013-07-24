LocalBitcoins Android API 0.0.2
====================
Android / Java Library to use the localbitcoins.com API

# Contents
lib/ - contains the android library
sample/ - constains the sample application which uses the library
README.md - this document
LICENSE - the licence for code reuse

Use the sample application to quickly understand how to use the library

Sample Application
====================
The sample application should be pretty straight forward and easy to get running. It is deliberatly very basic and not feature complete.

## Running the sample app

1. Add the LocalBitcoinLibrary as a library project to this project.

2. In the App class add your ClientID and ClientSecret from the localbitcoins API Dashboard https://localbitcoins.com/accounts/api/

3. Build and run!

## Additional Notes

> Enable or disable testing mode in App class by changing TEST_MODE. In testing mode getEscrow will return a fake Escrow to demo how a real escrow will appear. This is added because test users are not avaliable yet

Library Explanation
====================
## Classes
### LocalBitcoinAction
This object is used for any interaction with the API
connect
getEscrows
releaseEscrows
## Objects
### Escrow
This object holds the escrow information and is structured identically to the JSON returned by the API
### Access token
This object holds the access token and is structured identically to the JSON returned by the API
