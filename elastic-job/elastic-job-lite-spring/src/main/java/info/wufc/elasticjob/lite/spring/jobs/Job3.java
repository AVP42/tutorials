package info.wufc.elasticjob.lite.spring.jobs;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class Job3 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("Job3.execute");
    }
}
