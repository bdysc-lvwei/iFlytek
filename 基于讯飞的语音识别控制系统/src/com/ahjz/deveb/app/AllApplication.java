package com.ahjz.deveb.app;

import java.util.ArrayList;
import java.util.List;

import com.ahjz.deveb.app.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AllApplication
{
	/**
	 * 获取移动终端的所有应用程序,map对应pack+label
	 */
	public synchronized static List<AppInfo> getAllApplications(Context context)
	{

		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> applicationInfos = packageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (ApplicationInfo info : applicationInfos)
		{
			AppInfo appInfo = new AppInfo();
			appInfo.setLabel(info.loadLabel(packageManager).toString());
			appInfo.setIcon(info.loadIcon(packageManager));
			appInfo.setPackageName(info.packageName);
			appInfos.add(appInfo);
		}
		return appInfos;

	}

}
