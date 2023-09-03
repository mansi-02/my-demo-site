package com.my.demo.site.core.util;

public class ComponentFinderResultItem {
    String path;
    Integer occurrenceCounter=0;
    Boolean isExperienceFragment;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOccurrenceCounter() {
        return occurrenceCounter;
    }

    public void setOccurrenceCounter(Integer occurrenceCounter) {
        this.occurrenceCounter = occurrenceCounter;
    }

    public Boolean getIsExperienceFragment() {
        return isExperienceFragment;
    }

    public void setIsExperienceFragment(Boolean isExperienceFragment) {
        this.isExperienceFragment = isExperienceFragment;
    }
}
