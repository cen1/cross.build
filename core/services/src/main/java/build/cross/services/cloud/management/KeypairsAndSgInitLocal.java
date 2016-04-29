package build.cross.services.cloud.management;

import java.util.List;

import javax.ejb.Local;

import build.cross.models.jpa.VmSetting;

@Local
public interface KeypairsAndSgInitLocal {

	void createKeypairs();

	List<VmSetting> createInitialVmSettings();

	void createSecurityGroups();

}
