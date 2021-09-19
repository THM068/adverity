package adverity.api

import com.adverity.DataSource
import com.adverity.DataSourceService
import com.adverity.JsonParserUtility

class DataSourceController implements  JsonParserUtility{
	static responseFormats = ['json', 'xml']
    DataSourceService dataSourceService

    def index() {
        Map model = parseRequest(params.q)
        List<DataSource> list = dataSourceService.getDataSources(model)
        [dataSourceList: list]
    }
}
