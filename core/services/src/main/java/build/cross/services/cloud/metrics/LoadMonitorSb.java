package build.cross.services.cloud.metrics;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.jclouds.ContextBuilder;
import org.jclouds.aws.cloudwatch.AWSCloudWatchProviderMetadata;
import org.jclouds.aws.domain.Region;
import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.domain.Datapoint;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.EC2Constants;
import org.jclouds.cloudwatch.domain.GetMetricStatisticsResponse;
import org.jclouds.cloudwatch.domain.Namespaces;
import org.jclouds.cloudwatch.domain.Statistics;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.cloudwatch.features.MetricApi;
import org.jclouds.cloudwatch.options.ListMetricsOptions;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;

import build.cross.services.common.CloudCommon;

@SuppressWarnings("unused")
@Stateless
public class LoadMonitorSb implements LoadMonitorSbLocal {

	private Logger logger = Logger.getLogger(LoadMonitorSb.class.getSimpleName());

}
