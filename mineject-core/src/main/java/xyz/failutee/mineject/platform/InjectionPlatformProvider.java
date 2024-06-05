package xyz.failutee.mineject.platform;

@FunctionalInterface
public interface InjectionPlatformProvider {

    InjectionPlatform getPlatform(PlatformContext context);

}
