package info.wufc.elasticjob.lite.spring.jobs;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class Job4 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("Job4.execute");
    }
}
