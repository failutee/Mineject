package xyz.failutee.mineject.settings;

import xyz.failutee.mineject.processor.ProcessorConfigurer;

public interface DependencySettings {

    DependencySettingsBuilder DEFAULT_SETTINGS = new DependencySettingsBuilder()
            .processorConfigurer(ignore -> {})
            .packageName("*");

    String getPackageName();

    ProcessorConfigurer getProcessorConfigurer();

}
