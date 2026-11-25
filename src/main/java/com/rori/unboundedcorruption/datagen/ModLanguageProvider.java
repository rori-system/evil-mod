package com.rori.unboundedcorruption.datagen;

import com.rori.unboundedcorruption.evilportal;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.rori.unboundedcorruption.registry.ModBlocks.*;
        import static com.rori.unboundedcorruption.registry.ModItems.*;

public final class ModLanguageProvider extends LanguageProvider {
    private final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, evilportal.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        // Shared across all English variants
        add(BLOOD_SLIME.get(), "Blood Slime");
        add(AMALGAMATED_BLOCK.get(), "Amalgamated Block");
        add(HARD_AMALGAMATED_BLOCK.get(), "Dense Amalgamated Block");
        add(PERISHED_BALL.get(), "Perishing Extract");
        add(CONDENSED_PERISHMENT.get(), "Condensed Perishment");
        add("itemGroup.unboundedcorruption", "Unbounded Corruption");

    }
}