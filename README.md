###Adverity Quering Api
I have created a simple api to query the information you sent me.

The Api is composed of three endpoints:

| Endpoints                                         | Description                      |
| ----------------------------------------------    | -----------------------------    |
| get /adverity/api/reports/projections             | Creates reports like sum of clicks for a given campaign and also groups the results                            |
| get /adverity/api/reports/click-through-rate      | Calculates the click through rate for all the campaigns                             |
| get /adverity/api/reports/impressions-over-time   | Shows the impressions over time for a given campaign                             |

##Query operations
The query parameter q is used to for projections such as ['sum', 'avg', 'max', 'min']

| Syntax      | Example |
| ----------- | ----------- |
|  $sum       | http://localhost:8080/adverity/api/reports/projections?p={"$sum":["clicks"]}       |
Projections support $sum $avg $max $min and only supported on the /adverity/api/reports/projections endpoint

The query parameter h is used for grouping the datasource and campaigns e.g
Find the sum of clicks grouped by datasource and campaign
h={"$groupby": ["campaign", "dataSource"]

http://176.58.127.204:8080/adverity/api/reports/projections?p={"$sum":["clicks"]}&h={"$groupby": ["campaign", "dataSource"]}

FYI- please test using postman or insomnia

The q parameter is used to filter results e.g
http://176.58.127.204:8080/adverity/api/reports/projections?p={"$sum": ["clicks","impressions"]}&h={"$groupby": ["campaign", "dataSource"]}&q={"dataSource": "Google Ads", "$between": {"from": "12/11/2019", "to":"14/11/2019"}}

Example of queries
Get sum of clicks and impressions for datasource "Google Ads" between "12/11/2019" and "14/11/2019" grouped by "campaign", "dataSource"
http://176.58.127.204:8080/adverity/api/reports/projections?p={"$sum": ["clicks","impressions"]}&h={"$groupby": ["campaign", "dataSource"]}&q={"dataSource": "Google Ads", "$between": {"from": "12/11/2019", "to":"14/11/2019"}}


Click through rate
http://176.58.127.204:8080/adverity/api/reports/click-through-rate

Impressions overtime for a given campaign
http://176.58.127.204:8080/adverity/api/reports/impressions-over-time?q={"campaign": "WRKS"}
