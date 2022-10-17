package com.liaopeixin.lib_utils;

/**
 * @author huanghuaqiao
 * @date 2021/12/31
 */
public class BuildConfigUtil {

    private static BuildConfigUtil mBuildConfigUtil;
    private boolean isDebug;
    private int gitBuildCode;
    private String gitHashCode;
    private String buildType;

    private BuildConfigUtil() {

    }

    public static BuildConfigUtil getInstance() {
        if (mBuildConfigUtil == null) {
            synchronized (BuildConfigUtil.class) {
                if (mBuildConfigUtil == null) {
                    mBuildConfigUtil = new BuildConfigUtil();
                }
            }
        }
        return mBuildConfigUtil;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public BuildConfigUtil setIsDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public int getGitBuildCode() {
        return gitBuildCode;
    }

    public BuildConfigUtil setGitBuildCode(int gitBuildCode) {
        this.gitBuildCode = gitBuildCode;
        return this;
    }

    public String getGitHashCode() {
        return gitHashCode;
    }

    public BuildConfigUtil setGitHashCode(String gitHashCode) {
        this.gitHashCode = gitHashCode;
        return this;
    }

    public String getBuildType() {
        return buildType;
    }

    public BuildConfigUtil setBuildType(String buildType) {
        this.buildType = buildType;
        return this;
    }

}
