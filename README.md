localbitcoinslibrary
====================

Android / Java Library to use the localbitcoins.com API


LocalBitcoinSample
====================
The sample application should be pretty straight forward and easy to get running. It is deliberatly very basic and not feature complete.

1. Add the LocalBitcoinLibrary as a library project to this project.

2. In the App class add your ClientID and ClientSecret from the localbitcoins API Dashboard https://localbitcoins.com/accounts/api/

3. Build and run!

Additional Notes

> Enable or disable testing mode in App class by changing TEST_MODE. In testing mode getEscrow will return a fake Escrow to demo how a real escrow will appear. This is added because test users are not avaliable yet

