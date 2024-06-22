package xyz.failutee.mineject.settings;

import xyz.failutee.mineject.processor.ProcessorConfigurer;

public class DependencySettingsBuilder implements DependencySettings {

    private String packageName;
    private ProcessorConfigurer processorConfigurer;

    public DependencySettingsBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public DependencySettingsBuilder processorConfigurer(ProcessorConfigurer processorConfigurer) {
        this.processorConfigurer = processorConfigurer;
        return this;
    }

    @Override
    public String getPackageName() {
        return this.packageName;
    }

    @Override
    public ProcessorConfigurer getProcessorConfigurer() {
        return this.processorConfigurer;
    }
}
