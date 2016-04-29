package build.cross.loadmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

@Path("/load")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class LoadMonitorResource {
	
	private Logger logger = Logger.getLogger(LoadMonitorResource.class.getSimpleName());
	
	@Path("/cpu")
	@GET
	public Response cpuLoad() throws IOException {
		
		MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
		OperatingSystemMXBean os = ManagementFactory.newPlatformMXBeanProxy(
				mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		
		double cpuLoad = os.getSystemCpuLoad();
		if (cpuLoad<0.0) {
			String loadOut = this.executeCommand("iostat -c 3 proc");
			logger.info(loadOut);
			
			List<String> lines = Arrays.asList(loadOut.split("\n"));
			int i=0;
			int load=0;
			for (String line : lines) {
				i++;
				if (i<=3) {
					continue;
				}
				String[] tokens = line.split(" +");
				logger.info(Arrays.toString(tokens));
				int usLoad = Integer.parseInt(tokens[3]);
				int syLoad = Integer.parseInt(tokens[5]);
				logger.info(Integer.toString(usLoad));
				logger.info(Integer.toString(syLoad));
				load+=usLoad+syLoad;
				logger.info(Integer.toString(load));
			}
			cpuLoad=(double)load/2.0/100;
			logger.info(Double.toString(cpuLoad));
		}
		
		return Response.ok(cpuLoad).build();
		
	}
	
	@SuppressWarnings("restriction")
	@Path("/memory")
	@GET
	public Response memoryLoad() throws IOException {
		
		MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
		OperatingSystemMXBean os = ManagementFactory.newPlatformMXBeanProxy(
				mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		
		double memLoad = (double)(os.getTotalPhysicalMemorySize()-os.getFreePhysicalMemorySize())/os.getTotalPhysicalMemorySize();
		
		return Response.ok(memLoad).build();
	}
	
	@SuppressWarnings("restriction")
	@Path("/disk")
	@GET
	public Response diskLoad() throws IOException {
		
		MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
		OperatingSystemMXBean os = ManagementFactory.newPlatformMXBeanProxy(
				mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		
		File f = new File("/");
		double diskLoad = (double)(f.getTotalSpace()-f.getUsableSpace())/f.getTotalSpace();
		
		return Response.ok(diskLoad).build();
		
	}
	
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
}
