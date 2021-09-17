package com.adverity

import com.opencsv.CSVReader
import grails.gorm.transactions.Transactional

import java.text.SimpleDateFormat

@Transactional
class BootstrapService {

    Map<String, DataSource> createSources(List<String> sources) {
        List<DataSource> dataSourceList = []
        sources.each {
            DataSource dataSource = new DataSource(name: it).save()
            if (!dataSource.hasErrors()) {
                dataSourceList << dataSource
            }
        }
        dataSourceList.collectEntries { [(it.name): it] }
    }

    Map<String, Campaign> createCampaigns(List<String> campaigns) {
        List<Campaign> campaignList = []
        campaigns.each {
            Campaign campaign = new Campaign(name: it).save()
            if (!campaign.hasErrors()) {
                campaignList << campaign
            }
        }
        campaignList.collectEntries {[(it.name): it] }
    }

    void loadData(List<String> sources, List<String> campaigns) {
        Map<String, Campaign> campaignMap = createCampaigns(campaigns)
        Map<String, DataSource> dataSourceMap = createSources(sources)

        def resource = this.class.classLoader.getResource('adverity.csv')
        try {
            resource.openStream().withStream  { inputStream ->
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream)
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
                 List<String[]> csvData = readAll(bufferedReader)

                csvData.eachWithIndex{ String[] entry, int i ->
                    if(i > 0) {
                        def (datasource, campaign, daily, clicks, impressions) = entry
                        DataSource dataSourceDomain = dataSourceMap[(datasource)]
                        Campaign campaignDomain = campaignMap[(campaign)]
                        def(month,day, year) = daily.split("/")
                        Date date = new SimpleDateFormat("MM/dd/yyyy").parse("$month/$day/${year.length() == 2 ? "20$year": year}")

                        new CampaignStat(
                                campaign: campaignDomain,
                                dataSource: dataSourceDomain,
                                daily: date,
                                clicks: clicks,
                                impressions: impressions ).save()
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace()
        }
        catch (Exception ex) {
            ex.printStackTrace()
        }

    }

    private List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        try {
            list = csvReader.readAll();
        }
        catch (Exception ex) {
            ex.printStackTrace()
        }
        finally {
            reader.close();
            csvReader.close();
        }
        return list;
    }
 }
