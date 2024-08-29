package xyz.failutee.example.spigot.bridge.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import xyz.failutee.example.spigot.util.LegacyUtil;
import xyz.failutee.mineject.annotation.Bean;
import xyz.failutee.mineject.annotation.BeanSetup;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@BeanSetup
class AdventureSetup {

    @Bean
    MiniMessage miniMessage() {
        return MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();
    }

    private static class LegacyColorProcessor implements UnaryOperator<Component> {

        @Override
        public Component apply(@NotNull Component component) {
            return component.replaceText(builder -> builder.match(Pattern.compile(".*"))
                    .replacement((matchResult, localBuilder) -> LegacyUtil.format(matchResult.group())));
        }
    }
}
