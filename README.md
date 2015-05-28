#Party Advisor - Event Queue Service
### Travis CI - [![Build Status](https://travis-ci.org/Studio-Projektowe-AGH/PA_EventQueue.svg)](https://travis-ci.org/Studio-Projektowe-AGH/PA_EventQueue)
======
REST API:
Z kazdym zapytaniem musi byc wysylany Token aplikacji Party Advisor

POST        /events/checkin         controllers.Application.handleCheckInEvent
#{
#   "timestamp" : "<unix_timestamp>",
#   "clubId"    : "<club_id>"
#}

POST        /events/checkout        controllers.Application.handleCheckOutEvent
#{
#   "timestamp" : "<unix_timestamp>",
#   "clubId"    : "<club_id>"
#}

POST        /events/location        controllers.Application.handleLocationEvent
#{
#   "timestamp" : "<unix_timestamp>",
#   "longitude" : "<longitude>",
#   "latitude"  : "<latitude"
#}

POST        /events/qrscan          controllers.Application.handleQrScanEvent
#{
#   "timestamp" : "<unix_timestamp>",
#   "clubId"    : "<club_id>"
#   "payload"   : "<payload_string>"
#}

POST        /events/rating          controllers.Application.handleRatingEvent
#{
#   "timestamp" : "<unix_timestamp>",
#   "clubId"    : "<club_id>",
#   "rating"    : "<rating>"
#}
