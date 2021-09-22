package adverity.api

import com.adverity.BootstrapService
import com.adverity.Utility
import grails.util.Environment

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import grails.util.Environment
class BootStrap {

    BootstrapService bootstrapService

    Runnable executeLoadData = new Runnable() {
        @Override
        void run() {
            if(Environment.current != Environment.TEST) {
                bootstrapService.loadData(Utility.sources, Utility.campaigns)
            }
        }
    }

    def init = { servletContext ->
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(executeLoadData)
    }

    def destroy = {
    }
}
