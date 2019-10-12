<!DOCTYPE HTML>
<html>
  <body>
    <script src="https://static.opentok.com/v2/js/opentok.js" charset="utf-8"></script>
    <script charset="utf-8">
      var apiKey = '45704962';
      var sessionId = '1_MX40NTcwNDk2Mn5-MTQ3Njg3NjA1MjgxN35pNWVWR1VZbmxvSDBRVkdiV0FhMVNPaDJ-fg';
      var token = 'T1==cGFydG5lcl9pZD00NTcwNDk2MiZzaWc9NmIwZjBiZGM1ZmU1ZDU3NmM2NjNiN2E3MDA1ZDlmOTUxNzRkY2U1MTpzZXNzaW9uX2lkPTFfTVg0ME5UY3dORGsyTW41LU1UUTNOamczTmpBMU1qZ3hOMzVwTldWV1IxVlpibXh2U0RCUlZrZGlWMEZoTVZOUGFESi1mZyZjcmVhdGVfdGltZT0xNDc3MzkwNTQ5Jm5vbmNlPTAuMDk3OTA2NzUxMDkyNTIzMzQmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTQ3OTk4MjU0OQ==';
      var session = OT.initSession(apiKey, sessionId)
        .on('streamCreated', function(event) {
          session.subscribe(event.stream);
        })
        .connect(token, function(error) {
          var publisher = OT.initPublisher();
          session.publish(publisher);
        });
      form.addEventListener('submit', function(event) {
    	  event.preventDefault();

    	    session.signal({
    	        type: 'chat',
    	        data: msgTxt.value
    	      }, function(error) {
    	      if (!error) {
    	        msgTxt.value = '';
    	      }
    	    });
    	  });
    </script>
    <form action="">
    <input type="submit"/>
    </form>
  </body>
</html>